package com.kowshik.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,mPhone,mEmail,mPassword;
    Button mregisterbtn;//button
    TextView mLogInbtn;
    FirebaseAuth fAuth;//p
    ProgressBar progessBar;

    FirebaseFirestore fstore;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmail=findViewById(R.id.emailid);
        mFullName=findViewById(R.id.fullname);
        mLogInbtn=findViewById(R.id.loginText);
        mregisterbtn=findViewById(R.id.registerbtn);
        mPhone=findViewById(R.id.ph);
        mPassword=findViewById(R.id.pwd);

        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        progessBar=findViewById(R.id.progressBar);



        if(fAuth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();

        }

        mregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=mEmail.getText().toString().trim();
                final String pwd=mPassword.getText().toString().trim();

                final String fullname=mFullName.getText().toString();
                final String phoneno=mPhone.getText().toString();
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

                fAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"User created",Toast.LENGTH_SHORT).show();
                            UserId=fAuth.getCurrentUser().getUid();

                            DocumentReference documentReference=fstore.collection("Users").document(UserId);
                            Map<String,Object> user =new HashMap<>();
                            user.put("Email",email);
                            user.put("password",pwd);
                            user.put("full name",fullname);
                            user.put("Phone NO ",phoneno);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"onSuccess: User profile is created for"+UserId);

                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(Register.this,"Error :( "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progessBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });

        mLogInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }
}
