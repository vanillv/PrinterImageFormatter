package com.example.demo.service;

import com.example.demo.model.Slot;
import com.example.demo.model.SlotFormat;
import com.example.demo.model.SlotLayout;
import com.example.demo.util.MMtoPixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

public class CanvasService {
    private final SlotLayout slotLayout;
    private BufferedImage canvas;
    public CanvasService() {
        this.slotLayout = new SlotLayout();
        initializeCanvas();
    }
    private void initializeCanvas() {
        int width = MMtoPixel.convert(330.0);
        int height = MMtoPixel.convert(600.0);
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    public void placeImage(BufferedImage image, SlotFormat format) {
        List<Slot> slots = slotLayout.getSlots();
        for (Slot slot : slots) {
            if (slot.getFormat() == format && !slot.isOccupied()) {
                Graphics2D g2d = canvas.createGraphics();
                g2d.drawImage(image, slot.getX(), slot.getY(), null);
                g2d.dispose();
                slot.setOccupied(true);
                break;
            }
        }
    }
    public void saveCanvas(File outputFile) throws Exception {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
        writer.setOutput(ImageIO.createImageOutputStream(outputFile));
        IIOMetadata metadata = writer.getDefaultImageMetadata(
                new ImageTypeSpecifier(canvas),
                writer.getDefaultWriteParam()
        );
        setDpiMetadata(metadata, 300);

        writer.write(null, new IIOImage(canvas, null, metadata), null);
    }
    private void setDpiMetadata(IIOMetadata metadata, int dpi) {
        try {
            IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
            IIOMetadataNode dimension = new IIOMetadataNode("Dimension");
            IIOMetadataNode pixelSize = new IIOMetadataNode("PixelAspectRatio");
            pixelSize.setAttribute("value", "1.0");
            IIOMetadataNode horizontal = new IIOMetadataNode("HorizontalPixelSize");
            horizontal.setAttribute("value", Double.toString(25.4 / dpi));
            IIOMetadataNode vertical = new IIOMetadataNode("VerticalPixelSize");
            vertical.setAttribute("value", Double.toString(25.4 / dpi));
            dimension.appendChild(pixelSize);
            dimension.appendChild(horizontal);
            dimension.appendChild(vertical);
            root.appendChild(dimension);
            metadata.mergeTree("javax_imageio_1.0", root);
        } catch (IIOInvalidTreeException e) {
            throw new RuntimeException(e);
        }
    }
}
