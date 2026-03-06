package com.github.raevsk1i.dt2influx.dto.request;

import com.github.raevsk1i.dt2influx.entity.DatabaseInfo;
import lombok.Data;

import java.util.List;

@Data
public class DatabaseAddRequestDto {
    private List<DatabaseInfo> databases;
}
