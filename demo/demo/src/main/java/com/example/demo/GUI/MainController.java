package com.example.demo.GUI;
import com.example.demo.model.SlotLayout;
import com.example.demo.service.CanvasService;
import com.example.demo.service.FormatterService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.File;

public class MainController {
    private final static FormatterService formatterService = new FormatterService();
    protected final static CanvasService canvasService = new CanvasService();
    File output = new File("output.png");
    @FXML
    private AnchorPane slotsContainer;
    @FXML private Button saveButton;
    @FXML private Button testButton;
    @FXML private Button deleteButton;
    private SlotLayout slotLayout;
    private boolean isFlipped = false;
    @FXML
    public void initialize() {
        try {
            slotLayout = new SlotLayout();
            slotLayout.getSlots().forEach(slot -> {
                SlotView slotView = new SlotView(slot, canvasService, formatterService);
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
      try {
          canvasService.saveCanvas(output);
      } catch (Exception e) {}
    }
    public void handleDelete() {
        slotsContainer.getChildren().clear();
    }
    public void testImageSaving(){
        try {testImageSaving();} catch (Exception e){};
    }
}
