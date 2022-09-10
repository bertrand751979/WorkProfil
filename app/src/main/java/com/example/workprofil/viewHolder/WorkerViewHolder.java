package com.example.workprofil.viewHolder;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workprofil.OnImgDeleteClickedAction;
import com.example.workprofil.OnMatCardClickedAction;
import com.example.workprofil.R;
import com.example.workprofil.model.Worker;
import com.google.android.material.card.MaterialCardView;

public class WorkerViewHolder extends RecyclerView.ViewHolder {
    private ImageView vhWorkerPhoto;
    private TextView vhWorkerName;
    private TextView vhWorkerSkill;
    private ImageView vhDeleteWorker;
    private TextView vhPhotoPathText;
    private MaterialCardView vhMaterialCard;


    public WorkerViewHolder(@NonNull View view) {
        super(view);
        vhWorkerPhoto = view.findViewById(R.id.raw_photo);
        vhWorkerName = view.findViewById(R.id.raw_name);
        vhWorkerSkill = view.findViewById(R.id.raw_skill);
        vhDeleteWorker = view.findViewById(R.id.raw_btn_delete);
        vhMaterialCard = view.findViewById(R.id.raw_material_card);
        vhPhotoPathText = view.findViewById(R.id.raw_photo_path_text);
    }

    public WorkerViewHolder(@NonNull View itemView, ImageView vhWorkerPhoto, TextView vhWorkerName,
                            TextView vhWorkerSkill, ImageView vhDeleteWorker, TextView vhPhotoPathText,
                            MaterialCardView vhMaterialCard) {
        super(itemView);
        this.vhWorkerPhoto = vhWorkerPhoto;
        this.vhWorkerName = vhWorkerName;
        this.vhWorkerSkill = vhWorkerSkill;
        this.vhDeleteWorker = vhDeleteWorker;
        this.vhPhotoPathText = vhPhotoPathText;
        this.vhMaterialCard = vhMaterialCard;
    }

    public void bind(Worker worker, OnImgDeleteClickedAction onImgDeleteClickedAction, OnMatCardClickedAction onMatCardClickedAction){
        vhWorkerPhoto.setImageBitmap(BitmapFactory.decodeFile(worker.getPhotoWorker()));
        vhWorkerName.setText(worker.getNameWorker());
        vhWorkerSkill.setText(worker.getSkillWorker());
        //vhPhotoPathText.setText(worker.getTextPhotoPathWorker());
        vhDeleteWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImgDeleteClickedAction.delete(worker);
            }
        });
        vhMaterialCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMatCardClickedAction.goToDetails(worker);
            }
        });
    }
}
