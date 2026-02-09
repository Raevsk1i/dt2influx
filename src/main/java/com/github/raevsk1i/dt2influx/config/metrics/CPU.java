package com.github.raevsk1i.dt2influx.config.metrics;

import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import lombok.Data;

import java.util.List;

@Data
public final class CPU {
    public static final List<MetricDefinition> metrics = List.of(
            new MetricDefinition(
                    "usage_percent",
                    "(builtin:containers.cpu.usagePercent):names"),
            new MetricDefinition(
                    "limit_millicores",
                    "(builtin:containers.cpu.limit):names"),
            new MetricDefinition(
                    "usage_millicores",
                    "(builtin:containers.cpu.usageMilliCores):names"),
            new MetricDefinition(
                    "throttled_millicores",
                    "(builtin:containers.cpu.throttledMilliCores):names"),
            new MetricDefinition(
                    "throttled_time",
                    "(builtin:containers.cpu.throttledTime):names")


//          TODO: if new metrics will available

//            new MetricDefinition(
//                    "",
//                    "")
    );
}
