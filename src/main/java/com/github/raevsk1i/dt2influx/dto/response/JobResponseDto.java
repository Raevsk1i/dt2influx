package com.github.raevsk1i.dt2influx.dto.response;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import lombok.Data;

@Data
public class JobResponseDto {

    public JobResponseDto(JobInfo info, String message) {
        this.info = info;
        this.message = message;
    }

    public JobResponseDto(JobInfo info) {
        this.info = info;
    }

    private JobInfo info;

    private String message;
}
