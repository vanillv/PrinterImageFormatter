package main.java.com.photoredactor.demo.service;

import main.java.com.photoredactor.demo.util.MMtoPixel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CanvasService {
    private static final int TARGET_DPI = 300;

    public void saveCanvas(CanvasPane canvasPane, File outputFile) throws IOException {
        WritableImage fxImage = canvasPane.snapshot();
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);
        int canvasWidthPx = MMtoPixel.convert(330.0);
        int canvasHeightPx = MMtoPixel.convert(600.0);
        BufferedImage scaledImage = new BufferedImage(
                canvasWidthPx,
                canvasHeightPx,
                BufferedImage.TYPE_INT_ARGB
        );
        scaledImage.getGraphics().drawImage(bufferedImage, 0, 0, canvasWidthPx, canvasHeightPx, null);
        saveWithDPI(scaledImage, outputFile, TARGET_DPI);
    }

    private void saveWithDPI(BufferedImage image, File file, int dpi) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(file)) {
            writer.setOutput(ios);
            ImageWriteParam params = writer.getDefaultWriteParam();
            IIOMetadata metadata = writer.getDefaultImageMetadata(
                    new ImageTypeSpecifier(image),
                    params
            );
            setDPI(metadata, dpi);
            writer.write(null, new IIOImage(image, null, metadata), params);
        } finally {
            writer.dispose();
        }
    }

    private void setDPI(IIOMetadata metadata, int dpi) {
        String metaFormat = metadata.getNativeMetadataFormatName();
        IIOMetadataNode root = new IIOMetadataNode(metaFormat);

        IIOMetadataNode dimension = new IIOMetadataNode("Dimension");
        IIOMetadataNode pixelSize = new IIOMetadataNode("PixelAspectRatio");
        pixelSize.setAttribute("value", "1.0");

        IIOMetadataNode horizontal = new IIOMetadataNode("HorizontalPixelSize");
        horizontal.setAttribute("value", String.valueOf(25.4 / dpi));

        IIOMetadataNode vertical = new IIOMetadataNode("VerticalPixelSize");
        vertical.setAttribute("value", String.valueOf(25.4 / dpi));

        dimension.appendChild(pixelSize);
        dimension.appendChild(horizontal);
        dimension.appendChild(vertical);
        root.appendChild(dimension);

        try {
            metadata.mergeTree(metaFormat, root);
        } catch (IIOInvalidTreeException e) {
            throw new RuntimeException("Failed to set DPI metadata", e);
        }
    }
}