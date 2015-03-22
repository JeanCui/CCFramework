package com.ccframework.jc.ccframework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import java.io.IOException;


public class DrawPanel extends SurfaceView implements Callback, OnTouchListener{
	
//	private Paint pen = new Paint();
//	private Path path = new Path();

    Context mainContext;


    private Bitmap bitmap = null;
    private Bitmap scaledBitmap;
    private int leftMargin = 10, topMargin = 10;
    private int scaleRectMargin = 5;
    private int scaleRectTouchMargin = 10;
    private int origWidth, origHeight, scaledWidth, scaledHeight;
    private float aspectRatio = 1;
    private Paint scaleRectPaint = new Paint();
    private Paint rectPaint = new Paint();
    private int scaleRectVerLeft =0, scaleRectVerTop =0, scaleRectVerRight =0, scaleRectVerBottom =0;
    private int scaleRectHorLeft =0, scaleRectHorTop =0, scaleRectHorRight =0, scaleRectHorBottom =0;
    private int scaleRectWidth = 30, scaleRectHeight = 80;
    private boolean pressScaleRect = false;

    private int viewHeight=0, viewWidth=0;

//    private Canvas canvas;
    private BitmapFactory.Options options;
    private String imgPath;

    private DrawPanelThread drawPanelThread = null;

    public int CANVAS_STATE = DO_NOTHING;
    // Status
    static final int DO_NOTHING =           0;
    static final int DRAW_CANVAS =          1;
    static final int CLEAR_CANVAS =         2;
    static final int LOAD_IMAGE_TO_CANVAS = 3;
    static final int SCALE_IMAGE =          4;

	public DrawPanel(Context context, AttributeSet attrs) {
		super(context, attrs);

        mainContext = context;
		getHolder().addCallback(this);
		setOnTouchListener(this);

//        drawPanelThread = new DrawPanelThread(this);

        scaleRectPaint.setColor(getResources().getColor(R.color.blue_scale_rect));
        scaleRectPaint.setStyle(Style.FILL);

        // Experimental code
        rectPaint.setColor(getResources().getColor(R.color.blue_scale_rect));
        rectPaint.setStyle(Style.STROKE);
        rectPaint.setStrokeWidth(5);
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
            drawPanelThread.setBackFromImagePick(true);
            MainActivity.PICK_IMAGE_FINISH = false;
            CANVAS_STATE = LOAD_IMAGE_TO_CANVAS;
        }else if(bitmap != null)
        {
            CANVAS_STATE = DRAW_CANVAS;
        }else
        {
            CANVAS_STATE = CLEAR_CANVAS;
        }


        drawPanelThread.setRunning(true);
        drawPanelThread.start();

//        Thread.State state = drawPanelThread.getState();
//        if ( state== Thread.State.NEW || state == Thread.State.TERMINATED)
//        {


//        }




		
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



    private boolean touchScaleRect(float x, float y)
    {
        int left = scaleRectHorLeft - scaleRectTouchMargin;
        int top = scaleRectVerTop - scaleRectTouchMargin;
        int right = scaleRectVerRight + scaleRectTouchMargin;
        int bottom = scaleRectHorBottom + scaleRectTouchMargin;
        if(x >= left && x <= right && y <= bottom && y >= top){
            pressScaleRect = true;
            return true;
        }
//        CANVAS_STATE = DRAW_CANVAS;
        return false;
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

        if(origHeight > viewHeight)
        {
            float desiredScale = (float) (viewHeight-50) / origHeight;
            scaledHeight =(int) (desiredScale*origHeight);
            scaledWidth = (int)(desiredScale*origWidth);

            bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

        }
        if(origWidth > viewWidth)
        {
            origWidth = bitmap.getWidth();
            origHeight = bitmap.getHeight();

            float desiredScale = (float) (viewWidth-50) / origWidth;
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


        if(getFutureScaleRectVerRight(scaledWidth) > viewWidth || getFutureScaleRectHorBottom(scaledHeight) > viewHeight)
            return false;

        moveScaleRect(scaledWidth, scaledHeight);

//        options.inJustDecodeBounds = false;
//        options.inDither = false;
//        options.inSampleSize = 1;
//        options.inScaled = true;
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        BitmapFactory.decodeFile(imgPath, options);
//
//        float desiredScale = (float)scaledWidth/bitmap.getWidth();
//
//        Matrix matrix = new Matrix();
//        matrix.postScale(desiredScale, desiredScale);
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
//        scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        return true;

    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction())
		{
            case MotionEvent.ACTION_DOWN:
                if(isBitmapNull())
                    return true;
                touchScaleRect(event.getX(), event.getY());
    //			path.moveTo(event.getX(), event.getY());
    //			draw();
                break;
            case MotionEvent.ACTION_MOVE:
                if(pressScaleRect )
                {

//                    drawPanelThread.setScaleCoord(event.getX(), event.getY());
//                    CANVAS_STATE = SCALE_IMAGE;
                    if(scaleImage(event.getX(), event.getY()))
                    {
                        CANVAS_STATE = DRAW_CANVAS;

//                        draw();
                    }
                }
    //			path.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                if(pressScaleRect)
                {
//                    Toast.makeText(mainContext, "Touched and Move ScaleRect", Toast.LENGTH_SHORT).show();
                    pressScaleRect = false;
                    CANVAS_STATE = DO_NOTHING;
                }
                break;
		}
		return true;
	}
	
	public void clear(){
//		path.reset();
//		draw();
	}
	

}
