package com.qi.hospital.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCriteria {
    private String userName;

    private String idNumber;
}
