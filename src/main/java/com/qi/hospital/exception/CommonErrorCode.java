package com.qi.hospital.exception;

public enum CommonErrorCode implements ErrorCode {

    E_100101(100101,"用户名已经存在，请更换用户名"),
    E_100102(100102,"普通用户用户名不能为admin，请更换"),
    E_100103(100103,"用户名密码不匹配，请重试"),

    /**
     * 未知错误
     */
    UNKNOWN(999999,"未知错误");


    private int code;
    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private CommonErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static CommonErrorCode setErrorCode(int code) {
        for (CommonErrorCode errorCode : CommonErrorCode.values()) {
            if (errorCode.getCode()==code) {
                return errorCode;
            }
        }
        return null;
    }
    }