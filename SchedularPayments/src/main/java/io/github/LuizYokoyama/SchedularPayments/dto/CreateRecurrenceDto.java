package io.github.LuizYokoyama.SchedularPayments.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateRecurrenceDto {

    private int accountId;

    private String recurrenceName;

    private LocalDate occurrenceDate;

    private int duration;

    private float value;

    private int accountDestinationID;

}
