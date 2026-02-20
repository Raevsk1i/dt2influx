package com.github.raevsk1i.dt2influx.service;

import com.github.raevsk1i.dt2influx.entity.DatabaseInfo;
import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.jobs.IJob;

public interface IJobFactory {

    /**
     * Создать job для передачи метрик
     * @param info entity с информацией по задаче
     * @return Job для передачи метрик
     */
    IJob createJob(JobInfo info, String mzId);

    /**
     * Создать job для передачи метрик по БД
     * @param info entity с информацией по задаче
     * @return Job для передачи метрик по БД
     */
    IJob createJob(JobInfo info);

}
