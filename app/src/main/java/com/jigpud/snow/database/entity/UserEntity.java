package com.jigpud.snow.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.jigpud.snow.bean.SelfInformationResponse;
import com.jigpud.snow.bean.UserInformationResponse;
import lombok.Data;

/**
 * @author jigpud
 */
@Entity(tableName = "user", indices = {
        @Index(value = { "userid" }, unique = true)
})
@Data
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String username;

    private String userid;

    private String avatar;

    private String background;

    private String gender;

    private String signature;

    private String nickname;

    private long likes;

    private int age;

    private long followers;

    private long following;

    @ColumnInfo(name = "followed")
    private boolean followed;

    private long favorites;

    public static UserEntity create(UserInformationResponse userInfo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserid(userInfo.getUserid());
        userEntity.setGender(userInfo.getGender());
        userEntity.setAge(userInfo.getAge());
        userEntity.setNickname(userInfo.getNickname());
        userEntity.setSignature(userInfo.getSignature());
        userEntity.setLikes(userInfo.getLikes());
        userEntity.setFollowers(userInfo.getFollowers());
        userEntity.setFollowing(userInfo.getFollowing());
        userEntity.setBackground(userInfo.getBackground());
        userEntity.setAvatar(userInfo.getAvatar());
        userEntity.setFollowed(userInfo.isFollowed());
        return userEntity;
    }

    public static UserEntity create(SelfInformationResponse selfInfo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(selfInfo.getUsername());
        userEntity.setUserid(selfInfo.getUserid());
        userEntity.setGender(selfInfo.getGender());
        userEntity.setAge(selfInfo.getAge());
        userEntity.setNickname(selfInfo.getNickname());
        userEntity.setSignature(selfInfo.getSignature());
        userEntity.setFavorites(selfInfo.getFavorites());
        userEntity.setFollowers(selfInfo.getFollowers());
        userEntity.setFollowing(selfInfo.getFollowing());
        userEntity.setBackground(selfInfo.getBackground());
        userEntity.setAvatar(selfInfo.getAvatar());
        return userEntity;
    }
}
