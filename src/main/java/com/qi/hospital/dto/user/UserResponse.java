package com.qi.hospital.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserResponse {
    private String userName;

    private String idNumber;

    private String phoneNumber;

    private Integer gender;

    private String address;
}
