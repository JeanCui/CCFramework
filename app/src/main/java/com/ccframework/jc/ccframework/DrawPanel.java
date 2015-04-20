package com.ccframework.jc.ccframework;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.CountDownTimer;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.ccframework.jc.ccframework.BubbleWidget.CircleSpeechBubble;
import com.ccframework.jc.ccframework.BubbleWidget.SpeechBubbleWidget;
import com.ccframework.jc.ccframework.Helpers.CallbackInterface;
import com.ccframework.jc.ccframework.Helpers.CountDownTrigger;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.IOException;
import java.util.ArrayList;


public class DrawPanel extends SurfaceView implements Callback, OnTouchListener, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{

    Context mainContext;


    private static final String TAG = "DRAWPANEL";
    private Bitmap bitmap = null;
    private Bitmap scaledBitmap;
    private int leftMargin = 10, topMargin = 10;
    private int scaleRectMargin = 5;
    private int scaleRectTouchMargin = 10;
    private int origWidth, origHeight, scaledWidth, scaledHeight;
    private float aspectRatio = 1;
    private Paint scaleRectPaint = new Paint();

    private int scaleRectVerLeft =0, scaleRectVerTop =0, scaleRectVerRight =0, scaleRectVerBottom =0;
    private int scaleRectHorLeft =0, scaleRectHorTop =0, scaleRectHorRight =0, scaleRectHorBottom =0;
    private int scaleRectWidth = 30, scaleRectHeight = 80;
    private boolean pressScaleRect = false;

    private int viewHeight=0, viewWidth=0;

//    private Canvas canvas;
    private BitmapFactory.Options options;
    private String imgPath;

    private DrawPanelThread drawPanelThread = null;

    // STATUS
    public int TOUCH_EVENT = AppConstants.TOUCH_EVENT_NONE;
    public int CANVAS_STATE = AppConstants.DO_NOTHING;




    // Speech Bubble
    ArrayList<SpeechBubbleWidget> bubblesList = new ArrayList<SpeechBubbleWidget>();
    private static int mCurrentBubbleId = -1;

