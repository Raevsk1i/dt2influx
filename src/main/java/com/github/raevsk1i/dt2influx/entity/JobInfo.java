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

    private String namespace;
    private String mzId;
    private JobType type;
    private LocalDateTime createDate;
    private String name;
    private boolean cancelled = false;

    public JobInfo cancel() {
        cancelled = true;
        return this;
    }
}
