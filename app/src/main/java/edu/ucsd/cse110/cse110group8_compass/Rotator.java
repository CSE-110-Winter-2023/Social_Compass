package edu.ucsd.cse110.cse110group8_compass;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Rotator {

    public void move(ImageView pin_m, Float angle) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) pin_m.getLayoutParams();
        layoutParams.circleAngle = angle;
        pin_m.setLayoutParams(layoutParams);
    }

    public void rotate(ImageView pin_m, long animation_time_m , float start_angle_m, float end_angle_m) {
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
