package com.github.raevsk1i.dt2influx.jobs;

import com.github.raevsk1i.dt2influx.config.ReflexConfig;
import com.github.raevsk1i.dt2influx.entity.JobInfo;

public class KafkaJob extends AbstractJob {

    public KafkaJob(JobInfo info, ReflexConfig reflexConfig) {
        super(info, reflexConfig);
    }

    @Override
    public void run() {
        System.out.println("Starting Kafka Job");
    }
}
