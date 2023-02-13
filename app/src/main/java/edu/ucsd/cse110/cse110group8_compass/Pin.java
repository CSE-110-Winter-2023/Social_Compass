package edu.ucsd.cse110.cse110group8_compass;

import android.widget.TextView;

public class Pin {

    String name;
    int latitude;
    int longitude;

    public Pin(){
        this.name = "Parent";
    }

    public Pin(String label, int longitude, int latitude){
        this.name = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLabel(String label){
        this.name = label;
    }
}
