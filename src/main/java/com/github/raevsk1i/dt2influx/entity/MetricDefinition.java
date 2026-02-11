package com.github.raevsk1i.dt2influx.entity;

import com.github.raevsk1i.dt2influx.enums.MetricMeasurement;

public record MetricDefinition (MetricMeasurement measurement, String field, String metricSelector) {}
