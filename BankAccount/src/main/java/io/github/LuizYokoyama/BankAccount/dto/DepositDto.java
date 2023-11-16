package io.github.LuizYokoyama.BankAccount.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DepositDto {

    @Positive(message = "Forneça um valor positivo positivo!")
    private float value;

}
