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

    private double debugLatSave = 0;
    private double debugLongSave = 0;

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

    }

    public float northAngle() {
        return userLocation.bearingTo(NORTH_POLE_LOCATION);
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

       // System.out.println(azimuth);
        //System.out.println(targetBearing);

        //System.out.println("target lat: " + target_longitude);


        if(targetBearing < 0) {
            targetBearing = 360 + targetBearing;

        }

        if(azimuth > targetBearing) {
            System.out.println("case1: " + (360 - azimuth + targetBearing));
            System.out.println("azimuth: " + azimuth);
            System.out.println("targetBear"+ targetBearing);

            return   360 - azimuth + targetBearing;

        }
        else if (azimuth < targetBearing) {
            if(debugLatSave == target_latitude) {
                System.out.println("case2: " + (targetBearing - azimuth));
            }
            return targetBearing - azimuth;
        }
        else {
            System.out.println("case3:");
            return 0.0;
        }
    }








}
