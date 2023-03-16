package edu.ucsd.cse110.cse110group8_compass.model;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UUID.class}, version = 2, exportSchema = false)
public abstract class UUIDDatabase extends RoomDatabase{

    private volatile static UUIDDatabase instance = null;

    public abstract UUIDDao getDao();

    public synchronized static UUIDDatabase provide(Context context) {
        if (instance == null) {
            instance = UUIDDatabase.make(context);
        }
        return instance;
    }

    private static UUIDDatabase make(Context context) {
        return Room.databaseBuilder(context, UUIDDatabase.class, "note_app.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    @VisibleForTesting
    public static void inject(UUIDDatabase testDatabase) {
        if (instance != null ) {
            instance.close();
        }
        instance = testDatabase;
    }

}
