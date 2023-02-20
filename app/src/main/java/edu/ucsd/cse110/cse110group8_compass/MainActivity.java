package edu.ucsd.cse110.cse110group8_compass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private boolean reloadNeeded = false;
    private static final int EDIT_CODE = 31;
    DisplayCircle displayCircle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("BACK IN MAIN");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Intent intent = getIntent();
        System.out.println("IN HERE");
        String name = intent.getStringExtra("label");
        String latit = intent.getStringExtra("latitude");
        String longit = intent.getStringExtra("longitude");
        System.out.println("fromintent: " + name + latit + longit);
        TextView textView = (TextView) findViewById(R.id.Parent);
        textView.setText(name + ": " + latit + ", " + longit);
        System.out.println("FINISHED");

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);


        Pin pinOne = new Pin();
        if(longit!= null && latit != null){
            pinOne.setLocation(Double.valueOf(latit), Double.valueOf(longit)); //ongitude = Double.valueOf(longit);
           // pinOne.latitude = Double.valueOf(latit);
            System.out.println("long:" + pinOne.getLongitude());
            System.out.println("latitude:" +    pinOne.getLatitude());
            }

        */
            //displayCircle.rotatePin(findViewById(R.id.parent_pin), pinOne, azimuth, this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        LocationService locationService = new LocationService(this);
        LiveData<Pair<Double, Double>> userCoordinates;
        userCoordinates = locationService.getLocation();

        OrientationService orientationService = new OrientationService(this);
        // if user wants to manually input orientation data
        //Float userAzimuthInput = 3.14159F;
        //MutableLiveData<Float> userAzimuth = new MutableLiveData<>();
        //userAzimuth.setValue(userAzimuthInput);
        //orientationService.setMockOrientationSource(userAzimuth);
        // this ^ needs to be in the onclick of the set degree
        LiveData<Float> azimuth = orientationService.getOrientation();

        Pin northPin = new Pin(
                "North Pin",
                135.00,
                90.00
            );
        northPin.setPinTextView(findViewById(R.id.north_pin));

        displayCircle = new DisplayCircle(findViewById(R.id.compass),northPin,  this, azimuth, userCoordinates);
        //displayCircle.setUserCoordinate(userCoordinates);

        //displayCircle.rotateAllPins();


        SharedPreferences testPreferences = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = testPreferences.getString("pinList", "");
        Type type = new TypeToken<List<Pin>>(){}.getType();
        ArrayList<Pin> p = gson.fromJson(json, type);
        Log.i("pinlist size", ""+p.size());

        for ( int i = 0; i < p.size(); i++ ){
            if(i == 0){
                p.get(i).setPinTextView((TextView)findViewById(R.id.pin_one));
            }
            if(i == 1){
                p.get(i).setPinTextView((TextView)findViewById(R.id.pin_two));
            }
            if(i == 2) {
                p.get(i).setPinTextView((TextView) findViewById(R.id.pin_three));
            }
        }

        for(Pin curPin : p){
            curPin.getPinTextView().setVisibility(View.VISIBLE);
            curPin.getPinTextView().setText(curPin.getName());
        }

        Float azimuthFloat;

        /*final Observer<Float> nameObserver = new Observer<Float>() {
            @Override
            public void onChanged(@Nullable final Float azimuthValue) {
                azimuthFloat = azimuthValue;
            }
        };*/

        //azimuth.observe(this, new Observer<Float>() {
        //    @Override
        //    public void onChanged(Float value) {
                // Get the data from the LiveData object here
         //       if(findViewById(R.id.friend_pin) != null ) {
         //           displayCircle.rotatePin(findViewById(R.id.friend_pin), northPin, value);
         //       }

                //Log.d("LiveDataValue", String.valueOf(value));
        //    }
       // });

        //azimuth.observe(this, observer -> {
       //     azimuthFloat = observer;
       // });


        //displayCircle.setUserPin(userCoordinates);
       // displayCircle.addPin(northPin);

        /*Bundle extras = getIntent().getExtras();
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


         */


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

    @Override
    public void onResume(){
        super.onResume();
        Log.i("on resume", ""+this.reloadNeeded);
        if(this.reloadNeeded){
            this.reloadData();
        }
        this.reloadNeeded = true;
        //this.reloadNeeded = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            // Yes we did! Let's allow onResume() to reload the data
        this.reloadNeeded = true;

    }

    private void reloadData(){

        /*
        SharedPreferences prefs = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String s = prefs.getString("name", "default");
        TextView pin = findViewById(R.id.parent_pin);
         */
        Log.i("on reloaddata", "on reload data");
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = prefs.getString("pinList", "");
        Type type = new TypeToken<List<Pin>>(){}.getType();
        ArrayList<Pin> p = gson.fromJson(json, type);


        for ( int i = 0; i < p.size(); i++ ){
            if(i == 0){
                p.get(i).setPinTextView((TextView)findViewById(R.id.pin_one));
            }
            if(i == 1){
                p.get(i).setPinTextView((TextView)findViewById(R.id.pin_two));
            }
            if(i == 2) {
                p.get(i).setPinTextView((TextView) findViewById(R.id.pin_three));
            }
        }

        boolean flag = displayCircle.setPinList(p);

        for(Pin curPin : p){
            curPin.getPinTextView().setVisibility(View.VISIBLE);
            curPin.getPinTextView().setText(curPin.getName());
        }


    }


    public void onCreateLocationClick(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, Activity.RESULT_OK);
    }
    
    public void onChangeLabelClick(View view) {
        Intent intent = new Intent(this, LabelActivity.class);
        startActivity(intent);
    }

}
