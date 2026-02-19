package com.github.raevsk1i.dt2influx.controller;
import com.github.raevsk1i.dt2influx.dto.request.CreateFromToFSJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.StopJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.response.JobResponseDto;
import com.github.raevsk1i.dt2influx.dto.response.JobsAliveResponseDto;
import com.github.raevsk1i.dt2influx.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/dt2influx/api/v1/jobs")
public class JobController {
    private final IJobService service;

    @Autowired
    public JobController(IJobService service) {
        this.service = service;
    }

    @PostMapping("/create/job")
    public ResponseEntity<JobResponseDto> createJob(@RequestBody Object request) {
        return service.createJob(request);
    }

    @PostMapping("/create/job/from-to")
    public ResponseEntity<JobResponseDto> createFromToJob(@RequestBody CreateFromToFSJobRequestDto request) {
        return service.createFromToJob(request);
    }

    @PostMapping("/stop/job")
    public ResponseEntity<JobResponseDto> stopJob(@RequestBody StopJobRequestDto request) {
        return service.stopJob(request);
    }

    @GetMapping("/get/jobs")
    public ResponseEntity<JobsAliveResponseDto> getAliveJobs() {
        return service.getAliveJobs();
    }



}
