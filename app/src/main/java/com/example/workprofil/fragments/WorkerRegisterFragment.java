package com.example.workprofil.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.workprofil.R;
import com.example.workprofil.model.Worker;
import com.example.workprofil.viewModels.WorkerRegisterFragmentViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WorkerRegisterFragment extends Fragment {

    private EditText editName;
    private EditText editSkill;
    private TextView photoPath;
    private ImageView photoImageView;
    private Button btnTakePicture;
    private Button btnAddWorker;
    private Button btnDisplayPhotoPath;
    private WorkerRegisterFragmentViewModel workerRegisterFragmentViewModel;
    private ActivityResultLauncher<String>openAlertDialogDeviceToAllowToUseDeviceCamera;
    private ActivityResultLauncher<Intent>checkIfPermissionIsOkToOpenDeviceCameraToTakeAphoto;
    private ActivityResultLauncher<Intent> photoChooseFromGallery;
    private Bitmap imageBitmap;
    private Bitmap selectedImage;
    private AppCompatImageView photoPathImageDisplay;
    private boolean isImageFromCamera = true;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Saisie");
        workerRegisterFragmentViewModel = new ViewModelProvider(this).get(WorkerRegisterFragmentViewModel.class);
        openAlertDialogDeviceToAllowToUseDeviceCamera  = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    takePhotoWithDeviceCamera();
                }else{
                    Toast.makeText(WorkerRegisterFragment.this.getContext(), "Accès  Caméra Refusé", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkIfPermissionIsOkToOpenDeviceCameraToTakeAphoto = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            setImageCapture(result);
                        }
                    }
                });

        photoChooseFromGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            setImageChooseIntoTheGallery(result);
                        }
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_worker,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDisplayPhotoPath = view.findViewById(R.id.btn_display_image_from_photoPath);
        photoPathImageDisplay = view.findViewById(R.id.edit_photo_path_image);
        editName = view.findViewById(R.id.edit_worker_name);
        editSkill = view.findViewById(R.id.edit_worker_skill);
        photoImageView = view.findViewById(R.id.edit_camera_picture);
        btnTakePicture = view.findViewById(R.id.btn_take_picture);
        photoPath = view.findViewById(R.id.edit_photo_path);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertDialogChooseAction();
            }
        });
        btnAddWorker = view.findViewById(R.id.btn_save_worker);
        btnAddWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Worker worker = new Worker();
                worker.setNameWorker(editName.getText().toString());
                worker.setSkillWorker(editSkill.getText().toString());
                worker.setTextPhotoPathWorker(photoPath.getText().toString());
                if(isImageFromCamera==true){
                    worker.setPhotoWorker(saveToCacheMemory(getActivity(),imageBitmap));
                }else{worker.setPhotoWorker(saveToCacheMemory(getActivity(),selectedImage));}
                workerRegisterFragmentViewModel.addWorker(worker,getContext());
                Toast.makeText(WorkerRegisterFragment.this.getContext(), "Ajouté", Toast.LENGTH_SHORT).show();
            }
        });
        btnDisplayPhotoPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoPathImageDisplay.setImageBitmap(BitmapFactory.decodeFile(photoPath.getText().toString()));
            }
        });
    }

    private void openAlertDialogChooseAction() {
        final CharSequence[] options = { "Prendre une photo", "Prendre une photo de la gallerie","Annuler" };
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkerRegisterFragment.this.getContext());
        builder.setTitle("Photo de profil");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Prendre une photo"))
                {
                    openAlertDialogDeviceToAllowToUseDeviceCamera.launch(Manifest.permission.CAMERA);
                }
                else if (options[item].equals("Prendre une photo de la gallerie"))
                {
                    takePhotoFromDeviceGallery();
                }
                else if (options[item].equals("Annuler")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void takePhotoWithDeviceCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        checkIfPermissionIsOkToOpenDeviceCameraToTakeAphoto.launch(takePictureIntent);
    }


    private void takePhotoFromDeviceGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        photoChooseFromGallery.launch(intent);
    }

    public static String saveToCacheMemory(Activity activity, Bitmap bitmapImage){
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        ContextWrapper cw = new ContextWrapper(activity);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath=new File(directory,mDateFormat.format(new Date())  + ".jpeg");
        Worker worker = new Worker();
        worker.setTextPhotoPathWorker(mypath.getAbsolutePath());
        Log.d(TAG, "directory: " + directory);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Log.d(TAG, "bit exception: Success" );
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

    private void setImageCapture(ActivityResult result){
        if(result.getData()!=null){
            Bundle extras = result.getData().getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            photoImageView.setImageBitmap(imageBitmap);
            isImageFromCamera = true;
            saveToCacheMemory(getActivity(),imageBitmap);
            photoPath.setText(saveToCacheMemory(getActivity(), imageBitmap));

        }
    }

    private void setImageChooseIntoTheGallery(ActivityResult result){
        if(result.getData()!=null){
            Intent data = result.getData();
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                photoImageView.setImageBitmap(selectedImage);
                isImageFromCamera = false;
                saveToCacheMemory(getActivity(),selectedImage);
                photoPath.setText(saveToCacheMemory(getActivity(),selectedImage));


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(WorkerRegisterFragment.this.getContext(), "Erreur", Toast.LENGTH_LONG).show();
            }
        }
    }



}
