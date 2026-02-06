package com.github.raevsk1i.dt2influx.service.impl;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.enums.JobType;
import com.github.raevsk1i.dt2influx.exceptions.NoSuitableJobException;
import com.github.raevsk1i.dt2influx.job.AbstractJob;
import com.github.raevsk1i.dt2influx.job.FPJob;
import com.github.raevsk1i.dt2influx.service.IJobFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JobFactoryImpl implements IJobFactory {

    @Override
    public AbstractJob createJob(JobInfo info) {
        info.setCreateDate(LocalDateTime.now());
        switch (info.getType()) {
            case JobType.FP, JobType.ONETIME -> {
                return new FPJob(info);
            }
            case JobType.DB -> {
                return null;
            }
            default -> {
                throw new NoSuitableJobException(info);
            }
        }
    }
}
