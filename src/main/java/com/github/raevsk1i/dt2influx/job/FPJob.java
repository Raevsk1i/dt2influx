package com.github.raevsk1i.dt2influx.job;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FPJob extends AbstractJob {

    public FPJob(JobInfo info) {
        super(info);
    }

    @Override
    public void run() {
        System.out.println("РАБОТАЕМ, РАБОТАЕМ, ПОТОРАПЛИВАЕМСЯ!");
    }
}
