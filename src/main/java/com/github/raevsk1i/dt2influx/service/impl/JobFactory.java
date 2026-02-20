package com.github.raevsk1i.dt2influx.service.impl;

import com.github.raevsk1i.dt2influx.config.ReflexConfig;
import com.github.raevsk1i.dt2influx.entity.DatabaseInfo;
import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.enums.JobType;
import com.github.raevsk1i.dt2influx.exceptions.NoSuitableJobException;
import com.github.raevsk1i.dt2influx.jobs.DBJob;
import com.github.raevsk1i.dt2influx.jobs.FSJob;
import com.github.raevsk1i.dt2influx.jobs.IJob;
import com.github.raevsk1i.dt2influx.jobs.KafkaJob;
import com.github.raevsk1i.dt2influx.service.DatabaseStorage;
import com.github.raevsk1i.dt2influx.service.IJobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JobFactory implements IJobFactory {

    private final ReflexConfig reflexConfig;
    private final DatabaseStorage databaseStorage;

    @Autowired
    public JobFactory(ReflexConfig reflexConfig, DatabaseStorage databaseStorage) {
        this.reflexConfig = reflexConfig;
        this.databaseStorage = databaseStorage;
    }

    @Override
    public IJob createJob(JobInfo info, String mzId) {
        if (Objects.requireNonNull(info.getType()) == JobType.FS) {
            return new FSJob(info, mzId, reflexConfig);
        }
        throw new NoSuitableJobException(info);
    }

    @Override
    public IJob createJob(JobInfo info) {

        switch (Objects.requireNonNull(info.getType())) {
            case JobType.DB -> {
                return new DBJob(info, databaseStorage, reflexConfig);
            }
            case JobType.KAFKA -> {
                return new KafkaJob(info, reflexConfig);
            }
            default -> throw new NoSuitableJobException(info);
        }
    }
}
