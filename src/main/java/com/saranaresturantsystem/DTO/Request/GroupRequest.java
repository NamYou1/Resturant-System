package com.saranaresturantsystem.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequest {
    @NotNull(message = "Name can't null")
    @NotBlank(message = "Name is required")
    @Size(max = 50,message = "Group name must be at most 50 characters")
    private String name;
    private String description;
}
