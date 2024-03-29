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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.cse110group8_compass.model.PinViewModel;
import edu.ucsd.cse110.cse110group8_compass.model.UUID;
import edu.ucsd.cse110.cse110group8_compass.model.UUIDAPI;


public class MainActivity extends AppCompatActivity {
    private boolean reloadNeeded = false;
    private static final int EDIT_CODE = 31;
    private boolean useUserOrientation = false;
    private Activity activity = this;
    private PinViewModel pinViewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private Long timeOnline;
    private LocationService ourLocationService;
    MutableLiveData<Long> realTimeData = new MutableLiveData<>();
    ScheduledFuture<?> poller;
    private HashMap<String, Pin> pinHashMap = new HashMap<>();

    private final String northPinPublicCode = "qtgmI&@3$zH!us*X5!YKi&b1aWhijR5HMe&ruxJ6mxG5Fx#EcL$ou" +
            "SiaGMP*0oMGH&tnju36*K*qxaR%&iL20@5BFdpc0m^bhBoR";

    DisplayCircle displayCircle;

    private int currentZoomLevel = 2;

    private HashMap<String, Pin> currPins = new HashMap<String, Pin>();
    ArrayList<Pin> pinList = new ArrayList<>();
    int timeWithoutGPS;
    ArrayList<String> publicCodeList;
    ArrayList<LiveData<UUID>> uuids = new ArrayList<>();
    private String userName;


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        timeWithoutGPS = 0;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        System.out.println("BACK IN MAIN");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pinViewModel = new ViewModelProvider(this).get(PinViewModel.class);


        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean previouslyStarted = prefs.getBoolean("first", false);
        if (!previouslyStarted) {
            Intent intent = new Intent(this, EnterNameActivity.class);
            startActivity(intent);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("first", Boolean.TRUE);
            edit.commit();
        }


        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        Gson gson = new Gson();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        appSharedPrefs.getAll();
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        String json = appSharedPrefs.getString("publicCodeList", "");
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        publicCodeList = gson.fromJson(json, type);
        
        if(publicCodeList == null){
            publicCodeList = new ArrayList<>();
        }

        for (var public_code : publicCodeList) {
            uuids.add(pinViewModel.getUUIDFromRemote(public_code));
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        LocationService locationService = new LocationService(this);
        LiveData<Pair<Double, Double>> userCoordinates;
        userCoordinates = locationService.getLocation();


        checkGPSStatus().observe(this, this::GPSTime);


        OrientationService orientationService = new OrientationService(this);

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
        northPin.setPublic_code(northPinPublicCode);
        northPin.setPinTextView(findViewById(R.id.north_pin));

        // fetching from local database
        displayCircle = new DisplayCircle(findViewById(R.id.compass), northPin, this, azimuth, userCoordinates);
        setValidZoomLevel();
        setZoomLevel();
        displayCircle.restartObservers(new ZoomLevel(currentZoomLevel));

        displayCircle.setAllPinZones(new ZoomLevel(1));
        for (var uuid : uuids) {
            uuid.observe(this, this::pinObserver);
        }
    }

