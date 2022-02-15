package com.qi.hospital.exception;

import lombok.Data;

@Data
public class RestErrorResponse {
    private String errCode;
    private String errMessage;

    public RestErrorResponse(String errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }
}
