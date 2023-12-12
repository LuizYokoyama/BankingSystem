package io.github.LuizYokoyama.SchedularPayments.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IRecurrenceDto {

    public static final int MIN_DURATION = 1;
    public static final int MAX_DURATION = 12;


    public LocalDate getOccurrenceDate();

    public void setOccurrenceDate(LocalDate occurrenceDate);

    public int getMonthsDuration() ;

    public void setMonthsDuration(int duration);

    public BigDecimal getValue();

    public void setValue(BigDecimal value);


}
