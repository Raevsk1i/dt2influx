package com.github.raevsk1i.dt2influx.dto.reflex.response;

import com.github.raevsk1i.dt2influx.dto.reflex.additional.Result;
import lombok.Data;

import java.util.List;

@Data
public class ReflexResponse {
    private int totalCount;
    private String nextPageKey;
    private String resolution;
    private List<Result> result;
}
