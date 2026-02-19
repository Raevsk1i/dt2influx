package com.github.raevsk1i.dt2influx.service;

import com.github.raevsk1i.dt2influx.entity.JobInfo;
import com.github.raevsk1i.dt2influx.jobs.IJob;
import com.github.raevsk1i.dt2influx.jobs.ScheduledJob;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface IJobScheduler {

    // === Планирование периодических задач ===

    /**
     * Запланировать задачу с фиксированным интервалом (каждые 5 минут)
     * @return ScheduledJob - запланированная задача
     */
    ScheduledJob scheduleAtFixedRate(IJob job, Duration interval);

    /**
     * Запланировать задачу на один прогон
     * @return ScheduledJob - запланированная задача
     */
    IJob scheduleOneTime(IJob job);

    /**
     * Запланировать задачу с начальной задержкой
     */
    String scheduleAtFixedRate(IJob job, Duration initialDelay, Duration interval);

    /**
     * Запланировать задачу с задержкой между выполнениями
     * (следующий запуск только после завершения предыдущего)
     */
    String scheduleWithFixedDelay(IJob job, Duration delay);

    // === Управление задачами ===

    /**
     * Отменить задачу по ID
     */
    JobInfo cancel(String jobId);

    /**
     * Отменить все задачи
     */
    void cancelAll();

    /**
     * Получать Scheduled Job по jobId
     */
    Optional<ScheduledJob> getScheduledJob(String jobId);

    /**
     * Получить все запущенные jobs
     */
    List<ScheduledJob> getAllScheduledJobs();

    // === Завершение работы ===

    void shutdown();
    void shutdownAwait(Duration timeout);
}
