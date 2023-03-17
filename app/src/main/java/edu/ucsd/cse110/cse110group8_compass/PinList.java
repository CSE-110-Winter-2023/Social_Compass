package edu.ucsd.cse110.cse110group8_compass;

import java.util.ArrayList;

public class PinList {
    private ArrayList<Pin> pinList;
    private ArrayList<Boolean> validPins;

    PinList() {
        pinList = new ArrayList<>();
        validPins = new ArrayList<>();
    }

    boolean addPin(Pin pin) {
        pinList.add(pin);
        return true;
    }

    public void setPinList(ArrayList<Pin> pinArray) {
        pinList = pinArray;

    }


    public int size() {
        return pinList.size();
    }

    public void printList() {
        for(int i = 0; i < pinList.size(); i++){
            pinList.get(i).print();
        }
    }


    public Pin getPin(int i) {
        return pinList.get(i);
    }

}
