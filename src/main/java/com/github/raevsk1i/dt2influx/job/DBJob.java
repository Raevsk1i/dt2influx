package com.github.raevsk1i.dt2influx.job;

import com.github.raevsk1i.dt2influx.entity.JobInfo;

public class DBJob extends AbstractJob{

    public DBJob(JobInfo info) {
        super(info);
    }

    @Override
    public void run() {
        System.out.println("РАБОТАЕМ, РАБОТАЕМ, ПОТОРАПЛИВАЕМСЯ!");
    }
}
