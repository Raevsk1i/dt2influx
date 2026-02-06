package com.github.raevsk1i.dt2influx.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreateFromToJobRequestDto {
    @NonNull
    private String namespace;

    @NonNull
    private String mzId;

    @NonNull
    private String from;

    @NonNull
    private String to;
}
