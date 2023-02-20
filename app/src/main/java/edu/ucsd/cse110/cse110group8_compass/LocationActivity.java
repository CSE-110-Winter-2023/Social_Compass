package edu.ucsd.cse110.cse110group8_compass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
    }

    public void onNextClick(View view) {
        // SharedPreferences preferences = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor = preferences.edit();

        TextView error = findViewById(R.id.ErrorText);


        TextView latitude = findViewById(R.id.latitudeText);
        TextView longitude = findViewById(R.id.longitudeText);
        TextView label = findViewById(R.id.LabelTextView);

        System.out.println("latitude: " + latitude.getText().toString());
        System.out.println("longitude: " + longitude.getText().toString());
        Log.i("NumberGenerated", "BEFORE");
        if(latitude.getText().toString().isEmpty()){
            Log.i("NumberGenerated", "Function has generated zero.");
            error.setVisibility(View.VISIBLE);
        }
        else if(longitude.getText().toString().isEmpty()){
            Log.i("NumberGenerated", "Function has generated zero.");
            error.setVisibility(View.VISIBLE);
        }
        else{
            error.setVisibility(View.INVISIBLE);

            double longt = Double.parseDouble(((EditText)findViewById(R.id.longitudeText)).getText().toString());
            double latt = Double.parseDouble(((EditText)findViewById(R.id.latitudeText)).getText().toString());
            String lab = ((EditText) findViewById(R.id.LabelTextView)).getText().toString();

            Pin p = new Pin(lab,longt,latt);
            Gson gson = new Gson();

            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getApplicationContext());
            String json = appSharedPrefs.getString("pinList", "");
            Type type = new TypeToken<List<Pin>>(){}.getType();
            List<Pin> pinList = gson.fromJson(json, type);


            pinList = new ArrayList<Pin>();


            pinList.add(p);
            Log.i("pinlist size", ""+pinList.size());
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            String jsonToRet = gson.toJson(pinList);
            prefsEditor.putString("pinList", jsonToRet);
            prefsEditor.commit();

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();



        }
    }

    public void onCancelClick(View view) {
        finish();

    }
}
