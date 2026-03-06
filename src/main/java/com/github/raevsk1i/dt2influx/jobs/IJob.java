package com.github.raevsk1i.dt2influx.jobs;

import com.github.raevsk1i.dt2influx.entity.JobInfo;

public interface IJob extends Runnable {

    @Override
    void run();

    long getExecutionCount();

    JobInfo getInfo();
}
