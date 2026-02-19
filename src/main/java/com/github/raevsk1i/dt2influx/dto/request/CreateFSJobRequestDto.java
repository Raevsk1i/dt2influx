package com.github.raevsk1i.dt2influx.dto.request;

import lombok.Data;

@Data
public class CreateFSJobRequestDto {
    private String namespace;
    private String mzId;
}
