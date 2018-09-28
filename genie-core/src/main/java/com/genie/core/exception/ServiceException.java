package com.genie.core.exception;

/**
 * 服务异常类，service级别的异常管理，Resource自动处理该类型或者继承自该类型的异常。
 * Created by meng013 on 2017/6/3.
 */
public class ServiceException extends RuntimeException {

    /**
     * 错误码
     */
    private final String errorCode;

    /**
     * 错误参数
     */
    private final String[] errorParms;

    /**
     * 默认错误消息
     */
    private final String errorMessage;

    /**
     * @param errorCode 错误码
     */
    public ServiceException(String errorCode) {
        this(errorCode, "");
    }

    /**
     * @param errorCode  错误码
     * @param errorParms 错误参数
     */
    public ServiceException(String errorCode, String[] errorParms) {
        this(errorCode, errorParms, null);
    }

    /**
     * @param errorCode 错误码
     * @param errorMessage 默认错误消息
     */
    public ServiceException(String errorCode, String errorMessage) {
        this(errorCode, null, "", null);
    }

    /**
     * @param errorCode 错误码
     * @param cause 错误原因
     */
    public ServiceException(String errorCode, Throwable cause) {
        this(errorCode, null, "", cause);
    }

    /**
     * @param errorCode 错误码
     * @param errorParms 错误参数
     * @param cause 错误原因
     */
    public ServiceException(String errorCode, String[] errorParms, Throwable cause) {
        this(errorCode, errorParms, "", cause);
    }

    /**
     * @param errorCode 错误码
     * @param errorMessage 默认错误消息
     * @param cause 错误原因
     */
    public ServiceException(String errorCode, String errorMessage, Throwable cause) {
        this(errorCode, null, errorMessage, cause);
    }

    /**
     * @param errorCode 错误码
     */
    public ServiceException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * @param errorCode 错误码
     * @param errorParms 错误参数
     */
    public ServiceException(ErrorCode errorCode, String[] errorParms) {
        this(errorCode.getCode(), errorParms, null);
    }

    /**
     * @param errorCode 错误码
     * @param cause 错误原因
     */
    public ServiceException(ErrorCode errorCode, Throwable cause) {
        this(errorCode.getCode(), errorCode.getMessage(), cause);
    }

    /**
     * @param errorCode 错误码
     * @param errorParms 错误参数
     * @param cause 错误原因
     */
    public ServiceException(ErrorCode errorCode, String[] errorParms, Throwable cause) {
        this(errorCode.getCode(), errorParms, cause);
    }


    /**
     * @param errorCode 错误码
     * @param errorParms 错误参数
     * @param errorMessage 默认错误消息
     * @param cause 错误原因
     */
    public ServiceException(String errorCode, String[] errorParms, String errorMessage, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorParms = errorParms;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String[] getErrorParms() {
        return errorParms;
    }
}
