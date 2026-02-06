package com.github.raevsk1i.dt2influx.job;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractJob implements Runnable {

    private JobInfo info;

    public AbstractJob(JobInfo info) {
        this.info = info;
    }

    @Override
    public abstract void run();
}
