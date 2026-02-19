package com.github.raevsk1i.dt2influx.jobs;

import com.github.raevsk1i.dt2influx.config.ReflexConfig;
import com.github.raevsk1i.dt2influx.entity.JobInfo;

public class DBJob extends AbstractJob {



    public DBJob(JobInfo info, ReflexConfig reflexConfig) {
        super(info, reflexConfig);
    }

    @Override
    public void run() {

    }
}
