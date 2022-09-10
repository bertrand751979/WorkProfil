package com.example.workprofil.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.workprofil.model.Worker;
import com.example.workprofil.repository.RepositoryWorker;

public class WorkerRegisterFragmentViewModel extends ViewModel {
    public void addWorker(Worker worker, Context context){
        RepositoryWorker.getInstance().addWorker(worker,context);
    }
}
