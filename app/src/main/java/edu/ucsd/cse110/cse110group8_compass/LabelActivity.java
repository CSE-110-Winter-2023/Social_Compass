package edu.ucsd.cse110.cse110group8_compass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LabelActivity extends AppCompatActivity {

    int label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);
    }

    public void setInt(int x){
        this.label = x;
    }

    public void onSubmitClick(View view) {
        // Intent intent = new Intent(this, MainActivity.class);
        TextView label = findViewById(R.id.labelText);
        String newLabel = label.getText().toString();

        SharedPreferences prefs = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("name", newLabel);
        editor.commit();

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        // intent.putExtra("label", newLabel);
        // startActivity(intent);
        finish();
    }
}