package com.github.raevsk1i.dt2influx.config;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "env.influx")
public class InfluxConfig {

    @NotBlank(message = "InfluxDB URL is required")
    @Pattern(regexp = "^(http|https)://.*", message = "URL must start with http:// or https://")
    private String url;

    @NotBlank(message = "InfluxDB username is required")
    private String user;

    @NotBlank(message = "InfluxDB user password is required")
    private String pass;

    @NotBlank(message = "InfluxDB database name is required")
    private String database;

    @NotNull(message = "Batch size is required")
    @Min(1)
    @Max(10000)
    private Integer batch_size;

    @NotNull(message = "Batch interval in milliseconds is required")
    private Integer batch_interval_ms;

    @NotBlank(message = "Retention policy is required")
    private String retention_policy;

    private boolean enabled = true;

    @PostConstruct
    public void validate() {
        if (enabled) {
            Objects.requireNonNull(url, "InfluxDB URL must be set when enabled");
            Objects.requireNonNull(user, "InfluxDB username must be set when enabled");
            Objects.requireNonNull(pass, "InfluxDB password must be set when enabled");
        }
    }
}
