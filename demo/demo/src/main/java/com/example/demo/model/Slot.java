package com.example.demo.model;

import com.example.demo.util.MMtoPixel;
import lombok.Data;
@Data
public class Slot {
    private final int x;         // Координата X (в пикселях)
    private final int y;         // Координата Y (в пикселях)
    private final int width;     // Ширина (в пикселях)
    private final int height;    // Высота (в пикселях)
    private final SlotFormat format;
    public Slot(int x, int y, SlotFormat format) {
        this.x = x;
        this.y = y;
        this.format = format;
        this.width = MMtoPixel.convert(format.getWidthMM());
        this.height = MMtoPixel.convert(format.getHeightMM());
    }
}
