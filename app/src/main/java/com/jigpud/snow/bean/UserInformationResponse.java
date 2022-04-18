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
public class UserInformationResponse {
    private String userid;
    private String nickname;
    private String avatar;
    private String background;
    private String signature;
    private String gender;
    private int age;
    private long likes;
    private long followers;
    private long following;
    private boolean followed;
}
