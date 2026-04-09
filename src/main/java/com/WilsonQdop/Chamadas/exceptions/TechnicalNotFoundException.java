package com.WilsonQdop.Chamadas.exceptions;

public class TechnicalNotFoundException extends RuntimeException {
    public TechnicalNotFoundException() {
        super("Técnico não encontrado");
    }
}
