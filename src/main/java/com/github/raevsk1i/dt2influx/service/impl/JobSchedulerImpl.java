package com.github.raevsk1i.dt2influx.service.impl;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.enums.StopJobStatus;
import com.github.raevsk1i.dt2influx.job.AbstractJob;
import com.github.raevsk1i.dt2influx.service.IJobScheduler;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JobSchedulerImpl implements IJobScheduler {

    private final ConcurrentHashMap<String, ScheduledFuture<?>> jobs = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduler;

    /*
    Post construct
     */
    @PostConstruct
    public void initScheduler() {
        scheduler = Executors.newScheduledThreadPool(4);
    }

    @Override
    public JobInfo executeJob(AbstractJob job, Integer interval) {
        log.info("Job: {} is scheduled for {} seconds", job.getInfo(), interval);
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(job, 0, interval, TimeUnit.SECONDS);
        jobs.put(job.getInfo().getNamespace(), scheduledFuture);
        return job.getInfo();
    }

    @Override
    public JobInfo executeJob(AbstractJob job) {
        log.info("Job: {} is executed for one-time", job.getInfo());
        scheduler.execute(job);
        return job.getInfo();
    }

    @Override
    public StopJobStatus stopScheduledJob(String namespace) {
        ScheduledFuture<?> future = jobs.remove(namespace);

        if (future == null) {
            log.warn("Job with namespace {} wasn't found in the jobs map", namespace);
            return StopJobStatus.NOT_FOUND;
        }

        try {
            if (future.isCancelled() || future.isDone() || future.cancel(false)) {
                log.info("Job with namespace {} is stopped", namespace);
                return StopJobStatus.SUCCESS;
            }

            log.error("Failed to cancel job with namespace {}", namespace);
            jobs.put(namespace, future);
            return StopJobStatus.ERROR;
        } catch (Exception ex) {
            log.error("Unexpected error while stopping job with namespace {}", namespace, ex);
            jobs.put(namespace, future);
            return StopJobStatus.ERROR;
        }
    }

    @Override
    public ScheduledFuture<?> getAliveJob(String namespace) {
        return jobs.get(namespace);
    }

    /*
    Pre destroy
     */
    @PreDestroy
    public void shutdownScheduler() {
        scheduler.shutdown();

        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                log.warn("Scheduler did not terminate in time, forcing shutdown");
                scheduler.shutdownNow();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            scheduler.shutdownNow();
        }
    }
}
