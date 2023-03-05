package edu.ucsd.cse110.cse110group8_compass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private boolean useUserOrientation = false;
    private Activity activity = this;
    DisplayCircle displayCircle;

    private int currentZoomLevel = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("BACK IN MAIN");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // reset pinList
        if (false){
            Gson gson = new Gson();
            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getApplicationContext());

            List<Pin> pinList = new ArrayList<>();
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            String jsonToRet = gson.toJson(pinList);
            prefsEditor.putString("pinList", jsonToRet);
            prefsEditor.commit();
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        LocationService locationService = new LocationService(this);
        LiveData<Pair<Double, Double>> userCoordinates;
        userCoordinates = locationService.getLocation();

        OrientationService orientationService = new OrientationService(this);
        Button orientationButton = findViewById(R.id.orientation_activity_button);

        // if user wants to manually input orientation data
       // Float userAzimuthInput = 3.14159F;
        //MutableLiveData<Float> userAzimuth = new MutableLiveData<>();
        //userAzimuth.setValue(userAzimuthInput);
       // orientationService.setMockOrientationSource(userAzimuth);
        // this ^ needs to be in the onclick of the set degree
        LiveData<Float> azimuth = orientationService.getOrientation();

        Pin northPin = new Pin(
                "North Pin",
                135.00,
                90.00
            );
        northPin.setPinTextView(findViewById(R.id.north_pin));

        displayCircle = new DisplayCircle(findViewById(R.id.compass),northPin,  this, azimuth, userCoordinates);

        updatePins();

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
        Log.i("on reloaddata", "on reload data");
        updatePins();
    }

    /**
     * Reads in shared preferences
     */
    public void updatePins(){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = prefs.getString("pinList", "");
        Type type = new TypeToken<List<Pin>>(){}.getType();
        ArrayList<Pin> p = gson.fromJson(json, type);

        if (p == null){return;}

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
            if (curPin.getPinTextView() != null){
                curPin.getPinTextView().setVisibility(View.VISIBLE);
                curPin.getPinTextView().setText(curPin.getName());
            }
        }
    }

    public void onUserOrientationClick(View view) {
        Intent intent = new Intent(this, OrientationActivity.class);
        startActivity(intent);
    }

    public void onCreateLocationClick(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, Activity.RESULT_OK);
    }
    
    public void onChangeLabelClick(View view) {
        Intent intent = new Intent(this, LabelActivity.class);
        startActivity(intent);
    }


    public void setValidZoomLevel(){
        if(currentZoomLevel >= 4){
            currentZoomLevel = 4;
        }else if(currentZoomLevel <= 1){
            currentZoomLevel = 1;
        }
    }

    public void setZoomLevel(){
        if(currentZoomLevel == 1){
            findViewById(R.id.compass_background).setVisibility(View.VISIBLE);
            findViewById(R.id.compass_background_2).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_3).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_4).setVisibility(View.INVISIBLE);
        }else if(currentZoomLevel == 2){
            findViewById(R.id.compass_background_2).setVisibility(View.VISIBLE);
            findViewById(R.id.compass_background).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_3).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_4).setVisibility(View.INVISIBLE);
        }else if(currentZoomLevel == 3){
            findViewById(R.id.compass_background_3).setVisibility(View.VISIBLE);
            findViewById(R.id.compass_background).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_2).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_4).setVisibility(View.INVISIBLE);
        }else if(currentZoomLevel == 4){
            findViewById(R.id.compass_background_4).setVisibility(View.VISIBLE);
            findViewById(R.id.compass_background).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_2).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_3).setVisibility(View.INVISIBLE);
        }
    }

    public void ZoomInClick(View view){
        currentZoomLevel--;
        setValidZoomLevel();
        setZoomLevel();
    }

    public void ZoomOutClick(View view){
        currentZoomLevel++;
        setValidZoomLevel();
        setZoomLevel();
    }

}
