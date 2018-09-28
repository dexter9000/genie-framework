package com.genie.core.exception;

/**
 * 错误码枚举类型
 */
public class ErrorCode {

    public static final ErrorCode ERROR_TIMEOUT = new ErrorCode("error.common.timeout", "Service Timeout");
    public static final ErrorCode ERROR_UNKNOW = new ErrorCode("error.common.unknow", "Unknow error");
    public static final ErrorCode ERROR_ID_EXISTS = new ErrorCode("error.common.idExists", "ID existed");
    public static final ErrorCode ERROR_ID_NOT_EXISTS = new ErrorCode("error.common.idNotExists", "ID not exists");
    public static final ErrorCode ERROR_CLIENT_INTERNAL = new ErrorCode("error.common.clientInternal", "Client Internal Error");

    private String code;
    private String message;

    protected ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
