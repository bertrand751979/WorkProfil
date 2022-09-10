package com.example.workprofil.viewModels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.workprofil.model.Worker;
import com.example.workprofil.repository.RepositoryWorker;

public class DetailWorkerActivityViewModel extends ViewModel {
    public void updateWork(Worker worker, Context context){
        RepositoryWorker.getInstance().updateWorker(worker, context);
    }
}
