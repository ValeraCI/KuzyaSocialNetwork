package com.valeraci.kuzyasocialnetwork.dto.users;

import com.valeraci.kuzyasocialnetwork.models.enums.FamilyStatusTitle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
    private String email;
    private String password;
    private String lastName;
    private String firstName;
    private FamilyStatusTitle familyStatusTitle;
}
