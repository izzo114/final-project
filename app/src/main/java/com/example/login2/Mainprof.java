package com.example.login2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Mainprof extends AppCompatActivity implements View.OnClickListener {

    // private DatabaseReference referencee;


    TextInputLayout fullName, email, phoneno, password;
    TextView fullNameLabel, usernameLabel;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    SharedPreferences sharedPreferences;
    Button signOut;
    public CardView card1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainprof);
        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        card1 = (CardView) findViewById(R.id.c1);
        card1.setOnClickListener(this);


        //Hooks
        fullName = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.email_profile);
        phoneno = findViewById(R.id.phone_no_profile);
        password = findViewById(R.id.password_profile);
        fullNameLabel = findViewById(R.id.fullname);
        usernameLabel = findViewById(R.id.usernameProf);
        signOut = findViewById(R.id.btn_signout);


        String user_username = sharedPreferences.getString("useername", null);
        String user_name = sharedPreferences.getString("naame", null);
        String user_email = sharedPreferences.getString("emaail", null);
        String user_phoneno = sharedPreferences.getString("phoone", null);
        String user_paassword = sharedPreferences.getString("paassword", null);


        fullNameLabel.setText(user_name);
        usernameLabel.setText(user_username);
        fullName.getEditText().setText(user_name);
        email.getEditText().setText(user_email);
        phoneno.getEditText().setText(user_phoneno);
        password.getEditText().setText(user_paassword);


    }


    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent LoginActivity = new Intent(this, Login.class);
        startActivity(LoginActivity);
        finish();
    }


    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.c1:
                i = new Intent(this, booking.class);
                startActivity(i);
                break;

        }
    }
}