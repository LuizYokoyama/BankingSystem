package io.github.LuizYokoyama.BankAccount.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DepositDto {

    @NotBlank(message = "Forneça o valor!")
    @Positive(message = "O valor deve ser positivo!")
    private float value;

}
