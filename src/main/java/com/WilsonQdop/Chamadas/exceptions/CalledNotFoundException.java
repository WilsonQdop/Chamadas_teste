package com.WilsonQdop.Chamadas.exceptions;

public class CalledNotFoundException extends RuntimeException {
    public CalledNotFoundException() {
        super("Chamada não encontrada");
    }


}
