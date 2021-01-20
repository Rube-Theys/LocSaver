package com.example.locsaver.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Location implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "location_name")
    @NonNull public String name;

    @ColumnInfo(name = "location_coordinates")
    @NonNull public String coordinates;

    @ColumnInfo(name = "location_address")
    @Nullable public String address;

    @ColumnInfo(name = "location_description")
    @Nullable public String description;

    @ColumnInfo(name = "location_create_date")
    @Nullable public String createdDate;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB, name = "location_image")
    @Nullable public byte[] image;

    public Location(@NonNull String name) {
        this.name = name;
    }
}
