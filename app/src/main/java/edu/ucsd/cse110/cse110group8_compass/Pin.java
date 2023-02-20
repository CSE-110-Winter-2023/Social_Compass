package edu.ucsd.cse110.cse110group8_compass;

import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
}
