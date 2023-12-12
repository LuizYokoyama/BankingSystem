package io.github.LuizYokoyama.BankAccount.dto;

import io.github.LuizYokoyama.BankAccount.entity.OperationType;
import lombok.*;

import java.math.BigDecimal;
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

    private BigDecimal value;

    private LocalDateTime entryDateTime;

}
