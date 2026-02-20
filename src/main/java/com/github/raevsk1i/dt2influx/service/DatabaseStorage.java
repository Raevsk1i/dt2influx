package com.github.raevsk1i.dt2influx.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.raevsk1i.dt2influx.entity.DatabaseInfo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DatabaseStorage {

    private final Map<String, DatabaseInfo> databases = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${database.storage.file}")
    private String storageFile;

    @PostConstruct
    public void init() {
        loadFromFile();
    }

    public void addDatabase(DatabaseInfo databaseInfo) {
        databases.put(databaseInfo.getHost(), databaseInfo);
        saveToFile();
    }

    public void deleteDatabase(String host) {
        databases.remove(host);
        saveToFile();
    }

    public List<DatabaseInfo> getAllDatabases() {
        return databases.values().stream().toList();
    }

    private void loadFromFile() {
        File file = new File(storageFile);
        if (file.exists()) {
            try {
                Map<String, DatabaseInfo> loaded = new LinkedHashMap<>();
                loaded = objectMapper.readValue(file, loaded.getClass());

                databases.clear();
                databases.putAll(loaded);
            } catch (IOException e) {
                throw new RuntimeException("Не удалось загрузить базы данных из файла: " + storageFile, e);
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("error creating file {}", storageFile, e);
            }
        }
    }

    @PreDestroy
    private void saveToFile() {
        try {
            objectMapper.writeValue(new File(storageFile), databases);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить базы данных в файл: " + storageFile, e);
        }
    }
}
