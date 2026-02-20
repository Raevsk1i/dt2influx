package com.github.raevsk1i.dt2influx.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.raevsk1i.dt2influx.config.ReflexConfig;
import com.github.raevsk1i.dt2influx.config.metrics.DBMetrics;
import com.github.raevsk1i.dt2influx.dto.reflex.additional.DataResult;
import com.github.raevsk1i.dt2influx.dto.reflex.response.ReflexResponse;
import com.github.raevsk1i.dt2influx.entity.DatabaseInfo;
import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import com.github.raevsk1i.dt2influx.exceptions.ReflexResponseIsNotValidException;
import com.github.raevsk1i.dt2influx.service.DatabaseStorage;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBException;
import org.influxdb.dto.Point;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

@Slf4j
public class DBJob extends AbstractJob {

    private final Map<String, String> params;
    private final DatabaseStorage databaseStorage;
    private final String namespace;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String RESOLUTION = "1m";
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(120);

    public DBJob(JobInfo info, DatabaseStorage databaseStorage, ReflexConfig reflexConfig) {
        super(info, reflexConfig);
        this.databaseStorage = databaseStorage;
        this.namespace = info.getId();
        this.params = buildDefaultParams(info);
    }

    private Map<String, String> buildDefaultParams(JobInfo info) {
        Map<String, String> params = new LinkedHashMap<>(4);
        params.put("from", info.getFrom());
        params.put("to", info.getTo());
        params.put("resolution", RESOLUTION);
        return Collections.unmodifiableMap(params);
    }

    @Override
    public void run() {

        if (databaseStorage.getAllDatabases().isEmpty()) {
            return;
        }

        HttpClient httpClient = validateAndGetHttpClient();

        try (InfluxDB influxClient = validateAndGetInfluxClient()) {

            for (DatabaseInfo database : databaseStorage.getAllDatabases()) {
                for (MetricDefinition def : DBMetrics.metrics) {

                    Optional<ReflexResponse> response = fetchMetricResponse(def, httpClient, database.getHost());

                    if (response.isEmpty()) {
                        throw new ReflexResponseIsNotValidException("Reflex response isn't valid");
                    }

                    List<DataResult> dataResultList = response.get().getResult().getFirst().getData();

                    processHostMetrics(def, dataResultList, influxClient, database.getDatabase());

                }
            }

        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Job execution was interrupted", e);
        }
        catch (HttpClientErrorException e) {
            log.error("httpClient is null or httpClient is terminated", e);
        }
        catch (InfluxDBException e) {
            log.error("influxClient is null or influxClient is terminated", e);
        }
        catch (ReflexResponseIsNotValidException e) {
            log.error("Reflex API response error", e);
        }
        catch (Exception e) {
            log.error("Job execution failed", e);
        }
    }

    private void processHostMetrics(MetricDefinition def, List<DataResult> dataList, InfluxDB influxClient, String database) {
        for (DataResult data : dataList) {
            Map<String, String> dimensions = data.getDimensionMap();
            String host = dimensions.get("dt.entity.host.name");

            List<Point> points = createPoints(
                    data,
                    def.measurement(),
                    def.field(),
                    Map.of("host", host,
                            "namespace", namespace.toLowerCase(),
                            "database", database)
            );
            if (!points.isEmpty()) {
                writeToInfluxDB(influxClient, points, def);
            }
        }
    }

    private Optional<ReflexResponse> fetchMetricResponse(MetricDefinition def, HttpClient httpClient, String host)
            throws InterruptedException, IOException {

        HttpRequest request = buildHttpRequest(def, host);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            log.error("Reflex returned non-200 status code: {} for metric: {} (measurement: {})",
                    response.statusCode(), def.field(), def.measurement());
            log.error("Reflex response body: {}", response.body());
            return Optional.empty();
        }

        ReflexResponse reflexResponse = MAPPER.readValue(response.body(), ReflexResponse.class);

        if (!isValidResponse(reflexResponse)) {
            log.error("Invalid Reflex response for metric: {}", def.field());
            return Optional.empty();
        }
        return Optional.of(reflexResponse);
    }

    private HttpRequest buildHttpRequest(MetricDefinition def, String host) {
        String metricSelector = def.metricSelector().replaceAll("REPLACE_IT_FOR_DB_HOST", host);
        String encodedParams = encodeParams(params, metricSelector, getReflexUrl());

        return HttpRequest.newBuilder()
                .uri(URI.create(encodedParams))
                .header("Accept", "application/json")
                .header("Authorization", getToken())
                .timeout(REQUEST_TIMEOUT)
                .GET()
                .build();
    }

    private boolean isValidResponse(ReflexResponse response) {
        return response != null &&
                response.getResult() != null &&
                !response.getResult().isEmpty() &&
                response.getResult().getFirst().getData() != null &&
                !response.getResult().getFirst().getData().isEmpty();
    }
}
