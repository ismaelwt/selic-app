package com.challenge.selic.core.calculo.exception;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@Getter
public class CalculoConstraintException extends ConstraintViolationException {

    private static final long serialVersionUID = 1L;

    public CalculoConstraintException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }
}
