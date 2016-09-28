package com.github.yoojia.qrcode.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public interface CaptureCallback {

    void onCaptured(Bitmap bitmap);
}
