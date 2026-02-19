package com.github.raevsk1i.dt2influx.dto.response;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobResponseDto {
    private boolean success;
    private String message;

    private JobInfo job_info;
}
