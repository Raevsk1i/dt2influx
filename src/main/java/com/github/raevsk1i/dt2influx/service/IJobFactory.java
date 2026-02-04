package com.github.raevsk1i.dt2influx.service;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.job.AbstractJob;

public interface IJobFactory {

    AbstractJob createJob(JobInfo info);
}
