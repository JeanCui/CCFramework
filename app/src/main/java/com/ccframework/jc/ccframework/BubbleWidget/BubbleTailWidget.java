package com.ccframework.jc.ccframework.BubbleWidget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ccframework.jc.ccframework.Helpers.Point;

/**
 * Created by Home on 3/24/2015.
 */
public class BubbleTailWidget {

//    int mColor;
    protected Paint mPaint;
    protected Point mTailPoint;
    protected Point mInterP1, mInterP2;
    protected int mBubbleCX, mBubbleCY;

    public BubbleTailWidget(){
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
    }
    public BubbleTailWidget(Paint p){
        mPaint = p;
    }

    public void setBubbleCoord(int x, int y){
        int xDiff = x - mBubbleCX;
        int yDiff = y - mBubbleCY;

        mInterP1.add(xDiff, yDiff);
        mInterP2.add(xDiff, yDiff);

        mTailPoint.add(xDiff, yDiff);

        mBubbleCX = x;
        mBubbleCY = y;
    }


    public void setTailPoint(Point p){
        mTailPoint = p;
    }
    public void setTailPoint(float x, float y){
        if(mTailPoint == null)
            mTailPoint = new Point(x, y);
        else {
            mTailPoint.x = x;
            mTailPoint.y = y;
        }

    }


    public void createTail()
    {

    }

    public void drawTail(Canvas c)
    {

    }
}
