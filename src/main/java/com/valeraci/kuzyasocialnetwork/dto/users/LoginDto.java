package com.valeraci.kuzyasocialnetwork.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDto {
    @NotNull(message = "The email should not be empty")
    @Email(message = "No email was entered")
    private String email;
    @NotNull(message = "The password should not be empty")
    @Size(min = 6, max = 50,
            message = "The password should be in the range from 6 to 50")
    private String password;
}
