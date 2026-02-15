package com.github.raevsk1i.dt2influx.service;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.enums.StopJobStatus;
import com.github.raevsk1i.dt2influx.job.AbstractJob;

import java.util.concurrent.ScheduledFuture;

public interface IJobScheduler {
    JobInfo executeJob(AbstractJob job, Integer interval);

    JobInfo executeJob(AbstractJob job);

    StopJobStatus stopScheduledJob(String namespace);

    ScheduledFuture<?> getAliveJob(String namespace);
}
