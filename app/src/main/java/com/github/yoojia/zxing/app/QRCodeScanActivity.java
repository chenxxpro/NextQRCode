package com.github.yoojia.zxing.app;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.github.yoojia.qrcode.camera.Cameras;
import com.github.yoojia.qrcode.camera.LiveCameraView;
import com.github.yoojia.zxing.R;

/**
 * @author :   Yoojia.Chen (yoojia.chen@gmail.com)
 * 扫描二维码
 */
public class QRCodeScanActivity extends AppCompatActivity {

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
