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

import edu.ucsd.cse110.cse110group8_compass.model.UUID;

@RunWith(AndroidJUnit4.class)

public class enterUUIDTests {
    @Test
    public void testAddValidUUID(){

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            UUID forTest = new UUID("4444");
            int initialSize = activity.pinList.size();
            activity.pinObserver(forTest);
            int updatedSize = activity.pinList.size();
            assertEquals(updatedSize, initialSize + 1);

        });
    }

    @Test
    public void testBlankUUID(){

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            UUID blank = new UUID(null);
            int initialSize = activity.pinList.size();
            activity.pinObserver(blank);
            int secondSize = activity.pinList.size();
            assertEquals(secondSize, initialSize);

            UUID forTest = new UUID("4444");
            activity.pinObserver(forTest);
            int finalSize = activity.pinList.size();
            assertEquals(finalSize, initialSize + 1);

        });

    }
}
