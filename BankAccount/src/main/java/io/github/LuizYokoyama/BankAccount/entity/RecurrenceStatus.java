package io.github.LuizYokoyama.BankAccount.entity;

public enum RecurrenceStatus {

    CANCELED, //fully canceled
    PARTLY_DONE,
    PENDING,
    DONE,
    PARTLY_CANCELED;  //Some payments have already been done before cancellation

}
