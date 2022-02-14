package com.qi.hospital.dto.user;

import javax.validation.constraints.NotBlank;
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

    private Integer gender;

    private String address;
}
