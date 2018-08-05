package com.github.neone35.enalyzer.data.database;

import android.arch.persistence.room.TypeConverter;

import com.github.neone35.enalyzer.data.models.room.Hazard;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class RoomConverters {
    private static Gson gson = new Gson();

    @TypeConverter
    public static String hazardListToString(List<Hazard> objects) {
        return gson.toJson(objects);
    }

    @TypeConverter
    public static List<Hazard> stringToHazardList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Hazard>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }
}
