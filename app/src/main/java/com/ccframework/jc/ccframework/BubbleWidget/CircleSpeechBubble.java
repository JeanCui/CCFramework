package com.ccframework.jc.ccframework.BubbleWidget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.ccframework.jc.ccframework.Helpers.Point;

/**
 * Created by Jin Cui on 3/22/2015.
 */
public class CircleSpeechBubble extends SpeechBubbleWidget {

    protected int radius;
    private Point p1, p2;


    private void drawSelectedOutline(Canvas c)
    {
        if(isSelected)
        {
            c.drawCircle(mCX, mCY, radius, mOutlinePaint);
        }
    }

    public CircleSpeechBubble(Paint p, View v, int cx, int cy)
    {
        super(p, v, cx, cy);

        radius = widgetWidth>widgetHeight?widgetHeight:widgetWidth;
        radius = radius/2 - widgetMargin;
        mTailWidget = new TriangleTail(cx, cy, radius);
        mTailWidget.setTailPoint(mCX - (radius+20), mCY + (radius+20));
    }
    public CircleSpeechBubble(View v, int cx, int cy)
    {
        super(v, cx, cy);
        radius = widgetWidth>widgetHeight?widgetHeight:widgetWidth;
        radius = radius/2 - widgetMargin;


        mTailWidget = new TriangleTail(cx, cy, radius);
        mTailWidget.setTailPoint(mCX - (radius+20), mCY + (radius+20));
    }

    public void draw(Canvas c){
        super.draw(c);

        radius = widgetWidth>widgetHeight?widgetHeight:widgetWidth;
        radius = radius/2 - widgetMargin;

        c.drawCircle(mCX, mCY, radius, mPaint);


        drawSelectedOutline(c);

        if(mHasTail){
            mTailWidget.drawTail(c);
        }
    }



//    public boolean inBubbleArea(int x, int y){
//        return super.inBubbleArea(x, y);
//
//
//    }


}
