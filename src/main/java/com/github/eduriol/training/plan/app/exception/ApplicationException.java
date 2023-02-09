package com.github.eduriol.training.plan.app.exception;

import java.util.List;

public class ApplicationException extends RuntimeException {

    private final List<String> errors;

    public ApplicationException(List<String> errors) {
        super(String.join(", ", errors));
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}
