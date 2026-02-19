package com.github.raevsk1i.dt2influx.entity;

import com.github.raevsk1i.dt2influx.enums.JobType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JobInfo {
    @NotNull
    private final JobType type;

    @NotNull
    private final String id;

    private final String from;

    private final String to;

    private final LocalDateTime createdAt;

    public JobInfo(JobType type, String id, String from, String to) {
        this.type = type;
        this.id = id;
        this.from = from;
        this.to = to;
        this.createdAt = LocalDateTime.now();
    }

    public JobInfo(JobType type, String id) {
        this.type = type;
        this.id = id;
        this.from = "-30m";
        this.to = "now";
        this.createdAt = LocalDateTime.now();
    }

    public JobInfo copy() {
        return new JobInfo(type, id, from, to);
    }
}
