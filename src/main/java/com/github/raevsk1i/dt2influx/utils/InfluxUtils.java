package com.github.raevsk1i.dt2influx.utils;

import com.github.raevsk1i.dt2influx.config.InfluxConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@EnableAsync
@RequiredArgsConstructor
@Component
public final class InfluxUtils {

    private static InfluxConfig config;

    @Autowired
    public InfluxUtils(InfluxConfig config) {
        InfluxUtils.config = config;
    }

    public static synchronized InfluxDB getInfluxClient() {
        try {
            InfluxDB client = InfluxDBFactory.connect(config.getUrl(), config.getUser(), config.getPass());
            client.enableBatch(
                    config.getBatch_size(),
                    config.getBatch_interval_ms(),
                    TimeUnit.MILLISECONDS);

            client.setRetentionPolicy(config.getRetention_policy());
            client.setDatabase(config.getDatabase());

            return client;
        } catch (Exception ex) {
            log.error("Error while creating InfluxDB client", ex);
            return null;
        }
    }
}
