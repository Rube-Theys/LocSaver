package com.example.locsaver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locsaver.DAO.LocationDAO;
import com.example.locsaver.LocationDb;
import com.example.locsaver.R;
import com.example.locsaver.adapters.LocationListAdapter;
import com.example.locsaver.models.Location;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView locationListView;
    private ImageButton addNewLocationBTN;
    private ArrayList<Location> locations;
    private LocationDb db;
    private LocationDAO locationDAO;
    private LocationListAdapter adapter;
    private ImageButton mapsBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDatabase();
        initialiseWidgets();
        createList();
        setListeners();
    }

    private void getDatabase() {
        db = LocationDb.getInstance(this);
        locationDAO = db.getLocationDAO();
//        List<Location> tempList;
//        tempList = locationDAO.getAllLocations();
//        if (tempList.isEmpty())
//        {
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
//            Location newLocation = new Location("Home");
//            newLocation.address ="Oudenbos 97 Rillaar";
//            newLocation.createdDate = dateFormat.format(new Date());
//            newLocation.coordinates = "50.965150;4.904170";
//            newLocation.description = "My home";
//            locationDAO.insertLocation(newLocation);
//
//            newLocation.name = "Ucll Diepenbeek";
//            newLocation.address =" Agoralaan 1 Diepenbeek";
//            newLocation.createdDate = dateFormat.format(new Date());
//            newLocation.coordinates = "50.928540;5.388710";
//            newLocation.description = "My school!";
//            locationDAO.insertLocation(newLocation);
//        }
    }

    private void createList() {
        List<Location> locationList = locationDAO.getAllLocations();
        locations = new ArrayList<>();
        locations.addAll(locationList);
        adapter = new LocationListAdapter(this, R.layout.location_list_item, locations);
        locationListView.setAdapter(adapter);
    }

    private void setListeners() {
        locationListView.setOnItemClickListener((parent, v , position, id) ->{
            Location location = locations.get(position);
            Intent intent = new Intent(this, LocationDetails.class);
            intent.putExtra("location", location);
            startActivity(intent);
        });
        addNewLocationBTN.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddLocation.class);
            startActivity(intent);
        });

        mapsBTN.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsLocation.class);
            startActivity(intent);
        });
    }

    private void initialiseWidgets() {
        addNewLocationBTN = findViewById(R.id.backBTN);
        locationListView = findViewById(R.id.locationsListView);
        mapsBTN = findViewById(R.id.mapsBTN);
    }
}