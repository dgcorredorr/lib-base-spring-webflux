package com.fstech.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fstech.common.configuration.GeneralConfig;
import com.fstech.common.utils.enums.LogLevel;
import com.fstech.common.utils.enums.Task;
import com.fstech.common.utils.filter.ContextFilter;

/**
 * Logger de servicio personalizado para registrar mensajes con información
 * contextual adicional.
 *
 * <p>
 * Esta clase proporciona funcionalidades de logging personalizadas para la
 * aplicación,
 * incluyendo detalles como el nombre de la aplicación, el ID de la transacción,
 * y la tarea
 * específica que se está registrando. Está diseñada para ser utilizada en todo
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

    public void setLoggerClass(Class<T> classToLog) {
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
    public void log(String message, Task task, LogLevel level, Object object,
                          Long processingTime) {
        LogLevel currentLevel = LogLevel.valueOf(GeneralConfig.getLogLevel().toUpperCase());
        LogLevel messageLevel = LogLevel.valueOf(level.toString().toUpperCase());

        if (shouldLog(currentLevel, messageLevel)) {
            ServerWebExchange exchange = ContextFilter.getCurrentExchange();
            String applicationName = GeneralConfig.getAppId();
            String transactionId = exchange != null ? exchange.getLogPrefix() : "N/A";
            String logOrigin = task.getOriginString();
            logMessage(applicationName, task, task.getDescription(), transactionId, message, logOrigin, level, object, processingTime);
        }
    }

    private boolean shouldLog(LogLevel currentLevel, LogLevel messageLevel) {
        return currentLevel == LogLevel.ALL ||
                currentLevel == LogLevel.INFO ||
                (currentLevel == LogLevel.WARNING && messageLevel != LogLevel.INFO) ||
                (currentLevel == LogLevel.ERROR && messageLevel == LogLevel.ERROR);
    }

    private void logMessage(String applicationName, Task task, String taskDescription, String transactionId, String message, String logOrigin, LogLevel level, Object object, Long processingTime) {
        String logMsg = formatLogMessage(applicationName, task, taskDescription, transactionId, message, logOrigin, level, object, processingTime);
        switch (level) {
            case ERROR:
                logger.error(logMsg);
                break;
            case WARNING:
                logger.warn(logMsg);
                break;
            default:
                logger.info(logMsg);
                break;
        }
    }

    private String formatLogMessage(String applicationName, Task task, String taskDescription, String transactionId, String message, String logOrigin, LogLevel level, Object object, Long processingTime) {
        return String.format("{ \"applicationName\": \"%s\", \"task\": \"%s - %s\", \"transactionId\": \"%s\", \"message\": \"%s\", \"logOrigin\": \"%s\", \"level\": \"%s\", \"object\": \"%s\", \"processingTime\": \"%s\" }",
                applicationName, task, taskDescription, transactionId, message, logOrigin, level,
                object != null ? object.toString() : null,
                processingTime != null ? processingTime.toString() : null);
    }
}
