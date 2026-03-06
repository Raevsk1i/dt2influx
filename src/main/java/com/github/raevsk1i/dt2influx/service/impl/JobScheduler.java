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
    public ScheduledJob scheduleAtFixedRate(IJob job, Duration initialDelay, Duration interval) {
        String id = job.getInfo().getId().toUpperCase();

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(job, initialDelay.getSeconds(), interval.getSeconds(), TimeUnit.SECONDS);
        log.info("Scheduled job {} at fixed rate: {} with initial delay: {}", id, interval, initialDelay);

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
    public ScheduledJob scheduleWithFixedDelay(IJob job, Duration delay) {
        String id = job.getInfo().getId().toUpperCase();

        ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(job, 0, delay.getSeconds(), TimeUnit.SECONDS);
        log.info("Scheduled job {} with fixed delay: {}", id, delay);

        ScheduledJob scheduledJob = new ScheduledJob(
                id,
                job,
                future,
                delay
        );
        jobMap.putIfAbsent(id, scheduledJob);
        return scheduledJob;
    }

    @Override
    public JobInfo cancel(String jobId) {
        String normalizedJobId = jobId.toUpperCase();
        if (jobMap.containsKey(normalizedJobId)) {
            ScheduledJob job = jobMap.get(normalizedJobId);
            job.cancel();
            log.info("cancelled job {}", normalizedJobId);
            jobMap.remove(normalizedJobId);
            return job.job().getInfo().copy();
        }
        return null;
    }

    @Override
    public void cancelAll() {
        for (ScheduledJob job : jobMap.values()) {
            job.cancel();
        }
        jobMap.clear();
    }

    @Override
    public Optional<JobInfo> getScheduledJob(String jobId) {
        return Optional.ofNullable(jobMap.get(jobId.toUpperCase()))
                .map(job -> job.job().getInfo().copy());
    }

    @Override
    public List<JobInfo> getAllScheduledJobs() {
        return jobMap.values().stream()
                .map(job -> job.job().getInfo().copy())
                .toList();
    }

    @Override
    public void shutdown() {
        scheduler.shutdownNow();
        scheduler.close();
        jobMap.clear();
    }

    @Override
    public void shutdownAwait(Duration timeout) {
        try {
            scheduler.shutdown();
            scheduler.awaitTermination(timeout.getSeconds(), TimeUnit.SECONDS);
            jobMap.clear();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(e.getMessage());
            shutdown();
        }

    }

    @PreDestroy
    public void preDestroy() {
        shutdown();
    }
}
