package com.github.eduriol.training.plan.app.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String errorMessage) {
        super(errorMessage);
    }

}
