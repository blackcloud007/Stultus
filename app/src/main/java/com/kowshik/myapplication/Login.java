package com.kowshik.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mloginbtn;
    TextView mcreatebtn;
    FirebaseAuth fAuth;
    ProgressBar progessBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail=findViewById(R.id.emailid);

        mloginbtn=findViewById(R.id.loginbtn);
        mcreatebtn=findViewById(R.id.createText);
        mPassword=findViewById(R.id.pwd);

        fAuth=FirebaseAuth.getInstance();
        progessBar=findViewById(R.id.progressBar2);

        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                String pwd=mPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required ");
                    return;
                }
                if(TextUtils.isEmpty(pwd)){
                    mPassword.setError("Password required");
                    return;
                }
                if(pwd.length()<6){
                    mPassword.setError("Password length should be at least 6 ");
                    return;
                }
                progessBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(Login.this,"Error :( "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progessBar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });
        mcreatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

    }
}
