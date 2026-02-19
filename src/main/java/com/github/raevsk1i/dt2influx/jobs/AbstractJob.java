package com.github.raevsk1i.dt2influx.jobs;

import com.github.raevsk1i.dt2influx.config.ReflexConfig;
import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.utils.HttpsUtils;
import com.github.raevsk1i.dt2influx.utils.InfluxUtils;
import lombok.Getter;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBException;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public abstract class AbstractJob implements IJob {

    /**
     * -- GETTER --
     *  Получение JobInfo
     */
    @Getter
    private final JobInfo info;

    /**
     * -- GETTER --
     *  Получение URL Reflex
     */
    @Getter
    private final String reflexUrl;

    /**
     *  Получение токена для Reflex
     */
    private final String token;

    protected String getToken() {
        return token;
    }

    /**
     * -- GETTER --
     *  Получение кол-ва запусков с момента создания
     */
    @Getter
    private long executionCount;

    private final HttpClient httpClient;

    public AbstractJob(JobInfo info, ReflexConfig reflexConfig) {
        this.info = Objects.requireNonNull(info, "JobInfo must not be null");
        this.reflexUrl = Objects.requireNonNull(reflexConfig.getUrl(),  "ReflexUrl must not be null");
        this.token = Objects.requireNonNull(reflexConfig.getToken(),  "Token must not be null");
        this.httpClient = Objects.requireNonNull(HttpsUtils.getHttpClient(), "httpClient must not be null");
        this.executionCount = 0;
    }

    /**
     * Основной метод работы
     */
    @Override
    public abstract void run();

    private void plusExecutionCount() {
        executionCount++;
    }

    /**
     * Кодирует параметры в url query string формат.
     * Использует кодировку UTF-8
     *
     * @param params         HashMap<String, String> параметры
     * @param metricSelector MetricSelector для Reflex
     * @param reflexUrl      Dynatrace Reflex URL
     * @return закодированный url вместе с параметрами
     */
    protected String encodeParams(Map<String, String> params, String metricSelector, String reflexUrl) {
        StringJoiner joiner = new StringJoiner("&");

        joiner.add("metricSelector=" + URLEncoder.encode(metricSelector, StandardCharsets.UTF_8));
        params.forEach((key, value) ->
                joiner.add(key + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8)));

        return reflexUrl + joiner;
    }

    protected HttpClient validateAndGetHttpClient() {
        if (httpClient == null || httpClient.isTerminated()) {
            throw new HttpClientErrorException(HttpStatusCode.valueOf(1) ,"HTTP client is null or terminated");
        }
        return httpClient;
    }

    protected InfluxDB validateAndGetInfluxClient() {
        InfluxDB influxClient = InfluxUtils.getInfluxClient();
        if (influxClient == null || !influxClient.ping().isGood()) {
            throw new InfluxDBException("InfluxDB client is null or unreachable");
        }
        return influxClient;
    }
}
