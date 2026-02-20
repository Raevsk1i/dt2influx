package com.github.raevsk1i.dt2influx.dto.response;

import com.github.raevsk1i.dt2influx.entity.DatabaseInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DatabaseResponseDto {
    private boolean success;
    private String message;
    private int count;
    private List<DatabaseInfo> added_databases;
}
