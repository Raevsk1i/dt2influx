package com.github.raevsk1i.dt2influx.exceptions;

public class JobStopFailedException extends RuntimeException {
    public JobStopFailedException(String message) {
        super(message);
    }

    public JobStopFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
