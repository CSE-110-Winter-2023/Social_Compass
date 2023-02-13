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

//DisplayCircile coordiantes all the pins and makes them visible or not
public class  DisplayCircle {
     static private ConstraintLayout circle_constraint;
     static private ImageView pin_id;
     static private LiveData<Pair<Double, Double>> userCoordinateLive;

     static private Pin userPin;
     static private Pin northPin;

     //should have pin object of north and user already

     //constructor which takes in the compass constraint
      DisplayCircle (ConstraintLayout circle_m) {
          circle_constraint = circle_m;
     }

     //update the pins so that they are made visible
     public void setUserPin(LiveData<Pair<Double, Double>> userCoordinateLive) {
          this.userCoordinateLive = userCoordinateLive;
     }

     //should take the pin object as input
     //pin object should have location information
     public void rotatePin(ImageView targetPinImageView, Pin targetPin, LiveData<Float> targetAzimuth, Activity activity) {
           userCoordinateLive.observe((LifecycleOwner) activity, new Observer<Pair<Double, Double>>() {
                @Override
                public void onChanged(Pair<Double, Double> doubleDoublePair) {
                     AngleCalculator angleCalculator = new AngleCalculator(doubleDoublePair.first, doubleDoublePair.second);

                     targetAzimuth.observe((LifecycleOwner) activity, new Observer<Float>() {
                          @Override
                          public void onChanged(Float value) {
                               //Get the data from the LiveData object here

                               move(targetPinImageView, angleCalculator.angleOnCircle(targetPin.latitude, targetPin.longitude, value).floatValue());


                               //Log.d("LiveDataValue", String.valueOf(value));
                          }
                     });
                }
           });
           //AngleCalculator angleCalculator = new AngleCalculator(userPin.latitude, userPin.longitude)36
           //move(targetPinImageView, angleCalculator.angleOnCircle(targetPin.latitude, targetPin.longitude, targetAzimuth).floatValue());

     }

     //These two need to go into its own Rotator class
          public void move(ImageView pin_m, Float angle) {
               ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) pin_m.getLayoutParams();
               layoutParams.circleAngle = angle;
               pin_m.setLayoutParams(layoutParams);
          }

          public void rotate2(ImageView pin_m, long animation_time_m , float start_angle_m, float end_angle_m) {
               ValueAnimator anim = ValueAnimator.ofFloat(start_angle_m, end_angle_m);
               anim.addUpdateListener(valueAnimator -> {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) pin_m.getLayoutParams();
                    layoutParams.circleAngle = val;
                    pin_m.setLayoutParams(layoutParams);
               });
               anim.setDuration(animation_time_m);
               anim.setInterpolator(new LinearInterpolator());
               anim.start();
          }

     
     

}
