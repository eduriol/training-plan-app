package com.github.eduriol.training.plan.app.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class TrainingPlanControllerAdvisor extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(TrainingPlanControllerAdvisor.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleApplicationException(ApplicationException ex, WebRequest request) {
        Map<String, List<String>> bodyOfResponse = new HashMap<>();
        bodyOfResponse.put("errors", ex.getErrors());
        HttpStatus responseStatus;
        if (ex instanceof NotFoundException) {
            logger.warn("Not found exception: {}", ex.getMessage());
            responseStatus = HttpStatus.NOT_FOUND;
        } else if (ex instanceof BadRequestException) {
            logger.warn("Bad request exception: {}", ex.getMessage());
            responseStatus = HttpStatus.BAD_REQUEST;
        } else {
            logger.error("Illegal argument exception", ex);
            throw new IllegalArgumentException("The Exception type " + ex.getClass() + " is not supported.");
        }
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), responseStatus, request);
    }

}
