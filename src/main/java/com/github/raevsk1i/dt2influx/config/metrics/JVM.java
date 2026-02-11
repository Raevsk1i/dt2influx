package com.github.raevsk1i.dt2influx.config.metrics;

import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import com.github.raevsk1i.dt2influx.enums.MetricMeasurement;
import lombok.Data;

import java.util.List;

@Data
public final class JVM {
    public static final List<MetricDefinition> metrics = List.of(
            new MetricDefinition(
                    MetricMeasurement.JVM,
                    "pool_committed_bytes",
                    "(builtin:tech.jvm.memory.pool.committed:filter(in(\"dt.entity.process_group_instance\",entitySelector(\"type(~\"PROCESS_GROUP_INSTANCE~\"),entityName.contains(~\"App~\"\")))):names"),
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
                    "(builtin:tech.jvm.memory.pool.collectionTime:filter(in(\"dt.entity.process_group_instance\",entitySelector(\"type(~\"PROCESS_GROUP_INSTANCE~\"),entityName.contains(~\"App~\")\")))):names")


//          TODO: if new metrics will available

//            new MetricDefinition(
//                    "",
//                    "")
    );
}
