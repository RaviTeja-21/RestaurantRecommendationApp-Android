package com.example.foodworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodworld.adapter.PlacesListAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;


public class UserHome extends AppCompatActivity {

    RecyclerView showcuisines,showallresturant;
    TextView getcurrentlocation;
    ArrayList<cuisineDetails> cuisineDetails;
    ArrayList<restaurnatDetails> restaurnatDetails;
    FirebaseFirestore db;
    CuisineAdapter cuisineAdapter;
    restaurantAdapter restaurantAdapter;
    String lat,lng,address;
    private ListView listViewPlaces;
    private LocationManager locationManager;
    String _Location;

    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getcurrentlocation = findViewById(R.id.getcurrlocation);

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

        client = LocationServices.getFusedLocationProviderClient(UserHome.this);

        if (ActivityCompat.checkSelfPermission(UserHome.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getcurrentlocation();
        }else{
            ActivityCompat.requestPermissions(UserHome.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

       /* lat = getIntent().getStringExtra("lat");;
        lng = getIntent().getStringExtra("lng");;*/
        address = getIntent().getStringExtra("address");
        initView();

        if(address != null){
            getcurrentlocation.setText(address);


           // showallresturant.setVisibility(View.GONE);

            /*OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=23.0225,72.5714&radius=5000&type=restaurant&key=AIzaSyDE0rEZMQN8UeMSzhAKPMcDCdfvN4mOCF0")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UserHome.this, "Test", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Response response = null;
                                try {
                                    response = client.newCall(request).execute();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(UserHome.this, "Test"+response, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });*/

           /* OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
//            MediaType mediaType = MediaType.parse("text/plain");
//            RequestBody body = RequestBody.create(mediaType, "");
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Request request = new Request.Builder()
                    .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=23.0225,72.5714&radius=5000&type=restaurant&key=AIzaSyDE0rEZMQN8UeMSzhAKPMcDCdfvN4mOCF0")
                    .method("GET", null)
                    .build();
            //Response response = client.newCall(request).execute();
            try {

                Response response = client.newCall(request).execute();
               // Toast.makeText(UserHome.this, "Hello response: "+response, Toast.LENGTH_SHORT).show();


                StringBuilder StringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=23.0225,72.5714&radius=5000&type=restaurant&key=AIzaSyDE0rEZMQN8UeMSzhAKPMcDCdfvN4mOCF0");
                String Url = StringBuilder.toString();
                Object dataFetch[] = new Object[2];

                FetchData fetchData = new FetchData();
                fetchData.execute(dataFetch);



                */
            /*URL geturl = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=23.0225,72.5714&radius=5000&type=restaurant&key=AIzaSyDE0rEZMQN8UeMSzhAKPMcDCdfvN4mOCF0");
                HttpURLConnection httpURLConnection = null;
                httpURLConnection = (HttpURLConnection) geturl.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = null;
                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer so = new StringBuffer();

                String line = "";

                while ((line = bufferedReader.readLine())!=null){
                    so.append(line);
                }
                String urlData = so.toString();
                bufferedReader.close();*/
            /*

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Exception",e.getMessage());
            }finally {

            }*/

            /*try {

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(UserHome.this, "Hello"+response, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            /*OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType,"");
            Request request = new Request.Builder().url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=23.0225,72.5714&radius=5000&type=restaurant&key=AIzaSyDE0rEZMQN8UeMSzhAKPMcDCdfvN4mOCF0")
                    .method("GET",body).build();
            Toast.makeText(this, request.toString(), Toast.LENGTH_SHORT).show();*/
            /*try {
                Response response = client.newCall(request).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }*/


        }

        getcurrentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHome.this,SetCurrentLocation.class));
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_favorite:
                startActivity(new Intent(UserHome.this,UserProfile.class));
        }
        return super.onOptionsItemSelected(item);
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
                   // showallresturant.setVisibility(View.GONE);
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
                        getcurrentlocation.setText(_Location);
                    }

                    //  MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");

                    //   Toast.makeText(UserHome.this, "fdsfdl"+location.getLatitude(), Toast.LENGTH_SHORT).show();
                    String keyword = "Restaurant";
                    String key = getText(R.string.googlemapapi).toString();
                    String currentLocation = location.getLatitude() + "," + location.getLongitude();
                    int radius = 1500;
                    String type = "Restaurant";
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

    private void initView() {

        if (ContextCompat.checkSelfPermission(UserHome.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getcurrentlocation();
            /*locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 1000, 1, locationListener);*/
        } else {
            if (ContextCompat.checkSelfPermission(UserHome.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UserHome.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(UserHome.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UserHome.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }

        listViewPlaces = findViewById(R.id.listViewPlaces);


    }

    /*private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            String keyword = "Restaurant";
            String key = getText(R.string.googlemapapi).toString();
            String currentLocation = location.getLatitude() + "," + location.getLongitude();
            int radius = 1000;
            String type = "Restaurant";
            GoogleMapAPI googleMapAPI = APIClient.getClient().create(GoogleMapAPI.class);
            googleMapAPI.getNearBy(currentLocation, radius, type, keyword, key).enqueue(new Callback<PlacesResults>() {
                @Override
                public void onResponse(retrofit2.Call<PlacesResults> call, retrofit2.Response<PlacesResults> response) {
                    if (response.isSuccessful()) {
                        List<Result> results = response.body().getResults();
                        PlacesListAdapter placesListAdapter = new PlacesListAdapter(getApplicationContext(), results);
                        listViewPlaces.setAdapter(placesListAdapter);
                        Toast.makeText(UserHome.this, "Response: ", Toast.LENGTH_SHORT).show();
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

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getcurrentlocation();
            }
        }else{
          /*  getcurrentlocation();
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();*/
        }
    }
}