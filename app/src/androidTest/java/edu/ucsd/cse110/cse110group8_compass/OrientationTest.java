package edu.ucsd.cse110.cse110group8_compass;

import static org.junit.Assert.assertEquals;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

public class OrientationTest {

    @Test
    public void testOnline() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            // DisplayCircle displayCircle = new DisplayCircle(activity.findViewById(R.id.compass), )
        });

    }

}
