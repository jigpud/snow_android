package com.jigpud.snow.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
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
    private Long id;
    private String username;
    private String userid;
    private String avatar;
    private String background;
    private String gender;
    private String signature;
    private String nickname;
    private Long likes;
    private Integer age;
    private Long followers;
    private Long followed;
    @ColumnInfo(name = "have_followed")
    private Boolean haveFollowed;
}
