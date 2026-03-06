package com.github.raevsk1i.dt2influx.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.raevsk1i.dt2influx.entity.DatabaseInfo;
import com.github.raevsk1i.dt2influx.utils.HttpsUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DatabaseStorage {

    private final Map<String, DatabaseInfo> databases = new ConcurrentHashMap<>();

    @Value("${database.loader}")
    private String loader;

    @PostConstruct
    public void init() {
        loadDatabases(loader);
    }

    public void addDatabase(DatabaseInfo databaseInfo) {
        databases.put(databaseInfo.getHost(), databaseInfo);
    }

    public void deleteDatabase(String host) {
        databases.remove(host);
    }

    public List<DatabaseInfo> getAllDatabases() {
        return new ArrayList<>(databases.values());
    }

    private void loadDatabases(String loader) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            HttpClient client = HttpsUtils.getHttpClient();

            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(loader)).build();

            log.info("Request to load databases from {}", request.toString());

            if (client == null || client.isTerminated()) {
                log.warn("client is null or terminated");
                return;
            }

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if  (response.statusCode() != 200) {
                log.warn("Failed load databases from {}", request.toString());
                log.warn(response.body());
                return;
            }

            DatabaseLoaderInfoDto loaderInfoDto = mapper.convertValue(response.body(), DatabaseLoaderInfoDto.class);

            loaderInfoDto.getDbs().stream().parallel().forEach(db -> databases.put(db.getHost(), db));
            client.close();
            log.info("Database loader successfully pushed and has been closed");

        } catch (ConnectException ex) {
            log.error("Loader uri is invalid", ex);
        } catch (IOException | InterruptedException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private void loadDatabases() {
        /*
        TODO: реализовать логику загрузки баз данных из базы данных, тем самым перегрузив метод
         */
    }


}

@Data
class DatabaseLoaderInfoDto {
    private List<DatabaseInfo> dbs;
}
