package com.github.yoojia.qrcode.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public class CameraPreviewView extends SurfaceView implements SurfaceHolder.Callback {

    private final static String TAG = CameraPreviewView.class.getSimpleName();

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private PreviewReadyCallback mPreviewReadyCallback;

    public CameraPreviewView(Context context) {
        super(context);
        init();
    }

    public CameraPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraPreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Start preview display[SURFACE-CREATED]");
        startPreviewDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mSurfaceHolder.getSurface() == null){
            return;
        }
        Cameras.followScreenOrientation(getContext(), mCamera);
        Log.d(TAG, "Restart preview display[SURFACE-CHANGED]");
        stopPreviewDisplay();
        startPreviewDisplay(mSurfaceHolder);
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        checkCamera();
        final Camera.Parameters params = mCamera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        params.setSceneMode(Camera.Parameters.SCENE_MODE_BARCODE);
    }

    public void setPreviewReadyCallback(PreviewReadyCallback previewCallback) {
        mPreviewReadyCallback = previewCallback;
    }

    private void startPreviewDisplay(SurfaceHolder holder){
        checkCamera();
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "Error while START preview for camera", e);
        }
        if (mPreviewReadyCallback != null) {
            mPreviewReadyCallback.onStarted(mCamera);
        }
    }

    private void stopPreviewDisplay(){
        checkCamera();
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            Log.e(TAG, "Error while STOP preview for camera", e);
        }
        if (mPreviewReadyCallback != null) {
            mPreviewReadyCallback.onStopped();
        }
    }

    private void checkCamera(){
        if(mCamera == null) {
            throw new IllegalStateException("Camera must be set when start/stop preview, call <setCamera(Camera)> to set");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Stop preview display[SURFACE-DESTROYED]");
        stopPreviewDisplay();
    }

    public interface PreviewReadyCallback {
        /**
         * 预览功能已准备完成
         * @param camera 相机对象
         */
        void onStarted(Camera camera);

        /**
         * 预览功能已停止
         */
        void onStopped();
    }
}
