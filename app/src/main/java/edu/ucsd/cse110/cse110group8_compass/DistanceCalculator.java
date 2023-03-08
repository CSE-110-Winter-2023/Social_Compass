package edu.ucsd.cse110.cse110group8_compass;


import android.location.Location;

public class DistanceCalculator {

    protected Location userLocation;
    DistanceCalculator(double userLatitude, double userLongitude) {
        userLocation = new Location("provider");
        userLocation.setLatitude(userLatitude);
        userLocation.setLongitude(userLongitude);
    }

    public Double calculateDistance(double targetLatitude, double targetLongitude) {
        Location targetLocation = new Location("provider");
        userLocation.setLatitude(targetLatitude);
        userLocation.setLongitude(targetLongitude);

        Float distance = userLocation.distanceTo(targetLocation);
        return metersToMiles(distance.doubleValue());
    }

    private Double metersToMiles(Double meters) {
        return meters * 0.000621;
    }

}
