package com.challenge.selic.core.calculo.exception;

import lombok.Getter;

@Getter
public class CalculoValorEntradaMaiorIgualQueValorProdutoConstraintException extends RuntimeException {

    private static final long serialVersionUID = 2L;

    private String message;

    public CalculoValorEntradaMaiorIgualQueValorProdutoConstraintException(String message) {
        this.message = message;
    }
}