package com.example.foodworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Welcomescreen extends AppCompatActivity {
    TextView  privacypolicy,googletext;
    Button welcomeregister;
    Button welcomelogin;
    ImageView googlelogo;
    String text = "<font color=#000>By logging in or registering, you have agreed to the </font> <font color=#E90000>Terms and Conditions and Privacy Policy.</font>";

    com.google.android.gms.common.SignInButton  googlelogin;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomescreen);

        privacypolicy = findViewById(R.id.privacypolicy);
        welcomelogin = findViewById(R.id.welcomelogin);
        welcomeregister = findViewById(R.id.welcomeregister);
        googlelogin = findViewById(R.id.googlesigniwelcome);
       // googletext = findViewById(R.id.googletext);

        privacypolicy.setText(Html.fromHtml(text));

        firebaseAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        welcomelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcomescreen.this,LoginActivity.class));
            }
        });

        welcomeregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcomescreen.this,RegisterActivity.class));
            }
        });

        googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googlesignIn();
            }
        });

      /*  googletext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googlesignIn();
            }
        });*/
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
                                    String userId = firebaseAuth.getCurrentUser().getUid();
                                    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                                    DocumentReference docIdRef = rootRef.collection("Users").document(userId);
                                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                   // Toast.makeText(Welcomescreen.this, "User is exisits", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Welcomescreen.this, UserHome.class);
                                                    startActivity(intent);
                                                }
                                                else{

                                                    HashMap<String, Object> userdataMap = new HashMap<>();
                                                    userdataMap.put("email",  firebaseAuth.getCurrentUser().getEmail());
                                                    userdataMap.put("phone", firebaseAuth.getCurrentUser().getPhoneNumber());
                                                    userdataMap.put("username", firebaseAuth.getCurrentUser().getDisplayName());
                                                    userdataMap.put("id", firebaseAuth.getCurrentUser().getUid());

                                                    rootRef.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).set(userdataMap);

                                                  //  Toast.makeText(Welcomescreen.this, "User is not exisits", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Welcomescreen.this, UserHome.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });



                                } else {
                                    Toast.makeText(Welcomescreen.this, "User not Loggedin", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                /*finish();
                Intent i = new Intent(Welcomescreen.this,UserHome.class);
                startActivity(i);*/
            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}