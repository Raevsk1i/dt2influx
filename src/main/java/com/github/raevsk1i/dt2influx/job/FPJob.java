package com.github.raevsk1i.dt2influx.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.raevsk1i.dt2influx.config.metrics.FpMetrics;
import com.github.raevsk1i.dt2influx.dto.reflex.additional.DataResult;
import com.github.raevsk1i.dt2influx.dto.reflex.response.ReflexResponse;
import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import com.github.raevsk1i.dt2influx.enums.MetricMeasurement;
import com.github.raevsk1i.dt2influx.exceptions.HttpClientIsNullException;
import com.github.raevsk1i.dt2influx.exceptions.ReflexResponseIsNotValidException;
import com.github.raevsk1i.dt2influx.utils.HttpsUtils;
import com.github.raevsk1i.dt2influx.utils.InfluxUtils;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBException;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Job for fetching metrics from Reflex API and writing them to InfluxDB.
 * <p>
 * Handles CPU, Memory, JVM, Disk, and Network metrics with proper
 * resource management and error handling.
 * </p>
 */
@Slf4j
public class FPJob extends AbstractJob {

    private final Map<String, String> params;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Pattern PROCESS_NAME_PATTERN = Pattern.compile("\\(([^()]*)\\)");
    private static final String RESOLUTION = "1m";
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(120);

    public FPJob(JobInfo info, String reflexUrl, String token) {
        super(info, reflexUrl, token);
        this.params = buildDefaultParams(info);
    }

    /**
     * Builds default query parameters for Reflex API.
     *
     * @param info job configuration information
     * @return immutable map of query parameters
     */
    private Map<String, String> buildDefaultParams(JobInfo info) {
        Map<String, String> params = new LinkedHashMap<>(4);
        params.put("from", info.getFrom());
        params.put("to", info.getTo());
        params.put("resolution", RESOLUTION);
        params.put("mzSelector", info.getMzId());
        return Collections.unmodifiableMap(params);
    }

