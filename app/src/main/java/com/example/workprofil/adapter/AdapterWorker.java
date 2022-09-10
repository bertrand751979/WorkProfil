package com.example.workprofil.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workprofil.OnImgDeleteClickedAction;
import com.example.workprofil.OnMatCardClickedAction;
import com.example.workprofil.R;
import com.example.workprofil.model.Worker;
import com.example.workprofil.viewHolder.WorkerViewHolder;

import java.util.ArrayList;

public class AdapterWorker extends RecyclerView.Adapter<WorkerViewHolder> {
    private ArrayList<Worker> listWorkerAdapter = new ArrayList<>();
    private OnImgDeleteClickedAction onImgDeleteClickedAction;
    private OnMatCardClickedAction onMatCardClickedAction;

    public AdapterWorker(OnImgDeleteClickedAction onImgDeleteClickedAction, OnMatCardClickedAction onMatCardClickedAction) {
        this.onImgDeleteClickedAction = onImgDeleteClickedAction;
        this.onMatCardClickedAction = onMatCardClickedAction;
    }

    public void setListWorkerAdapter(ArrayList<Worker> listWorkerAdapter) {
        this.listWorkerAdapter = listWorkerAdapter;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.raw_worker,parent,false);
        return new WorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerViewHolder holder, int position) {
        holder.bind(listWorkerAdapter.get(position),onImgDeleteClickedAction,onMatCardClickedAction);
    }

    @Override
    public int getItemCount() {
        return listWorkerAdapter.size();
    }
}
