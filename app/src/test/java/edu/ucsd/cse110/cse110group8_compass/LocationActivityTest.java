package edu.ucsd.cse110.cse110group8_compass;
import static org.junit.Assert.assertEquals;

import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LocationActivityTest {

    @Test
    public void test_one_plus_equals_two() {
        ActivityScenario<LocationActivity> scenario = ActivityScenario.launch(LocationActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            TextView latText = (TextView)activity.findViewById(R.id.latitudeText);
            latText.setText("");
            TextView longText = (TextView)activity.findViewById(R.id.longitudeText);
            latText.setText("");
            activity.findViewById(R.id.nextButton).performClick();
            assertEquals((activity.findViewById(R.id.ErrorText)).getVisibility(), 0);
        });


    }

}
