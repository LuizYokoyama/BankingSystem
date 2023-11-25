package io.github.LuizYokoyama.BankAccount.exceptions;

public class NotFoundRuntimeException extends RuntimeException{
    public NotFoundRuntimeException(String message) {
        super(message);
    }
}
