package io.github.LuizYokoyama.BankAccount.dto;

import io.github.LuizYokoyama.BankAccount.entity.EntryStatus;
import io.github.LuizYokoyama.BankAccount.entity.OperationType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EntryDto {

    @EqualsAndHashCode.Include
    private UUID entryId;

    private OperationType operationType;

    private float value;

    private EntryStatus entryStatus;

}
