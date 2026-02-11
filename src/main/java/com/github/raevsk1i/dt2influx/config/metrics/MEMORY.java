package com.github.raevsk1i.dt2influx.config.metrics;

import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import com.github.raevsk1i.dt2influx.enums.MetricMeasurement;
import lombok.Data;

import java.util.List;

@Data
public final class MEMORY {
    public static final List<MetricDefinition> metrics = List.of(
            new MetricDefinition(
                    MetricMeasurement.MEMORY,
                    "usage_percent",
                    "(builtin:containers.memory.usagePercent):names"),
            new MetricDefinition(
                    MetricMeasurement.MEMORY,
                    "limit_bytes",
                    "(builtin:containers.memory.limitBytes):names"),
            new MetricDefinition(
                    MetricMeasurement.MEMORY,
                    "cache_bytes",
                    "(builtin:containers.memory.cacheBytes):names"),
            new MetricDefinition(
                    MetricMeasurement.MEMORY,
                    "resident_bytes",
                    "(builtin:containers.memory.residentSetBytes):names")


//          TODO: if new metrics will available

//            new MetricDefinition(
//                    "",
//                    "")
    );
}
