package com.example.chessproject.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chessproject.R;
import com.example.chessproject.UserUtils.UserData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class UserProfileScreenFragment extends Fragment implements View.OnClickListener {

    public UserProfileScreenFragment() {
        // Required empty public constructor
    }

    ActivityResultLauncher activityResultLauncher;
    ImageView profilePicture;
    EditText etUserName, etPassword;
    TextView tvEmail, chessScore;
    ProgressDialog progressDialog;
    Button saveBtn;
    DatabaseReference userReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        profilePicture = view.findViewById(R.id.profilePicture);
        profilePicture.setOnClickListener(this);

        profilePicture.setImageBitmap(UserData.bitmap == null ? BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_picture) : UserData.bitmap);
        
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Save Picture");
        etUserName = view.findViewById(R.id.etUserName);
        etPassword = view.findViewById(R.id.etPassword);
        tvEmail = view.findViewById(R.id.EmailTextView);
        etUserName.setText(UserData.username);
        etPassword.setText(UserData.password);
        tvEmail.setText(UserData.email);
        chessScore = view.findViewById(R.id.ChessScore);
        chessScore.setText("Score: " + UserData.ChessScore);
        takePictureFromCamera();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());

        saveBtn = view.findViewById(R.id.saveChanges);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserData();
            }
        });

        return view;

    }

    public void changeUserData(){
        if (!etUserName.getText().toString().equals(UserData.username)) {
            userReference.child("username").setValue(etUserName.getText().toString());
        }
        if (!etPassword.getText().toString().equals(UserData.password)) {
            userReference.child("password").setValue(etUserName.getText().toString());
            FirebaseAuth.getInstance().getCurrentUser().updatePassword(etPassword.getText().toString());
        }
        Toast.makeText(getContext(), "New data has been saved", Toast.LENGTH_LONG).show();
    }

    public void takePictureFromCamera(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null){
                    Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                    profilePicture.setImageBitmap(bitmap);
                    UserData.bitmap = bitmap;
                    UploadPictureToStorage();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == profilePicture){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activityResultLauncher.launch(intent);
        }
    }

    public void UploadPictureToStorage(){
        progressDialog.show();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap temp = UserData.bitmap;
        temp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        StorageReference imageReference = storageReference.child("Images/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        Task<Uri> uriTask = imageReference.putBytes(data).continueWithTask(task ->{
            if (task.isSuccessful()){
                Toast.makeText(getContext(), "New picture has been saved", Toast.LENGTH_LONG).show();
            }
            else if (!task.isSuccessful()){
                throw task.getException();
            }

            return imageReference.getDownloadUrl();
        });
        progressDialog.dismiss();
    }
}