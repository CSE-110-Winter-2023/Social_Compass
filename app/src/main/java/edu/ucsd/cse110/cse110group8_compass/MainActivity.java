package edu.ucsd.cse110.cse110group8_compass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import edu.ucsd.cse110.cse110group8_compass.model.PinViewModel;
import edu.ucsd.cse110.cse110group8_compass.model.UUID;
import edu.ucsd.cse110.cse110group8_compass.model.UUIDAPI;


public class MainActivity extends AppCompatActivity {
    private boolean reloadNeeded = false;
    private static final int EDIT_CODE = 31;
    private boolean useUserOrientation = false;
    private Activity activity = this;
    private PinViewModel pinViewModel;
    ScheduledFuture<?> poller;

    DisplayCircle displayCircle;

    private int currentZoomLevel = 1;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("BACK IN MAIN");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pinViewModel = new ViewModelProvider(this).get(PinViewModel.class);


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

        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.compass);
        float density = activity.getResources().getDisplayMetrics().density;
        /*
        //starting dynamic pin creation
        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.compass);
        ConstraintSet set = new ConstraintSet();

        TextView view = new TextView(this);


        view.setId(View.generateViewId());  // cannot set id after add
        layout.addView(view,0);
        set.clone(layout);
        set.connect(view.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, 60);
        set.applyTo(layout);

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        layoutParams.circleRadius = (int) (20 * activity.getResources().getDisplayMetrics().density);
        layoutParams.circleConstraint=R.id.compass;
        view.setLayoutParams(layoutParams);
        view.setText("hello");
        view.bringToFront();



        Pin testPin = new Pin(
                "Test Pin",
                35.00,
                -70.00
        );
        testPin.setPinTextView(view);
        */

        // fetching from local database
        LiveData<UUID> uuids = pinViewModel.getOrCreateUUID("38946729");


        Pin testPin = new PinBuilder(this, layout, density).config().withCoordinates(35.00, -70.00).withLabel("Test Pin").build();
        displayCircle = new DisplayCircle(findViewById(R.id.compass),northPin,  this, azimuth, userCoordinates);
        ArrayList<Pin> arrPin = new ArrayList<Pin>();
        arrPin.add(testPin);
        displayCircle.setPinList(arrPin);

        //^set the pin and add it to an pinList

        displayCircle.setAllPinZones(new ZoomLevel(1));



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

    /**Double
     * Reads in shared preferences
     */
    public void updatePins(){

        pinViewModel.getOrCreateUUID("38946729");
        pinViewModel.getOrCreateUUID("89347878");
        pinViewModel.getOrCreateUUID("09393999");
        /*SharedPreferences prefs = PreferenceManager
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

        displayCircle.setPinList(p);

        for(Pin curPin : p){
            if (curPin.getPinTextView() != null){
                curPin.getPinTextView().setVisibility(View.VISIBLE);
                curPin.getPinTextView().setText(curPin.getLabel());
            }
        }*/
        int i = 0;
    }

    public void onUserOrientationClick(View view) {
        // Intent intent = new Intent(this, OrientationActivity.class);
        // startActivity(intent);


        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        UUIDAPI api = new UUIDAPI();
        api.put(new UUID("this is a test bruh", 10.123, 10.123, "122-122-123"));



        /*
        UUIDAPI api = new UUIDAPI();
        MutableLiveData<UUID> realTimeData = new MutableLiveData<>();
        var executor = Executors.newSingleThreadScheduledExecutor();
        poller = executor.scheduleAtFixedRate(() -> {
            UUID n = api.get("testing");
            realTimeData.postValue(n);
        }, 0, 3000, TimeUnit.MILLISECONDS);
        MediatorLiveData<UUID> noteData = new MediatorLiveData<>();
        noteData.addSource(realTimeData, noteData::postValue);
        Log.i("checking connection", "longitude and latitude: " + noteData.toString());
        */
        // Start by fetching the note from the server ONCE.
        // Then, set up a background thread that will poll the server every 3 seconds.

        // You may (but don't have to) want to cache the LiveData's for each public_code, so that
        // you don't create a new polling thread every time you call getRemote with the same public_code.
        // You don't need to worry about killing background threads.

        // throw new UnsupportedOperationException("Not implemented yet");
    }

    public void onCreateLocationClick(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, Activity.RESULT_OK);
    }
    
    public void onChangeLabelClick(View view) {
        Intent intent = new Intent(this, LabelActivity.class);
        startActivity(intent);
    }


    public int getCurrentZoomLevel(){
        return this.currentZoomLevel;
    }

     public void setCurrentZoomLevel(int level){
            this.currentZoomLevel = level;
            this.setZoomLevel();
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
            findViewById(R.id.zoom_out_button).setEnabled(true);
            findViewById(R.id.zoom_in_button).setEnabled(false);
        }else if(currentZoomLevel == 2){
            findViewById(R.id.compass_background_2).setVisibility(View.VISIBLE);
            findViewById(R.id.compass_background).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_3).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_4).setVisibility(View.INVISIBLE);
            findViewById(R.id.zoom_out_button).setEnabled(true);
            findViewById(R.id.zoom_in_button).setEnabled(true);
        }else if(currentZoomLevel == 3){
            findViewById(R.id.compass_background_3).setVisibility(View.VISIBLE);
            findViewById(R.id.compass_background).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_2).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_4).setVisibility(View.INVISIBLE);
            findViewById(R.id.zoom_out_button).setEnabled(true);
            findViewById(R.id.zoom_in_button).setEnabled(true);
        }else if(currentZoomLevel == 4){
            findViewById(R.id.compass_background_4).setVisibility(View.VISIBLE);
            findViewById(R.id.compass_background).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_2).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_3).setVisibility(View.INVISIBLE);
            findViewById(R.id.zoom_out_button).setEnabled(false);
            findViewById(R.id.zoom_in_button).setEnabled(true);
        }
    }

    public void ZoomInClick(View view){
        currentZoomLevel--;
        setValidZoomLevel();
        setZoomLevel();
        System.out.println("ZoomInClick: " + currentZoomLevel);
        displayCircle.restartObservers(new ZoomLevel(currentZoomLevel));
    }

    public void ZoomOutClick(View view){
        currentZoomLevel++;
        setValidZoomLevel();
        setZoomLevel();
        System.out.println("ZoomOutClick: " + currentZoomLevel);
        displayCircle.restartObservers(new ZoomLevel(currentZoomLevel));
    }

}
