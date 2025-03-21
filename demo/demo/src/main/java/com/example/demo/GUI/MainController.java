package com.example.demo.GUI;
import com.example.demo.model.Slot;
import com.example.demo.model.SlotFormat;
import com.example.demo.model.SlotLayout;
import com.example.demo.service.CanvasService;
import com.example.demo.service.FormatterService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.awt.image.BufferedImage;
import java.io.File;

public class MainController {
    private final FormatterService formatterService = new FormatterService();
    private final CanvasService canvasService = new CanvasService();
    File output = new File("output.png");
    @FXML
    private AnchorPane slotsContainer;
    @FXML private Button saveButton;
    private SlotLayout slotLayout;

    @FXML
    public void initialize() {
        try {
            slotLayout = new SlotLayout();
            slotLayout.getSlots().forEach(slot -> {
                SlotView slotView = new SlotView(slot);
                configureSlotView(slotView);
                slotsContainer.getChildren().add(slotView);
            });
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки слота", e);
        }
    }

    private void configureSlotView(SlotView slotView) {
        slotView.setLayoutX(slotView.getSlot().getX());
        slotView.setLayoutY(slotView.getSlot().getY());
        slotView.setPrefSize(
                slotView.getSlot().getWidth(),
                slotView.getSlot().getHeight()
        );
    }
    public void handleSave(File file) {
      try {
          canvasService.saveCanvas(output);
      } catch (Exception e) {}
    }

    public void mirrorPicture() {

    }
    public void handleDelete() {

    }
    public void testImageSaving(){}
}
