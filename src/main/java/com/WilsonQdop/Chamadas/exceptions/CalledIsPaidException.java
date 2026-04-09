package com.WilsonQdop.Chamadas.exceptions;

public class CalledIsPaidException extends RuntimeException {

    public CalledIsPaidException() {
        super("Chamado Já está pago");
    }
}
