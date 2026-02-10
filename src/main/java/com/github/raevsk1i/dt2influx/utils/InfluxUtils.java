package com.github.raevsk1i.dt2influx.utils;

import com.github.raevsk1i.dt2influx.config.InfluxConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.TimeUnit;

@Slf4j
@EnableAsync
@RequiredArgsConstructor
public final class InfluxUtils {

    private static InfluxConfig config;

    public static synchronized InfluxDB getInfluxClient() {
        try {
            InfluxDB client = InfluxDBFactory.connect(config.getUrl(), config.getUser(), config.getPass());
            client = client.enableBatch(
                    config.getBatch_size(),
                    config.getBatch_interval_ms(),
                    TimeUnit.MILLISECONDS);

            return client.setRetentionPolicy(config.getRetention_policy());
        } catch (Exception ex) {
            return null;
        }
    }
}
