package com.github.raevsk1i.dt2influx.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.raevsk1i.dt2influx.config.metrics.FpMetrics;
import com.github.raevsk1i.dt2influx.dto.reflex.additional.DataResult;
import com.github.raevsk1i.dt2influx.dto.reflex.response.ReflexResponse;
import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.entity.MetricDefinition;
import com.github.raevsk1i.dt2influx.enums.MetricMeasurement;
import com.github.raevsk1i.dt2influx.exceptions.HttpClientIsNullException;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FPJob extends AbstractJob {

    private final Map<String, String> params;

    public FPJob(JobInfo info, String reflexUrl, String token) {
        super(info, reflexUrl, token);
        params = new LinkedHashMap<>();
        params.put("from", info.getFrom());
        params.put("to", info.getTo());
        params.put("resolution", "1m");
        params.put("mzSelector", info.getMzId());
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        try (InfluxDB influxClient = InfluxUtils.getInfluxClient()) {

            HttpClient client = HttpsUtils.getHttpClient();

            if (client == null) {
                throw new HttpClientIsNullException("Http client is null");
            } else if (influxClient == null) {
                throw new InfluxDBException("InfluxDB client is null");
            }

            for (MetricDefinition def : FpMetrics.metrics) {
                String encodedParams = encodeParams(params, def.metricSelector(), getReflexUrl());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(encodedParams))
                        .header("Accept", "application/json")
                        .header("Authorization", getToken())
                        .timeout(Duration.ofSeconds(120))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    log.error("Reflex returned a response with a status code other than 200\nmetric: {}\nmeasurement: {}", def.field(), def.measurement());
                    continue;
                }
                ReflexResponse reflexResponse = mapper.readValue(response.body(), ReflexResponse.class);

                if (!isValidResponse(reflexResponse)) {
                    log.error("Reflex response is not valid for metric: {}", def.field());
                    continue;
                }

                List<Point> points = processMetrics(def, reflexResponse);

                if (points == null) {
                    log.error("List points for influx batch is empty!");
                    continue;
                }

                BatchPoints batchPoints = BatchPoints.builder().points(points).build();

                if (!influxClient.isBatchEnabled()) {
                    influxClient.enableBatch();
                }

                influxClient.write(batchPoints);
                influxClient.flush();


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Point> processMetrics(MetricDefinition def, ReflexResponse response) {
        List<DataResult> dataResultList = response.getResult().getFirst().getData();
        return switch (def.measurement()) {
            case MetricMeasurement.CPU, MetricMeasurement.MEMORY -> processContainerMetrics(def, dataResultList);
            case MetricMeasurement.JVM -> processProcessMetrics(def, dataResultList);
            case MetricMeasurement.DISK, MetricMeasurement.NETWORK -> processHostMetrics(def, dataResultList);
        };
    }

    private List<Point> processHostMetrics(MetricDefinition def, List<DataResult> dataList) {
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
                return points;
            }
        }
        return null;
    }

    private List<Point> processContainerMetrics(MetricDefinition def, List<DataResult> dataList) {
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
                return points;
            }
        }
        return null;
    }

    private List<Point> processProcessMetrics(MetricDefinition def, List<DataResult> dataList) {
        for (DataResult data : dataList) {
            Map<String, String> dimensions = data.getDimensionMap();
            String poolname = dimensions.getOrDefault("poolname", "threads");
            String process = "";

            Matcher matcher = Pattern.compile("\\(([^()]*)\\)").matcher(dimensions.get("dt.entity.process_group_instance.name"));

            if (matcher.find()) {
                process = matcher.group(1);
            } else {
                return null;
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
                return points;
            }
        }
        return null;
    }

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

    private boolean isValidResponse(ReflexResponse response) {
        return response != null &&
                response.getResult() != null &&
                !response.getResult().isEmpty() &&
                response.getResult().getFirst().getData() != null &&
                !response.getResult().getFirst().getData().isEmpty();

    }
}
