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
        SharedPreferences preferences = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

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
            String lat = latitude.getText().toString();
            String longit = longitude.getText().toString();
            String name = label.getText().toString();


            Pin defaultPin = new Pin();
            Pin parent = new Pin("Testparent",Double.parseDouble(lat),
                    Double.parseDouble(longit));

            editor.putString("name", name);
            editor.putString("latitude", lat);
            editor.putString("longitude", longit);
            editor.commit();

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    }

    public void onCancelClick(View view) {
        finish();

    }
}