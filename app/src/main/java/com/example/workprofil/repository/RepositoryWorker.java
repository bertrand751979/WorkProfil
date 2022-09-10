package com.example.workprofil.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.workprofil.ApplicationDataBase;
import com.example.workprofil.model.Worker;

import java.util.List;
import java.util.concurrent.Executors;

public class RepositoryWorker {

    private RepositoryWorker(){}
    public static RepositoryWorker INSTANCE = null;
    public static RepositoryWorker getInstance(){
        if(INSTANCE ==null){
            INSTANCE = new RepositoryWorker();
        }
        return INSTANCE;
    }

    public void addWorker(Worker worker, Context context){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ApplicationDataBase.getInstance(context).getWorkerDao().add(worker);
            }
        });
    }

    public void deleteWorker(Worker worker, Context context){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ApplicationDataBase.getInstance(context).getWorkerDao().delete(worker);

            }
        });
    }

    public void updateWorker(Worker worker, Context context){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ApplicationDataBase.getInstance(context).getWorkerDao().update(worker);
            }
        });

    }
    public LiveData<List<Worker>> getWorkertList (Context context){
        return ApplicationDataBase.getInstance(context).getWorkerDao().getWorkerList();
    }
}
