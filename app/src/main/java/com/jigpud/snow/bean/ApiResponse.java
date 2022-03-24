package com.jigpud.snow.bean;

/**
 * @author jigpud
 */
public class ApiResponse<T> extends ApiResponseStatus {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess() && getData() != null;
    }
}
