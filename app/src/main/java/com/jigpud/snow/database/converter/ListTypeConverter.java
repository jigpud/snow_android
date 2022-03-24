package com.jigpud.snow.database.converter;

import androidx.room.TypeConverter;
import com.jigpud.snow.util.json.JsonUtil;

import java.util.List;

/**
 * @author jigpud
 */
public class ListTypeConverter {
    @TypeConverter
    public String listToString(List<String> list) {
        return JsonUtil.toJson(list);
    }

    @TypeConverter
    @SuppressWarnings("unchecked")
    public List<String> stringToList(String str) {
        return (List<String>) JsonUtil.fromJson(str, List.class);
    }
}
