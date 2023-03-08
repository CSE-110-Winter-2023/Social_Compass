package edu.ucsd.cse110.cse110group8_compass;

import static org.junit.Assert.assertEquals;

import android.location.LocationManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

<<<<<<< HEAD

=======
>>>>>>> 75d04baa0d854e0fb33097e6ef425a3130d92d90
@RunWith(RobolectricTestRunner.class)
public class DistanceCalculatorTest {

    @Test
    public void TestCalculateDistance(){

        LocationManager lm;

<<<<<<< HEAD
        DistanceCalculator dc = new DistanceCalculator(40.689202777778, -74.044219444444, true);
=======
        DistanceCalculator dc = new DistanceCalculator(40.689202777778, -74.044219444444);
>>>>>>> 75d04baa0d854e0fb33097e6ef425a3130d92d90
        assertEquals(201.65, dc.calculateMockDistance(38.889069444444, -77.034502777778), 0.1);

    }
}
