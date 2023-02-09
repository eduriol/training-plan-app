package com.github.eduriol.training.plan.app.exception;

import java.util.List;

public class NotFoundException extends ApplicationException {

    public NotFoundException(List<String> errors) {
        super(errors);
    }

}
