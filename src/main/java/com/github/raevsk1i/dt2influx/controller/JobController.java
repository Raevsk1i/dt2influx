package com.github.raevsk1i.dt2influx.controller;

import com.github.raevsk1i.dt2influx.dto.request.CreateFromToJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.CreateJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.StopJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.response.JobResponseDto;
import com.github.raevsk1i.dt2influx.dto.response.JobsAliveResponseDto;
import com.github.raevsk1i.dt2influx.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/dt2influx/api/v1/jobs")
public class JobController {
    private final JobService service;

    @Autowired
    public JobController(JobService service) {
        this.service = service;
    }

    @PostMapping("/create/job/fp")
    public ResponseEntity<JobResponseDto> createJob(@RequestBody CreateJobRequestDto request) {
        return service.createJob(request);
    }

    @PostMapping("/stop/job")
    public ResponseEntity<JobResponseDto> stopJob(@RequestBody StopJobRequestDto request) {
        return service.stopJob(request);
    }

    @GetMapping("/get/jobs")
    public ResponseEntity<JobsAliveResponseDto> getAliveJobs() {
        return service.getAliveJobs();
    }

    @PostMapping("/create/job/from-to")
    public ResponseEntity<JobResponseDto> createFromToJob(@RequestBody CreateFromToJobRequestDto request) {
        return service.createFromToJob(request);
    }

}
