package com.example.workprofil;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.workprofil.model.Worker;

import java.util.List;


@Dao
public interface WorkerDao {
    @Query("SELECT * FROM Worker")
    LiveData<List<Worker>> getWorkerList();

    @Insert
    void add(Worker worker);

    @Delete
    void delete(Worker worker);

    @Update
    void update(Worker worker);
}
