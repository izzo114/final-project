package com.example.login2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    TextInputLayout regName,regUsername,regEmail,regPhoneNo,regPassword;
    Button regBtn;
    TextView CallLogin;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth auth;
    RadioButton genderradioButton;
    RadioGroup radioGroup ;
    SharedPreferences sharedPreferences;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
//        admin = findViewById(R.id.admin);
//        client = findViewById(R.id.client);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        sharedPreferences=getSharedPreferences("myPref",MODE_PRIVATE);




        //*************************************************************************************
        regName=findViewById(R.id.fullname);
        regUsername=findViewById(R.id.username);
        regEmail=findViewById(R.id.email);
        regPhoneNo=findViewById(R.id.phoneno);
        regPassword=findViewById(R.id.pass);
        regBtn =findViewById(R.id.signUp) ;
        CallLogin = findViewById(R.id.login_screen);



       CallLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CallLoginIntent= new Intent(SignUp.this,Login.class);
                startActivity(CallLoginIntent);
            }
        });


    }

    private Boolean validateName(){
        String val = regName.getEditText().getText().toString();

        if (val.isEmpty()){
            regName.setError("Field cannot be empty");
            return false;
        }else{
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateUserName(){
        String val = regUsername .getEditText().getText().toString();
        String noWhiteSpace ="\\A\\W{4,20}\\z";
        if (val.isEmpty()){
            regUsername.setError("Field cannot be empty");
            return false;
        }else if (val.length() >=15){
            regUsername.setError("Username too long");
            return false;
        }else if (val.matches(noWhiteSpace)){
            regUsername.setError("white spaces are not allowed");
            return false;
        }
        else{
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateEmail(){
        String val = regEmail.getEditText().getText().toString();
        String emailPattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()){
            regEmail.setError("Field cannot be empty");
            return false;
        }else if (!val.matches(emailPattern)){
            regEmail.setError("Invalid email address");
            return false;
        }
        else{
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatePhoneNo(){
        String val = regPhoneNo.getEditText().getText().toString();

        if (val.isEmpty()){
            regPhoneNo.setError("Field cannot be empty");
            return false;
        }else{
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" + "(?=.*[a-zA-Z])" + /*"(?=.*[@#$%^&+=])" + "\\A\\W{4,20}\\z" +*/ ".{8,}" +"$";
        if (val.isEmpty()){
            regPassword.setError("Field cannot be empty");
            return false;
        }else if (!val.matches(passwordVal)){
           regPassword.setError("Password is too weak");
            return false;
        }
        else{
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }

    }


    public void registerUser(View view) {

        if(!validateName() | !validateUserName() | !validateEmail() | !validatePhoneNo() | !validatePassword()){
            return;
        }

        rootNode = FirebaseDatabase.getInstance();
        reference=rootNode.getReference("user");



        String name = regName.getEditText().getText().toString();
        String username = regUsername.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phoneno = regPhoneNo.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

            int selectedId = radioGroup.getCheckedRadioButtonId();
            genderradioButton = (RadioButton) findViewById(selectedId);
            if(selectedId==-1){
                Toast.makeText(SignUp.this,"Nothing selected", Toast.LENGTH_SHORT).show();
            }
            else{
//                Toast.makeText(SignUp.this,genderradioButton.getText(), Toast.LENGTH_SHORT).show();
                UserHelperClass helperClass = new UserHelperClass(name,username,email,phoneno,password);
                reference.child(genderradioButton.getText().toString()).child(username).setValue(helperClass);

            }




        Toast.makeText(this, "Your Account has been created successfully", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor  editor=sharedPreferences.edit();
        editor.putString("naame",name);
        editor.putString("useername",username);
        editor.putString("phoone",phoneno);
        editor.putString("emaail",email);
        editor.putString("paassword",password);
        editor.putString("userType",genderradioButton.getText().toString());
        editor.apply();

        Intent intennt = new Intent(SignUp.this,Project.class);

        startActivity(intennt);
        finish();

    }
}