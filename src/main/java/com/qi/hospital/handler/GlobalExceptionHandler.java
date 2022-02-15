package com.qi.hospital.handler;

import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.exception.ErrorCode;
import com.qi.hospital.exception.RestErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestErrorResponse processException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        if (e instanceof BusinessException) {
            log.info(((BusinessException) e).getErrorCode().getDesc());

            BusinessException businessException = (BusinessException) e;
            ErrorCode errorCode = businessException.getErrorCode();
            int code = errorCode.getCode();
            String desc = errorCode.getDesc();

            return new RestErrorResponse(String.valueOf(code), desc);
        }

        log.error("系统异常:", e);
        return new RestErrorResponse(String.valueOf(CommonErrorCode.UNKNOWN.getCode()), CommonErrorCode.UNKNOWN.getDesc());
    }
}