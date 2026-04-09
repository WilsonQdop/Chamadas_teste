package com.WilsonQdop.Chamadas.exceptions;

public class BusinessException extends RuntimeException {

    public BusinessException() {
        super("Chamado Já encerrado.");
    }
}
