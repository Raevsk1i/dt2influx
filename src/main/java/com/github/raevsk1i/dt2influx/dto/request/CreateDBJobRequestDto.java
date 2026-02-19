package com.github.raevsk1i.dt2influx.dto.request;

import lombok.Data;

@Data
public class CreateDBJobRequestDto {
    private String namespace;
    private String host;
    private String database;
}
