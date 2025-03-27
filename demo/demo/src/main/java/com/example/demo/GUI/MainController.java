package com.example.demo.GUI;
import com.example.demo.model.SlotLayout;
import com.example.demo.service.CanvasService;
import com.example.demo.service.FormatterService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainController {
    private final static FormatterService formatterService = new FormatterService();
    protected final static CanvasService canvasService = new CanvasService();
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
        slotView.setLayoutX(slotView.getSlot().getX() * 0.2);
        slotView.setLayoutY(slotView.getSlot().getY() * 0.2);
        slotView.setPrefSize(
                slotView.getSlot().getWidth() * 0.2,
                slotView.getSlot().getHeight() * 0.2
        );
    }
    public void handleSave() {
      try {
          int number = 0;
          String userName = System.getProperty("user.name");
          String fileName = "output" + number + ".png";
          Path path = Path.of("C:\\Users\\"+userName+"\\Downloads\\" + fileName);
          if (Files.exists(path)) {
              number++;
          };
          System.out.println(path.toString());
          File output = Files.createFile(path).toFile();
          canvasService.saveCanvas(output);
      } catch (Exception e) {}
    }
    public void handleTest() {
        try {
            handleClear();
            int number = 0;
            String userName = System.getProperty("user.name");
            String fileName = "test-result" + number + ".png";
            Path path = Path.of("C:\\Users\\"+userName+"\\Downloads\\" + fileName);
            if (Files.exists(path)) {
                number++;
            };
            File output = Files.createFile(path).toFile();
            canvasService.testCanvas(output, formatterService);
            handleClear();
        } catch (Exception e) {}
    }
    public void handleClear() {
        slotsContainer.getChildren().clear();
        initialize();
    }
}
