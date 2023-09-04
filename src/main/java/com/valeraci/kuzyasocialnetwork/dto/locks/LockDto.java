package com.valeraci.kuzyasocialnetwork.dto.locks;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LockDto {
    @NotNull(message = "Number of days should not be empty")
    @Min(value = 1, message = "Number of days should be one or more")
    private int days;
    @NotNull(message = "The reason should not be empty")
    @Size(min = 6, max = 50,
            message = "The reason should be in the range from 6 to 50")
    private String reason;
}
