package com.github.raevsk1i.dt2influx.config.metrics;

import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import com.github.raevsk1i.dt2influx.enums.MetricMeasurement;
import lombok.Data;

import java.util.List;

@Data
public final class DISK {
    public static final List<MetricDefinition> metrics = List.of(
            new MetricDefinition(
                    MetricMeasurement.DISK,
                    "avail_bytes",
                    "(builtin:host.disk.avail:splitBy(\"dt.entity.host\"):sum):names"),
            new MetricDefinition(
                    MetricMeasurement.DISK,
                    "used_bytes",
                    "(builtin:host.disk.used:splitBy(\"dt.entity.host\"):sum):names"),
            new MetricDefinition(
                    MetricMeasurement.DISK,
                    "read_ops",
                    "(builtin:host.disk.read0ps:splitBy(\"dt.entity.host\"):sum):names"),
            new MetricDefinition(
                    MetricMeasurement.DISK,
                    "write_ops",
                    "(builtin:host.disk.writeOps:splitBy(\"dt.entity.host\"):sum):names"),
            new MetricDefinition(
                    MetricMeasurement.DISK,
                    "read_bytes",
                    "(builtin:host.disk.bytesRead:splitBy(\"dt.entity.host\")):sum:names"),
            new MetricDefinition(
                    MetricMeasurement.DISK,
                    "written_bytes",
                    "(builtin:host.disk.bytesWritten:splitBy(\"dt.entity.host\")):sum:names")


//          TODO: if new metrics will available

//            new MetricDefinition(
//                    "",
//                    "")
    );
}
