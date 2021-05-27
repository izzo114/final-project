package com.example.login2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class VerifyPhoneNo extends AppCompatActivity {

    String verificationCodeBySystem;
    TextInputLayout phonenoEnteredByUser;
    Button verifyBtn;
    ProgressBar progressBar;
     FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);

        phonenoEnteredByUser = findViewById(R.id.verfication_by_user);
        verifyBtn = findViewById(R.id.verify_btn);
        progressBar = findViewById(R.id.progress_bar);

        String phonenom = getIntent().getStringExtra("phonenom");

        sendVerificationCodeToUser(phonenom);
    }

    private void sendVerificationCodeToUser(String phonenom) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+20"+phonenom)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity) TaskExecutors.MAIN_THREAD)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
          String code = phoneAuthCredential.getSmsCode();
          if (code!=null){
              progressBar.setVisibility(View.VISIBLE);
              verifyCode(code);
          }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhoneNo.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void verifyCode (String CodeByUser){
          PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCodeBySystem,CodeByUser);
          signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneNo.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent inttent=new Intent(getApplicationContext(),Project.class);
                            inttent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(inttent);
                        }
                        else {
                            Toast.makeText(VerifyPhoneNo.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}