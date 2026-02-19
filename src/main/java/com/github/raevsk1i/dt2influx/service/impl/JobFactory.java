package com.github.raevsk1i.dt2influx.service.impl;

import com.github.raevsk1i.dt2influx.config.ReflexConfig;
import com.github.raevsk1i.dt2influx.entity.DatabaseInfo;
import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.enums.JobType;
import com.github.raevsk1i.dt2influx.exceptions.NoSuitableJobException;
import com.github.raevsk1i.dt2influx.jobs.FSJob;
import com.github.raevsk1i.dt2influx.jobs.IJob;
import com.github.raevsk1i.dt2influx.service.IJobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JobFactory implements IJobFactory {

    private final ReflexConfig reflexConfig;

    @Autowired
    public JobFactory(ReflexConfig reflexConfig) {
        this.reflexConfig = reflexConfig;
    }

    @Override
    public IJob createJob(JobInfo info, String mzId) {
        if (Objects.requireNonNull(info.getType()) == JobType.FS) {
            return new FSJob(info, mzId, reflexConfig);
        }
        throw new NoSuitableJobException(info);
    }

    @Override
    public IJob createJob(JobInfo info, DatabaseInfo databaseInfo) {
        return null;
    }
}
