package edu.ucsd.cse110.cse110group8_compass;


import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.util.Pair;

public class AngleCalculator {
    private double userLatitude, userLongitude;
    //private double targetLatitude, targetLongitude;
    //private Double azimuth;
    private final Double NORTH_POLE_LONGITUDE = 135.0000;
    private final Double NORTH_POLE_LATITUDE = 90.0000;
    private final Location NORTH_POLE_LOCATION;

    private Location userLocation;

    AngleCalculator(Pair<Double , Double > user_coordinates)    {
        userLatitude = user_coordinates.first;
        userLongitude = user_coordinates.second;
        //this.azimuth = convertRadian(azimuth);
        NORTH_POLE_LOCATION = new Location("");
        NORTH_POLE_LOCATION.setLatitude(NORTH_POLE_LATITUDE);
        NORTH_POLE_LOCATION.setLongitude(NORTH_POLE_LONGITUDE);


        setUserLocation();
    }

    private double convertToDegree(Float rad) {
        if(rad < 0) {
            return 360 + Math.toDegrees(rad);
        }
        else {
            return Math.toDegrees(rad);
        }
    }

    private void setUserLocation() {
        userLocation = new Location("");
        userLocation.setLatitude(userLatitude);
        userLocation.setLongitude(userLongitude);

        //targetLocation = new Location("");
        //targetLocation.setLongitude(targetLongitude);
        //targetLocation.setLatitude(targetLatitude);
    }

    public float northAngle() {
        return userLocation.bearingTo(NORTH_POLE_LOCATION);
    }

    public Double angleOnCircle(Pair<Double , Double > target_coordinates,  Float azimuthRadian ) {
        Double azimuth = convertToDegree(azimuthRadian);
        Location targetLocation = new Location("");
        targetLocation.setLatitude(target_coordinates.first);
        targetLocation.setLongitude(target_coordinates.second);

        Double targetBearing = Double.valueOf(userLocation.bearingTo(targetLocation));

        if(azimuth > targetBearing) {
            return azimuth - targetBearing;
        }
        else if (azimuth < targetBearing) {
            return targetBearing - azimuth;
        }
        else {
            return 0.0;
        }
    }








}
