package com.github.raevsk1i.dt2influx.service.impl;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.job.AbstractJob;
import com.github.raevsk1i.dt2influx.service.IJobScheduler;
import org.springframework.stereotype.Component;

@Component
public class JobSchedulerImpl implements IJobScheduler {
    @Override
    public JobInfo scheduleJob(AbstractJob job) {
        return null;
    }

    @Override
    public JobInfo stopScheduledJob(String namespace) {
        return null;
    }
}
