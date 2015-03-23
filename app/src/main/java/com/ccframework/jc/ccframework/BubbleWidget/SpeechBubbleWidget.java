package com.ccframework.jc.ccframework.BubbleWidget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.ccframework.jc.ccframework.AppConstants;

/**
 * Created by Home on 3/22/2015.
 */
public class SpeechBubbleWidget {


    protected Paint mPaint;
//    protected Canvas mCanvas;
    protected int widgetMargin;
    protected int widgetWidth;
    protected int widgetHeight;

    protected View mView; // View that this bubble to be drew on
    protected int mViewWidth;
    protected int mViewHeight;

    protected int mCenterXCoord;
    protected int mCenterYCoord;

    private boolean mOutline;



    public SpeechBubbleWidget(Paint p, View v, int cx, int cy)
    {
        mPaint = p;

        widgetWidth = AppConstants.BUBBLE_WIDGET_WIDTH;
        widgetHeight = AppConstants.BUBBLE_WIDGET_HEIGHT;
        mView = v;
        mViewWidth = mView.getWidth();
        mViewHeight = mView.getHeight();
        mCenterXCoord = cx;
        mCenterYCoord = cy;
    }

    public SpeechBubbleWidget(View v, int cx, int cy){
        widgetWidth = AppConstants.BUBBLE_WIDGET_WIDTH;
        widgetHeight = AppConstants.BUBBLE_WIDGET_HEIGHT;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);

        mView = v;
        mViewWidth = mView.getWidth();
        mViewHeight = mView.getHeight();
        mCenterXCoord = cx;
        mCenterYCoord = cy;
        mOutline = false;
    }

    public void setColor(int c)
    {
        mPaint.setColor(c);

    }

    public void draw(Canvas c)
    {

    }
}
