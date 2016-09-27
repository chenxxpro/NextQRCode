package com.github.yoojia.qrcode.camera;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public abstract class DelayedFocusLooper {

    public static final String TAG = DelayedFocusLooper.class.getSimpleName();

    private static final int MSG_FOCUS = 999;

    private boolean mAllowNextFocus = false;

    private final Handler mDelayedHandler = new Handler(new Handler.Callback() {
        @Override public boolean handleMessage(Message msg) {
            Log.i(TAG, "-> Call auto focus");
            callAutoFocus();
            if (mAllowNextFocus) {
                sendNextAutoFocus(mPeriod);
            }
            return true;
        }
    });

    private int mPeriod = 1000;

    public void start(int period) {
        mAllowNextFocus = true;
        mPeriod = period;
        Log.i(TAG, "-> Start auto focus with period: " + period);
        sendNextAutoFocus(period);
    }

    public void stop(){
        Log.i(TAG, "-> Stop auto focus");
        mAllowNextFocus = false;
        mDelayedHandler.removeMessages(MSG_FOCUS);
    }

    private void sendNextAutoFocus(int period){
        mDelayedHandler.sendEmptyMessageDelayed(MSG_FOCUS, period);
    }

    public abstract void callAutoFocus();
}
