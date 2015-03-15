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

    private int viewHeight=0, viewWidth=0;

    private Canvas canvas;
    BitmapFactory.Options options;
    private String imgPath;

	public DrawPanel(Context context, AttributeSet attrs) {
		super(context, attrs);

        mainContext = context;
		getHolder().addCallback(this);
		setOnTouchListener(this);

        scaleRectPaint.setColor(getResources().getColor(R.color.blue_scale_rect));
        scaleRectPaint.setStyle(Style.FILL);

        // Experimental code
        rectPaint.setColor(getResources().getColor(R.color.blue_scale_rect));
        rectPaint.setStyle(Style.STROKE);
        rectPaint.setStrokeWidth(5);
	}
	public void drawCanvas()
    {
        canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.GRAY);
        getHolder().unlockCanvasAndPost(canvas);
    }
	public void draw(){
		canvas = getHolder().lockCanvas();

		canvas.drawColor(Color.GRAY);
        canvas.drawBitmap(scaledBitmap, leftMargin, topMargin,null);
        canvas.drawRect(scaleRectVerLeft, scaleRectVerTop, scaleRectVerRight, scaleRectVerBottom, scaleRectPaint);
        canvas.drawRect(scaleRectHorLeft, scaleRectHorTop, scaleRectHorRight, scaleRectHorBottom, scaleRectPaint);

		getHolder().unlockCanvasAndPost(canvas);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

        drawCanvas();
        if(MainActivity.PICK_IMAGE_FINISH)
        {
            try {
                drawImage(MainActivity.mImagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            MainActivity.PICK_IMAGE_FINISH = false;
        }

        if(bitmap != null)
        {
            draw();
        }
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

    private boolean pressScaleRect = false;

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

    public void drawImage(String imagePath) throws IOException {

        options = new BitmapFactory.Options();

        imgPath = imagePath;
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
        draw();
    }

    private int getScaleRectVerRight()
    {
        return scaleRectVerRight;
    }
    private int getScaleRectHorBottom()
    {
        return scaleRectHorBottom;
    }

    private boolean scaleImage(float x, float y)
    {
        float newX = y * aspectRatio;
        scaledWidth = (int)newX;
        scaledHeight = (int)y;

        moveScaleRect(scaledWidth, scaledHeight);
        if(getScaleRectVerRight() > viewWidth || getScaleRectHorBottom() > viewHeight)
            return false;

        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = 1;
        options.inScaled = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(imgPath, options);

        float desiredScale = (float)scaledWidth/bitmap.getWidth();

        Matrix matrix = new Matrix();
        matrix.postScale(desiredScale, desiredScale);
        scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        return true;

    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction())
		{
            case MotionEvent.ACTION_DOWN:
                touchScaleRect(event.getX(), event.getY());
    //			path.moveTo(event.getX(), event.getY());
    //			draw();
                break;
            case MotionEvent.ACTION_MOVE:
                if(pressScaleRect)
                {
                    if(scaleImage(event.getX(), event.getY()))
                        draw();
                }
    //			path.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                if(pressScaleRect)
                {
//                    Toast.makeText(mainContext, "Touched and Move ScaleRect", Toast.LENGTH_SHORT).show();
                    pressScaleRect = false;
                }
                break;
		}
		return true;
	}
	
	public void clear(){
//		path.reset();
		draw();
	}
	

}
