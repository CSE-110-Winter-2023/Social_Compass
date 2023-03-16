package edu.ucsd.cse110.cse110group8_compass;

import static org.junit.Assert.assertEquals;

import android.location.LocationManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DistanceCalculatorTest {

    @Test
    public void TestCalculateDistance(){

        LocationManager lm;

        DistanceCalculator dc = new DistanceCalculator(40.689202777778, -74.044219444444);
        assertEquals(201.65, dc.calculateMockDistance(38.889069444444, -77.034502777778), 0.5);

    }

    @Test
    public void TestCalculateDistance_1(){

        LocationManager lm;

        DistanceCalculator dc = new DistanceCalculator(35, 135);
        assertEquals(688.40256375, dc.calculateMockDistance(25, 135), 0.5);

    }

    @Test
    public void TestCalculateDistance_2(){

        LocationManager lm;

        DistanceCalculator dc = new DistanceCalculator(-35, 12.19444444);
        assertEquals(7101.172881, dc.calculateMockDistance(24, -77.034502777778), 0.5);

    }

    @Test
    public void TestCalculateDistance_3(){

        LocationManager lm;

        DistanceCalculator dc = new DistanceCalculator(-35, 12.19444444);
        assertEquals(7101.172881, dc.calculateMockDistance(24, -77.034502777778), 0.5);

    }

    @Test
    public void TestCalculateDistance_4(){

        LocationManager lm;

        DistanceCalculator dc = new DistanceCalculator(3, 3);
        assertEquals(137.99, dc.calculateMockDistance(3, 5), 0.5);

    }
    @Test
    public void TestCalculateDistance_5(){

        LocationManager lm;

        DistanceCalculator dc = new DistanceCalculator(3, 3);
        assertEquals(137.99, dc.calculateMockDistance(3, 5), 0.5);

    }


    @Test
    public void TestCalculateDistance_6(){

        LocationManager lm;

        DistanceCalculator dc = new DistanceCalculator(4, 4);
        assertEquals(137.99, dc.calculateMockDistance(4, 6), 0.5);

    }



}
