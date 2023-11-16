package io.github.LuizYokoyama.BankAccount.dto;

import io.github.LuizYokoyama.BankAccount.entity.AccountEntity;
import io.github.LuizYokoyama.BankAccount.entity.EntryStatus;
import io.github.LuizYokoyama.BankAccount.entity.OperationType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EntryDto {

    @EqualsAndHashCode.Include
    private UUID entryId;

    private Integer accountId;

    private OperationType operationType;

    private float value;

    private LocalDate entryDate;

    private EntryStatus entryStatus;

}
