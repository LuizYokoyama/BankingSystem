package io.github.LuizYokoyama.SchedularPayments.dto;

import java.time.LocalDate;

public interface IRecurrenceDto {

    public static final int MIN_DURATION = 1;
    public static final int MAX_DURATION = 12;


    public LocalDate getOccurrenceDate();

    public void setOccurrenceDate(LocalDate occurrenceDate);

    public int getDuration() ;

    public void setDuration(int duration);

    public float getValue();

    public void setValue(float value);


}
