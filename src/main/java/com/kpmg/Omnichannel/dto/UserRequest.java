package com.kpmg.Omnichannel.dto;

import com.kpmg.Omnichannel.model.KycStatus;
import com.kpmg.Omnichannel.model.UserStatus;
import com.kpmg.Omnichannel.model.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String phone;

    @NotNull(message = "User type is required")
    private UserType userType;

    private UserStatus status;
    
    private KycStatus kycStatus;
}

