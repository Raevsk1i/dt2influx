package com.github.raevsk1i.dt2influx.entity;

import lombok.Data;

@Data
public class DBInfo {
    private String host;
    private String namespace;
    private String db;
}
