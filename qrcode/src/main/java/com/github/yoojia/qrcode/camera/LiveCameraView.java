package com.github.yoojia.qrcode.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public class LiveCameraView extends CameraPreviewView{

    private static final String TAG = LiveCameraView.class.getSimpleName();

    public LiveCameraView(Context context) {
        super(context);
    }

    public LiveCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Camera mCamera;
    private CaptureCallback mCaptureCallback;

    private final Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.i(TAG, "-> Got preview frame data");
            mCaptureCallback.onCaptured(Cameras.previewCapture(camera, data));
        }
    };

    private final DelayedFocusLooper mFocusLooper = new DelayedFocusLooper() {

        private final Camera.AutoFocusCallback mHandler = new Camera.AutoFocusCallback() {
            @Override public void onAutoFocus(boolean success, Camera camera) {
                if (success){
                    camera.setOneShotPreviewCallback(mPreviewCallback);
                }else{
                    Log.w(TAG, "-> Request focus, but fail !");
                }
            }
        };

        @Override public void callAutoFocus() {
            mCamera.autoFocus(mHandler);
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Cameras.openBackDefault();
        if (mCamera != null){
            setCamera(mCamera);
        }
        super.surfaceCreated(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        mFocusLooper.stop();
        if (mCamera != null){
            mCamera.release();
        }
    }

    /**
     * 启动自动对焦拍摄
     * @param delay 每次聚焦的延时时间，单位：毫秒
     * @param captureCallback 聚焦后的拍摄图片回调接口
     */
    public void startAutoCapture(int delay, CaptureCallback captureCallback) {
        mCaptureCallback = captureCallback;
        if (mCamera != null){
            mFocusLooper.start(delay);
        }else{
            Toast.makeText(getContext(), "OPEN CAMERA FAIL", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 停止自动对焦拍摄
     */
    public void stopAutoCapture(){
        mFocusLooper.stop();
    }

    /**
     * @return 返回当前设备是否支持自动对焦拍摄功能
     */
    public boolean isAutoCaptureSupported(){
        return Cameras.isAutoFocusSupported(mCamera);
    }
}
