package io.github.LuizYokoyama.BankAccount.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.LuizYokoyama.BankAccount.entity.AccountEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

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

    private int duration;

    private float value;

    private int accountDestinationID;
}
