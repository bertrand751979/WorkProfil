package com.example.workprofil.activities;

import static android.content.ContentValues.TAG;
import static com.example.workprofil.activities.MainActivity.WORKER_KEY;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.workprofil.R;
import com.example.workprofil.model.Worker;
import com.example.workprofil.viewModels.DetailWorkerActivityViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailWorkerActivity extends AppCompatActivity {
    private ImageView descPhotoWorker;
    private EditText descNameWorker;
    private EditText descSkillWorker;
    private Button descUpdateWorker;
    private Button descCancel;
    private Worker worker;
    private Button descUpdatePhoto;
    private DetailWorkerActivityViewModel detailWorkerActivityViewModel;
    private ActivityResultLauncher<String> displayAlertDialogPermission;
    private ActivityResultLauncher<Intent> intentToTakePhotoCapture;
    private ActivityResultLauncher<Intent> intentTotakePhotofromGallery;
    private Bitmap imageBitmap;
    private Bitmap selectedImage;
    public boolean isImageFromCamera = true;
    public boolean isClicked = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        worker = (Worker) getIntent().getSerializableExtra(WORKER_KEY);
        Log.d("chemin :","Test"+(worker.getTextPhotoPathWorker()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detailWorkerActivityViewModel = new ViewModelProvider(this).get(DetailWorkerActivityViewModel.class);
        displayAlertDialogPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    takePhotoFromCapture();
                } else {
                    Toast.makeText(DetailWorkerActivity.this, "Accès  Caméra Refusé", Toast.LENGTH_SHORT).show();
                }
            }
        });


        intentToTakePhotoCapture = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    setImageCapture(result);
                }
            }
        });

        intentTotakePhotofromGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    setImageChooseIntoTheGallery(result);

                }
            }
        });

        descPhotoWorker = findViewById(R.id.desc_photo);
        descUpdatePhoto = findViewById(R.id.desc_change_photo);
        descNameWorker = findViewById(R.id.desc_name);
        descSkillWorker = findViewById(R.id.desc_skill);
        descUpdateWorker = findViewById(R.id.desc_btn_update);
        descCancel = findViewById(R.id.desc_btn_cancel);
        descPhotoWorker.setImageBitmap(BitmapFactory.decodeFile(worker.getPhotoWorker()));
        descNameWorker.setText(worker.getNameWorker());
        descSkillWorker.setText(worker.getSkillWorker());
        descUpdateWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                worker.setNameWorker(descNameWorker.getText().toString());
                worker.setSkillWorker(descSkillWorker.getText().toString());

                if (isClicked == true) {
                    if (isImageFromCamera == true) {
                        worker.setPhotoWorker(saveToCacheMemory(DetailWorkerActivity.this, imageBitmap));
                    } else {
                        worker.setPhotoWorker(saveToCacheMemory(DetailWorkerActivity.this, selectedImage));
                    }
                } else{
                    Toast.makeText(DetailWorkerActivity.this, "Pas de modif photo", Toast.LENGTH_SHORT).show();
                }

                detailWorkerActivityViewModel.updateWorker(worker, DetailWorkerActivity.this);
            }
        });

        descUpdatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (descUpdatePhoto == view) {
                    isClicked = true;
                }
                openAlertDialogChooseAction();
            }
        });

        descCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void takePhotoFromCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentToTakePhotoCapture.launch(intent);
    }

    private void takePhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intentTotakePhotofromGallery.launch(intent);
    }


    private void openAlertDialogChooseAction() {
        final CharSequence[] options = {"Prendre une photo", "Prendre une photo de la gallerie", "Annuler"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DetailWorkerActivity.this);
        builder.setTitle("Photo de profil");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Prendre une photo")) {
                    displayAlertDialogPermission.launch(Manifest.permission.CAMERA);
                } else if (options[item].equals("Prendre une photo de la gallerie")) {
                    takePhotoFromGallery();
                } else if (options[item].equals("Annuler")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static String saveToCacheMemory(Activity activity, Bitmap bitmapImage) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        ContextWrapper cw = new ContextWrapper(activity);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, mDateFormat.format(new Date()) + ".jpeg");
        Worker worker = new Worker();
        worker.setTextPhotoPathWorker(mypath.getAbsolutePath());
        Log.d(TAG, "directory: " + directory);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Log.d(TAG, "bit exception: Success");
        } catch (Exception e) {
            Log.d(TAG, "bit exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "io exce: " + e.getMessage());
            }
        }
        Log.d(TAG, "absolute path " + directory.getAbsolutePath());
        return mypath.getAbsolutePath();
    }

    private void setImageCapture(ActivityResult result) {
        if (result.getData() != null) {
            Bundle extras = result.getData().getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            descPhotoWorker.setImageBitmap(imageBitmap);
            isImageFromCamera = true;
            saveToCacheMemory(DetailWorkerActivity.this, imageBitmap);
        }
    }

    private void setImageChooseIntoTheGallery(ActivityResult result) {
        if (result.getData() != null) {
            Intent data = result.getData();
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = DetailWorkerActivity.this.getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                descPhotoWorker.setImageBitmap(selectedImage);
                isImageFromCamera = false;
                saveToCacheMemory(DetailWorkerActivity.this, selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
