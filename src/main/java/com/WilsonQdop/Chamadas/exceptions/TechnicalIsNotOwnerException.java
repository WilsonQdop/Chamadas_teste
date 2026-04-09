package com.WilsonQdop.Chamadas.exceptions;

public class TechnicalIsNotOwnerException extends RuntimeException {
    public TechnicalIsNotOwnerException() {
        super("Você não foi atribuído a este chamado");
    }
}
