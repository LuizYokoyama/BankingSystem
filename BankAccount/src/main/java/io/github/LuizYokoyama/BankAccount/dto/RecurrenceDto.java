package io.github.LuizYokoyama.BankAccount.dto;

import io.github.LuizYokoyama.BankAccount.entity.RecurrenceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecurrenceDto {

    @EqualsAndHashCode.Include
    private UUID id;

    private int accountId;

    private String recurrenceName;

    private LocalDate occurrenceDate;

    private int monthsDuration;

    private BigDecimal value;

    private int accountDestinationID;

    private RecurrenceStatus recurrenceStatus;
}
