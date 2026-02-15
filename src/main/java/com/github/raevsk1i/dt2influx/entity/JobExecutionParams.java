package com.github.raevsk1i.dt2influx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobExecutionParams {
    private String from;
    private String to;
    private String resolution;
    private String mzSelector;
}

