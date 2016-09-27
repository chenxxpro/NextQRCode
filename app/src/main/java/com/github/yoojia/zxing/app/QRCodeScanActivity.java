package com.github.yoojia.zxing.app;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.yoojia.qrcode.camera.CaptureCallback;
import com.github.yoojia.qrcode.camera.CameraPreviewView;
import com.github.yoojia.qrcode.camera.LiveCameraView;
import com.github.yoojia.zxing.R;

/**
 * @author :   Yoojia.Chen (yoojia.chen@gmail.com)
 * 扫描二维码
 */
public class QRCodeScanActivity extends AppCompatActivity {

    public static final String TAG = QRCodeScanActivity.class.getSimpleName();

    private LiveCameraView mLiveCameraView;

    private ImageView mCaptureImage;

    private final CaptureCallback mCaptureCallback = new CaptureCallback() {
        @Override public void onCaptured(Bitmap bitmap) {
            Log.i(TAG, "-> Got bitmap, show to capture view");
            mCaptureImage.setImageBitmap(bitmap);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_scan);
        mCaptureImage = (ImageView) findViewById(R.id.capture_image);
        mLiveCameraView = (LiveCameraView) findViewById(R.id.capture_preview_view);
        mLiveCameraView.setPreviewReadyCallback(new CameraPreviewView.PreviewReadyCallback() {
            @Override
            public void onStarted(Camera camera) {
                Log.i(TAG, "-> Camera started, start to auto capture");
                mLiveCameraView.startAutoCapture(2500, mCaptureCallback);
            }

            @Override
            public void onStopped() {
                Log.i(TAG, "-> Camera stopped");
                mLiveCameraView.stopAutoCapture();
            }
        });
    }

}
