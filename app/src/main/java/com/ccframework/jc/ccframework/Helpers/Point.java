package com.ccframework.jc.ccframework.Helpers;

/**
 * Created by Home on 3/28/2015.
 * also use as vector
 */
public class Point {
    public float x;
    public float y;

    public Point(){}
    public Point(float _x, float _y){
        x = _x;
        y = _y;

    }
    public void setPoint(float _x, float _y){
        x = _x;
        y = _y;
    }

    public float getSquare(){
        return x*x + y*y;
    }
    public double getValue(){
        double square = x*x + y*y;
        return Math.sqrt(square);
    }

    public void normalize(){
        double value = getValue();
        x /= value; y /= value;
    }

}
