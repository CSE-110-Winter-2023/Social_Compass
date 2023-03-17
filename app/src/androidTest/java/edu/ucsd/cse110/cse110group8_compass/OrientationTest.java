package edu.ucsd.cse110.cse110group8_compass;

import static org.junit.Assert.assertEquals;

import android.util.Pair;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;

import java.util.ArrayList;

public class OrientationTest {

    @Test
    public void testNorthPin() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);



        String northPinPublicCode = "qtgmI&@3$zH!us*X5!YKi&b1aWhijR5HMe&ruxJ6mxG5Fx#EcL$ou" +
                "SiaGMP*0oMGH&tnju36*K*qxaR%&iL20@5BFdpc0m^bhBoR";

        scenario.onActivity(activity -> {

            int currentZoomLevel = 2;
            float density = activity.getResources().getDisplayMetrics().density;

            MutableLiveData<Pair<Double, Double>> userCoordinatesSetMock = new MutableLiveData<>();
            MutableLiveData<Float> azimuthSetMock = new MutableLiveData<>();

            OrientationService orientationService = new OrientationService(activity);
            orientationService.setMockOrientationSource(azimuthSetMock);

            LiveData<Float> azimuth = orientationService.getOrientation();

            LocationService locationService = new LocationService(activity);
            locationService.setMockOrientationSource(userCoordinatesSetMock);

            LiveData<Pair<Double, Double>> userCoordinates = locationService.getLocation();

            Pin northPin = new Pin(
                    "North Pin",
                    135.00,
                    90.00
            );
            northPin.setPublic_code(northPinPublicCode);
            northPin.setPinTextView(activity.findViewById(R.id.north_pin));

            DisplayCircle displayCircle = new DisplayCircle(activity.findViewById(R.id.compass),northPin, activity, azimuth,userCoordinates  );
            displayCircle.restartObservers(new ZoomLevel(currentZoomLevel));

            azimuthSetMock.setValue(0F);
            userCoordinatesSetMock.setValue(Pair.create(-35.0, -75.0));

            Pin testPin = new Pin(
                    "testPin",
                    35.00,
                    -90.00
            );

            ArrayList<Pin> pinList = new ArrayList<>();
            ConstraintLayout layout = (ConstraintLayout) activity.findViewById(R.id.compass);
            Pin pin = new PinBuilder(activity, layout, density).config().withCoordinates(135.00, 90.00).withLabel("uuidTest").build();
            pinList.add(pin);
            displayCircle.setPinList(pinList, new ZoomLevel(currentZoomLevel));


            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) northPin.getPinTextView().getLayoutParams();
            assertEquals(360, layoutParams.circleAngle, 0.1);


            azimuthSetMock.setValue(3.142F);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) northPin.getPinTextView().getLayoutParams();
            assertEquals(180, layoutParams2.circleAngle, 0.1);

        });

    }

}
