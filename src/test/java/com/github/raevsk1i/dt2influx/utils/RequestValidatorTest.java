package com.github.raevsk1i.dt2influx.utils;

import com.github.raevsk1i.dt2influx.dto.request.CreateFromToJobRequestDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RequestValidatorTest {

    private final RequestValidator validator = new RequestValidator();

    @Test
    void validateCreateFromToRequestAcceptsIso8601() {
        CreateFromToJobRequestDto request = buildBaseRequest();
        request.setFrom("2024-01-01T10:00:00Z");
        request.setTo("2024-01-01T11:00:00Z");

        assertNull(validator.validateCreateFromToRequest(request));
    }

    @Test
    void validateCreateFromToRequestAcceptsDynatraceRelative() {
        CreateFromToJobRequestDto request = buildBaseRequest();
        request.setFrom("-30m");
        request.setTo("now");

        assertNull(validator.validateCreateFromToRequest(request));
    }

    @Test
    void validateCreateFromToRequestRejectsInvalidFormat() {
        CreateFromToJobRequestDto request = buildBaseRequest();
        request.setFrom("yesterday");
        request.setTo("now");

        assertEquals(
                "Invalid request: from should be ISO-8601 or Dynatrace relative format",
                validator.validateCreateFromToRequest(request)
        );
    }

    @Test
    void validateCreateFromToRequestRejectsFromAfterTo() {
        CreateFromToJobRequestDto request = buildBaseRequest();
        request.setFrom("2024-01-01T12:00:00Z");
        request.setTo("2024-01-01T11:00:00Z");

        assertEquals(
                "Invalid request: from should be less than or equal to to",
                validator.validateCreateFromToRequest(request)
        );
    }

    private CreateFromToJobRequestDto buildBaseRequest() {
        return new CreateFromToJobRequestDto("test", "mz-id", "-30m", "now");
    }
}
