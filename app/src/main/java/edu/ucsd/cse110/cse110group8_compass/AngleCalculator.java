package edu.ucsd.cse110.cse110group8_compass;


import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Pair;

public class AngleCalculator {
    protected double userLatitude, userLongitude;
    //private double targetLatitude, targetLongitude;
    //private Double azimuth;
    protected final Double NORTH_POLE_LONGITUDE = 135.0000;
    protected final Double NORTH_POLE_LATITUDE = 90.0000;
    protected final Location NORTH_POLE_LOCATION;

    protected Location userLocation;
    private static AngleCalculator instance;

    AngleCalculator (Double userLatitudeM, Double userLongitudeM)    {
        userLatitude = userLatitudeM;
        userLongitude = userLongitudeM;
        //this.azimuth = convertRadian(azimuth);
        NORTH_POLE_LOCATION = new Location("provider");
        NORTH_POLE_LOCATION.setLatitude(NORTH_POLE_LATITUDE);
        NORTH_POLE_LOCATION.setLongitude(NORTH_POLE_LONGITUDE);


        setUserLocation();
    }

   /* public static AngleCalculator singleton() {
        if (instance == null) {
            instance = new AngleCalculator(0.0, 0.0);
        }
        return instance;
    }*/

    public double convertToDegree(Float rad) {
        if(Math.abs(rad) > 2 * Math.PI) {
            rad = rad % (2 * (float)(Math.PI));
        }
        if(rad < 0) {
            return 360 + Math.toDegrees(rad);
        }
        else {
            return Math.toDegrees(rad);
        }
    }

    protected void setUserLocation() {
        userLocation = new Location("provider");
        userLocation.setLatitude(userLatitude);
        userLocation.setLongitude(userLongitude);

        //targetLocation = new Location("");
        //targetLocation.setLongitude(targetLongitude);
        //targetLocation.setLatitude(targetLatitude);
    }

    public float northAngle() {
        return userLocation.bearingTo(NORTH_POLE_LOCATION);
    }

    public Double angleOnCircle(Double target_latitude, Double target_longitude,  Float azimuthRadian ) {
        Double azimuth = convertToDegree(azimuthRadian);

        System.out.println("lat0: " + target_latitude);
        System.out.println("long0: " +  target_longitude);

        Location targetLocation = new Location(LocationManager.GPS_PROVIDER);
        targetLocation.setMock(true);
        targetLocation.setLatitude(Double.valueOf(target_latitude));
        targetLocation.setLongitude(Double.valueOf(target_longitude));

        System.out.println("lat: " + targetLocation.getLatitude());
        System.out.println("long: " + targetLocation.getLongitude());

        Double targetBearing = Double.valueOf(userLocation.bearingTo(targetLocation));

        System.out.println(azimuth);
        System.out.println(targetBearing);

        if(azimuth > targetBearing) {
            return azimuth - targetBearing;
        }
        else if (azimuth < targetBearing) {
            return 360 - targetBearing - azimuth;
        }
        else {

            return 0.0;
        }
    }








}
