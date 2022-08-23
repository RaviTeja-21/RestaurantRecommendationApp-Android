package com.example.foodworld;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddRestaurants extends AppCompatActivity {

    EditText restaurantname, restaurnataddress;
    TextView cuisinename;
    ImageView restaurnatimage;
    Button btnaddrestaurants;
   // Spinner cuisine_spinner;
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

    boolean[] selectedDay;
    ArrayList<Integer> daylist = new ArrayList<>();
    String[] finalMStringArray1;
   // String[] langArray;

    boolean[] selectedLanguage;
    ArrayList<Integer> langList = new ArrayList<>();
    String[] langArray = {"Java", "C++", "Kotlin", "C", "Python", "Javascript"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurants);

        restaurantname = findViewById(R.id.restaurantname);
        restaurnataddress = findViewById(R.id.restaurantaddress);
        restaurnatimage = findViewById(R.id.addrestaurantimage);
        btnaddrestaurants = findViewById(R.id.btnaddrestaurants);
      //  cuisine_spinner = findViewById(R.id.cuisine_spinner);
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

        ArrayList<String> types = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, types);
       /* dbroot.collection("cuisines").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d:list){
                    cuisineDetails obj = d.toObject(cuisineDetails.class);
                    types.add(obj.getName());
                  //  Toast.makeText(AddRestaurants.this, obj.getName(), Toast.LENGTH_SHORT).show();
                }
                cuisine_spinner.setAdapter(arrayAdapter);
                String[] mStringArray = new String[types.size()];
                mStringArray = types.toArray(mStringArray);

                for(int i = 0; i < mStringArray.length ; i++){
                    Log.d("string is",(String)mStringArray[i]);
                 //   Toast.makeText(AddRestaurants.this, mStringArray[i], Toast.LENGTH_SHORT).show();

                }
            }
        });*/

       // String[] finalMStringArray = mStringArray;



        ArrayList<String>  mStringList= new ArrayList<String>();
        dbroot.collection("cuisines").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d:list){
                    cuisineDetails obj = d.toObject(cuisineDetails.class);
                    mStringList.add(obj.getName());
                    //  Toast.makeText(AddRestaurants.this, obj.getName(), Toast.LENGTH_SHORT).show();
                }
                String[] mStringArray = new String[mStringList.size()];
                mStringArray = mStringList.toArray(mStringArray);
                finalMStringArray1 = mStringArray;

                selectedDay = new boolean[finalMStringArray1.length];
            }
        });

        selectedLanguage = new boolean[langArray.length];
      /*  AlertDialog.Builder builder = new AlertDialog.Builder(AddRestaurants.this);

        // set title
        builder.setTitle("Select Language");

        // set dialog non cancelable
        builder.setCancelable(false);

        builder.setMultiChoiceItems(finalMStringArray1, selectedDay, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position  in lang list
                    langList.add(i);
                    // Sort array list
                    Collections.sort(langList);
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    langList.remove(Integer.valueOf(i));
                }
            }
        });*/



        cuisinename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddRestaurants.this);

                // set title
                builder.setTitle("Select Language");

                // set dialog non cancelable
                builder.setCancelable(false);
                builder.setMultiChoiceItems(finalMStringArray1, selectedDay, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            langList.add(i);
                            // Sort array list
                            Collections.sort(langList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(finalMStringArray1[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        cuisinename.setText(stringBuilder.toString());
                        // set text on textView
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedLanguage.length; j++) {
                            // remove all selection
                            selectedLanguage[j] = false;
                            // clear language list
                            langList.clear();
                            // clear text view value
                            cuisinename.setText("");
                        }
                    }
                });
                builder.show();
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
            items.put("name", etrestaurantname);
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