package com.example.locsaver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locsaver.DAO.LocationDAO;
import com.example.locsaver.LocationDb;
import com.example.locsaver.R;
import com.example.locsaver.models.Location;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MapsLocation extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton backBTN;
    private LocationDb db;
    private LocationDAO locationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getDatabase();

        backBTN = findViewById(R.id.mapsBackBTN);
        backBTN.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void getDatabase() {
        db = LocationDb.getInstance(this);
        locationDAO = db.getLocationDAO();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.clear();

        List<Location> locations = locationDAO.getAllLocations();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (Location loc : locations)
        {
            String[] latlng = loc.coordinates.split(";");
            LatLng coordinates = new LatLng(Double.parseDouble(latlng[0]),Double.parseDouble(latlng[1]));
            googleMap.addMarker(new MarkerOptions().position(coordinates).title(loc.name));

            builder.include(coordinates);
        }

        LatLngBounds bounds = builder.build();
        int padding = 350;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cu);
    }
}
