package edu.ucsd.cse110.cse110group8_compass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EnterNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entername);
    }


    protected void onDestroy() {
        super.onDestroy();
        saveName();
    }

    public void saveName() {
        EditText nameView = findViewById(R.id.EnterNameText);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("name", nameView.getText().toString());
        myEdit.commit();
    }

    public void onSubmitClicked(View view) {
        finish();
    }

}
