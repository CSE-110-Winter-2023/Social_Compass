package edu.ucsd.cse110.cse110group8_compass;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.cse110group8_compass.model.UUID;


public class PinBuilderTest {
    @Test
    public void testValidPinBuilder() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            ConstraintLayout layout = (ConstraintLayout) activity.findViewById(R.id.compass);
            float density = activity.getResources().getDisplayMetrics().density;
            Pin pin = new PinBuilder(activity, layout, density).config().withCoordinates(30.0, 30.0).withLabel("test label").build();

            assertEquals((double)pin.latitude, 30.0, 0.1);
            assertEquals((double)pin.longitude, 30.0, 0.1);
            assertEquals(pin.label, "test label");

        });
    }
}
