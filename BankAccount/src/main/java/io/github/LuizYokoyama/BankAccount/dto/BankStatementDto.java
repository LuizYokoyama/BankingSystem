package io.github.LuizYokoyama.BankAccount.dto;

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
    private AccountCreatedDto accountCreatedDto;
    private PeriodDto periodDto;
    private List<EntryDto> entryList;
}
