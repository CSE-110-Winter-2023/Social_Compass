package edu.ucsd.cse110.cse110group8_compass;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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
     private LiveData<Float> azimuth;

     private float density;
     private Activity activity;
     private boolean validPins[];
     private boolean populatedPins[];
     private int numOfPins;
     private HashMap<Pin, Float > angleMap = new HashMap<>();
     private HashMap<Pin, Integer > positionMap = new HashMap<>();


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
                              targetPinTextView.setBackgroundResource(R.drawable.offline);
                              targetPinTextView.setPadding(0, 0, 0, 0);

                              int separation = collisionPinSeparation(numOfPinsInRange(targetPin), zoomLevel, miles);

                              ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) targetPin.getPinTextView().getLayoutParams();
                              layoutParams.circleRadius = (int) ((radiusConstraint + (separation * pinPosition(targetPin))) * activity.getResources().getDisplayMetrics().density);
                              targetPin.getPinTextView().setLayoutParams(layoutParams);

                         }
                         else {
                              TextView targetPinTextView = targetPin.getPinTextView();
                              targetPinTextView.setText(targetPin.label);
                              targetPinTextView.setBackgroundResource(android.R.color.transparent);

                              int separation = collisionPinSeparation(numOfPinsInRange(targetPin), zoomLevel, miles);

                              ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) targetPin.getPinTextView().getLayoutParams();
                              layoutParams.circleRadius = (int) ((radiusConstraint + (separation * pinPosition(targetPin))) * activity.getResources().getDisplayMetrics().density);
                              targetPin.getPinTextView().setLayoutParams(layoutParams);
                         }

                         //collisionPinseperation(int numOfPinsInSector, ZoomLevel zoomLevel , Double miles)

                    }
                    else {
                         ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) targetPin.getPinTextView().getLayoutParams();
                         layoutParams.circleRadius = (int) (180 * activity.getResources().getDisplayMetrics().density);
                         targetPin.getPinTextView().setLayoutParams(layoutParams);
                    }

               }
          });
     }


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

                                   if(layoutParams.circleRadius >= 170 * density) {
                                        layoutParams.width = (int) (35 * density);
                                        layoutParams.height = (int) (35 * density);
                                   }
                                   else {
                                        if(truncateSizeInt > 100) {
                                             layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                                             layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                        }
                                        else {
                                             layoutParams.width = (int) (truncateSizeInt * density);
                                        }
                                   }
                                   targetTextView.setLayoutParams(layoutParams);
                                   targetTextView.setMaxLines(1);
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

     boolean pinsIntersect(Pin p1, Pin p2){
          ConstraintLayout.LayoutParams p1Layout =
                  (ConstraintLayout.LayoutParams) p1.getPinTextView().getLayoutParams();
          ConstraintLayout.LayoutParams p2Layout =
                  (ConstraintLayout.LayoutParams) p2.getPinTextView().getLayoutParams();

          float angle1 = p1Layout.circleAngle;
          float angle2 = p2Layout.circleAngle;
          float rad1 = p1Layout.circleRadius;
          float rad2 = p2Layout.circleRadius;

          double x1 = rad1*Math.cos(angle1);
          double x2 = rad2*Math.cos(angle2);
          double y1 = rad1*Math.sin(angle1);
          double y2 = rad2*Math.sin(angle2);

          float w1 = p1Layout.width;
          float w2 = p2Layout.width;
          float h1 = p1Layout.height;
          float h2 = p2Layout.height;

          double intersectLength = Math.min(x1+w1/2, x2+w2/2) - Math.max(x1-w1/2,x2-w2/2);
          double intersectWidth = Math.min(y1+h1/2, y2+h2/2) - Math.max(y1-h1/2, y2-h2/2);

          return intersectLength*intersectWidth > 0;
     }

     private int truncateSize(Pin targetPin, Float currentAngle) {
          Pin p;
          double longestIntersection = 0;
          for ( int i = 0; i < pinList.size(); i++ ){
               p = pinList.getPin(i);
               if (pinsIntersect(targetPin, p)){
                    Log.i("Gavin Intersect", "INTERSECT!!");
                    ConstraintLayout.LayoutParams p1Layout =
                            (ConstraintLayout.LayoutParams) targetPin.getPinTextView().getLayoutParams();
                    ConstraintLayout.LayoutParams p2Layout =
                            (ConstraintLayout.LayoutParams) p.getPinTextView().getLayoutParams();
                    float angle1 = p1Layout.circleAngle;
                    float angle2 = p2Layout.circleAngle;
                    float rad1 = p1Layout.circleRadius;
                    float rad2 = p2Layout.circleRadius;
                    float w1 = p1Layout.width;
                    float w2 = p2Layout.width;
                    double intersectionLength =
                            Math.min(rad1*Math.cos(angle1)+w1/2, rad2*Math.cos(angle2)+w2/2) -
                            Math.max(rad1*Math.cos(angle1)-w1/2,rad2*Math.cos(angle2)-w2/2);
                    if (intersectionLength > longestIntersection){
                         longestIntersection = intersectionLength;
                    }
               }
          }
          return (int) (targetPin.getPinTextView().getLayoutParams().width - longestIntersection);
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
                              targetPinTextView.setBackgroundResource(R.drawable.offline);
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