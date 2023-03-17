package edu.ucsd.cse110.cse110group8_compass;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.widget.TextView;

import org.junit.Test;

public class PinListTest {

    @Test
    public void testAddPinValid(){
        PinList pl = new PinList();
        int sizeBefore = pl.size();

        Activity activity = new Activity();
        Pin pin = new Pin();
        pin.setPinTextView(new TextView(activity));
        pin.setLocation(0.0, 0.0);
        pin.setLabel("test");

        assertEquals(pl.addPin(pin), true);

        assertEquals(pl.size(), sizeBefore + 1);
    }
}
