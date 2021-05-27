package com.example.login2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



public class Login extends AppCompatActivity {
    CallbackManager mCallbackManager;

    Button callSignUp ,go;
    TextInputLayout username , password ;
    TextView sloganText,logoText;
    ImageView logo;
    LoginButton loginButton ;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Intent Iintent = new Intent(getApplicationContext() , Project.class);
            startActivity(Iintent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //initialize facebook sdk
        FacebookSdk.sdkInitialize(Login.this);

        //initialize database
        mAuth = FirebaseAuth.getInstance();

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });



        callSignUp = findViewById(R.id.signup_screen);
        logo = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        sloganText = findViewById(R.id.slogan_name);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        go = findViewById(R.id.go);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        createGoogleRequest();


        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(Login.this,SignUp.class);


                Pair[]  pairs= new Pair[7];
                pairs[0]=new Pair<View,String>(logo,"logo_image");
                pairs[1]=new Pair<View,String>(logoText,"logo_text");
                pairs[2]=new Pair<View,String>(sloganText,"logo_desc");
                pairs[3]=new Pair<View,String>(username,"user_tran");
                pairs[4]=new Pair<View,String>(password,"pass_tran");
                pairs[5]=new Pair<View,String>(go,"button_tran");
                pairs[6]=new Pair<View,String>(callSignUp,"login_signup_tran");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                    startActivity(intentSignUp,options.toBundle());
                }
            }
        });


    }




    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            Intent intentFa = new Intent(Login.this,Project.class);
            startActivity(intentFa);
            
        }else {
            Toast.makeText(this, "please sign in to continue", Toast.LENGTH_SHORT).show();
        }
                
    }


    private boolean validateUsername(){
        String val = username .getEditText().getText().toString();

        if (val.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        } else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }

    }
    private boolean validatePassword(){
        String val = password.getEditText().getText().toString();

        if (val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        } else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }
    public void loginUser(View view) {
        if (!validateUsername() | !validatePassword()){
           return;
        }else {
            isUser();
        }

    }

    private void isUser() {
        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        String userEnteredPassword = password.getEditText().getText().toString().trim();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("user");

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)){
                        password.setError(null);
                        password.setErrorEnabled(false);

                        Intent intentt=new Intent (Login.this,Project.class);
                        startActivity(intentt);
                    }else {
                        password.setError("wrong password");
                        password.requestFocus();
                    }
                }else {
                    username.setError("No such User exist");
                    password.requestFocus();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createGoogleRequest() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intentGoogle = new Intent(getApplicationContext() , Project.class);
                            startActivity(intentGoogle);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "sorry auth failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}