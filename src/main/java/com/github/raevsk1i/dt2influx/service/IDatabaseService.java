package com.github.raevsk1i.dt2influx.service;

import com.github.raevsk1i.dt2influx.dto.request.DatabaseAddRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.DatabaseDeleteRequestDto;
import com.github.raevsk1i.dt2influx.dto.response.DatabaseResponseDto;
import org.springframework.http.ResponseEntity;

public interface IDatabaseService {

    ResponseEntity<DatabaseResponseDto> addDatabase(DatabaseAddRequestDto requestDto);

    ResponseEntity<DatabaseResponseDto> deleteDatabase(DatabaseDeleteRequestDto requestDto);

    ResponseEntity<DatabaseResponseDto> getAllDatabases();



}
