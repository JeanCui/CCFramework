package com.ccframework.jc.ccframework.BubbleWidget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.ccframework.jc.ccframework.AppConstants;
import com.ccframework.jc.ccframework.Helpers.Point;
import com.ccframework.jc.ccframework.math.RayIntersectSphere;

/**
 * Created by Home on 3/28/2015.
 */
public class TriangleTail extends BubbleTailWidget {

    private float mDegree;
    private int mSphereX, mSphereY;
    private int mSphereRadius;
    private RayIntersectSphere rayIntersectSphere = new RayIntersectSphere();

    public TriangleTail()
    {
        super();
        mDegree = AppConstants.BUBBLE_TRIANGLE_DEGREE;
    }
    public TriangleTail(int cx, int cy, int radius){
        super();
        mDegree = AppConstants.BUBBLE_TRIANGLE_DEGREE;

        mSphereX = cx;
        mSphereY = cy;
        mSphereRadius = radius;
    }

    public TriangleTail(int cx, int cy){
        super();
        mDegree = AppConstants.BUBBLE_TRIANGLE_DEGREE;
        mSphereX = cx;
        mSphereY = cy;
    }

    public TriangleTail(Paint p){
        super(p);
        mDegree = AppConstants.BUBBLE_TRIANGLE_DEGREE;
    }

    public TriangleTail(float degree){
        super();
        mDegree = degree;
    }

    public void setDegree(float d){
        mDegree = d;
    }



    private void calculateIntersectPoints(){

        rayIntersectSphere.setSegmentStartPoint(mTailPoint);
        Point tailToCircle = new Point(mSphereX - mTailPoint.x, mSphereY - mTailPoint.y);
        tailToCircle.normalize();

        //TEST
//        mDegree = 0;
//        tailToCircle.x = 1;
//        tailToCircle.y = 0;

        double rad = (mDegree/2)*(Math.PI/180);
        double cos_a1 = Math.cos(rad);
        double sin_a1 = Math.sin(rad);

        Point segmentVector1 = new Point();
        segmentVector1.x = (float) (tailToCircle.x * cos_a1 - tailToCircle.y * sin_a1);
        segmentVector1.y = (float) (tailToCircle.x * sin_a1 + tailToCircle.y * cos_a1);


        rayIntersectSphere.setSphereCenter(mSphereX, mSphereY);
        rayIntersectSphere.setSphereRadius(mSphereRadius);

        rayIntersectSphere.setSegmentVector(segmentVector1);


        mInterP1 = rayIntersectSphere.getClosetIntersectPoint();

        double cos_a2 = Math.cos(-rad);
        double sin_a2 = Math.sin(-rad);

        Point segmentVector2 = new Point();
        segmentVector2.x = (float)(tailToCircle.x * cos_a2 - tailToCircle.y * sin_a2);
        segmentVector2.y = (float)(tailToCircle.x * sin_a2 + tailToCircle.y * cos_a2);

        rayIntersectSphere.setSegmentVector(segmentVector2);

        mInterP2 = rayIntersectSphere.getClosetIntersectPoint();

    }



    Path mTrianglePath = new Path();

    public void drawTail(Canvas c){

        super.drawTail(c);

        calculateIntersectPoints();
//        float[] pts = {mInterP1.x, mInterP1.y, mTailPoint.x, mTailPoint.y, mInterP2.x, mInterP2.y};
//        float[] pts = {100 , 100, 200, 100,     200, 100, 200, 200,         200, 200, 100, 100};

        mTrianglePath.moveTo(mInterP1.x, mInterP1.y);
        mTrianglePath.lineTo( mTailPoint.x, mTailPoint.y);
//        mTrianglePath.moveTo( mTailPoint.x, mTailPoint.y);
        mTrianglePath.lineTo(mInterP2.x, mInterP2.y);
//        mTrianglePath.moveTo(mInterP2.x, mInterP2.y);
        mTrianglePath.lineTo(mInterP1.x,  mInterP1.y);
//        mTrianglePath.moveTo(100,100);
//        mTrianglePath.lineTo( 200, 100);
//        mTrianglePath.moveTo( 200, 100);
//        mTrianglePath.lineTo(200, 200);
//        mTrianglePath.moveTo(200, 200);
//        mTrianglePath.lineTo(100 , 100);
        mTrianglePath.close();
        c.drawPath(mTrianglePath, mPaint);
//        c.drawLines(pts, mPaint);
//        c.drawVertices(Canvas.VertexMode.TRIANGLES, pts.length, pts,0, null, 0, null, 0, null, 0, 0, mPaint);
    }


}
