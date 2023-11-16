package io.github.LuizYokoyama.BankAccount.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PeriodDto {

    private LocalDate initDate;
    private LocalDate endDate;

}
