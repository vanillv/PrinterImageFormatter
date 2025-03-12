package com.example.demo.util;

public class MMtoPixel {
    private static final double DPI = 300;
    private static final double INCH_TO_MM = 25.4f;

    public static int convert(double mm) {
        return (int) (mm * DPI / INCH_TO_MM);
    }
}
