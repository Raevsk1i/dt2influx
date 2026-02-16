package com.github.raevsk1i.dt2influx.job;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.utils.HttpsUtils;
import lombok.Getter;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

@Getter
public abstract class AbstractJob implements Runnable {

    private final JobInfo info;
    private final String reflexUrl;
    private final String token;
    private final HttpClient httpClient;

    public AbstractJob(JobInfo info, String reflexUrl, String token) {
        this.info = Objects.requireNonNull(info, "JobInfo must not be null");
        this.reflexUrl = Objects.requireNonNull(reflexUrl, "ReflexUrl must not be null");
        this.token = Objects.requireNonNull(token, "Token must not be null");
        this.httpClient = Objects.requireNonNull(HttpsUtils.getHttpClient(), "httpClient must not be null");
    }

    @Override
    public abstract void run();

    /**
     * Encodes parameters into URL query string format.
     * Uses UTF-8 encoding consistently across all platforms.
     *
     * @param params         query parameters map
     * @param metricSelector the metric selector to encode
     * @param reflexUrl      the base URL
     * @return complete URL with encoded query parameters
     */
    protected String encodeParams(Map<String, String> params, String metricSelector, String reflexUrl) {
        StringJoiner joiner = new StringJoiner("&");

        joiner.add("metricSelector=" + URLEncoder.encode(metricSelector, StandardCharsets.UTF_8));
        params.forEach((key, value) ->
                joiner.add(key + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8)));

        return reflexUrl + joiner;
    }

}
