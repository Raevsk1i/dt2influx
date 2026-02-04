package com.github.raevsk1i.dt2influx.job;

import com.github.raevsk1i.dt2influx.entity.JobInfo;

public class FPJob extends AbstractJob {

    public FPJob(JobInfo info, boolean oneTime) {
        super(info, oneTime);
    }

    @Override
    public void run() {
        return;
    }
}
