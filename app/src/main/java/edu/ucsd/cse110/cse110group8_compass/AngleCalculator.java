package edu.ucsd.cse110.cse110group8_compass;

import android.location.Location;
import android.location.LocationManager;

public class AngleCalculator {

    protected Location userLocation;

    AngleCalculator (Double userLatitudeM, Double userLongitudeM)    {
        userLocation = new Location("provider");
        userLocation.setLatitude(userLatitudeM);
        userLocation.setLongitude(userLongitudeM);

    }

    public double convertToDegree(Float rad) {
        if(rad < 0) {
            return 360 + Math.toDegrees(rad);
        }
        else {
            return Math.toDegrees(rad);
        }
    }

    private Double calculateBearing(Double target_latitude, Double target_longitude) {

        Location targetLocation = new Location(LocationManager.GPS_PROVIDER);

        targetLocation.setLatitude(Double.valueOf(target_latitude));
        targetLocation.setLongitude(Double.valueOf(target_longitude));

        return Double.valueOf(userLocation.bearingTo(targetLocation));
    }

    public Double angleOnCircle(Double target_latitude, Double target_longitude,  Float azimuthRadian ) {
        Double azimuth = convertToDegree(azimuthRadian);

        Double targetBearing = calculateBearing( target_latitude, target_longitude);

        if(targetBearing < 0) {
            targetBearing = 360 + targetBearing;
        }

        if(azimuth > targetBearing) {
            return   360 - azimuth + targetBearing;
        }
        else if (azimuth < targetBearing) {
            return targetBearing - azimuth;
        }
        else {
            return 0.0;
        }
    }

}
