package com.qi.hospital.dto.user;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String userName;

    private String idNumber;

    private String phoneNumber;

    private String password;

    private Integer gender;

    private String address;
}
