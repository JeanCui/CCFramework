package com.ccframework.jc.ccframework.math;

import com.ccframework.jc.ccframework.Helpers.Point;

import java.util.Vector;

/**
 * Created by Home on 3/30/2015.
 */
public class RayIntersectSphere {
    private Point mSegmentStartPoint;

    private Point mSegmentVector;

    private Point mSphereCenter;

    private int mSphereRadius = 1;

    public RayIntersectSphere(){}

    public void setSegmentStartPoint(float x, float y){
        if(mSegmentStartPoint == null){
            mSegmentStartPoint = new Point(x, y);
        }else{
            mSegmentStartPoint.x = x;
            mSegmentStartPoint.y = y;
        }

    }

    public void setSegmentStartPoint(Point ss){
        mSegmentStartPoint = ss;
    }

    public void setSegmentVector(Point v)
    {
        mSegmentVector = v;
    }

    public void setSegmentVector(float x, float y){
        if(mSegmentVector == null){
            mSegmentVector = new Point(x, y);
        }else{
            mSegmentVector.x = x;
            mSegmentVector.y = y;
        }
    }

    public void setSphereCenter(Point c){
        mSphereCenter = c;
    }

    public void setSphereCenter(float x, float y){
        if(mSphereCenter == null){
            mSphereCenter = new Point(x, y);
        }else{
            mSphereCenter.x = x;
            mSphereCenter.y = y;
        }

    }

    public void setSphereRadius(int r){
        mSphereRadius = r;
    }

    public Point getClosetIntersectPoint(){

        float a = mSegmentVector.getSquare();
        float b = 2 * (mSegmentVector.x*mSegmentStartPoint.x + mSegmentVector.y*mSegmentStartPoint.y);
        float c = mSegmentStartPoint.getSquare() - mSphereRadius*mSphereRadius;

        float D = b*b - 4*a*c;
        if(D == 0){
            float t = -b/(2*a);
            Point p = new Point();
            p.x = mSegmentStartPoint.x + t*mSegmentVector.x;
            p.y = mSegmentStartPoint.y + t*mSegmentVector.y;

            return p;
        }
        if(D < 0)
        {
            return null;
        }

        if(D > 0){
            float sqrtD = (float)Math.sqrt(D);

            float t1 = (-b-sqrtD)/(2*a);
            Point p1 = new Point();
            p1.x = mSegmentStartPoint.x + t1*mSegmentVector.x;
            p1.y = mSegmentStartPoint.y + t1*mSegmentVector.y;
            return p1;
        }


        return null;
    }

    public Vector<Point> getIntersectPoints()
    {
        Vector<Point> ret = new Vector<Point>();
        float a = mSegmentVector.getSquare();
        float b = 2 * (mSegmentVector.x*mSegmentStartPoint.x + mSegmentVector.y*mSegmentStartPoint.y);
        float c = mSegmentStartPoint.getSquare() - mSphereRadius*mSphereRadius;

        float D = b*b - 4*a*c;
        if(D == 0){
            float t = -b/(2*a);
            Point p = new Point();
            p.x = mSegmentStartPoint.x + t*mSegmentVector.x;
            p.y = mSegmentStartPoint.y + t*mSegmentVector.y;

            ret.add(p);
            return ret;
        }

        if(D < 0){
            return null;
        }

        if(D > 0){
            float sqrtD = (float)Math.sqrt(D);

            float t1 = (-b-sqrtD)/(2*a);
            float t2 = (-b+sqrtD)/(2*a);

            Point p1 = new Point();
            p1.x = mSegmentStartPoint.x + t1*mSegmentVector.x;
            p1.y = mSegmentStartPoint.y + t1*mSegmentVector.y;

            Point p2 = new Point();
            p2.x = mSegmentStartPoint.x + t2*mSegmentVector.x;
            p2.y = mSegmentStartPoint.y + t2*mSegmentVector.y;

            ret.add(p1);
            ret.add(p2);
            return ret;
        }
        return null;

    }



}
