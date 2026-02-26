package com.github.raevsk1i.dt2influx.config.metrics;

import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import com.github.raevsk1i.dt2influx.enums.MetricMeasurement;

import java.util.List;

public class KafkaMetrics {

    // TODO: Добавить необходимые метрики для кафки
    public static final List<MetricDefinition> metrics = List.of(
            new MetricDefinition(
            MetricMeasurement.KAFKA,
                    "consumer_lag",
                            "")
    );
}
