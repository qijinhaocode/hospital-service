package com.qi.hospital.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    @NotBlank
    private String userName;
    @NotBlank
    private String idNumber;
    @NotBlank
    private String phoneNumber;

    private String password;

    private Integer gender;

    private String address;
}
