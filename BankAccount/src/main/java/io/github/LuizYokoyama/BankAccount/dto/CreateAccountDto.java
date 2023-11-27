package io.github.LuizYokoyama.BankAccount.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateAccountDto{

    public static final int NAME_MAX_SIZE = 40;
    public static final int NAME_MIN_SIZE  = 3;

    @NotBlank(message = "Forneça o nome do titular!")
    @Size(max = NAME_MAX_SIZE, min = NAME_MIN_SIZE)
    private String holderName;

    @PositiveOrZero
    private float balance;

}
