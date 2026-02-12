package com.github.raevsk1i.dt2influx.job;

import com.github.raevsk1i.dt2influx.entity.JobInfo;

public class DBJob extends AbstractJob{

    public DBJob(JobInfo info, String reflexUrl, String token) {
        super(info, reflexUrl, token);
    }

    @Override
    public void run() {
        System.out.println("РАБОТАЕМ, РАБОТАЕМ, ПОТОРАПЛИВАЕМСЯ!");
    }
}
