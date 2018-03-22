package com.example.cristianverdes.bakingapp.data.local;

/**
 * Created by cristian.verdes on 22.03.2018.
 */

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.cristianverdes.bakingapp.data.model.Timer;

/** CLASS: TimerDao
 *  The purpose of the class is to store the date of the moment we cached the data from the API.
 *  We'll check this value and if X time passed we'll make another request to the API.
 *
 *  @author Cristian Verdes
 *  @version 1.0
 *  @since 22-03-2018
 */

@Dao
public interface TimerDao {

    @Query("SELECT * FROM TIMER WHERE id = 0")
    Timer getCreatedAt();

    @Query("SELECT COUNT(*) FROM TIMER WHERE id = 0")
    int getEntryExists();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Timer... timers);

    @Delete
    void delete(Timer timer);
}
