package main.java.com.photoredactor.demo.gui;
import main.java.com.photoredactor.demo.service.CanvasService;
import main.java.com.photoredactor.demo.service.FormatterService;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.control.Alert;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

public class MainController {
    @FXML private CanvasPane canvasPane;
    @FXML private Pane rootPane;

    private final FormatterService formatter = new FormatterService();
    private final CanvasService canvasService = new CanvasService();
    private SlotView selectedSlot;

    @FXML
    public void initialize() {
        setupGlobalDragDrop();
        canvasPane.setOnSlotSelected(this::handleSlotSelection);
    }

    private void setupGlobalDragDrop() {
        rootPane.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
    }

    @FXML
    private void handleMirror() {
        if (selectedSlot != null) {
            selectedSlot.mirrorImage();
        } else {
            showAlert("No slot selected", "Select a slot first");
        }
    }

    @FXML
    private void handleSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG Files", "*.png")
        );
        File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());

        if (file != null) {
            try {
                canvasService.saveCanvas(canvasPane, file);
                showAlert("Success", "Canvas saved successfully");
            } catch (Exception e) {
                showAlert("Error", "Failed to save canvas: " + e.getMessage());
            }
        }
    }

    private void handleSlotSelection(SlotView slot) {
        this.selectedSlot = slot;
        canvasPane.highlightSlot(slot);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}