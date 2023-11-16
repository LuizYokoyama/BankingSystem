package io.github.LuizYokoyama.SchedularPayments.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EditRecurrenceDto {

    @NotNull
    private LocalDate occurrenceDate;

    @Positive
    @Max(12)
    private int duration;

    @Positive
    private float value;

}
