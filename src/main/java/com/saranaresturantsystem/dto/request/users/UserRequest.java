package com.saranaresturantsystem.dto.request.users;

import com.saranaresturantsystem.enums.StatusType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Set;

@Data
public class UserRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private StatusType isActive = StatusType.ACTIVE;
    private Long storeId;
    private Set<String> roleCodes;
}
