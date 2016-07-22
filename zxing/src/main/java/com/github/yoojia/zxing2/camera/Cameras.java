package com.github.yoojia.zxing2.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.Surface;

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

    public static void followScreenOrientation(Context context, Camera camera){
        final int orientation = context.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            camera.setDisplayOrientation(180);
        }else if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            camera.setDisplayOrientation(90);
        }
    }
}
