package com.WilsonQdop.Chamadas.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException() {
        super("Usuário não encontrado");
    }
}
