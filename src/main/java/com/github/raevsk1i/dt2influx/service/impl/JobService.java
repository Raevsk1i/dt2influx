package com.github.raevsk1i.dt2influx.service.impl;

import com.github.raevsk1i.dt2influx.config.ReflexConfig;
import com.github.raevsk1i.dt2influx.dto.request.CreateDBJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.CreateFSJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.CreateKafkaJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.StopJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.response.JobResponseDto;
import com.github.raevsk1i.dt2influx.dto.response.JobsAliveResponseDto;
import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.enums.JobType;
import com.github.raevsk1i.dt2influx.jobs.IJob;
import com.github.raevsk1i.dt2influx.jobs.ScheduledJob;
import com.github.raevsk1i.dt2influx.service.IJobFactory;
import com.github.raevsk1i.dt2influx.service.IJobScheduler;
import com.github.raevsk1i.dt2influx.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class JobService implements IJobService {

    private final IJobFactory factory;
    private final IJobScheduler scheduler;
    private final ReflexConfig reflexConfig;

    @Autowired
    public JobService(IJobFactory factory, IJobScheduler scheduler, ReflexConfig reflexConfig) {
        this.factory = factory;
        this.scheduler = scheduler;
        this.reflexConfig = reflexConfig;
    }

    @Override
    public ResponseEntity<JobResponseDto> createJob(Object rawRequest) {
        if (rawRequest instanceof CreateFSJobRequestDto request) {
            log.info("Create FS Job Request: {}", request);

            JobInfo jobInfo = new JobInfo(JobType.FS, request.getNamespace());
            IJob job = factory.createJob(jobInfo, request.getMzId());

            ScheduledJob sj = scheduler.scheduleAtFixedRate(
                    job,
                    Duration.ofSeconds(reflexConfig.getInterval())
            );

            return ResponseEntity.ok().body(JobResponseDto.builder()
                    .job_info(sj.job().getInfo())
                    .success(true)
                    .message("FS Job successfully created")
                    .build());
        }
        else if (rawRequest instanceof CreateDBJobRequestDto) {
            return ResponseEntity.badRequest().body(null);
        }
        else if (rawRequest instanceof CreateKafkaJobRequestDto) {
            return ResponseEntity.badRequest().body(null);
        }
        return buildInvalidRequestResponse(rawRequest);
    }

    @Override
    public ResponseEntity<JobResponseDto> createFromToJob(Object rawRequest) {
        if (rawRequest instanceof CreateFSJobRequestDto request) {
            log.info("Create one-time FS Job Request: {}", request);

            JobInfo jobInfo = new JobInfo(JobType.FS, request.getNamespace());
            IJob job = factory.createJob(jobInfo, request.getMzId());

            scheduler.scheduleOneTime(job);

            return ResponseEntity.ok().body(JobResponseDto.builder()
                    .job_info(job.getInfo())
                    .success(true)
                    .message("One-time FS Job successfully created")
                    .build());
        }
        else if (rawRequest instanceof CreateDBJobRequestDto) {
            return ResponseEntity.badRequest().body(null);
        }
        else if (rawRequest instanceof CreateKafkaJobRequestDto) {
            return ResponseEntity.badRequest().body(null);
        }
        return buildInvalidRequestResponse(rawRequest);
    }

    @Override
    public ResponseEntity<JobResponseDto> stopJob(Object rawRequest) {
        if (rawRequest instanceof StopJobRequestDto request) {
            log.info("Stop Job Request: {}", request);

            JobInfo info = scheduler.cancel(request.getId());
            if (info == null) {
                log.error("Can't cancel Job Request: {}", request);
                return ResponseEntity.badRequest().body(JobResponseDto.builder()
                        .job_info(null)
                        .success(false)
                        .message("Job not found")
                        .build());
            }
            log.info("Cancel Job Request: {}", request);
            return ResponseEntity.ok().body(JobResponseDto.builder()
                    .job_info(info)
                    .success(true)
                    .message("Job stopped")
                    .build());
        }
        return buildInvalidRequestResponse(rawRequest);
    }

    @Override
    public ResponseEntity<JobsAliveResponseDto> getAliveJobs() {
        return ResponseEntity.ok().body(JobsAliveResponseDto.builder()
                .jobs(scheduler.getAllScheduledJobs())
                .build());
    }

    private ResponseEntity<JobResponseDto> buildInvalidRequestResponse(Object rawRequest) {
        log.warn("Invalid request: {}", rawRequest.toString());
        return ResponseEntity.badRequest().body(JobResponseDto.builder()
                .job_info(null)
                .success(false)
                .message("Invalid request")
                .build());
    }
}
