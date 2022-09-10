package com.example.workprofil.activities;

import static com.example.workprofil.activities.MainActivity.WORKER_KEY;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.workprofil.ApplicationDataBase;
import com.example.workprofil.R;
import com.example.workprofil.model.Worker;
import com.example.workprofil.repository.RepositoryWorker;
import com.example.workprofil.viewModels.DetailWorkerActivityViewModel;

public class DetailWorkerActivity extends AppCompatActivity {
    private ImageView descPhotoWorker;
    private EditText descNameWorker;
    private EditText descSkillWorker;
    private ImageView descImagePhotoPath;
    private Button descUpdateWorker;
    private Button descCancel;
    private Button desTestBtn;
    private TextView descTxtPhotoPath;
    private Worker worker;
    private DetailWorkerActivityViewModel detailWorkerActivityViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        worker = (Worker) getIntent().getSerializableExtra(WORKER_KEY);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailWorkerActivityViewModel = new ViewModelProvider(this).get(DetailWorkerActivityViewModel.class);
        descPhotoWorker = findViewById(R.id.desc_photo);
        descNameWorker = findViewById(R.id.desc_name);
        descTxtPhotoPath = findViewById(R.id.desc_test_txt_photo_path);
        descSkillWorker = findViewById(R.id.desc_skill);
        descUpdateWorker = findViewById(R.id.desc_btn_update);
        descCancel = findViewById(R.id.desc_btn_cancel);
        descImagePhotoPath = findViewById(R.id.desc_test_photo);
        desTestBtn = findViewById(R.id.desc_btn_test_image_path);
        descPhotoWorker.setImageBitmap(BitmapFactory.decodeFile(worker.getPhotoWorker()));
        descNameWorker.setText(worker.getNameWorker());
        descSkillWorker.setText(worker.getSkillWorker());
        descTxtPhotoPath.setText(worker.getTextPhotoPathWorker());
        desTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descImagePhotoPath.setImageBitmap(BitmapFactory.decodeFile(descTxtPhotoPath.getText().toString()));

            }
        });

        descUpdateWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                worker.setNameWorker(descNameWorker.getText().toString());
                worker.setSkillWorker(descSkillWorker.getText().toString());
                detailWorkerActivityViewModel.updateWork(worker,DetailWorkerActivity.this);
            }
        });

        descCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
