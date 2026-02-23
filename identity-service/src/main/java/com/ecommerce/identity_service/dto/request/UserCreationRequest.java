package com.ecommerce.identity_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3,  message = "USERNAME_VALID")
    String username;

    String firstName;
    String lastName;

    @Size(min = 8,  message = "PASSWORD_VALID")
    String password;
    String email;
    String city;
    Date dob;
}