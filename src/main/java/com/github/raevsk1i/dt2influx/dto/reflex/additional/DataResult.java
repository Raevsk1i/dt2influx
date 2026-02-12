package com.github.raevsk1i.dt2influx.dto.reflex.additional;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataResult {
    private List<String> dimensions;
    private Map<String, String> dimensionMap;
    private List<Long> timestamps;
    private List<Long> values;
}
