package com.jigpud.snow.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jigpud
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponseStatus {
    private String message;
    private Integer code;

    public boolean isSuccess() {
        return getCode() == 200;
    }
}
