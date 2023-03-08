package edu.ucsd.cse110.cse110group8_compass;

import static org.junit.Assert.assertEquals;

import android.location.LocationManager;

import org.junit.Test;

public class DistanceCalculatorTest {

    @Test
    public void TestCalculateDistance(){

        LocationManager lm;

        DistanceCalculator dc = new DistanceCalculator(40.689202777778, -74.044219444444, true);
        assertEquals(201.65, dc.calculateMockDistance(38.889069444444, -77.034502777778), 0.01);

    }
}
