package io.github.LuizYokoyama.SchedularPayments.dto;

import io.github.LuizYokoyama.SchedularPayments.entity.EntryStatus;
import io.github.LuizYokoyama.SchedularPayments.entity.OperationType;
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

    private Integer originId;

    private OperationType operationType;

    private float value;

    private LocalDateTime entryDateTime;

    private EntryStatus entryStatus;

}
