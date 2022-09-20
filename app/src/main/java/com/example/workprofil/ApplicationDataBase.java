package com.example.workprofil;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.workprofil.model.Worker;


@Database(entities = {Worker.class},version = 28)
public abstract class ApplicationDataBase extends RoomDatabase {
    private static ApplicationDataBase INSTANCE;
    public abstract WorkerDao getWorkerDao();
    public static synchronized ApplicationDataBase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ApplicationDataBase.class, "worker_app").build();
        }
        return INSTANCE;
    }
}
