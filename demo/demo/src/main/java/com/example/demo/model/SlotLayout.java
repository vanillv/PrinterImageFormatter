package com.example.demo.model;
import com.example.demo.util.MMtoPixel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Data
public class SlotLayout {
    private final List<Slot> slots = new ArrayList<>();
    public SlotLayout() {
        try {
            InputStream is = getClass().getResourceAsStream("/layout.json");
            ObjectMapper mapper = new ObjectMapper();
            LayoutConfig config = mapper.readValue(is, LayoutConfig.class);
            for (SlotFormat format : SlotFormat.values()) {
                FormatConfig fc = config.getFormatConfig(format);
                if (fc != null) {
                    generateSlots(format, fc, config.getGapMM());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки layout.json", e);
        }
    }
    private void generateSlots(SlotFormat format, FormatConfig fc, double gapMM) {
        System.out.println("Generating slots for " + format + " with config: " + fc);
        int startX = MMtoPixel.convert(fc.getStarting_positionX());
        int startY = MMtoPixel.convert(fc.getStarting_positionY());
        int gapPixels = MMtoPixel.convert(gapMM);
        int slotWidth = MMtoPixel.convert(format.getWidthMM());
        int slotHeight = MMtoPixel.convert(format.getHeightMM());
        System.out.printf("Start X: %dpx (%.2fmm)%n", startX, fc.getStarting_positionX());
        System.out.printf("Start Y: %dpx (%.2fmm)%n", startY, fc.getStarting_positionY());
        System.out.printf("Slot dimensions: %dx%d px%n", slotWidth, slotHeight);
        for (int row = 0; row < fc.getY(); row++) {
            for (int col = 0; col < fc.getX(); col++) {
                int x = startX + col * (slotWidth + gapPixels);
                int y = startY + row * (slotHeight + gapPixels);
                System.out.printf("Created %s slot at [%dpx, %dpx] (col=%d, row=%d)%n",
                        format, x, y, col, row);
                slots.add(new Slot(x, y, format));
            }
        }
    }
}

