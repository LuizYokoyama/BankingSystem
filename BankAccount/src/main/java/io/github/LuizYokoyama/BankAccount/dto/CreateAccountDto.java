package io.github.LuizYokoyama.BankAccount.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateAccountDto {

    public static final int NAME_MAX_SIZE = 40;

    @NotBlank(message = "Forneça o nome do titular!")
    @Size(max = NAME_MAX_SIZE)
    private String holderName;

    private float balance;

}
