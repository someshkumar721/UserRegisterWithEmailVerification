package com.user_service.users.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {

    private UUID userId;

    @NotBlank(message = "User name is mandatory")
    private String userName;

    @NotBlank(message = "User email is mandatory")
    @Email(message = "User email must be a valid email address")
    private String userEmail;

    @NotBlank(message = "Mobile number is mandatory")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be a valid 10-digit number")
    private String mobileNo;

    private String parentName;

    private String grade;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
