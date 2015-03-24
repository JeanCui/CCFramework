package com.ccframework.jc.ccframework.BubbleWidget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.ccframework.jc.ccframework.AppConstants;
import com.ccframework.jc.ccframework.R;

/**
 * Created by Home on 3/22/2015.
 */
public class SpeechBubbleWidget {


    protected Paint mPaint;
    protected Paint mOutlinePaint;
//    protected Canvas mCanvas;
    protected int widgetMargin;
    protected int widgetWidth;
    protected int widgetHeight;
    protected int mTop, mLeft, mRight, mBottom;

    protected View mView; // View that this bubble to be drew on
    protected int mViewWidth;
    protected int mViewHeight;

    protected int mCX;
    protected int mCY;

    private boolean mOutline;
    protected boolean isSelected;
    protected int outlineStrokeWidth = AppConstants.BUBBLE_OUTLINE_WIDTH;



    public SpeechBubbleWidget(Paint p, View v, int cx, int cy)
    {
        mPaint = p;
        mOutlinePaint = new Paint();

        widgetWidth = AppConstants.BUBBLE_WIDGET_WIDTH;
        widgetHeight = AppConstants.BUBBLE_WIDGET_HEIGHT;
        mView = v;
        mViewWidth = mView.getWidth();
        mViewHeight = mView.getHeight();
        mCX = cx;
        mCY = cy;
        isSelected = true;
    }

    public SpeechBubbleWidget(View v, int cx, int cy){
        widgetWidth = AppConstants.BUBBLE_WIDGET_WIDTH;
        widgetHeight = AppConstants.BUBBLE_WIDGET_HEIGHT;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);

        mOutlinePaint = new Paint();
        mOutlinePaint.setAntiAlias(true);
        mOutlinePaint.setColor(v.getContext().getResources().getColor(R.color.sky_blue));
        mOutlinePaint.setStyle(Paint.Style.STROKE);
        mOutlinePaint.setStrokeWidth(AppConstants.BUBBLE_OUTLINE_WIDTH);


        mView = v;
        mViewWidth = mView.getWidth();
        mViewHeight = mView.getHeight();
        mCX = cx;
        mCY = cy;
        mOutline = false;

        isSelected = true;
    }

    public void setSelected(boolean b)
    {
        isSelected = b;
    }

    public void setColor(int c)
    {
        mPaint.setColor(c);

    }

    public void draw(Canvas c)
    {

    }

    public boolean inBubbleArea(int x, int y){
        mTop    = mCY - widgetHeight/2;
        mLeft   = mCX - widgetWidth/2;
        mRight  = mCX + widgetWidth/2;
        mBottom = mCY + widgetHeight/2;

        if(x > mLeft && x < mRight && y > mTop && y < mBottom)
        {
            return true;
        }
        return false;
    }

    public void setCX(int x){
        mCX = x;
    }
    public void setCY(int y){
        mCY = y;
    }
}
