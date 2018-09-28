package com.genie.core.web.rest.vm;


import com.genie.core.web.rest.errors.ErrorVM;

/**
 * Created by meng013 on 2017/5/19.
 */
public class ResultVM<T> {
    private ResultStatus status;    // 响应结果
    private T data;                 // 返回数据
    private String message;         // 响应消息
    private ErrorVM error;          // 错误响应信息
    private String resourceKey;     // 国际化Key，用于在前端实现国际化

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorVM getError() {
        return error;
    }

    public void setError(ErrorVM error) {
        this.error = error;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();

        result.append("ResultVM{");
        result.append("status='").append(status).append('\'');
        if (message != null) {
            result.append("message='").append(message).append('\'');
        }
        if (resourceKey != null) {
            result.append("resourceKey='").append(resourceKey).append('\'');
        }
        if (data != null) {
            result.append("data='").append(data).append('\'');
        }
        if (error != null) {
            result.append("error='").append(error).append('\'');
        }
        result.append("}");

        return result.toString();
    }
}
