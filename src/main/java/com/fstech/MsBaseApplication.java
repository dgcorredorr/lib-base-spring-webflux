package com.fstech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fstech.common.configuration.GeneralConfig;
import com.fstech.common.utils.ServiceLogger;
import com.fstech.common.utils.enums.LogLevel;
import com.fstech.common.utils.enums.Task;

@SpringBootApplication
public class MsBaseApplication {

	private static ServiceLogger<MsBaseApplication> logger;

	public MsBaseApplication(ServiceLogger<MsBaseApplication> logger) {
		logger.setLoggerClass(MsBaseApplication.class);
		MsBaseApplication.logger = logger;
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		SpringApplication.run(MsBaseApplication.class, args);
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		Task task = Task.INIT_MICROSERVICE;
		task.setOrigin(Task.Origin.builder()
				.originClass(MsBaseApplication.class.getName())
				.originMethod("main(String[] args)")
				.build());
		String logMessage = GeneralConfig.getAppName() + " | " 
		+ GeneralConfig.getAppVersion() + " | " 
		+ GeneralConfig.getAppDescription();
		MsBaseApplication.logger.log(logMessage, task, LogLevel.INFO, null, executionTime);
	}

}
