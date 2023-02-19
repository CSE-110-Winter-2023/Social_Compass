package edu.ucsd.cse110.cse110group8_compass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.Manifest;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("BACK IN MAIN");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        LocationService locationService = new LocationService(this);
        LiveData<Pair<Double, Double>> userCoordinates;
        userCoordinates = locationService.getLocation();

        OrientationService orientationService = new OrientationService(this);
        LiveData<Float> azimuth = orientationService.getOrientation();

        DisplayCircle displayCircle = new DisplayCircle(findViewById(R.id.compass), this, azimuth, userCoordinates);
        //displayCircle.setUserCoordinate(userCoordinates);

        Pin northPin = new Pin("North Pin",135.00, 90.00);
        northPin.setPinImageView(findViewById(R.id.north_pin));

        displayCircle.setNorthPin(northPin);
        //displayCircle.rotateAllPins();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Intent intent = getIntent();
            System.out.println("IN HERE");
            String name = intent.getStringExtra("label");
            String latit = intent.getStringExtra("latitude");
            String longit = intent.getStringExtra("longitude");
            System.out.println("fromintent: " + name + latit + longit);
            TextView textView = (TextView) findViewById(R.id.Parent);
            textView.setText(name + ": " + latit + ", " + longit);
            System.out.println("FINISHED");

            Pin pinOne = new Pin();
            pinOne.setLocation(Double.valueOf(latit), Double.valueOf(longit));
            pinOne.setPinImageView(findViewById(R.id.parent_pin));

            System.out.println("long:" + pinOne.getLongitude());
            System.out.println("latitude:" +    pinOne.getLatitude());

            displayCircle.addPin(pinOne);
            //displayCircle.rotatePin(pinOne, azimuth, this);

        }

    }
    
    public void onEnterLocationClick(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }
    
    public void onChangeLabelClick(View view) {
        Intent intent = new Intent(this, LabelActivity.class);
        startActivity(intent);
    }

}
