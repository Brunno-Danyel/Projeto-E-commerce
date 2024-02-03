package io.github.brunnodanyel.projetovendas.exception;

public class BadRequestExecption extends RuntimeException{
    public BadRequestExecption(String message) {
        super(message);
    }
}
