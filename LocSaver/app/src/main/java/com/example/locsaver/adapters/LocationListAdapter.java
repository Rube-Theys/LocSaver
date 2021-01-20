package com.example.locsaver.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.locsaver.R;
import com.example.locsaver.models.Location;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class LocationListAdapter extends ArrayAdapter<Location> {
    private Context context;
    int resource;

    public LocationListAdapter(Context c, int resource, ArrayList<Location> locations) {
        super(c, resource, locations);
        this.context = c;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Get current Location
        Location location = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        //Get views to set
        TextView locationName = convertView.findViewById(R.id.locationListItemName);
        TextView locationAddress = convertView.findViewById(R.id.locationListItemAddress);
        TextView locationDateTime = convertView.findViewById(R.id.locationListItemDateTime);
        ImageView locationImage = convertView.findViewById(R.id.locationListItemPicture);

        //Set views
        locationName.setText(location.name);
        locationAddress.setText(location.address);
        locationAddress.setText(location.address);
        locationDateTime.setText(location.createdDate);

        //set image
        if(location.image != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(location.image, 0, location.image.length);
            locationImage.setImageBitmap(bitmap);
        }
        return convertView;
    }
}
