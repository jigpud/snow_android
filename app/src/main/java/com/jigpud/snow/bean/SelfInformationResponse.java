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
public class SelfInformationResponse {
    private String username;
    private String nickname;
    private String signature;
    private String gender;
    private Integer age;
    private Long likes;
    private Long followers;
    private Long followed;
}
