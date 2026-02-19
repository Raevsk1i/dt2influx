package com.github.raevsk1i.dt2influx.jobs;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public record ScheduledJob(String jobId, IJob job, ScheduledFuture<?> future,
                           Duration interval) {

    // === Делегирование Future ===

    public boolean cancel() {
        return future.cancel(false);
    }

    public boolean isCancelled() {
        return future.isCancelled();
    }

    public boolean isDone() {
        return future.isDone();
    }

    public Duration getDelay() {
        return Duration.ofNanos(future.getDelay(TimeUnit.NANOSECONDS));
    }
}
