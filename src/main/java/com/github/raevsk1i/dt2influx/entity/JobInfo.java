package com.github.raevsk1i.dt2influx.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobInfo {
    private String namespace;
    private LocalDateTime createDate;
    private String name;
}
