package com.github.eduriol.training.plan.app.exception;

import java.util.List;

public class BadRequestException extends ApplicationException {

    public BadRequestException(List<String> errors) {
        super(errors);
    }

}
