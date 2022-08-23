package com.example.foodworld;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText etname, etemail, etphone, etaddress, etcpassword, etpassword;
    Button register;
    TextView txtlogin;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    String passwordpattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

    FirebaseAuth firebaseAuth;
    FirebaseFirestore dbroot;
    DatabaseReference dbref;

    com.google.android.gms.common.SignInButton  googlelogin;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etname = findViewById(R.id.name);
        etemail = findViewById(R.id.email);
        etphone = findViewById(R.id.phone);
        etaddress = findViewById(R.id.address);
        etcpassword = findViewById(R.id.cpassword);
        etpassword = findViewById(R.id.password);
        register = findViewById(R.id.register);
        txtlogin = findViewById(R.id.txtlogin);

        googlelogin = findViewById(R.id.googlesigninregister);

        dbref = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        dbroot = FirebaseFirestore.getInstance();

        register.setOnClickListener(view -> {
            createuser();
        });

        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
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

                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Sign in success, update UI with the signed-in user's information
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
                                                 //   Toast.makeText(RegisterActivity.this, "User is exisits", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegisterActivity.this, UserHome.class);
                                                    startActivity(intent);
                                                }
                                                else{

                                                    HashMap<String, Object> userdataMap = new HashMap<>();
                                                    userdataMap.put("email",  firebaseAuth.getCurrentUser().getEmail());
                                                    userdataMap.put("phone", firebaseAuth.getCurrentUser().getPhoneNumber());
                                                    userdataMap.put("username", firebaseAuth.getCurrentUser().getDisplayName());
                                                    userdataMap.put("id", firebaseAuth.getCurrentUser().getUid());

                                                    rootRef.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).set(userdataMap);

                                                  //  Toast.makeText(RegisterActivity.this, "User is not exisits", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegisterActivity.this, UserHome.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });



                                } else {
                                    Toast.makeText(RegisterActivity.this, "User not Loggedin", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


               /* Intent i = new Intent(RegisterActivity.this,UserHome.class);
                startActivity(i);*/
            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void createuser(){
        String name = etname.getText().toString();
        String email = etemail.getText().toString();
        String phone = etphone.getText().toString();
        String address = etaddress.getText().toString();
        String cpassword = etcpassword.getText().toString();
        String password = etpassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            etname.setError("Name can not be empty");
            etname.requestFocus();
        }
        else if(TextUtils.isEmpty(email)){
            etemail.setError("Email can not be empty");
            etemail.requestFocus();
        }
        else if(!email.trim().matches(emailPattern)){
            etemail.setError("Email format is not proper");
            etemail.requestFocus();
        }
        else if(TextUtils.isEmpty(phone)){
            etphone.setError("Phone Number can not be empty");
            etphone.requestFocus();
        }
        else if(!phone.matches(pattern)){
            etphone.setError("Phone Number is not valid");
            etphone.requestFocus();
        }
        else if(TextUtils.isEmpty(address)){
            etaddress.setError("Address can not be empty");
            etaddress.requestFocus();
        }
        else if(TextUtils.isEmpty(cpassword)){
            etcpassword.setError("Create Password can not be empty");
            etcpassword.requestFocus();
        }
        else if(!cpassword.matches(passwordpattern)){
            etcpassword.setError("Password format is not proper");
            etcpassword.requestFocus();
        }
        else if (TextUtils.isEmpty(password)){
            etpassword.setError("Password can not be empty");
            etpassword.requestFocus();
        }
        else if (!password.equals(cpassword)){
            etpassword.setError("Password is not matched");
            etpassword.requestFocus();
        }else{
            Map<String,String> items = new HashMap<>();
            String imgurl = "";
            items.put("username",name);
            items.put("email",email);
            items.put("phone",phone);
            items.put("address",address);
            items.put("password",password);
            items.put("imgurl",imgurl);


            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        items.put("id",firebaseAuth.getCurrentUser().getUid());
                        dbroot.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).set(items);
                        Toast.makeText(RegisterActivity.this, "User registered sucessfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Registration Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}