package com.ccframework.jc.ccframework.BubbleWidget;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.ccframework.jc.ccframework.AppConstants;
import com.ccframework.jc.ccframework.Helpers.Point;

/**
 * Created by Home on 3/28/2015.
 */
public class TriangleTail extends BubbleTailWidget {

    private float mDegree;

    public TriangleTail()
    {
        super();
        mDegree = AppConstants.BUBBLE_TRIANGLE_DEGREE;
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

    public void drawTail(Canvas c){
        super.drawTail(c);

        float[] pts = {mInterP1.x, mInterP1.y, mTailPoint.x, mTailPoint.y, mInterP2.x, mInterP2.y};
        c.drawLines(pts, mPaint);
    }


}
