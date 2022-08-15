package com.example.foodworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodworld.adapter.PlacesListAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class CuisineDetailsActivity extends AppCompatActivity {

    String cuisineId,restaurantId;
    FirebaseFirestore firebaseFirestore;
    ArrayList<restaurnatDetails> restaurnatDetails;
    TextView cusname;
    ImageView cusimage;
    restaurantAdapter restaurantAdapter;
    RecyclerView showallcuisineresturant;
    private ListView listViewPlaces;
    private LocationManager locationManager;

    String _Location;

    FusedLocationProviderClient client;

    public String cuisinename;
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

        client = LocationServices.getFusedLocationProviderClient(CuisineDetailsActivity.this);

        if (ActivityCompat.checkSelfPermission(CuisineDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getcurrentlocation();
        }else{
            ActivityCompat.requestPermissions(CuisineDetailsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        initView();

        DocumentReference documentcuisine = firebaseFirestore.collection("cuisines").document(cuisineId);

        documentcuisine.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                cuisinename = documentSnapshot.getString("name");
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
    private void initView() {

        if (ContextCompat.checkSelfPermission(CuisineDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getcurrentlocation();
        } else {
            if (ContextCompat.checkSelfPermission(CuisineDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CuisineDetailsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(CuisineDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CuisineDetailsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }


        listViewPlaces = findViewById(R.id.cuisinebaserestaurant);

    }
    private void getcurrentlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                    List<Address> listAddresses = null;
                    try {
                        listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(null!=listAddresses&&listAddresses.size()>0){
                        _Location = listAddresses.get(0).getAddressLine(0);
                    }

                    Toast.makeText(CuisineDetailsActivity.this, "cusinename: "+getIntent().getStringExtra("cusineName"), Toast.LENGTH_SHORT).show();
                    String keyword = getIntent().getStringExtra("cusineName");
                    String key = getText(R.string.googlemapapi).toString();
                    String currentLocation = location.getLatitude() + "," + location.getLongitude();
                    int radius = 500;
                    String type = "restaurant";
                    GoogleMapAPI googleMapAPI = APIClient.getClient().create(GoogleMapAPI.class);
                    googleMapAPI.getNearBy(currentLocation, radius, type, keyword, key).enqueue(new Callback<PlacesResults>() {
                        @Override
                        public void onResponse(retrofit2.Call<PlacesResults> call, retrofit2.Response<PlacesResults> response) {
                            if (response.isSuccessful()) {
                                List<Result> results = response.body().getResults();
                                PlacesListAdapter placesListAdapter = new PlacesListAdapter(getApplicationContext(), results);
                                listViewPlaces.setAdapter(placesListAdapter);
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<PlacesResults> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getcurrentlocation();
            }
        }else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}