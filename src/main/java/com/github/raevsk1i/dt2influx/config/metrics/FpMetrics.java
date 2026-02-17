package com.github.raevsk1i.dt2influx.config.metrics;

import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import com.github.raevsk1i.dt2influx.enums.MetricMeasurement;
import lombok.Data;

import java.util.List;

@Data
public final class FpMetrics {
    public static final List<MetricDefinition> metrics = List.of(
            /*
            CPU metrics start
             */
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
                    "(builtin:containers.cpu.throttledTime):names"),
            /*
            CPU metrics end
             */

            /*
            MEMORY metrics start
             */
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
                    "(builtin:containers.memory.residentSetBytes):names"),
            /*
            MEMORY metrics end
             */

            /*
            JVM metrics start
             */
            new MetricDefinition(
                    MetricMeasurement.JVM,
                    "pool_committed_bytes",
                    "(builtin:tech.jvm.memory.pool.committed:filter(in(\"dt.entity.process_group_instance\",entitySelector(\"type(~\"PROCESS_GROUP_INSTANCE~\"),entityName.contains(~\"App~\")\")))):names"),
            new MetricDefinition(
                    MetricMeasurement.JVM,
                    "pool_used_bytes",
                    "(builtin:tech.jvm.memory.pool.used:filter(in(\"dt.entity.process_group_instance\",entitySelector(\"type(~\"PROCESS_GROUP_INSTANCE~\"),entityName.contains(~\"App~\")\")))):names"),
            new MetricDefinition(
                    MetricMeasurement.JVM,
                    "pool_max_bytes",
                    "(builtin:tech.jvm.memory.pool.max:filter(in(\"dt.entity.process_group_instance\",entitySelector(\"type(~\"PROCESS_GROUP_INSTANCE~\"),entityName.contains(~\"App~\")\")))):names"),
            new MetricDefinition(
                    MetricMeasurement.JVM,
                    "pool_collection_count",
                    "(builtin:tech.jvm.memory.pool.collectionCount:filter(in(\"dt.entity.process_group_instance\",entitySelector(\"type(~\"PROCESS_GROUP_INSTANCE~\"),entityName.contains(~\"App~\")\")))):names"),
            new MetricDefinition(
                    MetricMeasurement.JVM,
                    "threads_count",
                    "(builtin:tech.jvm.threads.count:filter(in(\"dt.entity.process_group_instance\",entitySelector(\"type(~\"PROCESS_GROUP_INSTANCE~\"),entityName.contains(~\"App~\")\")))):names"),
            new MetricDefinition(
                    MetricMeasurement.JVM,
                    "pool_collection_time",
                    "(builtin:tech.jvm.memory.pool.collectionTime:filter(in(\"dt.entity.process_group_instance\",entitySelector(\"type(~\"PROCESS_GROUP_INSTANCE~\"),entityName.contains(~\"App~\")\")))):names"),
            /*
            JVM metrics end
             */

            /*
            DISK metrics start
             */
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
                    "(builtin:host.disk.readOps:splitBy(\"dt.entity.host\"):sum):names"),
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
                    "(builtin:host.disk.bytesWritten:splitBy(\"dt.entity.host\")):sum:names"),
            /*
            DISK metrics end
             */

            /*
            NETWORK metrics start
             */
            new MetricDefinition(
                    MetricMeasurement.NETWORK,
                    "traffic_out",
                    "(builtin:host.net.nic.trafficOut:splitBy(\"dt.entity.host\"):sum):names"),
            new MetricDefinition(
                    MetricMeasurement.NETWORK,
                    "traffic_in",
                    "(builtin:host.net.nic.trafficIn:splitBy(\"dt.entity.host\"):sum):names")
            /*
            NETWORK metrics end
             */

//          TODO: if new metrics will available

//            new MetricDefinition(
//                    "",
//                    "")
    );
}
