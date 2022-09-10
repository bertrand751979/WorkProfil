package com.example.workprofil.fragments;

import static com.example.workprofil.activities.MainActivity.WORKER_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workprofil.OnImgDeleteClickedAction;
import com.example.workprofil.OnMatCardClickedAction;
import com.example.workprofil.R;
import com.example.workprofil.activities.DetailWorkerActivity;
import com.example.workprofil.adapter.AdapterWorker;
import com.example.workprofil.model.Worker;
import com.example.workprofil.viewModels.WorkerDisplayFragmentViewModel;
import com.example.workprofil.viewModels.WorkerRegisterFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkerDisplayFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdapterWorker adapterWorker;
    private WorkerDisplayFragmentViewModel workerDisplayFragmentViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workerDisplayFragmentViewModel = new ViewModelProvider(this).get(WorkerDisplayFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_worker,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        setViewWorker();
    }
    private  void setViewWorker(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        OnMatCardClickedAction onMatCardClickedAction = new OnMatCardClickedAction() {
            @Override
            public void goToDetails(Worker worker) {
                Intent intent = new Intent(WorkerDisplayFragment.this.getContext(), DetailWorkerActivity.class);
                intent.putExtra(WORKER_KEY, worker);
                startActivity(intent);
            }
        };

        OnImgDeleteClickedAction onImgDeleteClickedAction = new OnImgDeleteClickedAction() {
            @Override
            public void delete(Worker worker) {
                workerDisplayFragmentViewModel.deleteWorker(worker,getContext());
            }
        };

        adapterWorker = new AdapterWorker(onImgDeleteClickedAction, onMatCardClickedAction);
        recyclerView.setAdapter(adapterWorker);
        workerDisplayFragmentViewModel.getDisplayList(getContext()).observe(getViewLifecycleOwner(), new Observer<List<Worker>>() {
            @Override
            public void onChanged(List<Worker> workers) {
                adapterWorker.setListWorkerAdapter(new ArrayList<>(workers));
            }
        });

    }

}
