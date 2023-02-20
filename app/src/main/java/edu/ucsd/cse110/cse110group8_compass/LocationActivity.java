package edu.ucsd.cse110.cse110group8_compass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
            // extract data from User input
            double longt = Double.parseDouble(((EditText)findViewById(R.id.longitudeText)).getText().toString());
            double latt = Double.parseDouble(((EditText)findViewById(R.id.latitudeText)).getText().toString());
            String lab = ((EditText) findViewById(R.id.LabelTextView)).getText().toString();

            // extract current pins from gson object and set to pinList
            Gson gson = new Gson();
            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getApplicationContext());
            String json = appSharedPrefs.getString("pinList", "");
            Type type = new TypeToken<List<Pin>>(){}.getType();
            List<Pin> pinList = gson.fromJson(json, type);

            TextView pinView;

            switch ( pinList.size() ){
                case 1:
                    pinView = findViewById(R.id.pin_one);
                case 2:
                    pinView = findViewById(R.id.pin_two);
                case 3:
                    pinView = findViewById(R.id.pin_three);
                default:
                    pinView = findViewById(R.id.north_pin);
            }

            // create new pin object to be added to pinList
            Pin p = new Pin(lab,longt,latt,pinView);

            // add newly created pin to pinList
            pinList.add(p);
            Log.i("pinlist size", ""+pinList.size());

            // inject updated pinList into gson object to transfer to main
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            String jsonToRet = gson.toJson(pinList);
            prefsEditor.putString("pinList", jsonToRet);
            prefsEditor.commit();

            // return to main with updated pinList
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    }

    public void onCancelClick(View view) {
        // Do nothing and return to main
        finish();
    }
}
