package com.valeraci.kuzyasocialnetwork.dto.users;

import com.valeraci.kuzyasocialnetwork.models.enums.FamilyStatusTitle;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
    @NotNull(message = "The email should not be empty")
    @Email(message = "No email was entered")
    private String email;
    @NotNull(message = "The password should not be empty")
    @Size(min = 6, max = 50,
            message = "The password should be in the range from 6 to 50")
    private String password;
    @NotNull(message = "The lastName should not be empty")
    @Size(min = 2, max = 50,
            message = "The lastName should be in the range from 2 to 50")
    private String lastName;
    @NotNull(message = "The firstName should not be empty")
    @Size(min = 2, max = 50,
            message = "The firstName should be in the range from 2 to 50")
    private String firstName;
    @NotNull(message = "The FamilyStatusTitle should not be empty")
    private FamilyStatusTitle familyStatusTitle;
}
