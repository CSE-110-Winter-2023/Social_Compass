package edu.ucsd.cse110.cse110group8_compass;
/*
    Zone 1: [0-1 mile)
    Zone 2: [1-10 miles)
    Zone 3: [10-500 miles)
    Zone 4: [500 miles, 500 miles +)
 */


import android.util.Pair;

public class ZoomLevel {

    private int zoomNum;
    private ZOOM_ONE_CONSTANTS zoomOne;
    private ZOOM_TWO_CONSTANTS zoomTwo;
    private ZOOM_THREE_CONSTANTS zoomThree;
    private ZOOM_FOUR_CONSTANTS zoomFour;
    public ZoomLevel(int zoomNum) {
        this.zoomNum = zoomNum;
        zoomOne = new ZOOM_ONE_CONSTANTS();
        zoomTwo = new ZOOM_TWO_CONSTANTS();
        zoomThree = new ZOOM_THREE_CONSTANTS();
        zoomFour = new ZOOM_FOUR_CONSTANTS();


    }

    private int getZone(Double miles) {
        if (miles < 0) {
            System.out.println("getRange(): negative mile value");
            return -9999;
        }


        if (miles < 1) {
            return 1;
        } else if (miles < 10) {
            return 2;
        } else if (miles < 500) {
            return 3;
        } else {
            return 4;
        }
    }

    public Pair<Integer, Integer>  stackZone(double miles) {
        int distanceZone = getZone(miles);
        if(zoomNum == 1) {
            System.out.println("1");
            switch(distanceZone) {
                case 1:
                    return Pair.create(zoomOne.ZONE_ONE, zoomOne.ZONE_ONE_STACK);
                default:
                    return Pair.create(zoomOne.OUTER_CIRCLE_RADIUS, zoomOne.OUTER_CIRCLE_RADIUS);
            }
        }
        else if(zoomNum == 2) {
            System.out.println("2");
            switch(distanceZone) {
                case 1:
                    return Pair.create(zoomTwo.ZONE_ONE, zoomTwo.ZONE_ONE_STACK);
                case 2:
                    return Pair.create(zoomTwo.ZONE_TWO, zoomTwo.ZONE_TWO_STACK);
                default:
                    return Pair.create(zoomTwo.OUTER_CIRCLE_RADIUS, zoomTwo.OUTER_CIRCLE_RADIUS);
            }
        }
        else if(zoomNum == 3){
            System.out.println("3");
            switch(distanceZone) {
                case 1:
                    return Pair.create(zoomThree.ZONE_ONE, zoomThree.ZONE_ONE_STACK);
                case 2:
                    return Pair.create(zoomThree.ZONE_TWO, zoomThree.ZONE_TWO_STACK);
                case 3:
                    return Pair.create(zoomThree.ZONE_THREE, zoomThree.ZONE_THREE_STACK);
                default:
                    return Pair.create(zoomThree.OUTER_CIRCLE_RADIUS, zoomThree.OUTER_CIRCLE_RADIUS);
            }
        }
        else if(zoomNum == 4) {
            System.out.println("4");
            switch(distanceZone) {
                case 1:
                    return Pair.create(zoomFour.ZONE_ONE, zoomFour.ZONE_ONE_STACK);
                case 2:
                    return Pair.create(zoomFour.ZONE_TWO, zoomFour.ZONE_TWO_STACK);
                case 3:
                    return Pair.create(zoomFour.ZONE_THREE, zoomFour.ZONE_THREE_STACK);
                case 4:
                    return Pair.create(zoomFour.ZONE_FOUR, zoomFour.ZONE_FOUR_STACK);
                default:
                    return Pair.create(zoomFour.OUTER_CIRCLE_RADIUS, zoomFour.OUTER_CIRCLE_RADIUS);
            }
        }
        else {
            System.out.println("ZoomLevel: invalid zoom num");

            return Pair.create(0, 0);
        }
    }

    public Boolean onEdge(double miles) {
        int radius = getRadius(miles);
        if(zoomNum == 1) {
           if(radius == zoomOne.OUTER_CIRCLE_RADIUS) {
               return true;
           }
           return false;
        }
        else if (zoomNum == 2) {
            if(radius == zoomTwo.OUTER_CIRCLE_RADIUS) {
                return true;
            }
            return false;
        }
        else if(zoomNum == 3) {
            if(radius == zoomThree.OUTER_CIRCLE_RADIUS) {
                return true;
            }
            return false;
        }
        else if(zoomNum == 4) {
            return false;
        }

        return false;
    }

    public int getRadius(double miles) {
        int distanceZone = getZone(miles);

        if(zoomNum == 1) {
            System.out.println("1");
            switch(distanceZone) {
                case 1:
                    return zoomOne.ZONE_ONE;
                default:
                    return zoomOne.OUTER_CIRCLE_RADIUS;
            }
        }
        else if(zoomNum == 2) {
            System.out.println("2");
            switch(distanceZone) {
                case 1:
                    return zoomTwo.ZONE_ONE;
                case 2:
                    return zoomTwo.ZONE_TWO;
                default:
                    return zoomTwo.OUTER_CIRCLE_RADIUS;
            }
        }
        else if(zoomNum == 3){
            System.out.println("3");
            switch(distanceZone) {
                case 1:
                    return zoomThree.ZONE_ONE;
                case 2:
                    return zoomThree.ZONE_TWO;
                case 3:
                    return zoomThree.ZONE_THREE;
                default:
                    return zoomThree.OUTER_CIRCLE_RADIUS;
            }
        }
        else if(zoomNum == 4) {
            System.out.println("4");
            switch(distanceZone) {
                case 1:
                    return zoomFour.ZONE_ONE;
                case 2:
                    return zoomFour.ZONE_TWO;
                case 3:
                    return zoomFour.ZONE_THREE;
                case 4:
                    return zoomFour.ZONE_FOUR;
                default:
                    return zoomFour.OUTER_CIRCLE_RADIUS;
            }
        }
        else {
            System.out.println("ZoomLevel: invalid zoom num");

            return -99;
        }
    }

}
