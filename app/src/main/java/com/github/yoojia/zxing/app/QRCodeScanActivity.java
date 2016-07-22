package com.github.yoojia.zxing.app;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.github.yoojia.zxing.R;
import com.github.yoojia.zxing2.camera.Cameras;
import com.github.yoojia.zxing2.camera.LiveCameraView;

/**
 * @author :   Yoojia.Chen (yoojia.chen@gmail.com)
 * 扫描二维码
 */
public class QRCodeScanActivity extends Activity{

    private LiveCameraView mCameraView;

    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_scan);
        mCameraView = (LiveCameraView) findViewById(R.id.capture_preview_view);
        mCamera = Cameras.open(0);
        mCameraView.setCamera(mCamera);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.release();
    }
}
