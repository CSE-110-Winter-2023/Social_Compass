package edu.ucsd.cse110.cse110group8_compass;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;


public class CompassZoomInOutTest {
    @Test
    public void testZoomIn() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.setCurrentZoomLevel(3);
            Button zoomInButton = (Button) activity.findViewById(R.id.zoom_in_button);
            zoomInButton.performClick();
            assertEquals(2, activity.getCurrentZoomLevel());
            assertEquals(View.INVISIBLE, activity.findViewById(R.id.compass_background).getVisibility());
            assertEquals(View.VISIBLE, activity.findViewById(R.id.compass_background_2).getVisibility());
            assertEquals(View.INVISIBLE, activity.findViewById(R.id.compass_background_3).getVisibility());
            assertEquals(View.INVISIBLE, activity.findViewById(R.id.compass_background_4).getVisibility());
        });
    }

    @Test
    public void testZoomOut() {

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.setCurrentZoomLevel(3);
            Button zoomOutButton = (Button) activity.findViewById(R.id.zoom_out_button);
            zoomOutButton.performClick();
            assertEquals(activity.getCurrentZoomLevel(), 4);
            assertEquals(View.INVISIBLE, activity.findViewById(R.id.compass_background).getVisibility());
            assertEquals(View.INVISIBLE, activity.findViewById(R.id.compass_background_2).getVisibility());
            assertEquals(View.INVISIBLE, activity.findViewById(R.id.compass_background_3).getVisibility());
            assertEquals(View.VISIBLE, activity.findViewById(R.id.compass_background_4).getVisibility());
        });
    }

    @Test
    public void testGreyOutMaxZoomed() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.setCurrentZoomLevel(1);
            Button zoomInButton = (Button) activity.findViewById(R.id.zoom_in_button);
            Button zoomOutButton = (Button) activity.findViewById(R.id.zoom_out_button);

            assertEquals(false, zoomInButton.isEnabled());
            assertEquals(true, zoomOutButton.isEnabled());

        });
    }

    @Test
    public void testGreyOutMinZoomed() {

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            activity.setCurrentZoomLevel(3);
            Button zoomInButton = (Button) activity.findViewById(R.id.zoom_in_button);
            Button zoomOutButton = (Button) activity.findViewById(R.id.zoom_out_button);
            zoomOutButton.performClick();
            assertEquals(true, zoomInButton.isEnabled());
            assertEquals(false, zoomOutButton.isEnabled());

        });
    }


}
