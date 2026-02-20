package com.github.raevsk1i.dt2influx.jobs;

import com.github.raevsk1i.dt2influx.config.ReflexConfig;
import com.github.raevsk1i.dt2influx.dto.reflex.additional.DataResult;
import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import com.github.raevsk1i.dt2influx.enums.MetricMeasurement;
import com.github.raevsk1i.dt2influx.utils.HttpsUtils;
import com.github.raevsk1i.dt2influx.utils.InfluxUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBException;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
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

    protected void writeToInfluxDB(InfluxDB influxClient, List<Point> points, MetricDefinition def) {
        try {
            BatchPoints batchPoints = BatchPoints.builder()
                    .points(points)
                    .build();

            influxClient.write(batchPoints);
            influxClient.flush();

            log.debug("Successfully wrote {} points for metric: {}", points.size(), def.field());
        } catch (InfluxDBException e) {
            log.error("Failed to write points to InfluxDB for metric: {}", def.field(), e);
        }
    }

    protected List<Point> createPoints(DataResult dataResult,
                                     MetricMeasurement measurement,
                                     String field,
                                     Map<String, String> tags) {

        List<Point> points = new ArrayList<>();
        List<Long> timestamps = dataResult.getTimestamps();
        List<Long> values = dataResult.getValues();

        for (int i = 0; i < timestamps.size() && i < values.size(); i++) {
            Point point = Point.measurement(measurement.toString().toLowerCase())
                    .time(timestamps.get(i), TimeUnit.MILLISECONDS)
                    .addField(field, values.get(i))
                    .tag(tags)
                    .build();
            points.add(point);
        }
        return points;
    }
}
