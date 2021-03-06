package com.ccframework.jc.ccframework;

/**
 * Created by Home on 3/22/2015.
 */
public final class AppConstants {

    // Canvas Status
    static final int DO_NOTHING =           0;
    static final int DRAW_CANVAS =          1;
    static final int CLEAR_CANVAS =         2;
    static final int LOAD_IMAGE_TO_CANVAS = 3;
    static final int SCALE_IMAGE =          4;

    // Touch event
    static final int TOUCH_EVENT_NONE = 0;
    static final int TOUCH_EVENT_SCALE_IMAGE = 1;
    static final int TOUCH_EVENT_DOWN_IN_BUBBLE = 2;
    static final int TOUCH_EVENT_ADD_BUBBLE = 3;
    static final int TOUCH_EVENT_MOVE_BUBBLE = 4;
    static final int TOUCH_EVENT_CLICK_BUBBLE = 5;

    // Speech Bubble
    public static final int BUBBLE_WIDGET_WIDTH = 200;
    public static final int BUBBLE_WIDGET_HEIGHT = 200;

    public static final int BUBBLE_OUTLINE_WIDTH = 8;
    public static final int BUBBLE_MOVE_TIMEOUT = 50;

    //Bubble Tail
    public static final float BUBBLE_TRIANGLE_DEGREE = 30;

}
