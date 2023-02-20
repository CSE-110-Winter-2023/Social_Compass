package edu.ucsd.cse110.cse110group8_compass;

import android.app.Activity;
import android.util.Pair;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;

//DisplayCircle coordinates all the pins and makes them visible or not
public class  DisplayCircle {
     static private ConstraintLayout circle_constraint;

     static private LiveData<Pair<Double, Double>> userCoordinateLive;
     static private Pin pinList[];
     static private LiveData<Float> azimuth;
     static private Activity activity;
     static private boolean validPins[];
     static private boolean populatedPins[];
     static private int numOfPins;

     DisplayCircle (ConstraintLayout circle_m, Pin northPin,Activity activity, LiveData<Float> azimuth, LiveData<Pair<Double, Double>> userCoordinateLive) {
          this.circle_constraint = circle_m;
          this.activity = activity;
          this.azimuth = azimuth;
          this.userCoordinateLive = userCoordinateLive;

          pinList = new Pin[4];
          validPins = new boolean[]{false, false, false, false};
          populatedPins = new boolean[]{false, false, false, false};
          pinList[0] = northPin;
          populatedPins[0] = true;
          numOfPins = 1;
          rotateAllPins();
     }

     private boolean checkNullPins() {
          boolean valid = true;

          for(int i = 0; i < 4; i++) {
               if(populatedPins[i] == true){
                    if(pinList[i].getPinTextView() == null) {
                         valid = false;
                         System.out.println("NULL VALUE:" + i);
                         validPins[i] = false;
                    }
                    else {
                         valid = true;
                         validPins[i] = true;
                    }
               }
          }
          return valid;
     }



     public boolean setPinList(ArrayList<Pin> pinArray) {
          numOfPins = 0;
          if(pinArray.size() <= 4 ) {
               for(int i = 0; i < pinArray.size(); i++) {
                    pinList[i] = pinArray.get(i);
                    populatedPins[i] = true;
                    numOfPins++;
               }
               rotateAllPins();
               return true;
          }
          else {
               System.out.println("too many values");
               return false;
          }
     }

     public boolean addPin(Pin newPin) {
          if(numOfPins < 4 && populatedPins[0] == true ) {
               pinList[numOfPins] = newPin;
               populatedPins[numOfPins] = true;
               rotateAllPins();
               numOfPins++;
               return true;
          }
          else {
               return false;
          }
     }

     public int size(){
          return numOfPins;
     }

     public Pin[] getPinList(){
          return pinList;
     }


     private void rotateAllPins() {
          checkNullPins();
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
                               rotator.move(targetPin.getPinTextView(), pinAngle );


                          }
                     });
                }
           });
     }

}
