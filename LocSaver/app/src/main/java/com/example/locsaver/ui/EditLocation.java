package com.example.locsaver.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locsaver.DAO.LocationDAO;
import com.example.locsaver.LocationDb;
import com.example.locsaver.R;
import com.example.locsaver.models.Location;

import java.io.ByteArrayOutputStream;

public class EditLocation extends AppCompatActivity {

    private Location loc;

    private ImageButton backBTN;
    private Button saveBTN;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText descriptionEditText;
    private ImageView pictureImageView;
    private TextView gpsTextView;
    private TextView dateTextView;
    private LocationDb db;
    private LocationDAO locationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_edit);

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
            Intent intent = new Intent(this, LocationDetails.class);
            intent.putExtra("location", loc);
            startActivity(intent);
        });

        saveBTN.setOnClickListener(v -> {
            if(nameEditText.getText().toString() == null || addressEditText.getText().toString() == null)
            {
                Toast.makeText(this, "Name or address can't be empty", Toast.LENGTH_SHORT).show();
            }
            else
            {
                loc.name = nameEditText.getText().toString();
                loc.address = addressEditText.getText().toString();
                loc.description = descriptionEditText.getText().toString().isEmpty() ? null : descriptionEditText.getText().toString();

                locationDAO.updateLocation(loc);
                Intent intent = new Intent(this, LocationDetails.class);
                intent.putExtra("location", loc);
                startActivity(intent);
            }
        });
    }

    private void initialiseWidgets(){
        backBTN = findViewById(R.id.editBackBTN);
        saveBTN = findViewById(R.id.editLocationSaveButton);
        nameEditText = findViewById(R.id.editLocationNameEditText);
        descriptionEditText = findViewById(R.id.editLocationDescriptionEditText);
        addressEditText = findViewById(R.id.editLocationAddressEditText);
        gpsTextView = findViewById(R.id.editGpsTextView);
        dateTextView = findViewById(R.id.editDateTextView);
        pictureImageView = findViewById(R.id.editPictureImageView);
    }

    private void setWidgets() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(loc.image, 0, loc.image.length);
        pictureImageView.setImageBitmap(bitmap);
        gpsTextView.setText("LatLng: " + loc.coordinates);
        addressEditText.setText(loc.address);
        dateTextView.setText("DateTime: "+loc.createdDate);
        nameEditText.setText(loc.name);
        descriptionEditText.setText(loc.description.isEmpty() ? "" : loc.description);
    }
}
