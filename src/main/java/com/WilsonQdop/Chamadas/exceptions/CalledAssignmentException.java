package com.WilsonQdop.Chamadas.exceptions;

public class CalledAssignmentException extends RuntimeException {
    public CalledAssignmentException() {
        super("Você não pode pegar este chamado");
    }
}
