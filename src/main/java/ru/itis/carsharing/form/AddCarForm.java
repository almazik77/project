package ru.itis.carsharing.form;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCarForm {
    @NotNull
    private String model;
    @NotNull
    @Min(value = 1)
    private Long cost;
    @NotNull
    private String coordinates;
}
