package com.example.demo.model;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
public class LayoutConfig {
    private final Map<SlotFormat, FormatConfig> formats = new HashMap<>();
    private double gapMM;
    @JsonAnySetter
    public void addFormat(String formatKey, FormatConfig config) {
        SlotFormat format = SlotFormat.valueOf(formatKey.toUpperCase());
        formats.put(format, config);
    }
    public FormatConfig getFormatConfig(SlotFormat format) {
        return formats.get(format);
    }
}
