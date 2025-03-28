package com.example.demo.GUI;

import com.example.demo.model.Slot;
import com.example.demo.service.CanvasService;
import com.example.demo.service.FormatterService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static com.example.demo.GUI.MainController.canvasService;
public class SlotView extends StackPane {
    private File originalFile;
    private boolean isFlipped = false;
    private final Slot slot;
    private ImageView imageView;
    private final CanvasService canvas;
    private final FormatterService formatter;

    public SlotView(Slot slot, CanvasService canvas, FormatterService formatter) {
        this.slot = slot;
        this.canvas = canvas;
        this.formatter = formatter;
        initializeSlot();
    }
    private void initializeSlot() {
        this.setStyle("-fx-border-color: #666; -fx-border-width: 2px;");
        this.setOnDragOver(this::handleDragOver);
        this.setOnDragDropped(this::handleDragDropped);
        this.setFocusTraversable(true);
        this.setOnMouseClicked(event -> this.requestFocus());
        focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                setStyle("-fx-border-color: #0099ff;");
            } else {
                setStyle("-fx-border-color: #666;");
            }
        });
        this.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.E) {
                System.out.println("e pressed");
                flipImage();
                e.consume();
            }
        });
    }
    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
            this.setStyle("-fx-border-color: #0099ff;");
        }
        event.consume();
    }
    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            try {
                File file = db.getFiles().get(0);
                originalFile = db.getFiles().get(0);
                Image image = new Image(file.toURI().toString());
                setImage(image);
                BufferedImage bufferedImage = formatter.resizeForSlot(formatter.convertToPng(file), slot.getFormat());
                canvas.placeImage(bufferedImage, slot);
                event.setDropCompleted(true);
            } catch (Exception e) {
                event.setDropCompleted(false);
            }
        }
        this.setStyle("-fx-border-color: #666;");
        event.consume();
    }
    private void setImage(Image image) {
        if (imageView == null) {
            imageView = new ImageView(image);
            imageView.setPreserveRatio(false);
            imageView.fitWidthProperty().bind(this.widthProperty().multiply(0.95));
            imageView.fitHeightProperty().bind(this.heightProperty().multiply(0.95));
            this.getChildren().add(imageView);
        } else {
            imageView.setImage(image);
        }
        this.setMaxSize(this.getPrefWidth(), this.getPrefHeight());
    }
    private void flipImage() {
        System.out.println("flipping image");
        if (originalFile == null) return;
        try {
            BufferedImage image = ImageIO.read(originalFile);
            BufferedImage flipped = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    image.getType()
            );
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(), 0);
            Graphics2D g = flipped.createGraphics();
            g.setTransform(tx);
            g.drawImage(image, 0, 0, null);
            g.dispose();
            File flippedFile = new File(
                    originalFile.getParent(),
                    "flipped_" + originalFile.getName()
            );
            ImageIO.write(flipped, "png", flippedFile);
            originalFile = flippedFile;
            updateImage();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void updateImage() {
        if (originalFile != null) {
            Image image = new Image(originalFile.toURI().toString());
            setImage(image);
            BufferedImage awtImage = null;
            try {
                awtImage = ImageIO.read(originalFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            canvasService.placeImage(awtImage, slot);
        }
    }
    public void clear() {
        imageView = null;
        slot.setOccupied(false);
    }
    public Slot getSlot() {
        return slot;
    }
}
