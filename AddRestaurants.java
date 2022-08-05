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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddRestaurants extends AppCompatActivity {

    EditText restaurantname, restaurnataddress,cuisinename;
    ImageView restaurnatimage;
    Button btnaddrestaurants;
  //  Spinner selectcuisine;
    ValueEventListener listener;
    ArrayList<String> list;
    ArrayAdapter<String>   cusineadapter;

    String userid;
    public Uri imageurl;
    public String restaurantimageurl;

    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore dbroot;
    ActivityResultLauncher<Intent> mgcontet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurants);

        restaurantname = findViewById(R.id.restaurantname);
        restaurnataddress = findViewById(R.id.restaurantaddress);
        restaurnatimage = findViewById(R.id.addrestaurantimage);
        btnaddrestaurants = findViewById(R.id.btnaddrestaurants);
     //   selectcuisine = findViewById(R.id.selectcuisine);
        cuisinename = findViewById(R.id.cuisinenames);

        list = new ArrayList<String>();
        cusineadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        //selectcuisine.setAdapter(cusineadapter);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        dbroot = FirebaseFirestore.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("cuisines");
        //fetchcuisnedata();

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        restaurnatimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        btnaddrestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addrestaurants();
            }
        });

    }

    private void addrestaurants() {

        String etrestaurantname = restaurantname.getText().toString();
        String etrestaurantaddress = restaurnataddress.getText().toString();
        String etcuisines = cuisinename.getText().toString();
        //  String ettheaterimageurl = theaterimageurl;

        if(TextUtils.isEmpty(etrestaurantname)){
            restaurantname.setError("Restaurnat name can not be empty");
            restaurantname.requestFocus();
        }else if(TextUtils.isEmpty(etrestaurantaddress)){
            restaurnataddress.setError("Restaurnat address can not be empty");
            restaurnataddress.requestFocus();

        }else {

            Map<String, String> items = new HashMap<>();
            items.put("restaurnatname", etrestaurantname);
            items.put("address", etrestaurantaddress);
            items.put("cuisinenames", etcuisines);
            items.put("restaurnatimageUrl",restaurantimageurl );
            items.put("adminid", userid);

            dbroot.collection("restaurant").add(items);
            // Toast.makeText(AddTheaterActivity.this, "Url" + theaterimageurl, Toast.LENGTH_SHORT).show();
            Toast.makeText(AddRestaurants.this, "Add restaurant SuccessFully", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //  if(requestCode==1 && requestCode==RESULT_OK && data!=null && data.getData()!=null){
        imageurl = data.getData();
        restaurnatimage.setImageURI(imageurl);
        final String randomkey = UUID.randomUUID().toString();
        final StorageReference sr = storageReference.child("restaurant/"+randomkey);
        sr.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(AddRestaurants.this, "uri: "+uri, Toast.LENGTH_SHORT).show();
                        restaurantimageurl = uri.toString();
                    }
                });
            }

        });
    }
}