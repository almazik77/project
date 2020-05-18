package ru.itis.carsharing.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpForm {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull(message = "{errors.null.email}")
    @Length(min = 3, message = "{errors.incorrect.email}")
    private String email;
    @NotNull(message = "{errors.incorrect.password}")
    @Length(min = 3, message = "{errors.incorrect.password}")
    private String password;
}
