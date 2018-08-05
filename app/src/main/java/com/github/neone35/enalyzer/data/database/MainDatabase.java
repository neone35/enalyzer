package com.github.neone35.enalyzer.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.github.neone35.enalyzer.data.models.room.Additive;
import com.github.neone35.enalyzer.data.models.room.CodeCategory;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;
import com.orhanobut.logger.Logger;

// version number needs to be incremented if schema models change
@Database(entities = {Additive.class, ScanPhoto.class, CodeCategory.class}, version = 1)
@TypeConverters({RoomConverters.class})
public abstract class MainDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "mainDB";
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MainDatabase sInstance;

    public static MainDatabase getInstance(Context context) {
//        Logger.d("Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MainDatabase.class, MainDatabase.DATABASE_NAME).build();
                Logger.d("Made new database");
            }
        }
        return sInstance;
    }

    // The associated DAOs for the database
    public abstract AdditiveDao additiveDao();

    public abstract ScanPhotoDao scanPhotoDao();

    public abstract CodeCategoryDao codeCategoryDao();
}
