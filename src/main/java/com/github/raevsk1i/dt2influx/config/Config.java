package com.github.raevsk1i.dt2influx.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        InfluxConfig.class,
        ReflexConfig.class,
})

public class Config {
}
