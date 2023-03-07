package edu.ucsd.cse110.cse110group8_compass;

import static org.junit.Assert.assertEquals;

import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


public class CompassZoomInOutTest {
        /*
        @Test
        public void testZoomIn() {

            ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.moveToState(Lifecycle.State.STARTED);

            scenario.onActivity(activity -> {
                    activity.setCurrentZoomLevel(3);
                    Button zoomInButton = (Button)activity.findViewById(R.id.zoom_in_button);
                    zoomInButton.performClick();
                    assertEquals(activity.getCurrentZoomLevel(), 2);
                    assertEquals(activity.findViewById(R.id.compass_background_2).getVisibility(),0);
                    assertEquals(activity.findViewById(R.id.compass_background).getVisibility(),1);
                    assertEquals(activity.findViewById(R.id.compass_background_3).getVisibility(),0);
                    assertEquals(activity.findViewById(R.id.compass_background_4).getVisibility(),1);
            });
    }
        @Test
        public void testZoomOut() {

                ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
                scenario.moveToState(Lifecycle.State.CREATED);
                scenario.moveToState(Lifecycle.State.STARTED);

                scenario.onActivity(activity -> {
                        activity.setCurrentZoomLevel(3);
                        Button zoomInButton = (Button)activity.findViewById(R.id.zoom_out_button);
                        zoomInButton.performClick();
                        assertEquals(activity.getCurrentZoomLevel(), 4);
                        assertEquals(activity.findViewById(R.id.compass_background_2).getVisibility(),0);
                        assertEquals(activity.findViewById(R.id.compass_background).getVisibility(),0);
                        assertEquals(activity.findViewById(R.id.compass_background_3).getVisibility(),0);
                        assertEquals(activity.findViewById(R.id.compass_background_4).getVisibility(),0);
                });
        }

         */
}
