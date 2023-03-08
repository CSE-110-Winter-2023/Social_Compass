package edu.ucsd.cse110.cse110group8_compass;

import android.widget.TextView;

public class Pin {

    private String name;
    private Double latitude;
    private Double longitude;
    private TextView pinTextView;


    public Pin(){
        this.name = "Parent";
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

    public boolean checkValid() {
        if(this.name == null) {
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

}
