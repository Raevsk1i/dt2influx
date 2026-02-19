package com.github.raevsk1i.dt2influx.service.impl;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.jobs.IJob;
import com.github.raevsk1i.dt2influx.jobs.ScheduledJob;
import com.github.raevsk1i.dt2influx.service.IJobScheduler;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@Slf4j
@Component
public class JobScheduler implements IJobScheduler {

    private final ConcurrentHashMap<String, ScheduledJob> jobMap;
    private final ScheduledExecutorService scheduler;

    public JobScheduler() {
        this.scheduler = new ScheduledThreadPoolExecutor(4);
        jobMap = new ConcurrentHashMap<>();
    }

    @Override
    public ScheduledJob scheduleAtFixedRate(IJob job, Duration interval) {

        String id = job.getInfo().getId().toUpperCase();

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(job, 0, interval.getSeconds(), TimeUnit.SECONDS);
        log.info("Scheduled job {} at fixed rate: {}", id, interval);

        ScheduledJob scheduledJob = new ScheduledJob(
                id,
                job,
                future,
                interval
        );
        jobMap.putIfAbsent(id, scheduledJob);
        return scheduledJob;
    }

    @Override
    public IJob scheduleOneTime(IJob job) {
        String id = job.getInfo().getId().toUpperCase();
        scheduler.execute(job);
        log.info("executed one time job {}", id);
        return job;
    }

    @Override
    public String scheduleAtFixedRate(IJob job, Duration initialDelay, Duration interval) {
        return "";
    }

    @Override
    public String scheduleWithFixedDelay(IJob job, Duration delay) {
        return "";
    }

    @Override
    public JobInfo cancel(String jobId) {
        ScheduledJob job = jobMap.get(jobId);
        job.cancel();
        log.info("cancelled job {}", jobId);
        jobMap.remove(jobId);
        return job.job().getInfo().copy();
    }

    @Override
    public void cancelAll() {
        for (ScheduledJob job : jobMap.values()) {
            job.cancel();
        }
        jobMap.clear();
    }

    @Override
    public Optional<ScheduledJob> getScheduledJob(String jobId) {
        return Optional.empty();
    }

    @Override
    public List<ScheduledJob> getAllScheduledJobs() {
        return jobMap.values().stream().toList();
    }

    @Override
    public void shutdown() {
        scheduler.shutdown();
        jobMap.clear();
    }

    @Override
    public void shutdownAwait(Duration timeout) {
        try {
            scheduler.awaitTermination(timeout.getSeconds(), TimeUnit.SECONDS);
            Thread.sleep(timeout);
            jobMap.clear();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            shutdown();
        }

    }

    @PreDestroy
    public void preDestroy() {
        shutdown();
    }
}
