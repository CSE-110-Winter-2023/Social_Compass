package edu.ucsd.cse110.cse110group8_compass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ContactCreationActivity extends AppCompatActivity {
    ArrayList<String> publicCodeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        Gson gson = new Gson();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        appSharedPrefs.getAll();
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        String json = appSharedPrefs.getString("publicCodeList", "");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        publicCodeList = gson.fromJson(json, type);

        if(publicCodeList == null){
            publicCodeList = new ArrayList<>();
        }

    }

    public void onCreateContactButtonClicked(View view){
        TextView uuid = findViewById(R.id.uuid_text);
        String text = uuid.getText().toString();

        if(text.isEmpty()){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Cannot leave blank");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        else{
            publicCodeList.add(text);

            Gson gson = new Gson();
            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            String jsonToRet = gson.toJson(publicCodeList);
            prefsEditor.putString("publicCodeList", jsonToRet);
            prefsEditor.commit();

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }

    }



}
