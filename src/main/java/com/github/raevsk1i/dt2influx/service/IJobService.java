package com.github.raevsk1i.dt2influx.service;

import com.github.raevsk1i.dt2influx.dto.response.JobResponseDto;
import com.github.raevsk1i.dt2influx.dto.response.JobsAliveResponseDto;
import org.springframework.http.ResponseEntity;

/**
 * Интерфейс сервиса управления задачами.
 * Предоставляет методы для создания, остановки и мониторинга задач.
 */
public interface IJobService {

    /**
     * Создать job для передачи метрик функциональной системы
     * @param request DTO с параметрами задачи
     * @return ответ с информацией о созданной задаче
     */
    ResponseEntity<JobResponseDto> createJob(Object request);

    /**
     * Создать job для функциональной системы или БД, который вытаскивает метрики с from до to
     * @param request DTO с параметрами задачи (from-to)
     * @return ответ с информацией о созданной задаче
     */
    ResponseEntity<JobResponseDto> createFromToJob(Object request);

    /**
     * Остановить задачу по ID
     * @param request DTO с идентификатором задачи
     * @return ответ с результатом операции
     */
    ResponseEntity<JobResponseDto> stopJob(Object request);

    /**
     * Получить список активных задач
     * @return ответ со списком активных задач
     */
    ResponseEntity<JobsAliveResponseDto> getAliveJobs();
}
