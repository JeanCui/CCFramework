package com.ccframework.jc.ccframework.Helpers;

import android.os.CountDownTimer;

/**
 * Created by Home on 3/26/2015.
 */
public class CountDownTrigger extends CountDownTimer {
    CallbackInterface callbackInterface;



    public CountDownTrigger(CallbackInterface cb, long triggerTime, long interval)
    {
        super(triggerTime, interval);
        if ((cb == null)) throw new AssertionError();
        callbackInterface = cb;
        start();
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {
        callbackInterface.CallbackHandler();

    }
}
