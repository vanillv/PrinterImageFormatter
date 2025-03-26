package com.example.demo.GUI;

import com.example.demo.model.Slot;
import com.example.demo.service.CanvasService;
import com.example.demo.service.FormatterService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
        this.setOnDragExited(this::handleDragExited);
        this.setFocusTraversable(true); // Разрешаем фокус
        this.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.E) {
                flipImage();
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
    private void handleDragExited(DragEvent event) {
        clear();
        event.isAccepted();
    }
    private void setImage(Image image) {
        if (imageView == null) {
            imageView = new ImageView(image);
            imageView.setPreserveRatio(false);
            imageView.fitWidthProperty().bind(this.widthProperty());
            imageView.fitHeightProperty().bind(this.heightProperty());
            this.getChildren().add(imageView);
        } else {
            imageView.setImage(image);
        }
    }
    private void flipImage() {
        if (originalFile == null) return;
        try {
            // Загружаем изображение
            BufferedImage image = ImageIO.read(originalFile);

            // Горизонтальное отражение
            BufferedImage flipped = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    image.getType()
            );
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(), 0);
            Graphics2D g = flipped.createGraphics();
            g.drawImage(image, tx, null);
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
