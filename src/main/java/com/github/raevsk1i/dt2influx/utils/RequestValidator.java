package com.github.raevsk1i.dt2influx.utils;

import com.github.raevsk1i.dt2influx.dto.request.CreateFromToJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.CreateJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.StopJobRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RequestValidator {

    /**
     * Validates the job creation request by checking for null and empty required fields.
     * Ensures both namespace and mzId are provided and non-empty strings.
     *
     * @param request DTO containing job creation parameters
     * @return true if request is valid, false otherwise
     */
    public boolean isValidRequest(CreateJobRequestDto request) {
        return request != null &&
                StringUtils.hasText(request.getNamespace()) &&
                StringUtils.hasText(request.getMzId());
    }

    /**
     * Validates the job stopping request by checking for null and empty required field.
     * Ensures namespace is provided and non-empty string.
     *
     * @param request DTO containing job creation parameters
     * @return true if request is valid, false otherwise
     */
    public boolean isValidRequest(StopJobRequestDto request) {
        return request != null &&
                StringUtils.hasText(request.getNamespace());
    }

    public boolean isValidRequest(CreateFromToJobRequestDto request) {
        return request != null &&
                StringUtils.hasText(request.getNamespace()) &&
                StringUtils.hasText(request.getMzId()) &&
                StringUtils.hasText(request.getFrom()) &&
                StringUtils.hasText(request.getTo());
    }
}
