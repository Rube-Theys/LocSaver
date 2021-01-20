package com.example.locsaver.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.locsaver.DAO.LocationDAO;
import com.example.locsaver.LocationDb;
import com.example.locsaver.R;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddLocation extends AppCompatActivity implements LocationListener {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;

    private ImageButton pictureBTN;
    private ImageButton backBTN;
    private ImageButton gpsBTN;
    private EditText nameEditText;
    private ImageView pictureImageView;
    private EditText descriptionEditText;
    private TextView gpsDateTextView;
    private TextView addressTextView;
    private LocationDb db;
    private LocationDAO locationDAO;
    private TextView dateTextView;
    private Button addBTN;
    private Bitmap image;

    private LocationManager locationManager;
    private String address;
    private String lat;
    private String lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_add);

        getDatabase();
        initialiseWidgets();
        setListeners();
        checkLocationPermissions();
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

        pictureBTN.setOnClickListener(v -> {
            askCameraPermission();
        });

        gpsBTN.setOnClickListener(v -> {
            getMyLocation();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
            String date = dateFormat.format(new Date());
            if(address == null){
                addressTextView.setText("Please enable location service");
                gpsDateTextView.setText("If its already enabled please try again in a couple seconds");
            }
            else
            {
                gpsDateTextView.setText("lng: " + lng + " lat: " + lat);
                dateTextView.setText(date);
                addressTextView.setText(address);
            }
        });

        addBTN.setOnClickListener(v -> {
            if(nameEditText.getText().toString() == null || lat == null || image == null)
            {
                Toast.makeText(this, "Please make sure to take a picture and fill in the name. Also click on the location button", Toast.LENGTH_LONG).show();
            }
            else
            {
                com.example.locsaver.models.Location newLocation = new com.example.locsaver.models.Location(nameEditText.getText().toString());
                newLocation.coordinates = lat + ";" + lng;
                newLocation.address = address;
                newLocation.createdDate = dateTextView.getText().toString();
                newLocation.description = descriptionEditText.getText().toString().isEmpty() ? null : descriptionEditText.getText().toString();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                newLocation.image = stream.toByteArray();
                image.recycle();

                locationDAO.insertLocation(newLocation);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(AddLocation.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddLocation.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initialiseWidgets() {
        pictureBTN = findViewById(R.id.cameraBTN);
        backBTN = findViewById(R.id.backBTN);
        gpsBTN = findViewById(R.id.gpsBTN);
        nameEditText = findViewById(R.id.locationNameEditText);
        descriptionEditText = findViewById(R.id.locationDescriptionEditText);
        pictureImageView = findViewById(R.id.pictureImageView);
        addressTextView = findViewById(R.id.addressTextView);
        gpsDateTextView = findViewById(R.id.gpsTextView);
        dateTextView = findViewById(R.id.dateTextView);
        addBTN = findViewById(R.id.locationAddButton);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            image = (Bitmap) data.getExtras().get("data");
            pictureImageView.setImageBitmap(image);
        }
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation() {
        try{
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, AddLocation.this);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat = "" + location.getLatitude();
        lng = "" + location.getLongitude();

        try {
            Geocoder geocoder = new Geocoder(AddLocation.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat),Double.parseDouble(lng),1);
            address = addresses.get(0).getAddressLine(0);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
