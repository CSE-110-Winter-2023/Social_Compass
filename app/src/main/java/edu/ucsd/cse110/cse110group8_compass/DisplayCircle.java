package edu.ucsd.cse110.cse110group8_compass;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.util.Pair;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

//DisplayCircle coordinates all the pins and makes them visible or not
public class  DisplayCircle {
     static private ConstraintLayout circle_constraint;
     static private LiveData<Pair<Double, Double>> userCoordinateLive;
     static private Pin pinList[];
     static private LiveData<Float> azimuth;
     static private Activity activity;
     static private boolean validPins[];
     static private int numOfPins;

     DisplayCircle (ConstraintLayout circle_m, Pin northPin,Activity activity, LiveData<Float> azimuth, LiveData<Pair<Double, Double>> userCoordinateLive) {
          this.circle_constraint = circle_m;
          this.activity = activity;
          this.azimuth = azimuth;
          this.userCoordinateLive = userCoordinateLive;

          pinList = new Pin[4];
          validPins = new boolean[]{false, false, false, false};
          pinList[0] = northPin;
          validPins[0] = true;
          numOfPins = 1;
          rotateAllPins();
     }

     public boolean addPin(Pin newPin) {
          if(numOfPins < 4 && validPins[0] == true ) {
               pinList[numOfPins] = newPin;
               validPins[numOfPins] = true;
               rotateAllPins();
               numOfPins++;
               return true;
          }
          else {
               return false;
          }
     }




     /*private void setUserCoordinate(LiveData<Pair<Double, Double>> userCoordinateLive) {
          this.userCoordinateLive = userCoordinateLive;
     }*/

     private void rotateAllPins() {
          for(int i = 0; i < 4;i++ ) {
               if(validPins[i] == true) {
                    rotatePin(pinList[i], azimuth, activity);
               }
          }
     }

     private void rotatePin(Pin targetPin, LiveData<Float> targetAzimuth, Activity activity) {
           userCoordinateLive.observe((LifecycleOwner) activity, new Observer<Pair<Double, Double>>() {
                @Override
                public void onChanged(Pair<Double, Double> doubleDoublePair) {
                     AngleCalculator angleCalculator = new AngleCalculator(doubleDoublePair.first, doubleDoublePair.second);

                     targetAzimuth.observe((LifecycleOwner) activity, new Observer<Float>() {
                          @Override
                          public void onChanged(Float value) {
                               Float pinAngle = angleCalculator.angleOnCircle(targetPin.getLatitude(), targetPin.getLongitude(), value).floatValue();
                               Rotator rotator = new Rotator();
                               rotator.move(targetPin.getPinImageView(), pinAngle );

                          }
                     });
                }
           });
     }

     //These two need to go into its own Rotator class

}
