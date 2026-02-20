package com.github.raevsk1i.dt2influx.service.impl;

import com.github.raevsk1i.dt2influx.dto.request.DatabaseAddRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.DatabaseDeleteRequestDto;
import com.github.raevsk1i.dt2influx.dto.response.DatabaseResponseDto;
import com.github.raevsk1i.dt2influx.entity.DatabaseInfo;
import com.github.raevsk1i.dt2influx.service.DatabaseStorage;
import com.github.raevsk1i.dt2influx.service.IDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService implements IDatabaseService {

    private final DatabaseStorage databaseStorage;

    @Autowired
    public DatabaseService(DatabaseStorage databaseStorage) {
        this.databaseStorage = databaseStorage;
    }

    @Override
    public ResponseEntity<DatabaseResponseDto> addDatabase(DatabaseAddRequestDto requestDto) {
        for (DatabaseInfo databaseInfo : databaseStorage.getAllDatabases()) {
            databaseStorage.addDatabase(databaseInfo);
        }

        return ResponseEntity.ok().body(DatabaseResponseDto.builder()
                .success(true)
                .message("Databases added successfully")
                .count(requestDto.getDatabases().size())
                .added_databases(requestDto.getDatabases())
                .build());
    }

    @Override
    public ResponseEntity<DatabaseResponseDto> deleteDatabase(DatabaseDeleteRequestDto requestDto) {
        for (DatabaseInfo databaseInfo : databaseStorage.getAllDatabases()) {
            databaseStorage.deleteDatabase(databaseInfo.getHost());
        }

        return ResponseEntity.ok().body(DatabaseResponseDto.builder()
                .success(true)
                .message("Databases deleted successfully")
                .count(requestDto.getDatabases().size())
                .added_databases(requestDto.getDatabases())
                .build());
    }

    @Override
    public ResponseEntity<DatabaseResponseDto> getAllDatabases() {
        List<DatabaseInfo> list = databaseStorage.getAllDatabases();
        return ResponseEntity.ok().body(DatabaseResponseDto.builder()
                .success(true)
                .message("Got all databases")
                .count(list.size())
                .added_databases(list)
                .build());
    }
}
