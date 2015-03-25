package com.ccframework.jc.ccframework.BubbleWidget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Home on 3/24/2015.
 */
public class BubbleTailWidget {

//    int mColor;
    Paint mPaint;

    public BubbleTailWidget(){
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
    }
    public BubbleTailWidget(Paint p){
        mPaint = p;
    }

    void drawTriangleTail(Canvas c, float[] pts)
    {
        c.drawLines(pts, mPaint);
    }
}
