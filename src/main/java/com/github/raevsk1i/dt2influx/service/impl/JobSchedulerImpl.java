package com.github.raevsk1i.dt2influx.service.impl;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.exceptions.JobStopFailedException;
import com.github.raevsk1i.dt2influx.job.AbstractJob;
import com.github.raevsk1i.dt2influx.service.IJobScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JobSchedulerImpl implements IJobScheduler {

    @Override
    public JobInfo executeJob(AbstractJob job, Integer interval) {
        log.info("Job: {} is scheduled for {} seconds", job.getInfo(), interval);
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(job, 0, interval, TimeUnit.SECONDS);
        jobs.put(job.getInfo().getNamespace(), scheduledFuture);
        return job.getInfo();
    }

    @Override
    public JobInfo executeJob(AbstractJob job) {
        log.info("Job: {} is executed for one-time", job.getInfo());
        scheduler.execute(job);
        return job.getInfo();
    }

    @Override
    public Boolean stopScheduledJob(String namespace) throws JobStopFailedException {

        try {
            if (jobs.containsKey(namespace)) {
                ScheduledFuture<?> future =  jobs.get(namespace);

                for (int cycle = 0; cycle < 5; cycle++) {
                    boolean cancelled = future.cancel(false);

                    if (!cancelled) {
                        log.info("Job is stopping...");
                        Thread.sleep(Duration.ofSeconds(3));
                        continue;
                    }
                    log.info("{} is stopped:", future);
                    jobs.remove(namespace);
                    return true;
                }
            }
            log.warn("Job with namespace {} wasn't found in the jobs map", namespace);
            return false;
        } catch (Exception ex) {
            throw new JobStopFailedException("Job didn't stop for 5 times", ex);
        }
    }

    @Override
    public ScheduledFuture<?> getAliveJob(String namespace) {
        return jobs.get(namespace);
    }
}
