package com.jigpud.snow.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import lombok.Data;

/**
 * @author jigpud
 */
@Entity(tableName = "token", indices = {
        @Index(value = { "username" }, unique = true)
})
@Data
public class TokenEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String username;
    private String token;
    @ColumnInfo(name = "refresh_token")
    private String refreshToken;
}
