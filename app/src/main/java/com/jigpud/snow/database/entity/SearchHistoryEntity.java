package com.jigpud.snow.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import lombok.Data;

/**
 * @author : jigpud
 */
@Entity(tableName = "search_history", indices = {
        @Index(value = "key_words", unique = true)
})
@Data
public class SearchHistoryEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String userid;
    @ColumnInfo(name = "search_time")
    private Long searchTime;
    @ColumnInfo(name = "key_words")
    private String keyWords;
}
