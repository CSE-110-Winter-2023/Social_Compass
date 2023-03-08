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
        if(pin.checkValid() == true) {
            pinList.add(pin);
            return true;
        }
        else {
            return false;
        }
    }

    private boolean checkNullPins() {
        boolean valid = true;

        for(int i = 0; i < pinList.size(); i++) {
            if(pinList.get(i).checkValid() == false){
                valid = false;
            }
        }
        return valid;
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
