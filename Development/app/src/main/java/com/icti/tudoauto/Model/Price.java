package com.icti.tudoauto.Model;

import android.location.LocationManager;

import javax.crypto.spec.PSource;

public class Price {

    private float pricefuelt0;
    private float pricefuelt1;
    private float pricefuelt2;
    private float pricefuelt3;
    private float pricefuelt4;
    private float pricefuelt5;
    private float pricefuelt6;
    private float pricefuelt7;
    private Position locationPosition;

    public Price() {
    }

    public float getPricefuel(int id) {
        switch (id) {
            case(0):
                return pricefuelt0;
            case(1):
                return pricefuelt1;
            case(2):
                return pricefuelt2;
            case(3):
                return pricefuelt3;
            case(4):
                return pricefuelt4;
            case(5):
                return pricefuelt5;
            case(6):
                return pricefuelt6;
            case(7):
                return pricefuelt7;
            default:
                return pricefuelt0;
        }
    }

    public void setPricefuel(int id, float pricefuel) {
        switch (id) {
            case(0):
                this.pricefuelt0 = pricefuel;
            case(1):
                this.pricefuelt1 = pricefuel;
            case(2):
                this.pricefuelt2 = pricefuel;
            case(3):
                this.pricefuelt3 = pricefuel;
            case(4):
                this.pricefuelt4 = pricefuel;
            case(5):
                this.pricefuelt5 = pricefuel;
            case(6):
                this.pricefuelt6 = pricefuel;
            case(7):
                this.pricefuelt7 = pricefuel;
            default:
                this.pricefuelt0 = pricefuel;
        }
    }

    public Position getLocationPosition() {
        return locationPosition;
    }

    public void setLocationPosition(Position locationPosition) {
        this.locationPosition = locationPosition;
    }


}
