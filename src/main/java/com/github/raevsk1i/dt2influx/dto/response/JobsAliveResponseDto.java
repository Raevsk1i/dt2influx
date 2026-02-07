package com.github.raevsk1i.dt2influx.dto.response;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import lombok.Data;

import java.util.List;

@Data
public class JobsAliveResponseDto {
    List<JobInfo> jobs;
}
