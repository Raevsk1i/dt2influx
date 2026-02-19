package com.github.raevsk1i.dt2influx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DatabaseInfo {
    private String host;
    private String database;
    private String namespace;
}
