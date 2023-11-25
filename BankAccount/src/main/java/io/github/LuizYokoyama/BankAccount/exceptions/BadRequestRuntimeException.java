package io.github.LuizYokoyama.BankAccount.exceptions;

public class BadRequestRuntimeException extends RuntimeException{
    public BadRequestRuntimeException(String message) {
        super(message);
    }
}
