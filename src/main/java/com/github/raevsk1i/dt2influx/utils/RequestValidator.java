package com.github.raevsk1i.dt2influx.utils;

import com.github.raevsk1i.dt2influx.dto.request.CreateFromToJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.CreateJobRequestDto;
import com.github.raevsk1i.dt2influx.dto.request.StopJobRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RequestValidator {

    private static final Pattern RELATIVE_TIME_PATTERN = Pattern.compile("^(?:now(?:-(\\d+)([smhdwMy]))?|-(\\d+)([smhdwMy]))$");

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
        return validateCreateFromToRequest(request) == null;
    }

    public String validateCreateFromToRequest(CreateFromToJobRequestDto request) {
        if (request == null ||
                !StringUtils.hasText(request.getNamespace()) ||
                !StringUtils.hasText(request.getMzId()) ||
                !StringUtils.hasText(request.getFrom()) ||
                !StringUtils.hasText(request.getTo())) {
            return "Invalid request: namespace, mzId, from and to are required";
        }

        Instant from = parseSupportedTime(request.getFrom());
        if (from == null) {
            return "Invalid request: from should be ISO-8601 or Dynatrace relative format";
        }

        Instant to = parseSupportedTime(request.getTo());
        if (to == null) {
            return "Invalid request: to should be ISO-8601 or Dynatrace relative format";
        }

        if (from.isAfter(to)) {
            return "Invalid request: from should be less than or equal to to";
        }

        return null;
    }

    private Instant parseSupportedTime(String value) {
        String trimmed = value.trim();

        try {
            return Instant.parse(trimmed);
        } catch (DateTimeParseException ignored) {
        }

        try {
            return OffsetDateTime.parse(trimmed).toInstant();
        } catch (DateTimeParseException ignored) {
        }

        try {
            return LocalDateTime.parse(trimmed).toInstant(ZoneOffset.UTC);
        } catch (DateTimeParseException ignored) {
        }

        Matcher matcher = RELATIVE_TIME_PATTERN.matcher(trimmed);
        if (!matcher.matches()) {
            return null;
        }

        if ("now".equals(trimmed)) {
            return Instant.now();
        }

        String amountGroup = matcher.group(1) != null ? matcher.group(1) : matcher.group(3);
        String unitGroup = matcher.group(2) != null ? matcher.group(2) : matcher.group(4);

        if (amountGroup == null || unitGroup == null) {
            return null;
        }

        long amount = Long.parseLong(amountGroup);
        return Instant.now().minusSeconds(toSeconds(amount, unitGroup.charAt(0)));
    }

    private long toSeconds(long amount, char unit) {
        return switch (unit) {
            case 's' -> amount;
            case 'm' -> amount * 60;
            case 'h' -> amount * 60 * 60;
            case 'd' -> amount * 60 * 60 * 24;
            case 'w' -> amount * 60 * 60 * 24 * 7;
            case 'M' -> amount * 60 * 60 * 24 * 30;
            case 'y' -> amount * 60 * 60 * 24 * 365;
            default -> throw new IllegalArgumentException("Unsupported relative time unit: " + unit);
        };
    }
}
