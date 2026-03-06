package com.github.raevsk1i.dt2influx.controller;

import com.github.raevsk1i.dt2influx.dto.request.DatabaseAddRequestDto;
import com.github.raevsk1i.dt2influx.dto.response.DatabaseResponseDto;
import com.github.raevsk1i.dt2influx.service.IDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/dt2influx/api/v1/dbs")
public class DatabasesJobController {

    private IDatabaseService service;

    @Autowired
    public DatabasesJobController(IDatabaseService service) {
        this.service = service;
    }

    @PostMapping("/add/database")
    public ResponseEntity<DatabaseResponseDto>  addDatabase(@RequestBody DatabaseAddRequestDto request) {
        return service.addDatabase(request);
    }

    @GetMapping("/get/databases")
    public ResponseEntity<DatabaseResponseDto> getDatabases() {
        return service.getAllDatabases();
    }
}
