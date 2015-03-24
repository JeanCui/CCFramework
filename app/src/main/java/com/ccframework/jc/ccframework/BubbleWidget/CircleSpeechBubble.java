package com.ccframework.jc.ccframework.BubbleWidget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Jin Cui on 3/22/2015.
 */
public class CircleSpeechBubble extends SpeechBubbleWidget {

    protected int radius;

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
        radius = 0;
    }
    public CircleSpeechBubble(View v, int cx, int cy)
    {
        super(v, cx, cy);

        radius = 0;

    }

    public void draw(Canvas c){
        super.draw(c);

        radius = widgetWidth>widgetHeight?widgetHeight:widgetWidth;
        radius = radius/2 - widgetMargin;

        c.drawCircle(mCX, mCY, radius, mPaint);


        drawSelectedOutline(c);
    }

//    public boolean inBubbleArea(int x, int y){
//        return super.inBubbleArea(x, y);
//
//
//    }


}