    @Override
    public void run() {
        HttpClient httpClient = validateAndGetHttpClient();

        try (InfluxDB influxClient = validateAndGetInfluxClient()) {

            for (MetricDefinition def : FpMetrics.metrics) {

                Optional<ReflexResponse> response = fetchMetricResponse(def, httpClient);

                if (response.isEmpty()) {
                    throw new ReflexResponseIsNotValidException("Reflex response isn't valid");
                }

                processMetrics(def, response.get(), influxClient);

            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Job execution was interrupted", e);
        }
        catch (HttpClientIsNullException e) {
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

    /**
     * Validates and returns a healthy HTTP client.
     *
     * @return validated HTTP client
     * @throws HttpClientIsNullException if the client is null or terminated
     */
    private HttpClient validateAndGetHttpClient() {
        HttpClient httpClient = getHttpClient();
        if (httpClient == null || httpClient.isTerminated()) {
            throw new HttpClientIsNullException("HTTP client is null or terminated");
        }
        return httpClient;
    }

    /**
     * Validates and returns a healthy InfluxDB client.
     *
     * @return validated InfluxDB client
     * @throws InfluxDBException if the client is null or unreachable
     */
    private InfluxDB validateAndGetInfluxClient() {
        InfluxDB influxClient = InfluxUtils.getInfluxClient();
        if (influxClient == null || !influxClient.ping().isGood()) {
            throw new InfluxDBException("InfluxDB client is null or unreachable");
        }
        return influxClient;
    }

    private void processMetrics(MetricDefinition def, ReflexResponse response, InfluxDB influxClient) {
        List<DataResult> dataResultList = response.getResult().getFirst().getData();
        switch (def.measurement()) {
            case MetricMeasurement.CPU, MetricMeasurement.MEMORY -> processContainerMetrics(def, dataResultList, influxClient);
            case MetricMeasurement.JVM -> processProcessMetrics(def, dataResultList, influxClient);
            case MetricMeasurement.DISK, MetricMeasurement.NETWORK -> processHostMetrics(def, dataResultList, influxClient);
        };
    }

    private void processHostMetrics(MetricDefinition def, List<DataResult> dataList,  InfluxDB influxClient) {
        for (DataResult data : dataList) {
            Map<String, String> dimensions = data.getDimensionMap();
            String host = dimensions.get("dt.entity.host.name");

            List<Point> points = createPoints(
                    data,
                    def.measurement(),
                    def.field(),
                    Map.of("host", host,
                            "namespace", getInfo().getNamespace().toLowerCase())
            );
            if (!points.isEmpty()) {
                writeToInfluxDB(influxClient, points, def);
            }
        }
    }

    private void processContainerMetrics(MetricDefinition def, List<DataResult> dataList,  InfluxDB influxClient) {
        for (DataResult data : dataList) {
            Map<String, String> dimensions = data.getDimensionMap();
            String instance = dimensions.get("dt.entity.container_group_instance.name").split(" ", 2)[0];
            String container = dimensions.get("Container");

            List<Point> points = createPoints(
                    data,
                    def.measurement(),
                    def.field(),
                    Map.of("instance", instance,
                            "container", container,
                            "namespace", getInfo().getNamespace().toLowerCase())
            );
            if (!points.isEmpty()) {
                writeToInfluxDB(influxClient, points, def);
            }
        }
    }

    /**
     * Processes process metrics.
     *
     * @param def       the metric definition
     * @param dataList  the data results from API
     */
    private void processProcessMetrics(MetricDefinition def, List<DataResult> dataList,   InfluxDB influxClient) {
        for (DataResult data : dataList) {
            Map<String, String> dimensions = data.getDimensionMap();
            String poolname = dimensions.getOrDefault("poolname", "threads");
            String process = "";

            Matcher matcher = PROCESS_NAME_PATTERN.matcher(dimensions.get("dt.entity.process_group_instance.name"));

            if (matcher.find()) {
                process = matcher.group(1);
            } else {
                return;
            }

            List<Point> points = createPoints(
                    data,
                    def.measurement(),
                    def.field(),
                    Map.of("process", process,
                            "poolname", poolname,
                            "namespace", getInfo().getNamespace().toLowerCase())
            );
            if (!points.isEmpty()) {
                writeToInfluxDB(influxClient, points, def);
            }
        }
    }

    /**
     * Fetches metric data from Reflex API.
     *
     * @param def        the metric definition
     * @param httpClient the HTTP client
     * @return Optional containing the response, or empty if fetch failed
     * @throws InterruptedException if the execution is interrupted
     * @throws IOException if an I/O error occurs
     */
    private Optional<ReflexResponse> fetchMetricResponse(MetricDefinition def, HttpClient httpClient)
            throws InterruptedException, IOException {

        HttpRequest request = buildHttpRequest(def);
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

    /**
     * Builds an HTTP request for fetching metric data.
     *
     * @param def the metric definition
     * @return the built HTTP request
     */
    private HttpRequest buildHttpRequest(MetricDefinition def) {
        String encodedParams = encodeParams(params, def.metricSelector(), getReflexUrl());

        return HttpRequest.newBuilder()
                .uri(URI.create(encodedParams))
                .header("Accept", "application/json")
                .header("Authorization", getToken())
                .timeout(REQUEST_TIMEOUT)
                .GET()
                .build();
    }

    /**
     * Creates data points from the data result.
     *
     * @param dataResult  the data result containing timestamps and values
     * @param measurement the measurement type
     * @param field       the field name
     * @param tags        the tags to include
     * @return list of data points
     */
    private List<Point> createPoints(DataResult dataResult,
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

    /**
     * Writes points to InfluxDB with proper error handling.
     *
     * @param influxClient the InfluxDB client
     * @param points       the points to write
     * @param def          the metric definition for logging context
     */
    private void writeToInfluxDB(InfluxDB influxClient, List<Point> points, MetricDefinition def) {
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

    /**
     * Validates the Reflex API response structure.
     *
     * @param response the response to validate
     * @return true if response is valid, false otherwise
     */
    private boolean isValidResponse(ReflexResponse response) {
        return response != null &&
                response.getResult() != null &&
                !response.getResult().isEmpty() &&
                response.getResult().getFirst().getData() != null &&
                !response.getResult().getFirst().getData().isEmpty();

    }
}
