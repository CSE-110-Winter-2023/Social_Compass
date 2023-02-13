package edu.ucsd.cse110.cse110group8_compass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("BACK IN MAIN");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Intent intent = getIntent();
            System.out.println("IN HERE");
            String name = intent.getStringExtra("label");
            String latit = intent.getStringExtra("latitude");
            String longit = intent.getStringExtra("longitude");
            System.out.println("fromintent: " + name + latit + longit);
            TextView textView = (TextView) findViewById(R.id.Parent);
            textView.setText(name + ": " + latit + ", " + longit);
            System.out.println("FINISHED");
        }
    }

    public void onChangeLabelClick(View view) {
        Intent intent = new Intent(this, Label.class);
        startActivity(intent);
    }

}