package com.jigpud.snow.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jigpud
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ApiResponse<T> extends ApiResponseStatus {
    private T data;

    public ApiResponse(String message, int code, T data) {
        super(message, code);
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess() && getData() != null;
    }
}
