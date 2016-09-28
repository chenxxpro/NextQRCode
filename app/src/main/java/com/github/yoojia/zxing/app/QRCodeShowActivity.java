package com.github.yoojia.zxing.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.yoojia.zxing.R;
import com.github.yoojia.qrcode.qrcode.QRCodeEncoder;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author :   Yoojia.Chen (yoojia.chen@gmail.com)
 * 生成二维码并展示的界面
 */
public class QRCodeShowActivity extends AppCompatActivity {

    private ImageView mQRCodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        setTitle("生成二维码");
        mQRCodeImage = (ImageView) findViewById(R.id.qrcode_image);
        final WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final Display display = manager.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        final int size = Math.min(displaySize.x, displaySize.y);
        generateQRCode(size);
    }

    private void generateQRCode(final int size){
        Observable.just("http://weixin.qq.com/r/p-kgOF-E5wKYrRtb96y1")
                .map(new Func1<String, Bitmap>() {
                    @Override public Bitmap call(String content) {
                        final Bitmap centerImage = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        return new QRCodeEncoder.Builder()
                                .width(size)
                                .height(size)
                                .paddingPx(0)
                                .marginPt(3)
                                .centerImage(centerImage)
                                .build()
                                .encode(content);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        mQRCodeImage.setImageBitmap(bitmap);
                    }
                });
    }
}
