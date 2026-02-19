package com.github.raevsk1i.dt2influx.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreateFromToFSJobRequestDto {
    private String namespace;
    private String mzId;
    private String from;
    private String to;
}
