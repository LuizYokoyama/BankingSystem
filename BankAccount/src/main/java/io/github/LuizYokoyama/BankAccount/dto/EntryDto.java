package io.github.LuizYokoyama.BankAccount.dto;

import io.github.LuizYokoyama.BankAccount.entity.EntryStatus;
import io.github.LuizYokoyama.BankAccount.entity.OperationType;
import lombok.*;

import java.time.LocalDateTime;
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

    private LocalDateTime entryDateTime;

    private EntryStatus entryStatus;

}
