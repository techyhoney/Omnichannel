package com.kpmg.Omnichannel.dto;

import com.kpmg.Omnichannel.model.KycStatus;
import com.kpmg.Omnichannel.model.UserStatus;
import com.kpmg.Omnichannel.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserType userType;
    private UserStatus status;
    private KycStatus kycStatus;
    private Set<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

