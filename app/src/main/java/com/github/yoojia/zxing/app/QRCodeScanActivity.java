package com.github.yoojia.zxing.app;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.yoojia.qrcode.camera.CameraPreviewView;
import com.github.yoojia.qrcode.camera.CaptureCallback;
import com.github.yoojia.qrcode.camera.LiveCameraView;
import com.github.yoojia.qrcode.qrcode.QRCodeDecoder;
import com.github.yoojia.zxing.R;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author :   Yoojia.Chen (yoojia.chen@gmail.com)
 * 扫描二维码
 */
public class QRCodeScanActivity extends AppCompatActivity {

    public static final String TAG = QRCodeScanActivity.class.getSimpleName();

    private LiveCameraView mLiveCameraView;
    private ImageView mCaptureImage;
    private TextView mContentView;

    private final CaptureCallback mCaptureCallback = new CaptureCallback() {
        @Override public void onCaptured(Bitmap bitmap) {
            Log.i(TAG, "-> Got bitmap, show to capture view");
            mCaptureImage.setImageBitmap(bitmap);
            Observable.just(bitmap)
                    .map(new Func1<Bitmap, String>() {
                        private final QRCodeDecoder mDecoder = new QRCodeDecoder.Builder().build();
                        @Override
                        public String call(Bitmap bitmap) {
                            return mDecoder.decode(bitmap);
                        }
                    })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override public void call(String content) {
                            mContentView.setText(content);
                        }
                    });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_scan);
        mCaptureImage = (ImageView) findViewById(R.id.capture_image);
        mContentView = (TextView) findViewById(R.id.content);
        mLiveCameraView = (LiveCameraView) findViewById(R.id.capture_preview_view);
        mLiveCameraView.setPreviewReadyCallback(new CameraPreviewView.PreviewReadyCallback() {
            @Override
            public void onStarted(Camera camera) {
                Log.i(TAG, "-> Camera started, start to auto capture");
                mLiveCameraView.startAutoCapture(1500, mCaptureCallback);
            }

            @Override
            public void onStopped() {
                Log.i(TAG, "-> Camera stopped");
                mLiveCameraView.stopAutoCapture();
            }
        });
    }

}
