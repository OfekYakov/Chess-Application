package com.example.chessproject.AppActivities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chessproject.R;
import com.example.chessproject.UserUtils.User;
import com.example.chessproject.UserUtils.UserData;
import com.example.chessproject.UserUtils.VerificationUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etPassword, etConfirmPassword,etUsername;
    TextView tvErrorDisplay;
    Button btnSignUp, btnTakePic;
    ImageView ShowProfilePicture;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        btnTakePic = findViewById(R.id.btnTakePic);
        btnTakePic.setOnClickListener(this);
        etEmail = findViewById(R.id.edittextEmail);
        etPassword = findViewById(R.id.etPass);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etUsername = findViewById(R.id.etUserName);
        ShowProfilePicture = findViewById(R.id.ivProfilePicture);
        tvErrorDisplay = findViewById(R.id.tvErrorDisplay);tvErrorDisplay.setText("");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Signing up");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        takePictureFromCamera();

    }

    @Override
    public void onClick(View v) {
        if (v == btnSignUp){
            if (checkFields())
                SignUpToApp(etEmail.getText().toString(), etPassword.getText().toString());
        }
        else if (v == btnTakePic){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activityResultLauncher.launch(intent);
        }
    }

    public boolean checkFields(){
        if (etPassword.getText().toString().equals("") || etUsername.getText().toString().equals("") || etConfirmPassword.getText().toString().equals("") || etEmail.getText().toString().equals("")) {
            tvErrorDisplay.setText("please fill all the fields");
            return false;
        }
        String errors = VerificationUtils.passwordVerification(etPassword.getText().toString()) + "\n" + VerificationUtils.UserNameVerification(etUsername.getText().toString());
        if (!(etPassword.getText().toString().equals(etConfirmPassword.getText().toString())))
            errors += "\nConfirm password failed";
        if (errors.equals("\n"))
            return true;
        tvErrorDisplay.setText(errors);
        return false;
    }

    public void takePictureFromCamera() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null){
                    UserData.bitmap = (Bitmap) result.getData().getExtras().get("data");
                    ShowProfilePicture.setImageBitmap(UserData.bitmap);
                }
            }
        });
    }

    public void UploadPictureToStorage(){
        if (UserData.bitmap == null) {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            progressDialog.dismiss();
            return;
        }
        storageReference = FirebaseStorage.getInstance().getReference();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap temp = UserData.bitmap;
        temp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        StorageReference imageReference = storageReference.child("Images/" + firebaseAuth.getUid());
        Task<Uri> uriTask = imageReference.putBytes(data).continueWithTask(task ->{
            if (task.isSuccessful()){
                progressDialog.dismiss();
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if (!task.isSuccessful()){
                progressDialog.dismiss();
                throw task.getException();
            }

            return imageReference.getDownloadUrl();
        });
    }

    public void SignUpToApp(String email, String password){
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    addUsersDetailsToDatabase();
                }
                else if (!task.isSuccessful()) {
                    tvErrorDisplay.setText("Email is already in use, Try another one");
                }
            }
        });
    }

    public void addUsersDetailsToDatabase(){
        User user = new User(etPassword.getText().toString(), etUsername.getText().toString(), 0);
        firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(user);
        UploadPictureToStorage();
    }
}