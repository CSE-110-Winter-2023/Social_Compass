package edu.ucsd.cse110.cse110group8_compass;


import android.location.Location;
import android.location.LocationManager;

public class DistanceCalculator {

    protected Location userLocation;
    protected boolean mock;
    DistanceCalculator(double userLatitude, double userLongitude) {
        userLocation = new Location(LocationManager.GPS_PROVIDER);
        userLocation.setLatitude(userLatitude);
        userLocation.setLongitude(userLongitude);
    }

    DistanceCalculator(double userLatitude, double userLongitude, Boolean mock) {


        userLocation = new Location("mockprovider");
        userLocation.setMock(true);
        userLocation.setLatitude(userLatitude);
        userLocation.setLongitude(userLongitude);
    }

    public Double calculateMockDistance(double targetLatitude, double targetLongitude) {
        Location targetLocation = new Location(LocationManager.GPS_PROVIDER);
        targetLocation.setMock(true);
        targetLocation.setLatitude(targetLatitude);
        targetLocation.setLongitude(targetLongitude);


        System.out.println("USERLAT: " + userLocation.getLatitude());
        System.out.println("USERLONG: " + userLocation.getLongitude());
        System.out.println("TARGETLAT: " + targetLocation.getLatitude());
        Float distance = userLocation.distanceTo(targetLocation);
        System.out.print("VAL:" + distance);
        return metersToMiles(distance.doubleValue());
    }

    public Double calculateDistance(double targetLatitude, double targetLongitude) {
        Location targetLocation = new Location("provider");
        targetLocation.setLatitude(targetLatitude);
        targetLocation.setLongitude(targetLongitude);

        Float distance = userLocation.distanceTo(targetLocation);
        System.out.print("VAL:" + distance);
        return metersToMiles(distance.doubleValue());
    }

    private Double metersToMiles(Double meters) {
        return meters * 0.000621;
    }

}
