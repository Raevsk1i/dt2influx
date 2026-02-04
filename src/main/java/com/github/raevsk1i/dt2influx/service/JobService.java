package com.github.raevsk1i.dt2influx.service;

import com.github.raevsk1i.dt2influx.dto.request.CreateFromToJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.CreateJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.StopJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.response.JobResponseDto;
import com.github.raevsk1i.dt2influx.dto.response.JobsAliveResponseDto;
import com.github.raevsk1i.dt2influx.service.impl.JobSchedulerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    private final IJobScheduler scheduler;
    private final JobFactory

    @Autowired
    public JobService(JobSchedulerImpl scheduler) {
        this.scheduler = scheduler;
    }

    public ResponseEntity<JobResponseDto> createJob(CreateJobRequestDto request) {
        return null;
    }

    public ResponseEntity<JobResponseDto> stopJob(StopJobRequestDto request) {
        return null;
    }

    public ResponseEntity<JobResponseDto> createFromToJob(CreateFromToJobRequestDto request) {
        return null;
    }

    public ResponseEntity<JobsAliveResponseDto> getAliveJobs() {
        return null;
    }
}
