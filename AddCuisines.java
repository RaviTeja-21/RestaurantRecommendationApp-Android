package com.example.foodworld;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddCuisines extends AppCompatActivity {

    EditText cuisinesname;
    ImageView addcuisineimage;
    Button btnaddcusines;
    String userid;
    public Uri imageurl;
    public String cuisineimageurl;

    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore dbroot;
    ActivityResultLauncher<Intent> mgcontet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cuisines);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        dbroot = FirebaseFirestore.getInstance();

        cuisinesname = findViewById(R.id.cuisinesname);
        addcuisineimage = findViewById(R.id.addcuisinesimage);
        btnaddcusines = findViewById(R.id.btnaddcusines);

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        addcuisineimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        btnaddcusines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcuisine();
            }
        });



    }

    private void addcuisine() {

        String etcuisinesname = cuisinesname.getText().toString();
        //  String ettheaterimageurl = theaterimageurl;

        if(TextUtils.isEmpty(etcuisinesname)){
            cuisinesname.setError("cusinename can not be empty");
            cuisinesname.requestFocus();
        }else {

            Map<String, String> items = new HashMap<>();
            items.put("name", etcuisinesname);
            items.put("imageUrl", cuisineimageurl);
            items.put("adminid", userid);

            dbroot.collection("cuisines").add(items);
            // Toast.makeText(AddTheaterActivity.this, "Url" + theaterimageurl, Toast.LENGTH_SHORT).show();
            Toast.makeText(AddCuisines.this, "Add cuisines SuccessFully", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //  if(requestCode==1 && requestCode==RESULT_OK && data!=null && data.getData()!=null){
        imageurl = data.getData();
        addcuisineimage.setImageURI(imageurl);
        final String randomkey = UUID.randomUUID().toString();
        final StorageReference sr = storageReference.child("cuisine/"+randomkey);
        sr.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(AddCuisines.this, "uri: "+ uri, Toast.LENGTH_SHORT).show();
                        cuisineimageurl = uri.toString();
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddCuisines.this, "Failead"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}