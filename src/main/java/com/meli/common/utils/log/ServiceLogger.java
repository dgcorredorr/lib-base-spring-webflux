package com.meli.common.utils.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.meli.application.filter.ContextFilter;
import com.meli.common.configuration.GeneralConfig;
import com.meli.common.utils.enums.LogLevel;
import com.meli.common.utils.tasks.Task;

import lombok.Builder;
import lombok.Data;

/**
 * Logger de servicio personalizado para registrar mensajes con información
 * contextual adicional.
 *
 * <p>
 * Esta clase proporciona funcionalidades de logging personalizadas para la
 * aplicación,
 * incluyendo detalles como el nombre de la aplicación, el ID de la transacción,
 * y la tarea
 * específica que se está registrando. Está diseñada para ser utilizada a lo
 * largo
 * el sistema
 * para asegurar un formato de log coherente y detallado.
 * </p>
 *
 * @param <T> El tipo de la clase para la cual se realiza el log.
 */
@Component
@Scope("prototype")
public class ServiceLogger<T> {
    private Logger logger;

    public ServiceLogger(Class<T> classToLog) {
        this.logger = LoggerFactory.getLogger(classToLog);
    }

    /**
     * Registra un mensaje de log con información detallada y contextual.
     *
     * @param message        El mensaje a registrar.
     * @param task           La tarea específica que se está registrando.
     * @param level          El nivel de log.
     * @param object         El objeto a incluir, si está disponible.
     * @param processingTime El tiempo de procesamiento en milisegundos, si está
     *                       disponible.
     * @return Un que representa el resultado de la operación de log.
     */
    public void log(String message, Task task, LogLevel level, Object object, Long processingTime) {
        LogLevel currentLevel = LogLevel.valueOf(GeneralConfig.getLogLevel().toUpperCase());
        LogLevel messageLevel = LogLevel.valueOf(level.toString().toUpperCase());

        if (shouldLog(currentLevel, messageLevel)) {
            ServerWebExchange exchange = ContextFilter.getCurrentExchange();
            String applicationName = GeneralConfig.getAppId();
            String transactionId = exchange != null ? exchange.getLogPrefix() : "N/A";
            String logOrigin = task != null ? task.getOriginString() : "N/A";
            ServiceLog serviceLog = ServiceLog.builder()
                    .applicationName(applicationName)
                    .task(task)
                    .taskDescription(task != null ? task.getDescription() : "N/A")
                    .transactionId(transactionId)
                    .message(message)
                    .logOrigin(logOrigin)
                    .level(level)
                    .object(object)
                    .processingTime(processingTime)
                    .build();

            logMessage(serviceLog);
        }
    }

    private boolean shouldLog(LogLevel currentLevel, LogLevel messageLevel) {
        return messageLevel.ordinal() >= currentLevel.ordinal();
    }

    public void logMessage(ServiceLog serviceLog) {
        String logMsg = formatLogMessage(serviceLog);
        switch (serviceLog.getLevel()) {
            case ERROR:
                logger.error(logMsg);
                break;
            case WARN: // Cambié de WARNING a WARN para coincidir con los niveles estándar de SLF4J
                logger.warn(logMsg);
                break;
            default:
                logger.info(logMsg);
                break;
        }
    }

    private String formatLogMessage(ServiceLog serviceLog) {
        return String.format(
                "{ \"applicationName\": \"%s\", \"task\": \"%s - %s\", \"transactionId\": \"%s\", \"message\": \"%s\", \"logOrigin\": \"%s\", \"level\": \"%s\", \"object\": \"%s\", \"processingTime\": \"%s\" }",
                serviceLog.getApplicationName(),
                serviceLog.getTask(),
                serviceLog.getTaskDescription(),
                serviceLog.getTransactionId(),
                serviceLog.getMessage(),
                serviceLog.getLogOrigin(),
                serviceLog.getLevel(),
                serviceLog.getObject() != null ? serviceLog.getObject().toString() : null,
                serviceLog.getProcessingTime() != null ? serviceLog.getProcessingTime().toString() : null);
    }

    @Data
    @Builder
    private static class ServiceLog {
        private String applicationName;
        private Task task;
        private String taskDescription;
        private String transactionId;
        private String message;
        private String logOrigin;
        private LogLevel level;
        private Object object;
        private Long processingTime;
    }
}
