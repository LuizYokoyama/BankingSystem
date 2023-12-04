package io.github.LuizYokoyama.BankAccount.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BankStatementDto {
    private AccountCreatedDto account;
    private PeriodDto period;
    @Valid
    private List<EntryDto> entryList;
}
