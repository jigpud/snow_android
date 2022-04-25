package com.jigpud.snow.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.jigpud.snow.database.entity.AttractionEntity;

import java.util.List;

/**
 * @author : jigpud
 */
@Dao
public interface AttractionDao {
    @Query("SELECT * FROM attraction WHERE attraction_id=:attractionId")
    AttractionEntity getAttraction(String attractionId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AttractionEntity attraction);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AttractionEntity> attractionList);

    @Query("SELECT * FROM attraction")
    List<AttractionEntity> getAttractionList();
}
