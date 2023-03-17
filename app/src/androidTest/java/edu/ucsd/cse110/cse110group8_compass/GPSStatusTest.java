package edu.ucsd.cse110.cse110group8_compass;

import static org.junit.Assert.assertEquals;

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

@RunWith(AndroidJUnit4.class)
public class GPSStatusTest {

    @Test
    public void testOnline() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.GPSTime(0);
            ImageView onlineButton = activity.findViewById(R.id.online);
            ImageView offlineButton = activity.findViewById(R.id.offline);
            assertEquals(View.VISIBLE, onlineButton.getVisibility());
            assertEquals(View.INVISIBLE, offlineButton.getVisibility());

        });

    }

    @Test
    public void testOffline() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.GPSTime(2);
            ImageView onlineButton = activity.findViewById(R.id.online);
            ImageView offlineButton = activity.findViewById(R.id.offline);
            assertEquals(View.INVISIBLE, onlineButton.getVisibility());
            assertEquals(View.VISIBLE, offlineButton.getVisibility());

        });
    }

    @Test
    public void testOfflineShowMins() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.GPSTime(2);
            ImageView offlineButton = activity.findViewById(R.id.offline);
            assertEquals(View.VISIBLE, offlineButton.getVisibility());

            TextView offlineIndicator = activity.findViewById(R.id.timeOffline);
            assertEquals(offlineIndicator.getText().toString(),
                    "" + activity.timeWithoutGPS + " min" );
        });
    }

}
