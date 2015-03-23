package com.ccframework.jc.ccframework.BubbleWidget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Jin Cui on 3/22/2015.
 */
public class CircleSpeechBubble extends SpeechBubbleWidget {

    public CircleSpeechBubble(Paint p, View v, int cx, int cy)
    {
        super(p, v, cx, cy);
    }
    public CircleSpeechBubble(View v, int cx, int cy)
    {
        super(v, cx, cy);



    }

    public void draw(Canvas c){
        super.draw(c);

        int radius = widgetWidth>widgetHeight?widgetHeight:widgetWidth;
        radius = radius/2 - widgetMargin;

        c.drawCircle(mCenterXCoord, mCenterYCoord, radius, mPaint);

    }
}
