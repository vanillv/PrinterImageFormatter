package com.example.demo.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
@Getter
public enum SlotFormat {
    @JsonProperty КРУГЛЫЙ(71.550, 38.950),
    @JsonProperty КВАДРАТНЫЙ(68.950, 38.7),
    @JsonProperty МИКРОФОН(48.088, 22.588);
    private final double widthMM;
    private final double heightMM;
    SlotFormat(double widthMM, double heightMM){
        this.widthMM = widthMM;
        this.heightMM = heightMM;
    };
}
