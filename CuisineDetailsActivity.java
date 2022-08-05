package com.example.foodworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CuisineDetailsActivity extends AppCompatActivity {

    String cuisineId,restaurantId;
    FirebaseFirestore firebaseFirestore;
    ArrayList<restaurnatDetails> restaurnatDetails;
    TextView cusname;
    ImageView cusimage;
    restaurantAdapter restaurantAdapter;
    RecyclerView showallcuisineresturant;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_details);

        cusname = findViewById(R.id.cusname);
        cusimage = findViewById(R.id.cusimage);

        cuisineId = getIntent().getStringExtra("cusineID");
        restaurantId = getIntent().getStringExtra("restaurnatID");

        firebaseFirestore = FirebaseFirestore.getInstance();

        showallcuisineresturant = (RecyclerView) findViewById(R.id.showallcuisineresturant);
        showallcuisineresturant.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));


        restaurnatDetails = new ArrayList<>();
        restaurantAdapter = new restaurantAdapter(restaurnatDetails,getApplicationContext());
        showallcuisineresturant.setAdapter(restaurantAdapter);

        DocumentReference documentcuisine = firebaseFirestore.collection("cuisines").document(cuisineId);

        documentcuisine.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                cusname.setText("Selected cuisine : "+documentSnapshot.getString("name"));
                Picasso.get().load(documentSnapshot.getString("imageURL")).into(cusimage);

            }
        });
        firebaseFirestore.collection("restaurant").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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