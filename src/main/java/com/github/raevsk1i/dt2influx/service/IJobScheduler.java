package com.github.raevsk1i.dt2influx.service;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.job.AbstractJob;

import java.util.concurrent.*;

public interface IJobScheduler {
    ConcurrentHashMap<String, ScheduledFuture<?>> map = new ConcurrentHashMap<>();
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

    JobInfo scheduleJob(AbstractJob job, Integer interval);

    JobInfo stopScheduledJob(String namespace) throws InterruptedException;
}
