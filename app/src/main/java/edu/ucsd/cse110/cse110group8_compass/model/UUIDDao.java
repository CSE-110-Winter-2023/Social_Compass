package edu.ucsd.cse110.cse110group8_compass.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

import edu.ucsd.cse110.cse110group8_compass.Pin;
@Dao
public abstract class UUIDDao {

    @Upsert
    public abstract long upsert(UUID uuid);

    @Query("SELECT EXISTS(SELECT 1 FROM uuid WHERE public_code = :public_code)")
    public abstract boolean exists(String public_code);

    @Query("SELECT * FROM uuid WHERE public_code = :public_code")
    public abstract LiveData<UUID> get(String public_code);

    @Query("SELECT * FROM uuid ORDER BY public_code")
    public abstract LiveData<List<UUID>> getAll();

    @Delete
    public abstract int delete(UUID uuid);

}
