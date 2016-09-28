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

    private final Handler mDelayedHandler = new Handler(new Handler.Callback() {
        @Override public boolean handleMessage(Message msg) {
            Log.i(TAG, "-> Call auto focus");
            callAutoFocus();
            if (mCanNextFocus) {
                sendNextAutoFocus(mPeriod);
            }
            return true;
        }
    });

    private boolean mCanNextFocus = false;
    private int mPeriod = 1000;

    public void start(int period) {
        mCanNextFocus = true;
        mPeriod = Math.abs(period);
        Log.i(TAG, "-> Start auto focus with period: " + period);
        sendNextAutoFocus(0/*first: no delay*/);
    }

    public void stop(){
        Log.i(TAG, "-> Stop auto focus");
        mCanNextFocus = false;
        mDelayedHandler.removeMessages(MSG_FOCUS);
    }

    private void sendNextAutoFocus(int period){
        if (period == 0){
            mDelayedHandler.sendEmptyMessage(MSG_FOCUS);
        }else{
            mDelayedHandler.sendEmptyMessageDelayed(MSG_FOCUS, period);
        }
    }

    public abstract void callAutoFocus();
}
