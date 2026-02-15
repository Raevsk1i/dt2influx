package com.github.raevsk1i.dt2influx.service;

import com.github.raevsk1i.dt2influx.config.ReflexConfig;
import com.github.raevsk1i.dt2influx.dto.request.CreateFromToJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.CreateJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.StopJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.response.JobResponseDto;
import com.github.raevsk1i.dt2influx.dto.response.JobsAliveResponseDto;
import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.enums.JobType;
import com.github.raevsk1i.dt2influx.enums.StopJobStatus;
import com.github.raevsk1i.dt2influx.job.AbstractJob;
import com.github.raevsk1i.dt2influx.utils.RequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final IJobScheduler scheduler;
    private final IJobFactory factory;
    private final ReflexConfig reflexConfig;
    private final RequestValidator validator;

    private final Map<String, AbstractJob> createdJobs = new ConcurrentHashMap<>();

    /**
     * Creates and schedules a new job based on the provided request data.
     * Validates the request, checks for existing jobs with the same namespace,
     * creates job metadata, instantiates the job via factory, schedules it with
     * the configured interval, and stores it in the tracking map.
     * Returns appropriate HTTP response with job details or error message.
     *
     * @param request DTO containing job creation parameters (namespace and mzId)
     * @return ResponseEntity with JobResponseDto containing job info and status message
     */
    public ResponseEntity<JobResponseDto> createJob(CreateJobRequestDto request) {
        if (!validator.isValidRequest(request)) {
            return ResponseEntity.badRequest()
                    .body(new JobResponseDto(null, "Invalid request: namespace and mzId are required"));
        }

        String namespace = request.getNamespace().toUpperCase();

        if (createdJobs.containsKey(namespace)) {
            log.warn("Job already exists for namespace: {}", namespace);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new JobResponseDto(null, "Job with this namespace already exists"));
        }

        JobInfo jobInfo = new JobInfo(namespace, request.getMzId(), JobType.FP);
        AbstractJob job = factory.createJob(jobInfo);

        try {
            scheduler.executeJob(job, reflexConfig.getInterval());
            createdJobs.put(namespace, job);


            return ResponseEntity.ok(new JobResponseDto(job.getInfo(), "Job created successfully"));
        } catch (Exception e) {
            log.error("Failed to schedule job for namespace: {}", namespace, e);
            return ResponseEntity.internalServerError()
                    .body(new JobResponseDto(null, "Failed to create job: " + e.getMessage()));
        }
    }

    /**
     * Stops a previously created job identified by its namespace.
     * Validates the request, retrieves the job from the tracking map,
     * requests the scheduler to stop the job, removes it from tracking,
     * and handles various failure scenarios including interruptions.
     * Returns appropriate HTTP response with operation result.
     *
     * @param request DTO containing the namespace of the job to stop
     * @return ResponseEntity with JobResponseDto containing job info and status message
     */
    public ResponseEntity<JobResponseDto> stopJob(StopJobRequestDto request) {
        if (!validator.isValidRequest(request)) {
            return ResponseEntity.badRequest()
                    .body(new JobResponseDto(null, "Namespace is required"));
        }

        String namespace = request.getNamespace().toUpperCase();
        AbstractJob job = createdJobs.get(namespace);

        if (job == null) {
            log.warn("Job not found for namespace: {}", namespace);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new JobResponseDto(null, "Job not found"));
        }

        StopJobStatus stopStatus = scheduler.stopScheduledJob(namespace);

        if (stopStatus == StopJobStatus.SUCCESS) {
            createdJobs.remove(namespace);
            log.info("Job stopped successfully for namespace: {}", namespace);
            return ResponseEntity.ok(new JobResponseDto(job.getInfo().cancel(), "Job stopped successfully"));
        }

        if (stopStatus == StopJobStatus.NOT_FOUND) {
            createdJobs.remove(namespace);
            log.warn("Scheduled job not found for namespace: {}", namespace);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new JobResponseDto(job.getInfo(), "Scheduled job not found"));
        }

        log.error("Failed to stop job for namespace: {}", namespace);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new JobResponseDto(job.getInfo(), "Failed to stop job"));
    }

    /**
     * Creates and executes a one-time job based on the provided request parameters.
     * This method is designed for running jobs that execute immediately for a specific
     * time range (from-to) rather than on a recurring schedule.
     * <p>
     * The method validates the request using a dedicated validator, creates job metadata
     * with ONETIME type, instantiates the job via factory, and immediately executes it
     * without scheduling. Unlike scheduled jobs, one-time jobs are not tracked in the
     * createdJobs map for long-term management.
     * <p>
     * Note: This implementation differs from createJob() which schedules recurring jobs
     * with FP (Face Props) type.
     *
     * @param request DTO containing parameters for one-time job creation including namespace and mzId
     * @return ResponseEntity with JobResponseDto containing job information and execution status
     */
    public ResponseEntity<JobResponseDto> createFromToJob(CreateFromToJobRequestDto request) {
        if (!validator.isValidRequest(request)) {
            return ResponseEntity.badRequest()
                    .body(new JobResponseDto(null, "Invalid request: namespace and mzId are required"));
        }

        String namespace = request.getNamespace().toUpperCase();

        JobInfo jobInfo = new JobInfo(namespace, request.getMzId(), JobType.ONETIME);
        AbstractJob job = factory.createJob(jobInfo);

        try {
            scheduler.executeJob(job);

            return ResponseEntity.ok(new JobResponseDto(job.getInfo(), "Job created successfully"));
        } catch (Exception e) {
            log.error("Failed to schedule job for namespace: {}", namespace, e);
            return ResponseEntity.internalServerError()
                    .body(new JobResponseDto(null, "Failed to create job: " + e.getMessage()));
        }
    }

    /**
     * Retrieves information about all currently active (alive) jobs.
     * Collects job information from the internal tracking map and returns
     * a list of all jobs that are currently scheduled and running.
     * Returns an empty list if no active jobs exist.
     *
     * @return ResponseEntity with JobsAliveResponseDto containing list of active job information
     */
    public ResponseEntity<JobsAliveResponseDto> getAliveJobs() {
        // Get all currently active jobs from the tracking map
        List<JobInfo> activeJobs = createdJobs.values().stream()
                .map(AbstractJob::getInfo)
                .collect(Collectors.toList());

        log.info("Retrieved {} active jobs", activeJobs.size());

        // Create response DTO with the collected job information
        JobsAliveResponseDto response = new JobsAliveResponseDto();
        response.setJobs(activeJobs);

        // Return successful response with the list of active jobs
        return ResponseEntity.ok(response);
    }
}