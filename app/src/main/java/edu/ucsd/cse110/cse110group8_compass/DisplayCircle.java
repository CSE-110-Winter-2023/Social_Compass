package edu.ucsd.cse110.cse110group8_compass;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

//DisplayCircile coordiantes all the pins and makes them visible or not
public class  DisplayCircle {
     static private ConstraintLayout circle_constraint;
     static private ImageView pin_id;
=

     //An array of 3 specific pins, need to update based on name
     static private DropdownPin[3] pins;


     //constructor which takes in the compass constraint
      DisplayCircle (ConstraintLayout circle_m) {
          circle_constraint = circle_m;
     }

     //update the pins so that they are made visible
     public void update_visible() {

     }

     //These two need to go into its own Rotator class
          public void rotate(ImageView pin_m, float angle) {
               ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) pin_m.getLayoutParams();
               layoutParams.circleAngle = angle;
               pin_id.setLayoutParams(layoutParams);
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
