package com.example.demo.service;

import com.example.demo.model.SlotFormat;
import com.example.demo.util.MMtoPixel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FormatterService {
    public BufferedImage convertToPng(File inputFile) throws Exception {
        String extension = getFileExtension(inputFile);
        switch (extension.toLowerCase()) {
            case "pdf":
                return renderPdf(inputFile);
            case "jpg":
            case "jpeg":
            case "png":
                return ImageIO.read(inputFile);
            default:
                throw new IllegalArgumentException("Unsupported format: " + extension);
        }
    }
    public BufferedImage resizeForSlot(BufferedImage image, SlotFormat format) {
        int targetWidth = MMtoPixel.convert(format.getWidthMM());
        int targetHeight = MMtoPixel.convert(format.getHeightMM());
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(image, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return scaledImage;
    }
    private BufferedImage renderPdf(File pdfFile) throws Exception {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);
            return renderer.renderImage(0, 1.0f);
        }
    }
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return (lastDot == -1) ? "" : name.substring(lastDot + 1);
    }
}
