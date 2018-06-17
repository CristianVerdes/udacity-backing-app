package com.example.cristianverdes.bakingapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by cristian.verdes on 22.03.2018.
 */

@Entity(tableName = "timer")
public class Timer {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "created_at")
    private String createdAt;

    // Constructor
    public Timer(String createdAt) {
        // We set the id to 0 because we cache the data only once.
        // And we update this entry when we need to cache again.
        this.id = 0;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
