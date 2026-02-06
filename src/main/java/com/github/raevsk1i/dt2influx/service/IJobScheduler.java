package com.github.raevsk1i.dt2influx.service;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.job.AbstractJob;

import java.util.concurrent.*;

public interface IJobScheduler {
    ConcurrentHashMap<String, ScheduledFuture<?>> jobs = new ConcurrentHashMap<>();
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    JobInfo executeJob(AbstractJob job, Integer interval);

    JobInfo executeJob(AbstractJob job);

    Boolean stopScheduledJob(String namespace) throws InterruptedException;

    ScheduledFuture<?> getAliveJob(String namespace);
}
