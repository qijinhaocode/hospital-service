package com.qi.hospital.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserRequest {
    @NotBlank
    private String userName;
    @NotBlank
    private String idNumber;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String password;
    @NotNull
    private Integer gender;
    @NotBlank
    private String address;
}
