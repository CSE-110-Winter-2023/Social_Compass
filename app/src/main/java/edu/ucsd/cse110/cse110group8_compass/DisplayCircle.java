package edu.ucsd.cse110.cse110group8_compass;

import android.app.Activity;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//DisplayCircle coordinates all the pins and makes them visible or not
public class  DisplayCircle {
     private ConstraintLayout circle_constraint;

     private final String northPinPublicCode = "qtgmI&@3$zH!us*X5!YKi&b1aWhijR5HMe&ruxJ6mxG5Fx#EcL$ou" +
             "SiaGMP*0oMGH&tnju36*K*qxaR%&iL20@5BFdpc0m^bhBoR";
     private LiveData<Pair<Double, Double>> userCoordinateLive;
     //private Pin pinList[];
     private LiveData<Float> azimuth;

     private float density;
     private Activity activity;
     private boolean validPins[];
     private boolean populatedPins[];
     private int numOfPins;
     private HashMap<Pin, Float > angleMap = new HashMap<>();
     private HashMap<Pin, Integer > positionMap = new HashMap<>();
    // private ArrayList<>

     private final int default_zoom_level = 2;

     private PinList pinList;

     DisplayCircle (ConstraintLayout circle_m, Pin northPin,Activity activity, LiveData<Float> azimuth, LiveData<Pair<Double, Double>> userCoordinateLive) {
          this.circle_constraint = circle_m;
          this.activity = activity;
          this.azimuth = azimuth;
          this.userCoordinateLive = userCoordinateLive;
          this.pinList = new PinList();
          this.pinList.addPin(northPin);
          this.density = activity.getResources().getDisplayMetrics().density;


          ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) circle_constraint.getLayoutParams();
          int val = layoutParams.circleRadius;
          //pinList = new Pin[4];
         // validPins = new boolean[]{false, false, false, false};
         // populatedPins = new boolean[]{false, false, false, false};
          //pinList.addPin(northPin);
          //populatedPins[0] = true;
         // numOfPins = 1;
          setPositionMap();
          rotateAllPins(new ZoomLevel(default_zoom_level));
          setAllPinZones(new ZoomLevel(default_zoom_level));
     }

     private void setPositionMap() {
          for(int i = 0; i < pinList.size(); i++) {
               positionMap.put(pinList.getPin(i), i);
          }
     }

     private void clearPositionMap() {
          positionMap.clear();
     }


     public void setPinList(ArrayList<Pin> pinArray, ZoomLevel currentZoomLevel) {
          pinList.setPinList( pinArray);
          restartObservers(currentZoomLevel);
     }



     //public void setPinList(PinList newPinList) {
      //    pinList = newPinList;
    // }


     public int size(){
          return pinList.size();
     }

     public PinList getPinList(){
          return pinList;
     }


     private void rotateAllPins( ZoomLevel zoomLevel) {
          for(int i = 0; i < pinList.size();i++ ) {
               if(pinList.getPin(i).checkValid() == true) {
                    rotatePin(pinList.getPin(i), azimuth, activity, zoomLevel);
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
          clearPositionMap();
          setPositionMap();
          setAllPinZones(zoomLevel);
          rotateAllPins( zoomLevel);
     }


     private void setPinZone(Pin targetPin, ZoomLevel zoomLevel, Activity activity) {
          userCoordinateLive.observe((LifecycleOwner) activity, new Observer<Pair<Double, Double>>() {
               @Override
               public void onChanged(Pair<Double, Double> doubleDoublePair) {
                    DistanceCalculator distanceCalculator = new DistanceCalculator(doubleDoublePair.first, doubleDoublePair.second);
                    Double miles = distanceCalculator.calculateDistance(targetPin.getLatitude(), targetPin.getLongitude());

                    //miles = 0.5;
                    System.out.println("For: " + targetPin.getLabel() + " miles: "+ miles);

                    int radiusConstraint = zoomLevel.getRadius(miles);
                    System.out.println("For: " + targetPin.getLabel() + " rad: "+ radiusConstraint);



                    //check if pin is a north pin
                    //if not northpin, normal zone,
                    //otherwise set the radius to the outside so north pin is always there
                    if(targetPin.getPublic_code() != northPinPublicCode) {
                         if(zoomLevel.onEdge(miles)) {
                              TextView targetPinTextView = targetPin.getPinTextView();
                              targetPinTextView.setText("");
                              targetPinTextView.setBackgroundResource(R.drawable.offline2);
                         }
                         else {
                              TextView targetPinTextView = targetPin.getPinTextView();
                              targetPinTextView.setText(targetPin.label);
                              targetPinTextView.setBackgroundResource(android.R.color.transparent);
                         }

                         //collisionPinseperation(int numOfPinsInSector, ZoomLevel zoomLevel , Double miles)
                         int separation = collisionPinSeparation(numOfPinsInRange(targetPin), zoomLevel, miles);


                         ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) targetPin.getPinTextView().getLayoutParams();
                         layoutParams.circleRadius = (int) ((radiusConstraint + (separation * pinPosition(targetPin))) * activity.getResources().getDisplayMetrics().density);
                         targetPin.getPinTextView().setLayoutParams(layoutParams);
                    }
                    else {
                         ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) targetPin.getPinTextView().getLayoutParams();
                         layoutParams.circleRadius = (int) (180 * activity.getResources().getDisplayMetrics().density);
                         targetPin.getPinTextView().setLayoutParams(layoutParams);
                    }

               }
          });
     }
     // Pin pin = new PinBuilder(this, layout, density).config().withCoordinates(uuid.longitude, uuid.latitude).withLabel(uuid.label).build();
     //            currPins.put(uuid.public_code, pin);
     //            pinList.add(pin);
     //            displayCircle.setPinList(pinList, new ZoomLevel(currentZoomLevel));


     private int angleSector(Float angle) {
          return (int) Math.floor(angle / 10);
     }


     private void rotatePin(Pin targetPin, LiveData<Float> targetAzimuth, Activity activity , ZoomLevel zoomLevel) {

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

                              // curr edit
                               if(targetPin.getPublic_code() != northPinPublicCode) {
                                    angleMap.put(targetPin, pinAngle);

                                    int truncateSizeInt = truncateSize(targetPin, pinAngle);

                                    TextView targetTextView = targetPin.getPinTextView();

                                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) targetTextView.getLayoutParams();

                                    if(truncateSizeInt > 100 ) {
                                         layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                                    }
                                    else {
                                         layoutParams.width = truncateSizeInt;
                                    }
                                    //layoutParams.width = truncateSize;
                                    targetTextView.setLayoutParams(layoutParams);


                                   // targetTextView.setMaxWidth(truncateSize);
                                    targetTextView.setMaxLines(1);

                                    //targetPin.setPinTextView(targetTextView);



                               }


                          }
                     });
                }
           });
     }

     private int collisionPinSeparation(int numOfPinsInSector, ZoomLevel zoomLevel , Double miles) {
          Pair<Integer, Integer> limits = zoomLevel.stackZone(miles);
          Integer low = limits.first;
          Integer high = limits.second;

          int range = high - low;
          int pinSeparation = (range)/ numOfPinsInSector;

          System.out.println("collisionPinSeperation: " + pinSeparation );
          return pinSeparation;

     }

     private int numOfPinsInRange(Pin targetPin) {
          Float targetPinAngle = angleMap.get(targetPin);

          if(targetPinAngle == null) {
               return 1;
          }

          int cnt = 0;

          for (Float value : angleMap.values()) {
               if(Math.abs(targetPinAngle - value) <= 10) {
                    cnt++;
               }
          }
          System.out.println("numOfPinsInRange: " + cnt);
          return cnt;
     }

     private int pinPosition(Pin targetPin) {
          Float targetPinAngle = angleMap.get(targetPin);

          if(targetPinAngle == null) {
               return 1;
          }

          ArrayList<Pin> pinArr = new ArrayList<>();

          for (Map.Entry<Pin, Float> entry : angleMap.entrySet()) {
               Pin key = entry.getKey();
               Float value = entry.getValue();
               if(Math.abs(targetPinAngle - value) <= 10) {
                    pinArr.add(key);
               }
          }

          int posCnt = 1;
          Integer targetPinPos = positionMap.get(targetPin);



          for(int i = 0; i < pinArr.size(); i++) {
               if(targetPinPos < positionMap.get(pinArr.get(i))) {
                    posCnt++;
               }
          }

          return posCnt;
     }


     private int truncateSize(Pin targetPin, Float currentAngle) {
          if(numOfPinsInRange(targetPin) > 1) {
               if (currentAngle > 40 && currentAngle < 140) {

                    if (currentAngle > 60 && currentAngle < 120) {
                         System.out.println("truncaeSize: " + 16);

                         return 16;
                    }
                    System.out.println("truncaeSize: " + 32);

                    return 32;
               } else if (currentAngle > 40 + 180 && currentAngle < 140 + 180) {

                    if (currentAngle > 60 + 180 && currentAngle < 120 + 180) {
                         System.out.println("truncaeSize: " + 16);

                         return 16;
                    }
                    System.out.println("truncaeSize: " + 32);

                    return 32;
               }

          }
          System.out.println("truncaeSize: " + 10000);
          return 10000;
     }


     private void setPinZoneConflict(Pin targetPin, ZoomLevel zoomLevel, Activity activity, int numOfPinsInSector) {
          userCoordinateLive.observe((LifecycleOwner) activity, new Observer<Pair<Double, Double>>() {
               @Override
               public void onChanged(Pair<Double, Double> doubleDoublePair) {
                    DistanceCalculator distanceCalculator = new DistanceCalculator(doubleDoublePair.first, doubleDoublePair.second);
                    Double miles = distanceCalculator.calculateDistance(targetPin.getLatitude(), targetPin.getLongitude());

                    int radiusConstraint = zoomLevel.getRadius(miles);

                    //check if pin is a north pin
                    //if not northpin, normal zone,
                    //otherwise set the radius to the outside so north pin is always there
                    if(targetPin.getPublic_code() != northPinPublicCode) {
                         if(zoomLevel.onEdge(miles)) {
                              TextView targetPinTextView = targetPin.getPinTextView();
                              targetPinTextView.setText("");
                              targetPinTextView.setBackgroundResource(R.drawable.offline2);
                         }
                         else {
                              TextView targetPinTextView = targetPin.getPinTextView();
                              targetPinTextView.setText(targetPin.label);
                              targetPinTextView.setBackgroundResource(android.R.color.transparent);
                         }

                         ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) targetPin.getPinTextView().getLayoutParams();
                         layoutParams.circleRadius = (int) (radiusConstraint * activity.getResources().getDisplayMetrics().density);
                         targetPin.getPinTextView().setLayoutParams(layoutParams);
                    }
                    else {
                         ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) targetPin.getPinTextView().getLayoutParams();
                         layoutParams.circleRadius = (int) (180 * activity.getResources().getDisplayMetrics().density);
                         targetPin.getPinTextView().setLayoutParams(layoutParams);
                    }





               }
          });
     }

}
