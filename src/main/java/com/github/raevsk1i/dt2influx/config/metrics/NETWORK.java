package com.github.raevsk1i.dt2influx.config.metrics;

import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import lombok.Data;

import java.util.List;

@Data
public final class NETWORK {
    public static final List<MetricDefinition> metrics = List.of(
            new MetricDefinition(
                    "traffic_out",
                    "(builtin:host.net.nic.trafficOut:splitBy(\"dt.entity.host\"):sum):names"),
            new MetricDefinition(
                    "traffic_in",
                    "(builtin:host.net.nic.trafficIn:splitBy(\"dt.entity.host\"):sum):names")


//          TODO: if new metrics will available

//            new MetricDefinition(
//                    "",
//                    "")
    );
}
