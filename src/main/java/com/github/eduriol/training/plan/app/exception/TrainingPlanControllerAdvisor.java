package com.github.eduriol.training.plan.app.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TrainingPlanControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleApplicationException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        HttpStatus responseStatus;
        if (ex instanceof NotFoundException) {
            responseStatus = HttpStatus.NOT_FOUND;
        } else if (ex instanceof NoContentException) {
            responseStatus = HttpStatus.NO_CONTENT;
        } else if (ex instanceof BadRequestException) {
            responseStatus = HttpStatus.BAD_REQUEST;
        } else {
            throw new IllegalArgumentException("The Exception type " + ex.getClass() + " is not supported.");
        }
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), responseStatus, request);
    }

}
