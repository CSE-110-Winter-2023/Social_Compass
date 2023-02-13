package edu.ucsd.cse110.cse110group8_compass;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import org.junit.Test;

public class AngleCalculatorTest {
    AngleCalculator testCalc = new AngleCalculator(90.0000, 135.0000);

     @Test
    public void test_convertToDegree_0() {
         assertEquals(0, testCalc.convertToDegree( 0.0F), 0.001);
     }

     @Test
    public void test_convertToDegree_1() {
        assertEquals(180, testCalc.convertToDegree((float) Math.PI), 0.001);
    }

    @Test
    public void test_convertToDegree_2() {
        assertEquals(90, testCalc.convertToDegree((float) Math.PI / 2), 0.001);
    }

    @Test
    public void test_convertToDegree_3() {
        assertEquals(270, testCalc.convertToDegree((float) - Math.PI / 2), 0.001);
    }

    @Test
    public void test_convertToDegree_4() {
        assertEquals(114.592, testCalc.convertToDegree((float) 2), 0.001);
    }

    @Test
    public void test_convertToDegree_5() {
        assertEquals(360 - 114.592, testCalc.convertToDegree((float) - 2), 0.001);
    }

    /*
    @Test
    public void test_convertToDegree_6() {
        assertEquals(181.52, testCalc.convertToDegree(1.0F), 0.01);
    }

    @Test
    public void test_convertToDegree_7() {
        assertEquals(360 - 181.52, testCalc.convertToDegree(- 3.0F), 0.01);
    }
    */

    //AngleCalculator testAngleCalc = new AngleCalculator(66.09229522345977, 2.259528661085639);
   // @Test
    //public void test_angleOnCircle() {
        //assertEquals(0,testAngleCalc.northAngle(), 0.001);
    //    assertEquals(48.037, testAngleCalc.angleOnCircle(28.90719090233516, -0.7329190625000237, 0.0F), 0.001);
    //}



}
