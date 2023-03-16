package edu.ucsd.cse110.cse110group8_compass;

import android.app.Activity;
import android.util.Pair;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;

//DisplayCircle coordinates all the pins and makes them visible or not
public class  DisplayCircle {
     private ConstraintLayout circle_constraint;


     private LiveData<Pair<Double, Double>> userCoordinateLive;
     //private Pin pinList[];
     private LiveData<Float> azimuth;
     private Activity activity;
     private boolean validPins[];
     private boolean populatedPins[];
     private int numOfPins;

     private final int default_zoom_level = 2;

     private PinList pinList;

     DisplayCircle (ConstraintLayout circle_m, Pin northPin,Activity activity, LiveData<Float> azimuth, LiveData<Pair<Double, Double>> userCoordinateLive) {
          this.circle_constraint = circle_m;
          this.activity = activity;
          this.azimuth = azimuth;
          this.userCoordinateLive = userCoordinateLive;
          this.pinList = new PinList();
          this.pinList.addPin(northPin);

          ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) circle_constraint.getLayoutParams();
          int val = layoutParams.circleRadius;
          //pinList = new Pin[4];
         // validPins = new boolean[]{false, false, false, false};
         // populatedPins = new boolean[]{false, false, false, false};
          //pinList.addPin(northPin);
          //populatedPins[0] = true;
         // numOfPins = 1;
          rotateAllPins();
          setAllPinZones(new ZoomLevel(default_zoom_level));
     }


     public void setPinList(ArrayList<Pin> pinArray, ZoomLevel currentZoomLevel) {
          pinList.setPinList(pinArray);
          rotateAllPins();
          setAllPinZones(currentZoomLevel);
     }



     public void setPinList(PinList newPinList) {
          pinList = newPinList;
     }


     public int size(){
          return pinList.size();
     }

     public PinList getPinList(){
          return pinList;
     }


     private void rotateAllPins() {
          for(int i = 0; i < pinList.size();i++ ) {
               if(pinList.getPin(i).checkValid() == true) {
                    rotatePin(pinList.getPin(i), azimuth, activity);
               }
          }
     }

     public void setAllPinZones(ZoomLevel zoom) {
          for(int i = 0; i < pinList.size();i++ ) {
               if(pinList.getPin(i).checkValid() == true) {
                    setPinZone(pinList.getPin(i), zoom, activity);
               }
          }
     }

     public void restartObservers( ZoomLevel zoomLevel) {
          userCoordinateLive.removeObservers((LifecycleOwner) activity);
          setAllPinZones(zoomLevel);
          rotateAllPins();
     }


     private void setPinZone(Pin targetPin, ZoomLevel zoomLevel, Activity activity) {
          userCoordinateLive.observe((LifecycleOwner) activity, new Observer<Pair<Double, Double>>() {
               @Override
               public void onChanged(Pair<Double, Double> doubleDoublePair) {
                    DistanceCalculator distanceCalculator = new DistanceCalculator(doubleDoublePair.first, doubleDoublePair.second);
                    //DistanceCalculator distanceCalculator = new DistanceCalculator(32.8801, -117.2340);
                    Double miles = distanceCalculator.calculateDistance(targetPin.getLatitude(), targetPin.getLongitude());
                    //Double miles = distanceCalculator.calculateDistance(32.596280, -115.870056);

                    //miles = 0.5;
                    System.out.println("For: " + targetPin.getLabel() + " miles: "+ miles);

                    int radiusConstraint = zoomLevel.getRadius(miles);
                    System.out.println("For: " + targetPin.getLabel() + " rad: "+ radiusConstraint);
                    //System.out.println("radC: " + radiusConstraint);

                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) targetPin.getPinTextView().getLayoutParams();
                    layoutParams.circleRadius = (int) (radiusConstraint * activity.getResources().getDisplayMetrics().density);
                    targetPin.getPinTextView().setLayoutParams(layoutParams);


               }
          });
     }


     private void rotatePin(Pin targetPin, LiveData<Float> targetAzimuth, Activity activity) {

          ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) targetPin.getPinTextView().getLayoutParams();
          TextView nTV = new TextView(activity);
          nTV.setLayoutParams(layoutParams);
          Rotator nrotator = new Rotator();

          nrotator.move(nTV, 170F);
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
