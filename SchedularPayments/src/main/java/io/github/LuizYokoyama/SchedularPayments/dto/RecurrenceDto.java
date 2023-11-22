package io.github.LuizYokoyama.SchedularPayments.dto;

import io.github.LuizYokoyama.SchedularPayments.entity.RecurrenceStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecurrenceDto {

    @EqualsAndHashCode.Include
    private UUID id;

    @Min(1)
    private int accountId;

    private String recurrenceName;

    private LocalDate occurrenceDate;

    @Min(1)
    private int duration;

    private float value;

    @Min(1)
    private int accountDestinationID;

    private RecurrenceStatus recurrenceStatus;

}