	public DrawPanel(Context context, AttributeSet attrs) {
		super(context, attrs);

        mainContext = context;
		getHolder().addCallback(this);
		setOnTouchListener(this);

        scaleRectPaint.setColor(getResources().getColor(R.color.sky_blue));
        scaleRectPaint.setStyle(Style.FILL);


        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this.getContext(),this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);


	}
	public void clearCanvas(Canvas canvas)
    {
//        canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.GRAY);
//        getHolder().unlockCanvasAndPost(canvas);
    }
	public void draw(Canvas canvas){
//		canvas = getHolder().lockCanvas();

		canvas.drawColor(Color.GRAY);
        canvas.drawBitmap(scaledBitmap, leftMargin, topMargin,null);
        canvas.drawRect(scaleRectVerLeft, scaleRectVerTop, scaleRectVerRight, scaleRectVerBottom, scaleRectPaint);
        canvas.drawRect(scaleRectHorLeft, scaleRectHorTop, scaleRectHorRight, scaleRectHorBottom, scaleRectPaint);

        // draw bubbles
        for (SpeechBubbleWidget sb : bubblesList){
            sb.draw(canvas);
        }

//		getHolder().unlockCanvasAndPost(canvas);
	}

    public boolean isBitmapNull()
    {
        if(bitmap == null)
            return true;
        return false;
    }


	@Override
	public void surfaceCreated(SurfaceHolder holder) {

        if(drawPanelThread==null)
        {
            drawPanelThread = new DrawPanelThread(this);
        }
//        clearCanvas();
        if (MainActivity.PICK_IMAGE_FINISH) {

            imgPath = MainActivity.mImagePath;
//            drawPanelThread.setBackFromImagePick(true);
            MainActivity.PICK_IMAGE_FINISH = false;
            CANVAS_STATE = AppConstants.LOAD_IMAGE_TO_CANVAS;
        }else if(bitmap != null)
        {
            CANVAS_STATE = AppConstants.DRAW_CANVAS;
        }else
        {
            CANVAS_STATE = AppConstants.CLEAR_CANVAS;
        }


        drawPanelThread.setRunning(true);
        drawPanelThread.start();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
        drawPanelThread.setRunning(false);
        while (retry){
            try{
                drawPanelThread.join();
                retry = false;
            }catch (InterruptedException e)
            {
                Log.e("DRAW PANEL", e.getMessage());
            }
        }


        drawPanelThread = null;
		
	}

    private void createNewBubble(int x, int y)
    {
        for(SpeechBubbleWidget sbw : bubblesList){
            sbw.setSelected(false);
        }
        bubblesList.add(new CircleSpeechBubble(this, (int)x, (int)y));
    }

    private void updateSelectedBubble(int bID){
        if(mCurrentBubbleId == -1 || bID == mCurrentBubbleId)
            return;

        bubblesList.get(mCurrentBubbleId).setSelected(false);

        mCurrentBubbleId = bID;
        SpeechBubbleWidget cur = bubblesList.get(mCurrentBubbleId);
        cur.setSelected(true);
    }


    private int touchDownBubbleId = -1;
    private int inABubbleArea(int x, int y){
        int current = 0;
        for (SpeechBubbleWidget sbw : bubblesList){
            if(sbw.inBubbleArea(x, y))
            {
                return current;
            }
            current ++;
        }
        return -1;
    }

    private void touchEventInitialize(){
        TOUCH_EVENT = AppConstants.TOUCH_EVENT_NONE;
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {


        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        Log.i(TAG, "----onSingleTapUp");
//        touchDownBubbleId = inABubbleArea((int)e.getX(), (int)e.getY());
        // One Bubble Is Selected
        if(TOUCH_EVENT == AppConstants.TOUCH_EVENT_DOWN_IN_BUBBLE )
        {

            moveBubbleCountDown.cancel();
//            updateSelectedBubble(touchDownBubbleId);
//            CANVAS_STATE = AppConstants.DRAW_CANVAS;

            // Reinit TOUCH_EVENT
            touchEventInitialize();
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

        Log.i(TAG, "----onLongPress");

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    CountDownTrigger moveBubbleCountDown;
    class MoveBubbleCallback implements CallbackInterface{
        @Override
        public void CallbackHandler() {
            TOUCH_EVENT = AppConstants.TOUCH_EVENT_MOVE_BUBBLE;
        }
    }


    private void touchDownEventDetect(float x, float y)
    {
        int left = scaleRectHorLeft - scaleRectTouchMargin;
        int top = scaleRectVerTop - scaleRectTouchMargin;
        int right = scaleRectVerRight + scaleRectTouchMargin;
        int bottom = scaleRectHorBottom + scaleRectTouchMargin;

        // Touch down on Scale Rect area
        if(x >= left && x <= right && y <= bottom && y >= top){
            TOUCH_EVENT = AppConstants.TOUCH_EVENT_SCALE_IMAGE;
            pressScaleRect = true;
            return;

        }


        touchDownBubbleId = inABubbleArea((int)x, (int)y);


        // IF IT's NOT IN BUBBLE AREA
        if(touchDownBubbleId == -1)
         //if click on empty space
        {
            // Add Bubble
            TOUCH_EVENT = AppConstants.TOUCH_EVENT_ADD_BUBBLE;

            // create bubble
            createNewBubble((int) x, (int) y);


            mCurrentBubbleId = bubblesList.size() - 1;
            CANVAS_STATE = AppConstants.DRAW_CANVAS;

            // Added and be able to move
            TOUCH_EVENT = AppConstants.TOUCH_EVENT_DOWN_IN_BUBBLE;
            touchDownBubbleId = mCurrentBubbleId;
            moveBubbleCountDown = new CountDownTrigger(new MoveBubbleCallback(), AppConstants.BUBBLE_MOVE_TIMEOUT, AppConstants.BUBBLE_MOVE_TIMEOUT);
        }else
        {
            TOUCH_EVENT = AppConstants.TOUCH_EVENT_DOWN_IN_BUBBLE;

            updateSelectedBubble(touchDownBubbleId);
            CANVAS_STATE = AppConstants.DRAW_CANVAS;

            moveBubbleCountDown = new CountDownTrigger(new MoveBubbleCallback(), AppConstants.BUBBLE_MOVE_TIMEOUT, AppConstants.BUBBLE_MOVE_TIMEOUT);
            Log.i(TAG, "----ACTION DOWN");
        }

    }

    private void moveScaleRect(int width, int height)
    {
        scaleRectVerLeft = width + leftMargin + scaleRectMargin;
        scaleRectVerRight = scaleRectVerLeft + scaleRectWidth;
        scaleRectVerTop = height + topMargin - (scaleRectHeight - scaleRectWidth - scaleRectMargin);
        scaleRectVerBottom = scaleRectVerTop + scaleRectHeight;

        scaleRectHorLeft = width + leftMargin - (scaleRectHeight - scaleRectWidth - scaleRectMargin);
        scaleRectHorRight = scaleRectHorLeft + scaleRectHeight;
        scaleRectHorTop = height + topMargin + scaleRectMargin;
        scaleRectHorBottom = scaleRectHorTop + scaleRectWidth;
    }

    public void updateImage(Canvas canvas) throws IOException {

        options = new BitmapFactory.Options();


//        options.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(imgPath, options);

        origWidth = bitmap.getWidth();
        origHeight = bitmap.getHeight();


        viewWidth = getWidth();
        viewHeight = getHeight();

        if(origHeight > viewHeight - scaleRectMargin - scaleRectWidth - leftMargin)
        {
            float desiredScale = (float) (viewHeight - scaleRectMargin - scaleRectWidth - leftMargin) / origHeight;
            scaledHeight =(int) (desiredScale*origHeight);
            scaledWidth = (int)(desiredScale*origWidth);

            bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

        }
        if(origWidth > viewWidth - scaleRectMargin - scaleRectWidth - leftMargin)
        {
            origWidth = bitmap.getWidth();
            origHeight = bitmap.getHeight();

            float desiredScale = (float) (viewWidth- scaleRectMargin - scaleRectWidth - leftMargin) / origWidth;
            scaledHeight =(int) (desiredScale*origHeight);
            scaledWidth = (int)(desiredScale*origWidth);

            bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

            origWidth = bitmap.getWidth();
            origHeight = bitmap.getHeight();
        }

        aspectRatio = origWidth/(float)origHeight;



        scaleRectVerLeft = origWidth + leftMargin + scaleRectMargin;
        scaleRectVerRight = scaleRectVerLeft + scaleRectWidth;
        scaleRectVerTop = origHeight + topMargin - (scaleRectHeight - scaleRectWidth - scaleRectMargin);
        scaleRectVerBottom = scaleRectVerTop + scaleRectHeight;

        scaleRectHorLeft = origWidth + leftMargin - (scaleRectHeight - scaleRectWidth - scaleRectMargin);
        scaleRectHorRight = scaleRectHorLeft + scaleRectHeight;
        scaleRectHorTop = origHeight + topMargin + scaleRectMargin;
        scaleRectHorBottom = scaleRectHorTop + scaleRectWidth;

        scaledBitmap = bitmap;
        draw(canvas);
    }

    private int getFutureScaleRectVerRight(int width)
    {
        return width + leftMargin + scaleRectMargin + scaleRectWidth;
    }
    private int getFutureScaleRectHorBottom(int height)
    {
        return height + topMargin + scaleRectMargin + scaleRectWidth;
    }

    public boolean scaleImage(float x, float y)
    {
        float newX = y * aspectRatio;
        scaledWidth = (int)newX;
        scaledHeight = (int)y;


        if(getFutureScaleRectVerRight(scaledWidth)+scaleRectMargin > viewWidth || getFutureScaleRectHorBottom(scaledHeight)+scaleRectMargin > viewHeight)
            return false;

        moveScaleRect(scaledWidth, scaledHeight);

        scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

        return true;
    }
    public void moveBubble(float x, float y){
        if(touchDownBubbleId == -1)
            return;

        SpeechBubbleWidget currentBubble = bubblesList.get(touchDownBubbleId);
        currentBubble.move((int)x, (int)y);
//        currentBubble.setCX((int)x);
//        currentBubble.setCY((int)y);
    }

    private GestureDetectorCompat mDetector;
    @Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction())
		{

            case MotionEvent.ACTION_DOWN:
                if(isBitmapNull())
                    return true;

                touchDownEventDetect(event.getX(), event.getY());

                break;

            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "----ACTION MOVE");
                switch (TOUCH_EVENT)
                {
                    case AppConstants.TOUCH_EVENT_SCALE_IMAGE:
                        if(scaleImage(event.getX(), event.getY()))
                        {
                            CANVAS_STATE = AppConstants.DRAW_CANVAS;
                        }
                        break;

                    case AppConstants.TOUCH_EVENT_MOVE_BUBBLE:
                        moveBubble(event.getX(), event.getY());
                        CANVAS_STATE = AppConstants.DRAW_CANVAS;
                        break;

                    default:
                        break;
                }
//                if(pressScaleRect )
//                {
//
//                    if(scaleImage(event.getX(), event.getY()))
//                    {
//                        CANVAS_STATE = AppConstants.DRAW_CANVAS;
//                    }
//                }
                break;
            case MotionEvent.ACTION_UP:
                switch (TOUCH_EVENT){
                    case AppConstants.TOUCH_EVENT_SCALE_IMAGE:
                        pressScaleRect = false;
                        CANVAS_STATE = AppConstants.DO_NOTHING;
                        touchEventInitialize();
                        break;
                    case AppConstants.TOUCH_EVENT_ADD_BUBBLE:
                        touchEventInitialize();
                        break;
                    case AppConstants.TOUCH_EVENT_MOVE_BUBBLE:
                        touchEventInitialize();
                        break;

                }

//                if(pressScaleRect)
//                {
//                    pressScaleRect = false;
//                    CANVAS_STATE = AppConstants.DO_NOTHING;
//                }
                break;
		}
        this.mDetector.onTouchEvent(event);
		return true;
	}
	
	public void clear(){
//		path.reset();
//		draw();
	}
	

}
