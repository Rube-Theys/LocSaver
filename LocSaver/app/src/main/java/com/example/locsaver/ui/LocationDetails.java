package com.example.locsaver.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.locsaver.DAO.LocationDAO;
import com.example.locsaver.LocationDb;
import com.example.locsaver.R;
import com.example.locsaver.models.Location;

public class LocationDetails extends AppCompatActivity {

    private Location loc;
    private LocationDb db;
    private LocationDAO locationDAO;

    private ImageButton backBTN;
    private ImageButton deleteBTN;
    private ImageButton editBTN;
    private TextView nameTextView;
    private TextView addressTextView;
    private TextView dateTextView;
    private TextView latTextView;
    private TextView lngTextView;
    private TextView descriptionTextView;
    private ImageView pictureImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_details);

        Intent incomingIntent = getIntent();
        loc = (Location) incomingIntent.getSerializableExtra("location");

        getDatabase();
        initialiseWidgets();
        setWidgets();
        setListeners();
    }

    private void getDatabase() {
        db = LocationDb.getInstance(this);
        locationDAO = db.getLocationDAO();
    }

    private void setListeners() {
        backBTN.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        editBTN.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditLocation.class);
            intent.putExtra("location", loc);
            startActivity(intent);
        });

        deleteBTN.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(LocationDetails.this).create();
            alertDialog.setTitle("Confirm");
            alertDialog.setMessage("Are you sure you want to delete this location: " + loc.name + "?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", (dialog, which) -> {
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", (dialog, which) -> {
                locationDAO.deleteLocation(loc);
                Toast.makeText(this, loc.name + " has been deleted from the database", Toast.LENGTH_SHORT).show();
            });
            alertDialog.show();
        });
    }

    private void initialiseWidgets() {
        backBTN = findViewById(R.id.detailsBackBTN);
        editBTN = findViewById(R.id.detailsEditBTN);
        deleteBTN = findViewById(R.id.detailsDeleteBTN);
        nameTextView = findViewById(R.id.detailsNameTextView);
        addressTextView = findViewById(R.id.detailsAddressTextView);
        dateTextView = findViewById(R.id.detailsDateTextView);
        latTextView = findViewById(R.id.detailsLatTextView);
        lngTextView = findViewById(R.id.detailsLngTextView);
        descriptionTextView = findViewById(R.id.detailsDescriptionBoxTextView);
        pictureImageView = findViewById(R.id.detailsPictureImageView);
    }

    private void setWidgets() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(loc.image, 0, loc.image.length);
        String[] latlng = loc.coordinates.split(";");

        pictureImageView.setImageBitmap(bitmap);
        latTextView.setText("Lat: " + latlng[0]);
        lngTextView.setText("Lng: " + latlng[1]);
        addressTextView.setText("Address: "+loc.address);
        dateTextView.setText("DateTime: "+loc.createdDate);
        nameTextView.setText("Name: "+loc.name);
        descriptionTextView.setText(loc.description.isEmpty() ? "" : loc.description);
    }
}
