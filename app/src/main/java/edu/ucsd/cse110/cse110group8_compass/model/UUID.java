package edu.ucsd.cse110.cse110group8_compass.model;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import edu.ucsd.cse110.cse110group8_compass.Pin;

@Entity(tableName = "uuid")
public class UUID {

    public String label;
    public Double latitude;
    public Double longitude;
    public long updatedAt;

    @PrimaryKey
    @SerializedName("public_code")
    @NonNull
    public String public_code;
    public UUID(){}
    public UUID(String label, Double latitude, Double longitude, String public_code){
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
        this.public_code = public_code;
    }

    public String getPublic_code() {
        return public_code;
    }

    public void setPublic_code(String public_code) {
        this.public_code = public_code;
    }

    public static UUID fromJSON(String json) {
        return new Gson().fromJson(json, UUID.class);
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
