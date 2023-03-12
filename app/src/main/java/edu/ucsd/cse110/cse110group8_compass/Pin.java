package edu.ucsd.cse110.cse110group8_compass;

import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

public class Pin {

    private String name;
    private MutableLiveData<Double> latitude;
    private MutableLiveData<Double> longitude;
    private TextView pinTextView;


    public Pin(){
        this.name = "Parent";
    }

    public void setLocation( Double latitude , Double longitude) {
        this.latitude.setValue(latitude);
        this.longitude.setValue(longitude);
    }

    public void setPinTextView(TextView textView) {
        this.pinTextView = textView;
    }

    public TextView getPinTextView() {
        return pinTextView;
    }
    public void setLiveData(MutableLiveData<Double> latitude, MutableLiveData<Double> longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MutableLiveData<Double> getLiveDataLongitude() {
        return longitude;
    }
    public MutableLiveData<Double> getLiveDataLatitude() {
        return latitude;

    }

    public Double getLatitude() {
        return this.latitude.getValue();
    }

    public Double getLongitude() {
        return this.longitude.getValue();
    }

    public String getName() {
        return name;
    }

    public Pin(String label, Double longitude, Double latitude){
        this.name = label;
        this.latitude.setValue(latitude);
        this.longitude.setValue(longitude);
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

    void print() {
        System.out.println("Name:" + name);
        System.out.println("TextView:" + getPinTextView());
    }


}
