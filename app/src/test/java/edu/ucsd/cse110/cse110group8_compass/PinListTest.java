package edu.ucsd.cse110.cse110group8_compass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.app.Activity;
import android.util.Pair;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

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

    @Test
    public void testAddPinInvalid(){
        PinList pl = new PinList();
        int sizeBefore = pl.size();

        Pin pin = new Pin();

        assertEquals(pl.addPin(pin), false);
        assertEquals(pl.size(), sizeBefore);
    }
}
