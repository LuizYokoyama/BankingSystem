package io.github.LuizYokoyama.SchedularPayments.entity;

public enum RecurrenceStatus {

    CANCELED, //fully canceled
    PARTLY_DONE,
    PENDING,
    DONE,
    PARTLY_CANCELED;  //Some payments have already been done before cancellation

}
