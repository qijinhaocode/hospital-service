package com.qi.hospital.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginRequest {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}
