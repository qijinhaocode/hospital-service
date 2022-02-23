package com.qi.hospital.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AdminLoginRequest {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}
