package com.github.raevsk1i.dt2influx.entity;

import com.github.raevsk1i.dt2influx.enums.JobType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobInfo {

    public JobInfo(String namespace, String mzId, JobType type) {
        this.namespace = namespace;
        this.mzId = mzId;
        this.type = type;
    }

    public JobInfo(String namespace, String mzId, JobType type, String from, String to) {
        this.namespace = namespace;
        this.mzId = mzId;
        this.type = type;
        this.from = from;
        this.to = to;
    }

    private String namespace;
    private String mzId;
    private JobType type;
    private LocalDateTime createDate;
    private String name;
    private boolean cancelled = false;
    private String from = "-30m";
    private String to = "now";

    public JobInfo cancel() {
        cancelled = true;
        return this;
    }
}
