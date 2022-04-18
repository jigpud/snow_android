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
    private String userid;
    private String nickname;
    private String avatar;
    private String background;
    private String signature;
    private String gender;
    private int age;
    private long favorites;
    private long followers;
    private long following;
}
