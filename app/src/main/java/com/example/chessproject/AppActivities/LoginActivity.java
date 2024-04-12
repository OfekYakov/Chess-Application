package com.example.chessproject.AppActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chessproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    Button btnLogin;
    EditText etPass, etEmail;
    ProgressDialog progressDialog;
    TextView errorDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(this);
        etPass = findViewById(R.id.etPass);
        etEmail = findViewById(R.id.edittextEmail);
        progressDialog = new ProgressDialog(this);
        errorDisplay = findViewById(R.id.tvErrorDisplay);
        errorDisplay.setText("");
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin){
            // both email and password are empty
            if (!etEmail.getText().toString().isEmpty() && !etPass.getText().toString().isEmpty()){
                LoginToApp(etEmail.getText().toString(), etPass.getText().toString());
            }
            // show error message
            else{
                errorDisplay.setText("Email or password cannot be empty");
            }
        }
    }

    // in case user want to register
    public void StartSignUpActivity(View view){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void LoginToApp(String email, String password){
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        }
                        else if (!task.isSuccessful()){
                            etPass.setText("");
                            etEmail.setText("");
                            errorDisplay.setText("Email or password are not correct");
                            progressDialog.dismiss();
                        }
                    }
                });}

}