package com.example.foodworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.auth.User;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SetCurrentLocation extends AppCompatActivity {

    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    String _Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_current_location);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.currentlocationmap);
        client = LocationServices.getFusedLocationProviderClient(SetCurrentLocation.this);

        if (ActivityCompat.checkSelfPermission(SetCurrentLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(SetCurrentLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    private void getCurrentLocation() {
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
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
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

                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
                            googleMap.addMarker(markerOptions).showInfoWindow();
                           // Toast.makeText(SetCurrentLocation.this, _Location, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SetCurrentLocation.this, UserHome.class);
                            i.putExtra("lat",location.getLatitude());
                            i.putExtra("lng",location.getLongitude());
                            i.putExtra("address",_Location);
                            startActivity(i);
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
                getCurrentLocation();
            }
        }else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}