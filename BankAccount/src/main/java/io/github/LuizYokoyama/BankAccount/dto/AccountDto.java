package io.github.LuizYokoyama.BankAccount.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountDto {

    public static final int NAME_MAX_SIZE = 40;

    @EqualsAndHashCode.Include
    @NotBlank(message = "Forneça a conta!")
    private int accountId;

    @NotBlank(message = "Forneça o nome do titular!")
    @Size(max = NAME_MAX_SIZE)
    private String holderName;

    private float balance;

    private Set<RecurrenceDto> recurrenceList;

    @Valid
    private Set<EntryDto> entryList;
}
