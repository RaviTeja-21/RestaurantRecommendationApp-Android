package com.example.foodworld;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    EditText etloginemail;
    TextInputLayout etloginpassword;
    Button login;
    TextView etregister, forgotpassword;
    FirebaseAuth firebaseAuth;
 //   ImageView googlelogin;
    com.google.android.gms.common.SignInButton  googlelogin;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etloginemail = findViewById(R.id.loginemail);
        etloginpassword = findViewById(R.id.loginpassword);
        login = findViewById(R.id.login);
        etregister = findViewById(R.id.txtregister);
        forgotpassword = findViewById(R.id.forgotpassword);
        googlelogin = findViewById(R.id.googlesignInlogin);



        firebaseAuth = FirebaseAuth.getInstance();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        login.setOnClickListener(view -> {
            Loginuser();
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

        etregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        /*googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();*/

        googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googlesignIn();
            }
        });


    }


    private void googlesignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

                /*SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = googleCredential.getGoogleIdToken();*/

                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                //  AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(LoginActivity.this, "User Loggedin", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    Intent intent = new Intent(LoginActivity.this, UserHome.class);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.


                                }
                            }
                        });

                finish();
                Intent i = new Intent(LoginActivity.this,UserHome.class);
                startActivity(i);
            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void Loginuser(){
        String email = etloginemail.getText().toString();
        String password = etloginpassword.getEditText().getText().toString();

        if(TextUtils.isEmpty(email)){
            etloginemail.setError("Email can not be empty");
            etloginemail.requestFocus();
        }
        else if (TextUtils.isEmpty(password)){
            etloginpassword.setError("Password can not be empty");
            etloginpassword.requestFocus();
        }

        else{
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "User Loggedin successfully", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String useremail = user.getEmail().toString();
                        if(useremail.equals("testaccount@gmail.com")){
                            startActivity(new Intent(LoginActivity.this,Home.class));
                        }
                        else{
                            startActivity(new Intent(LoginActivity.this,UserHome.class));
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Login Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}