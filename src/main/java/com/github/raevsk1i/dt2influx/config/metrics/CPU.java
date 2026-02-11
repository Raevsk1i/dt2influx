package com.github.raevsk1i.dt2influx.config.metrics;

import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import com.github.raevsk1i.dt2influx.enums.MetricMeasurement;
import lombok.Data;

import java.util.List;

@Data
public final class CPU {
    public static final List<MetricDefinition> metrics = List.of(
            new MetricDefinition(
                    MetricMeasurement.CPU,
                    "usage_percent",
                    "(builtin:containers.cpu.usagePercent):names"),
            new MetricDefinition(
                    MetricMeasurement.CPU,
                    "limit_millicores",
                    "(builtin:containers.cpu.limit):names"),
            new MetricDefinition(
                    MetricMeasurement.CPU,
                    "usage_millicores",
                    "(builtin:containers.cpu.usageMilliCores):names"),
            new MetricDefinition(
                    MetricMeasurement.CPU,
                    "throttled_millicores",
                    "(builtin:containers.cpu.throttledMilliCores):names"),
            new MetricDefinition(
                    MetricMeasurement.CPU,
                    "throttled_time",
                    "(builtin:containers.cpu.throttledTime):names")


//          TODO: if new metrics will available

//            new MetricDefinition(
//                    "",
//                    "")
    );
}
