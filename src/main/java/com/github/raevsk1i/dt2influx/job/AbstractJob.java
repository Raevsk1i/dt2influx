package com.github.raevsk1i.dt2influx.job;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import lombok.Getter;
import lombok.Setter;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractJob implements Runnable {

    private JobInfo info;
    private String reflexUrl;
    private String token;

    public AbstractJob(JobInfo info, String reflexUrl, String token) {
        this.info = info;
        this.reflexUrl = reflexUrl;
        this.token = token;
    }

    @Override
    public abstract void run();

    protected String encodeParams(Map<String, String> params, String metricSelector, String reflexUrl) {

        StringBuilder paramsBuilder = new StringBuilder();

        paramsBuilder
                .append("metricSelector")
                .append("=")
                .append(URLEncoder.encode(metricSelector, Charset.defaultCharset()))
                .append("&");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramsBuilder
                    .append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), Charset.defaultCharset()));

            if (!entry.equals(params.entrySet().toArray()[params.size() - 1])) {
                paramsBuilder.append("&");
            }
        }

        return reflexUrl + paramsBuilder;
    }
}
