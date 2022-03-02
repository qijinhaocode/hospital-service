package com.qi.hospital.exception;

public enum CommonErrorCode implements ErrorCode {

    E_100101(100101,"用户已经存在，请更换电话号码或者身份证号！"),
    E_100102(100102,"普通用户用户名不能为admin，请更换"),
    E_100103(100103,"用户名密码不匹配，请重试"),
    E_100104(100104,"部门名称重复，请更换部门名称重试！"),
    E_100105(100105,"预删除部门名称不存在，请重试！"),
    E_100106(100106,"预编辑部门名称不存在，请重试！"),
    E_100107(100107,"医生工号重复，请更换工号重试！"),
    E_100108(100108,"预删除医生工号不存在，请重试！"),
    E_100109(100109,"预修改医生工号不存在，请重试！"),
    E_100110(100110,"预删除医生工号不存在，请重试！"),

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