package edu.ucsd.cse110.cse110group8_compass;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextAvoidance {
    /**
     * Updates what corner the text is being displayed from either being
     * UR, UL, LL, LR - Upper Right, Upper Left, Lower Left, Lower Right
     *
     * @param list the DisplayCircle's pinList
     */
    public static void updateText(PinList list){
        ArrayList<ArrayList<Integer>> options = (ArrayList) List.of(
                (ArrayList) List.of(Gravity.TOP, Gravity.LEFT),
                (ArrayList) List.of(Gravity.BOTTOM, Gravity.RIGHT),
                (ArrayList) List.of(Gravity.TOP, Gravity.LEFT),
                (ArrayList) List.of(Gravity.BOTTOM, Gravity.RIGHT)
        );
        ArrayList<PinList> overlappingList = getOverlappingPins(list);
        for (PinList overlapping : overlappingList){
            for ( int i = 0; i < overlapping.size(); i++ ){
                TextView tv = overlapping.getPin(i).getPinTextView();
                tv.setGravity(options.get(i).get(0));
                tv.setGravity(options.get(i).get(1));
            }
        }
    }

    /**
     * Loops over all pins in the display circle and returns a list of
     * PinList where each PinList contains Pins that are intersecting.
     *
     * @param pinList the DisplayCircle's pinList
     *
     * @return ArrayList<PinList> A list of pinLists
     */
    static private ArrayList<PinList> getOverlappingPins(PinList pinList){

        /**
         * Loop over pinList and for every pin in the list check which pins are enclosed
         * within a radius spanned by a pin's width of the current pin. Remove all of
         * the pins that fit this criteria from pinList and create a newPinList with these overlapping
         * pins insert this list into the ArrayList.
         */

        return new ArrayList<>();
    }


}
