package ru.itis.carsharing.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInForm {
    @NotNull(message = "{errors.null.email}")
    @Email(message = "{errors.incorrect.email}")
    private String email;
    @NotNull(message = "{errors.incorrect.password}")
    private String password;
}
