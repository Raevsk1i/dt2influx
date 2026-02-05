package com.github.raevsk1i.dt2influx.dto.request;


import lombok.NonNull;
import lombok.Data;

@Data
public class CreateJobRequestDto {

    @NonNull
    private String namespace;

    @NonNull
    private String mzId;
}
