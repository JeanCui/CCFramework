package com.ccframework.jc.ccframework;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Home on 3/15/2015.
 */
public class DrawPanelThread extends Thread {
    private DrawPanel drawPanel;
    private boolean running = false;
    private boolean backFromImagePick = false;
    private String mImagePath;

    private float scaledImageXCoord = 0, scaledImageYCoord = 0;

    // Status
    static final int DO_NOTHING =           0;
    static final int DRAW_CANVAS =          1;
    static final int CLEAR_CANVAS =         2;
    static final int LOAD_IMAGE_TO_CANVAS = 3;
    static final int SCALE_IMAGE =          4;



    public DrawPanelThread(DrawPanel panel)
    {
        drawPanel = panel;
    }

    public void setRunning(boolean run)
    {
        running = run;
    }

    public void setBackFromImagePick(boolean b)
    {
        backFromImagePick = b;
    }


    public void run()
    {
        Log.d("DRAW THREAD", "running");

        while (running){
            Canvas canvas = null;
            try{
                if(drawPanel.CANVAS_STATE == DO_NOTHING)
                    continue;


                Log.d("DRAW THREAD", Integer.toString(drawPanel.CANVAS_STATE));
                canvas = drawPanel.getHolder().lockCanvas();
                synchronized (drawPanel.getHolder()) {
                    switch (drawPanel.CANVAS_STATE) {

                        case DRAW_CANVAS:
                            drawPanel.draw(canvas);

                            break;
                        case CLEAR_CANVAS:
                            canvas.drawColor(Color.GRAY);
//                            drawPanel.CANVAS_STATE = DO_NOTHING;
                            break;
                        case LOAD_IMAGE_TO_CANVAS:
                            canvas.drawColor(Color.GRAY);
                            drawPanel.updateImage(canvas);
//                            drawPanel.CANVAS_STATE = DO_NOTHING;
                            break;
                        case SCALE_IMAGE:
                            if (drawPanel.scaleImage(scaledImageXCoord, scaledImageYCoord)) {
                                drawPanel.draw(canvas);
                            }
                            break;
                        default:
                            break;
                    }
                    drawPanel.CANVAS_STATE = DO_NOTHING;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (canvas != null){
                    drawPanel.getHolder().unlockCanvasAndPost(canvas);
                }
            }

        }
    }

    public void setScaleCoord(float x, float y) {
        scaledImageXCoord = x;

        scaledImageYCoord = y;
    }
}
