package edu.ucsd.cse110.cse110group8_compass;

import android.widget.ImageView;
import android.widget.TextView;

public class Pin {

    private String name;
    private Double latitude;
    private Double longitude;
    private ImageView pinImageView;


    public Pin(){
        this.name = "Parent";
    }

    public void setLocation( Double latitude , Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setPinImageView(ImageView imageView) {
        this.pinImageView = imageView;
    }

    public ImageView getPinImageView() {
        return pinImageView;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public Pin(String label, Double longitude, Double latitude){
        this.name = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLabel(String label){
        this.name = label;
    }
}
