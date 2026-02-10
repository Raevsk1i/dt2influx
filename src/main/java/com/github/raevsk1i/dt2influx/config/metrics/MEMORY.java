package com.github.raevsk1i.dt2influx.config.metrics;

import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import lombok.Data;

import java.util.List;

@Data
public final class MEMORY {
    public static final List<MetricDefinition> metrics = List.of(
            new MetricDefinition(
                    "usage_percent",
                    "(builtin:containers.memory.usagePercent):names"),
            new MetricDefinition(
                    "limit_bytes",
                    "(builtin:containers.memory.limitBytes):names"),
            new MetricDefinition(
                    "cache_bytes",
                    "(builtin:containers.memory.cacheBytes):names"),
            new MetricDefinition(
                    "resident_bytes",
                    "(builtin:containers.memory.residentSetBytes):names")


//          TODO: if new metrics will available

//            new MetricDefinition(
//                    "",
//                    "")
    );
}
