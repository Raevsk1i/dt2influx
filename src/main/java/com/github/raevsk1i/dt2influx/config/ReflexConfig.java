package com.github.raevsk1i.dt2influx.config;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Slf4j
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "env.reflex")
public class ReflexConfig {

    @NotBlank(message = "Dynatrace Reflex URL is required")
    @Pattern(regexp = "^(http|https)://.*", message = "URL must start with http:// or https://")
    private String url;

    @NotBlank(message = "Dynatrace token is required")
    private String token;

    @NotBlank(message = "Request period is required")
    private String period;

    @NotNull(message = "Request interval is required")
    private Integer interval;

    private boolean enabled = true;

    @PostConstruct
    public void validate() {
        if (enabled) {
            Objects.requireNonNull(url, "Dynatrace Reflex URL must be set when enabled");
            Objects.requireNonNull(token, "Dynatrace token must be set when enabled");
            Objects.requireNonNull(interval, "Request interval must be set when enabled");
            Objects.requireNonNull(period, "Request period must be set when enabled");
        }
    }
}
