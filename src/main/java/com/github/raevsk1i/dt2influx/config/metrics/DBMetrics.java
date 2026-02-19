package com.github.raevsk1i.dt2influx.config.metrics;

import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import com.github.raevsk1i.dt2influx.enums.MetricMeasurement;

import java.util.List;

public class DBMetrics {
    public static final List<MetricDefinition> metrics = List.of(
            /*
            CPU metrics start
             */
            new MetricDefinition(
                    MetricMeasurement.CPU,
                    "load_1m",
                    "builtin:host.cpu.load:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"), entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\"))):names"),
            new MetricDefinition(
                    MetricMeasurement.CPU,
                    "load_5m",
                    "builtin:host.cpu.load5m:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"), entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\"))):names"),
            new MetricDefinition(
                    MetricMeasurement.CPU,
                    "load_15m",
                    "builtin:host.cpu.load15m:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"), entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\"))):names")

            /*
            MEM metrics start
             */
//            new MetricDefinition(
//                    MetricMeasurement.MEMORY,
//                    "memory_total",
//                    "builtin:host.mem.total:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\")))"),
//            new MetricDefinition(
//                    MetricMeasurement.MEMORY,
//                    "memory_reclaim",
//                    "builtin:host.mem.recl:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\")))"),
//            new MetricDefinition(
//                    MetricMeasurement.MEMORY,
//                    "memory_used",
//                    "builtin:host.mem.used:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\")))"),
//            new MetricDefinition(
//                    MetricMeasurement.MEMORY,
//                    "memory_pfps",
//                    "builtin:host.mem.avail.pfps:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\")))"),
//            new MetricDefinition(
//                    MetricMeasurement.MEMORY,
//                    "memory_swap_total",
//                    "builtin:host.mem.swap.total:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\")))"),
//            new MetricDefinition(
//                    MetricMeasurement.MEMORY,
//                    "memory_swap_used",
//                    "builtin:host.mem.swap.used:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\")))"),
//
//            /*
//            NETWORK metrics start
//             */
//            new MetricDefinition(
//                    MetricMeasurement.NETWORK,
//                    "traffic_in",
//                    "builtin:host.net.nic.trafficIn:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"), entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\"))):names"),
//            new MetricDefinition(
//                    MetricMeasurement.NETWORK,
//                    "traffic_out",
//                    "builtin:host.net.nic.trafficOut:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"), entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\"))):names"),
//            new MetricDefinition(
//                    MetricMeasurement.NETWORK,
//                    "traffic_packets_rx",
//                    "builtin:host.net.nic.packets.rx:filter(or(in(\"dt.entity.network_interface\",entitySelector(\"type(NETWORK_INTERFACE) AND fromRelationships.isNetworkInterfaceOf(type(HOST),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\"))\")))):splitBy(\"dt.entity.host\"):sum"),
//            new MetricDefinition(
//                    MetricMeasurement.NETWORK,
//                    "traffic_packets_tx",
//                    "builtin:host.net.nic.packets.tx:filter(or(in(\"dt.entity.network_interface\",entitySelector(\"type(NETWORK_INTERFACE) AND fromRelationships.isNetworkInterfaceOf(type(HOST),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\"))\")))):splitBy(\"dt.entity.host\"):sum"),
//
//            /*
//            DISK metrics start
//             */
//            new MetricDefinition(
//                    MetricMeasurement.DISK,
//                    "disk_avail",
//                    "(builtin:host.disk.avail:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"), entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\"))):splitBy(\"dt.entity.host\")):names"),
//            new MetricDefinition(
//                    MetricMeasurement.DISK,
//                    "disk_used_bytes",
//                    "(builtin:host.disk.used:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"), entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\"))):splitBy(\"dt.entity.host\")):names"),
//            new MetricDefinition(
//                    MetricMeasurement.DISK,
//                    "disk_used_percent",
//                    "(builtin:host.disk.usedPct:filter(in(\"dt.entity.host\", entitySelector(\"type(~\"HOST~\"), entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\")\"))):splitBy(\"dt.entity.host\")):names"),
//            new MetricDefinition(
//                    MetricMeasurement.DISK,
//                    "disk_bytes_written",
//                    "builtin:host.disk.bytesWritten:filter(or(in(\"dt.entity.disk\",entitySelector(\"type(DISK) AND fromRelationship.isDiskOf(type(HOST),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\"))\")))):splitBy(\"dt.entity.host\"):sum"),
//            new MetricDefinition(
//                    MetricMeasurement.DISK,
//                    "disk_bytes_read",
//                    "builtin:host.disk.bytesRead:filter(or(in(\"dt.entity.disk\",entitySelector(\"type(DISK) AND fromRelationship.isDiskOf(type(HOST),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\"))\")))):splitBy(\"dt.entity.host\"):sum"),
//            new MetricDefinition(
//                    MetricMeasurement.DISK,
//                    "disk_write_ops",
//                    "builtin:host.disk.writeOps:filter(or(in(\"dt.entity.disk\",entitySelector(\"type(DISK) AND fromRelationship.isDiskOf(type(HOST),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\"))\")))):splitBy(\"dt.entity.host\"):sum"),
//            new MetricDefinition(
//                    MetricMeasurement.DISK,
//                    "disk_read_ops",
//                    "builtin:host.disk.readOps:filter(or(in(\"dt.entity.disk\",entitySelector(\"type(DISK) AND fromRelationship.isDiskOf(type(HOST),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\"))\")))):splitBy(\"dt.entity.host\"):sum")
//

    );

    public static final List<MetricDefinition> generic_metrics = List.of(
            new MetricDefinition(
                    MetricMeasurement.MEMORY,
                    "working_set_size",
                    "builtin:tech.generic.mem.workingSetSize:filter(or(in(\"dt.entity.process_group_instance\",entitySelector(\"type(PROCESS_GROUP_INSTANCE) AND fromRelationship.isProcessOf(type(HOST),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\"))\")))):splitBy(\"dt.entity.process_group_instance\")"),
            new MetricDefinition(
                    MetricMeasurement.CPU,
                    "cpu_usage",
                    "builtin:tech.generic.cpu.usage:filter(or(in(\"dt.entity.process_group_instance\",entitySelector(\"type(PROCESS_GROUP_INSTANCE) AND fromRelationship.isProcessOf(type(HOST),entityName.contains(~\"REPLACE_IT_FOR_DB_HOST~\"))\")))):splitBy(\"dt.entity.process_group_instance\")\n")

    );
}
