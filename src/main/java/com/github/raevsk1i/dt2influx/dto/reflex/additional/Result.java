package com.github.raevsk1i.dt2influx.dto.reflex.additional;

import lombok.Data;

import java.util.List;

@Data
public class Result {
    private String metricId;
    private double dataPointCountRatio;
    private double dimensionCountRatio;
    private List<DataResult> data;
}
