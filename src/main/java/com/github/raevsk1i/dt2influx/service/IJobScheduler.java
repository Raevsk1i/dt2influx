package com.github.raevsk1i.dt2influx.service;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.job.AbstractJob;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public interface IJobScheduler {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

    JobInfo scheduleJob(AbstractJob job);

    JobInfo stopScheduledJob(String namespace);
}
