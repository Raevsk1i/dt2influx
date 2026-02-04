package com.github.raevsk1i.dt2influx.exceptions;

import com.github.raevsk1i.dt2influx.entity.JobInfo;

/**
 * Исключение, которое выбрасывается, когда для заданного типа
 * не удаётся создать подходящую задачу (job).
 */
public class NoSuitableJobException extends RuntimeException {

    private final JobInfo info;

    /**
     * Создаёт исключение с информацией о типе, для которого не нашлось задачи.
     *
     * @param info Тип задачи, для которого не было найдено подходящей реализации.
     * @param message Детальное сообщение об ошибке.
     */
    public NoSuitableJobException(JobInfo info, String message) {
        // Передаём понятное сообщение в родительский конструктор
        super(String.format("Failed to create job for type '%s'. Reason: %s", info, message));
        this.info = info;
    }

    /**
     * Создаёт исключение с информацией о типе (упрощённый конструктор).
     *
     * @param info Тип задачи, для которого не было найдено подходящей реализации.
     */
    public NoSuitableJobException(JobInfo info) {
        this(info, "No suitable job implementation found for the given type.");
    }

    /**
     * @return Тип задачи, который вызвал ошибку.
     */
    public JobInfo getInfo() {
        return info;
    }
}

