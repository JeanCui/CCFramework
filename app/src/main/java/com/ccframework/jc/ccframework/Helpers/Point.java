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
    public float getValue(){
        return (float)Math.sqrt(x*x + y*y);
    }


}
