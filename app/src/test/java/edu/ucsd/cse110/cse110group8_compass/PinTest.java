package edu.ucsd.cse110.cse110group8_compass;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;

public class PinTest {

    @Test
    public void testCheckoutPinValid(){
        Activity activity = new Activity();
        Pin pin = new Pin();
        pin.setPinTextView(new TextView(activity));
        pin.setLocation(0.0, 0.0);
        pin.setLabel("test");

        assertEquals(true, pin.checkValid());
    }

    @Test
    public void testCheckoutPinInvalid(){
        Activity activity = new Activity();
        Pin pin = new Pin();

        assertEquals(false, pin.checkValid());
    }
}
