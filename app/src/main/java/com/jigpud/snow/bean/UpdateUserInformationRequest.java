package com.jigpud.snow.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : jigpud
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateUserInformationRequest {
    private String nickname;
    private String gender;
    private Integer age;
    private String signature;
    private String avatar;
    private String background;
}
