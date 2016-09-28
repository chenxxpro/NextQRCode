package com.github.yoojia.qrcode.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import java.io.ByteArrayOutputStream;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public class Cameras {

    /**
     * 打开相机设备
     * @param cameraId 相机ID
     * @return Camera
     */
    public static Camera open(int cameraId) {
        try{
            return Camera.open(cameraId);
        }catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    /**
     * 是否存在相机设备
     * @param context Context
     */
    public static boolean hasCameraDevice(Context context) {
        return context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 是否支持自动对焦
     * @param camera Camera
     */
    public static boolean isAutoFocusSupported(Camera camera) {
        return camera.getParameters().getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO);
    }

    /**
     * 设置相机预览方式为跟随屏幕方向
     * @param context Context
     * @param camera Camera
     */
    public static void followScreenOrientation(Context context, Camera camera){
        final int orientation = context.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            camera.setDisplayOrientation(180);
        }else if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            camera.setDisplayOrientation(90);
        }
    }

    public static Bitmap previewCapture(Camera camera, byte[] data){
        final Camera.Parameters parameters = camera.getParameters();
        final int width = parameters.getPreviewSize().width;
        final int height = parameters.getPreviewSize().height;
        final YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), width, height, null);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuv.compressToJpeg(new Rect(0, 0, width, height), 100, out);// Best
        final byte[] bytes = out.toByteArray();
        final Bitmap src = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        final Matrix matrix = new Matrix();
        matrix.setRotate(90);
        final int originWidth = src.getWidth();
        final int originHeight = src.getHeight();
        final int targetWH = originWidth > originHeight ? originHeight : originWidth;
        final int offsetX = originWidth > originHeight ? (originWidth - originHeight): 0;
        final int offsetY = originWidth > originHeight ? 0 : (originHeight - originWidth);
        return Bitmap.createBitmap(src, offsetX, offsetY, targetWH, targetWH, matrix, true);
    }
}
