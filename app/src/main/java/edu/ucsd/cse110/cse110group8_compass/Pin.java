package edu.ucsd.cse110.cse110group8_compass;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "pins")
public class Pin {

    private String label;
    private Double latitude;
    private Double longitude;
    private TextView pinTextView;
    public long updatedAt;

    @PrimaryKey
    @SerializedName("title")
    @NonNull
    private String public_code;

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPublic_code() {
        return public_code;
    }

    public void setPublic_code(String public_code) {
        this.public_code = public_code;
    }

    public Pin(){
        this.label = "Parent";
    }

    public void setLocation( Double latitude , Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setPinTextView(TextView textView) {
        this.pinTextView = textView;
    }

    public TextView getPinTextView() {
        return pinTextView;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getLabel() {
        return label;
    }

    public Pin(String label, Double longitude, Double latitude){
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public boolean checkValid() {
        if(this.label == null) {
            return false;
        }
        else if(this.pinTextView == null) {
            return false;
        }
        else if(this.latitude == null || this.longitude == null) {
            return false;
        }
        else {
            return true;
        }
    }

    void print() {
        System.out.println("Name:" + label);
        System.out.println("TextView:" + getPinTextView());
    }

    public static Pin fromJSON(String json) {
        return new Gson().fromJson(json, Pin.class);
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }


}
