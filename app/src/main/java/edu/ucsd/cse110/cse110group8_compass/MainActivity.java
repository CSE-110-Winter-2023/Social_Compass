package edu.ucsd.cse110.cse110group8_compass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.Manifest;


public class MainActivity extends AppCompatActivity {

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
        /*LiveData<Float> azimuth = orientationService.getOrientation();

        /*DisplayCircle displayCircle = new DisplayCircle(findViewById(R.id.compass));

        Pin northPin = new Pin();
        northPin.longitude = 135.0000;
        northPin.latitude = 90.0000;

        /*final Float[] azimuthFloat = new Float[1];

        final Observer<Float> nameObserver = new Observer<Float>() {
            @Override
            public void onChanged(@Nullable final Float azimuthValue) {
                azimuthFloat[0] = azimuthValue;
            }
        };

        azimuth.observe(this,nameObserver );

        displayCircle.rotatePin(findViewById(R.id.friend_pin), northPin, azimuthFloat[0]);*/



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
        }


//        ImageView pin1 = new ImageView(this);
//        pin1.setImageResource(R.drawable.pindrop);
//
//        ConstraintLayout compassLayout = (ConstraintLayout) findViewById(R.id.compass);
//
//        ConstraintSet c = new ConstraintSet();
//        c.clone(compassLayout);
//        c.constrainCircle(pin1.getId(), R.id.compass, 40, 90);
//        c.applyTo(compassLayout); // Apply back our ConstraintSet on ConstraintLayout.
//
//        compassLayout.addView(pin1);

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
