package com.github.raevsk1i.dt2influx.dto.response;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.jobs.ScheduledJob;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JobsAliveResponseDto {
    List<JobInfo> jobs;
}
