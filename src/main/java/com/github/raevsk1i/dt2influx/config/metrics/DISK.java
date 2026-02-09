package com.github.raevsk1i.dt2influx.config.metrics;

import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import lombok.Data;

import java.util.List;

@Data
public final class DISK {
    public static final List<MetricDefinition> metrics = List.of(
            new MetricDefinition(
                    "avail_bytes",
                    "(builtin:host.disk.avail:splitBy(\"dt.entity.host\"):sum):names"),
            new MetricDefinition(
                    "used_bytes",
                    "(builtin:host.disk.used:splitBy(\"dt.entity.host\"):sum):names"),
            new MetricDefinition(
                    "read_ops",
                    "(builtin:host.disk.read0ps:splitBy(\"dt.entity.host\"):sum):names"),
            new MetricDefinition(
                    "write_ops",
                    "(builtin:host.disk.writeOps:splitBy(\"dt.entity.host\"):sum):names"),
            new MetricDefinition(
                    "read_bytes",
                    "(builtin:host.disk.bytesRead:splitBy(\"dt.entity.host\")):sum:names"),
            new MetricDefinition(
                    "written_bytes",
                    "(builtin:host.disk.bytesWritten:splitBy(\"dt.entity.host\")):sum:names")


//          TODO: if new metrics will available

//            new MetricDefinition(
//                    "",
//                    "")
    );
}
