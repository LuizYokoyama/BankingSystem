package io.github.LuizYokoyama.BankAccount.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DepositDto {

    @Positive(message = "Forneça um valor positivo positivo!")
    private BigDecimal value;

}
