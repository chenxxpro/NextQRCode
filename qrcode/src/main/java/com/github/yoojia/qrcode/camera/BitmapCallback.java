package com.github.yoojia.qrcode.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public abstract class BitmapCallback implements Camera.PictureCallback {

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        onPictureTaken(BitmapFactory.decodeByteArray(data, 0, data.length));
    }

    public abstract void onPictureTaken(Bitmap bitmap);
}
