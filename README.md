# NextQRCode ZXing开源库的精简版

基于ZXing Android实现的二维码扫描支持库。
包括`生成二维码图片`、`解析二维码图片`和`相机扫描即时解码`三部分功能。

# 与原ZXingMini项目对比

**NextQRCode做了重大架构修改，原ZXingMini项目与当前NextQRCode不兼容**

# 依赖

```groovy
dependencies {
    compile 'com.github.yoojia:next-qrcode:2.0-2'
    ...
}
```

# 生成二维码图案

 ```java
 // 二维码中间图标
 final Bitmap centerImage = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
 // 生成的二维码图案
 Bitmap qrCodeImage = new QRCodeEncoder.Builder()
         .width(size) // 二维码图案的宽度
         .height(size)
         .paddingPx(0) // 二维码的内边距
         .marginPt(3) // 二维码的外边距
         .centerImage(centerImage) // 二维码中间图标
         .build()
         .encode(content);

 ```

# 二维码解码

```java
final QRCodeDecoder mDecoder = new QRCodeDecoder.Builder().build();
// 传入二维码图案Bitmap对象然后解码成文本内容
String content = mDecoder.decode(bitmap);
```

# 使用 LiveCameraView 自动扫描解码

```java
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

```