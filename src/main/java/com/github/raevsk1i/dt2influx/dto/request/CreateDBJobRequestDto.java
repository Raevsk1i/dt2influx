package com.github.raevsk1i.dt2influx.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreateDBJobRequestDto {

    @NonNull
    private String namespace;

    @NonNull
    private String host;

    @NonNull
    private String database;
}
