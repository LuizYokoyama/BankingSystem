package io.github.LuizYokoyama.BankAccount.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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

    @PastOrPresent
    @NotNull
    private LocalDate initDate;

    @NotNull
    private LocalDate endDate;

}
