package com.example.foodworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserHome extends AppCompatActivity {

    RecyclerView showcuisines,showallresturant;
    ArrayList<cuisineDetails> cuisineDetails;
    ArrayList<restaurnatDetails> restaurnatDetails;
    FirebaseFirestore db;
    CuisineAdapter cuisineAdapter;
    restaurantAdapter restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        showcuisines = (RecyclerView) findViewById(R.id.showcuisines);
        showcuisines.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        showallresturant = (RecyclerView) findViewById(R.id.showallresturant);
        showallresturant.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));


        cuisineDetails = new ArrayList<>();
        cuisineAdapter = new CuisineAdapter(cuisineDetails,getApplicationContext());
        showcuisines.setAdapter(cuisineAdapter);

        restaurnatDetails = new ArrayList<>();
        restaurantAdapter = new restaurantAdapter(restaurnatDetails,getApplicationContext());
        showallresturant.setAdapter(restaurantAdapter);


        db = FirebaseFirestore.getInstance();
        db.collection("cuisines").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d:list){
                    String cusineID = d.getId();
                    cuisineDetails obj = d.toObject(cuisineDetails.class);
                    obj.setCusineID(cusineID);
                    cuisineDetails.add(obj);
                }
                cuisineAdapter.notifyDataSetChanged();
            }
        });
        db.collection("restaurant").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d:list){
                    String restaurnatID = d.getId();
                    restaurnatDetails obj = d.toObject(restaurnatDetails.class);
                    obj.setRestaurnatID(restaurnatID);
                    restaurnatDetails.add(obj);
                }
                restaurantAdapter.notifyDataSetChanged();
            }
        });



    }
}