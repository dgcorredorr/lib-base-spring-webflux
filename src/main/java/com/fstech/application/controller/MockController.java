package com.fstech.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fstech.application.dto.GenericResponse;
import com.fstech.application.service.MessageService;
import com.fstech.common.utils.ServiceLogger;
import com.fstech.common.utils.enums.LogLevel;
import com.fstech.common.utils.enums.MessageMapping;
import com.fstech.common.utils.enums.Task;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/v1/mock")
public class MockController {
    private final ServiceLogger<MockController> logger;

    private final MessageService messageService;

    public MockController(MessageService messageService, ServiceLogger<MockController> logger) {
        logger.setLoggerClass(MockController.class);
        this.logger = logger;
        this.messageService = messageService;
    }

    @GetMapping
    public Mono<GenericResponse> getMock(@RequestParam String testParam1, @RequestParam String testParam2) {
        GenericResponse response = GenericResponse.builder()
                .origin("api/v1/mock")
                .build();
                
        return messageService.mapMessage(MessageMapping.DEFAULT_SUCCESS)
                .doOnSuccess(message -> {
                    response.setMessage(message);
                    response.setSuccess(true);
                    response.setDocuments(testParam1 + testParam2);
                    logger.log(message,Task.TEST_TASK,LogLevel.INFO,testParam1+testParam2,null);
                })
                .thenReturn(response);
    }

    @PostMapping
    public Mono<GenericResponse> postMock(@Valid @RequestBody String entity) {
        GenericResponse response = GenericResponse.builder()
                .origin("api/v1/mock")
                .build();
                
        return messageService.mapMessage(MessageMapping.DEFAULT_SUCCESS)
                .doOnSuccess(message -> {
                    response.setMessage(message);
                    response.setSuccess(true);
                    response.setDocuments(entity);
                    logger.log(message,Task.TEST_TASK,LogLevel.INFO,entity,null);
                })
                .thenReturn(response);
    }
    
}