    public void pinObserver(UUID uuid) {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.compass);
        float density = activity.getResources().getDisplayMetrics().density;
        updatePins();
        if (uuid == null) {
            return;
        }
        if(!currPins.containsKey(uuid.public_code)) {
            Pin pin = new PinBuilder(this, layout, density).config().withCoordinates(uuid.longitude, uuid.latitude).withLabel(uuid.label).build();
            currPins.put(uuid.public_code, pin);
            pinList.add(pin);
            displayCircle.setPinList(pinList, new ZoomLevel(currentZoomLevel));
        } else {
            currPins.get(uuid.public_code).setLocation(uuid.latitude, uuid.longitude);
        }
    }

    public void GPSTime(Integer offlineMins) {
        timeWithoutGPS = offlineMins;
        TextView gpsView = findViewById(R.id.timeOffline);

        ImageView onlineButton = findViewById(R.id.online);
        ImageView offlineButton = findViewById(R.id.offline);

        if (offlineMins < 1) {
            gpsView.setText("online");
            onlineButton.setVisibility(View.VISIBLE);
            offlineButton.setVisibility(View.INVISIBLE);

        } else {
            gpsView.setText("" + timeWithoutGPS + " min");
            onlineButton.setVisibility(View.INVISIBLE);
            offlineButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.reloadNeeded) {
            this.reloadData();
        }
        this.reloadNeeded = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.reloadNeeded = true;
    }

    private void reloadData() {
        updatePins();
        SharedPreferences prefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        pinViewModel.repo.api.url = prefs.getString("mockUrl", "https://socialcompass.goto.ucsd.edu/");
        // Log.i("pinViewModel: ", ""+pinViewModel.repo.api.url);


    }

    public void updatePins() {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.compass);
        float density = activity.getResources().getDisplayMetrics().density;

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = prefs.getString("publicCodeList", "");
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        publicCodeList = gson.fromJson(json, type);

        if (publicCodeList == null) {
            publicCodeList = new ArrayList<>();
        }
        Log.i("updatePins", publicCodeList.toString());
        for (var public_code : publicCodeList) {
            if (!currPins.containsKey(public_code)) {
                LiveData<UUID> uuid = pinViewModel.getUUIDFromRemote(public_code);
                uuid.observe(this, this::pinObserver);
                uuids.add(uuid);
            }
        }
    }


    public void onContactCreationClick(View view) {
        Intent intent = new Intent(this, ContactCreationActivity.class);
        startActivityForResult(intent, Activity.RESULT_OK);
    }

    public int getCurrentZoomLevel() {
        return this.currentZoomLevel;
    }

    public void setCurrentZoomLevel(int level) {
        this.currentZoomLevel = level;
        this.setZoomLevel();
    }

    public void setValidZoomLevel() {
        if (currentZoomLevel >= 4) {
            currentZoomLevel = 4;
        } else if (currentZoomLevel <= 1) {
            currentZoomLevel = 1;
        }
    }

    public void setZoomLevel() {
        if (currentZoomLevel == 1) {
            findViewById(R.id.compass_background).setVisibility(View.VISIBLE);
            findViewById(R.id.compass_background_2).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_3).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_4).setVisibility(View.INVISIBLE);
            findViewById(R.id.zoom_out_button).setEnabled(true);
            findViewById(R.id.zoom_in_button).setEnabled(false);
        } else if (currentZoomLevel == 2) {
            findViewById(R.id.compass_background_2).setVisibility(View.VISIBLE);
            findViewById(R.id.compass_background).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_3).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_4).setVisibility(View.INVISIBLE);
            findViewById(R.id.zoom_out_button).setEnabled(true);
            findViewById(R.id.zoom_in_button).setEnabled(true);
        } else if (currentZoomLevel == 3) {
            findViewById(R.id.compass_background_3).setVisibility(View.VISIBLE);
            findViewById(R.id.compass_background).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_2).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_4).setVisibility(View.INVISIBLE);
            findViewById(R.id.zoom_out_button).setEnabled(true);
            findViewById(R.id.zoom_in_button).setEnabled(true);
        } else if (currentZoomLevel == 4) {
            findViewById(R.id.compass_background_4).setVisibility(View.VISIBLE);
            findViewById(R.id.compass_background).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_2).setVisibility(View.INVISIBLE);
            findViewById(R.id.compass_background_3).setVisibility(View.INVISIBLE);
            findViewById(R.id.zoom_out_button).setEnabled(false);
            findViewById(R.id.zoom_in_button).setEnabled(true);
        }
    }

    public void ZoomInClick(View view) {
        currentZoomLevel--;
        setValidZoomLevel();
        setZoomLevel();
        System.out.println("ZoomInClick: " + currentZoomLevel);
        displayCircle.restartObservers(new ZoomLevel(currentZoomLevel));
    }

    public void ZoomOutClick(View view) {
        currentZoomLevel++;
        setValidZoomLevel();
        setZoomLevel();
        System.out.println("ZoomOutClick: " + currentZoomLevel);
        displayCircle.restartObservers(new ZoomLevel(currentZoomLevel));
    }

    public void onEnterNameClicked(View view) {
        Intent intent = new Intent(this, EnterNameActivity.class);
        startActivity(intent);
    }

    public LiveData<Integer> checkGPSStatus() {
        ourLocationService = new LocationService(this);

        var executor = Executors.newSingleThreadScheduledExecutor();
        MutableLiveData<Integer> timeOnlineData = new MutableLiveData<>();
        poller = executor.scheduleAtFixedRate(() -> {
            long milliSecsSinceGPS = ourLocationService.lastFix();
            System.out.println("MS_GPS_STST" + milliSecsSinceGPS);
            int minSinceGPS = (int) milliSecsSinceGPS / 60000;
            timeOnlineData.postValue(minSinceGPS);
        }, 0, 3000, TimeUnit.MILLISECONDS);

        MediatorLiveData<Integer> timeData = new MediatorLiveData<>();
        timeData.addSource(timeOnlineData, timeData::postValue);

        return timeData;

    }

    public void onEnterUrlClick(View view) {
        Intent intent = new Intent(this, EnterUrlActivity.class);
        startActivity(intent);
    }
}
