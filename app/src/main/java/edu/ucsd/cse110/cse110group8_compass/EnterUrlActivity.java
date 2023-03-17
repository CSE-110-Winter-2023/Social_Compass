package edu.ucsd.cse110.cse110group8_compass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EnterUrlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_url);
    }

    public void onsubmitUrlClick(View view) {
        SharedPreferences prefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String url = ((EditText)findViewById(R.id.mockUrl)).getText().toString();
        editor.putString("mockUrl", url).commit();

        Intent intent = new Intent();
        finish();
    }
}