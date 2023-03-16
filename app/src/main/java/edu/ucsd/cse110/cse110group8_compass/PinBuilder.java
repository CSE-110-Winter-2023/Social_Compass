package edu.ucsd.cse110.cse110group8_compass;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class PinBuilder {

    TextView view;
    String label;
    Double longitude;
    Double latitude;
    Context context;
    ConstraintSet set;
    ConstraintLayout layout;
    float density;

    public PinBuilder(Context context, ConstraintLayout layout, float density){
        this.context = context;
        this.view = new TextView(context);
        this.set = new ConstraintSet();
        this.layout = layout;
        this.density = density;
    }

    public PinBuilder config(){
        this.view.setId(View.generateViewId());
        this.layout.addView(view,0);
        this.set.clone(layout);
        this.set.connect(view.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, 60);
        this.set.applyTo(layout);

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        layoutParams.circleRadius = (int) (20 * density);
        layoutParams.circleConstraint=R.id.compass;
        layoutParams.height = 200;
        layoutParams.width = 100;

        this.view.setBackgroundResource(R.drawable.pindrop);
        this.view.setLayoutParams(layoutParams);
        this.view.bringToFront();
        return this;
    }

    public PinBuilder withLabel(String label){
        this.label = label;
        this.view.setText(label);
        this.view.setTextSize(14);
        this.view.setGravity(Gravity.TOP);
        this.view.setTextColor(Color.BLACK);
        this.view.setSingleLine(true);
        this.view.setEllipsize(TextUtils.TruncateAt.END);
        return this;
    }

    public PinBuilder withCoordinates(Double longit, Double latit){
        this.longitude = longit;
        this.latitude = latit;
        return this;
    }

    public Pin build(){
        Pin pin = new Pin(label, longitude, latitude);
        pin.setPinTextView(this.view);
        return pin;
    }


}
