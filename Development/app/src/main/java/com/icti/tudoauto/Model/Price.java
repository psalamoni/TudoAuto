package com.icti.tudoauto.Model;

import android.location.Address;
import android.location.LocationManager;

import java.util.List;

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
    private Position position;
    private long timestamp;

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
                break;
            case(1):
                this.pricefuelt1 = pricefuel;
                break;
            case(2):
                this.pricefuelt2 = pricefuel;
                break;
            case(3):
                this.pricefuelt3 = pricefuel;
                break;
            case(4):
                this.pricefuelt4 = pricefuel;
                break;
            case(5):
                this.pricefuelt5 = pricefuel;
                break;
            case(6):
                this.pricefuelt6 = pricefuel;
                break;
            case(7):
                this.pricefuelt7 = pricefuel;
                break;
            default:
                this.pricefuelt0 = pricefuel;
                break;
        }
    }

    public float getPricefuelt0() {
        return pricefuelt0;
    }

    public void setPricefuelt0(float pricefuelt0) {
        this.pricefuelt0 = pricefuelt0;
    }

    public float getPricefuelt1() {
        return pricefuelt1;
    }

    public void setPricefuelt1(float pricefuelt1) {
        this.pricefuelt1 = pricefuelt1;
    }

    public float getPricefuelt2() {
        return pricefuelt2;
    }

    public void setPricefuelt2(float pricefuelt2) {
        this.pricefuelt2 = pricefuelt2;
    }

    public float getPricefuelt3() {
        return pricefuelt3;
    }

    public void setPricefuelt3(float pricefuelt3) {
        this.pricefuelt3 = pricefuelt3;
    }

    public float getPricefuelt4() {
        return pricefuelt4;
    }

    public void setPricefuelt4(float pricefuelt4) {
        this.pricefuelt4 = pricefuelt4;
    }

    public float getPricefuelt5() {
        return pricefuelt5;
    }

    public void setPricefuelt5(float pricefuelt5) {
        this.pricefuelt5 = pricefuelt5;
    }

    public float getPricefuelt6() {
        return pricefuelt6;
    }

    public void setPricefuelt6(float pricefuelt6) {
        this.pricefuelt6 = pricefuelt6;
    }

    public float getPricefuelt7() {
        return pricefuelt7;
    }

    public void setPricefuelt7(float pricefuelt7) {
        this.pricefuelt7 = pricefuelt7;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
