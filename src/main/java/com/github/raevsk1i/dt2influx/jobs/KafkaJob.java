package com.github.raevsk1i.dt2influx.jobs;

import com.github.raevsk1i.dt2influx.config.ReflexConfig;
import com.github.raevsk1i.dt2influx.entity.JobInfo;

public class KafkaJob extends AbstractJob implements IJob {

    public KafkaJob(JobInfo info, ReflexConfig reflexConfig) {
        super(info, reflexConfig);
    }

    // TODO: Реализовать логику работы основного метода KafkaJob
    @Override
    public void run() {
        System.out.println("Starting Kafka Job");
    }
}
