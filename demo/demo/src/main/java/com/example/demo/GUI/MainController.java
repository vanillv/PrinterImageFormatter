package com.example.demo.GUI;
import com.example.demo.model.Slot;
import com.example.demo.model.SlotLayout;
import com.example.demo.service.CanvasService;
import com.example.demo.service.FormatterService;
import com.example.demo.util.MMtoPixel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import java.io.File;

public class MainController {
    private final FormatterService formatterService = new FormatterService();
    private final CanvasService canvasService = new CanvasService();
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
    public void handleSave() {
        for (Slot slot : slotLayout.getSlots()) {

        }
    }

    public void mirrorPicture() {

    }
    public void handleDelete() {

    }
    public void testImageSaving(){}
}
