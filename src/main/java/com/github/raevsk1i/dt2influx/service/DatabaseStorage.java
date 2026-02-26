package com.github.raevsk1i.dt2influx.service;

import com.github.raevsk1i.dt2influx.entity.DatabaseInfo;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
        loadDatabases();
    }

    public void addDatabase(DatabaseInfo databaseInfo) {
        databases.put(databaseInfo.getHost(), databaseInfo);
    }

    public void deleteDatabase(String host) {
        databases.remove(host);
    }

    public List<DatabaseInfo> getAllDatabases() {
        List<DatabaseInfo> dbs = new ArrayList<>();
        databases.values().stream().parallel().forEach(dbs::add);
        return dbs;
    }

    private void loadDatabases() {
        /*
        TODO: реализовать логику загрузки баз данных из базы данных)
         */
    }
}
