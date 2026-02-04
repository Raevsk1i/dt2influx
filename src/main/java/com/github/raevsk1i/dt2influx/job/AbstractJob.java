package com.github.raevsk1i.dt2influx.job;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractJob implements Runnable {

    private JobInfo info;

    public final boolean oneTime;

    public AbstractJob(JobInfo info, boolean oneTime) {
        this.info = info;
        this.oneTime = oneTime;
    }

    @Override
    public abstract void run();
}
