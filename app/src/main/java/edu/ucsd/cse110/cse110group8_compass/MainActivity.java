package edu.ucsd.cse110.cse110group8_compass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView pin1 = new ImageView(this);
        pin1.setImageResource(R.drawable.pindrop);

        ConstraintLayout compassLayout = (ConstraintLayout) findViewById(R.id.compass);
        ConstraintSet c = new ConstraintSet();
        c.clone(compassLayout);
        c.constrainCircle(pin1.getId(), R.id.compass, 40, 180);
        c.applyTo(compassLayout); // Apply back our ConstraintSet on ConstraintLayout.

        compassLayout.addView(pin1);
    }
}