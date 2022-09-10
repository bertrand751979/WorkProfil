package com.example.workprofil.viewModels;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.workprofil.model.Worker;
import com.example.workprofil.repository.RepositoryWorker;
import java.util.List;

public class WorkerDisplayFragmentViewModel extends ViewModel {
    public LiveData<List<Worker>> getDisplayList (Context context){
        return RepositoryWorker.getInstance().getWorkertList(context);
    }

    public void deleteWorker(Worker worker, Context context){
        RepositoryWorker.getInstance().deleteWorker(worker, context);
    }
}
