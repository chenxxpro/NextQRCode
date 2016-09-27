package com.github.yoojia.qrcode.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public class QRCodeEncoder {

    public static final int DEF_BACKGROUND_COLOR = 0xFF_FF_FF_FF;
    public static final int DEF_POINT_COLOR = 0xFF_00_00_00;

    public static final String TAG = "QRCodeEncoder";

    private final int mBackgroundColor;
    private final int mPointColor;
    private final int mWidth;
    private final int mHeight;
    private final int mMargin;
    private final String mCharset;
    private final PixelsProcessor mPixelsProcessor;
    private final ErrorCorrectionLevel mErrorCorrectionLevel;
    private final List<BitmapProcessor> mBitmapProcessors;

    private final MultiFormatWriter mFormatWriter = new MultiFormatWriter();

    private QRCodeEncoder(Builder builder) {
        mBackgroundColor = builder.backgroundColor;
        mPointColor = builder.pointColor;
        mWidth = builder.width;
        mHeight = builder.height;
        mMargin = builder.margin;
        mCharset = builder.charset;
        mErrorCorrectionLevel = builder.errorCorrectionLevel;
        mBitmapProcessors = builder.processors;
        mPixelsProcessor = builder.pixelsProcessor == null ? new QRPixelsProcessor() : builder.pixelsProcessor;
        // Padding
        if(builder.padding > 0) {
            mBitmapProcessors.add(new PaddingProcessor(builder.padding, builder.backgroundColor));
        }
        // Center image
        if(builder.centerImage != null) {
            mBitmapProcessors.add(new CenterImageProcessor(builder.centerImage, builder.ratio));
        }
    }

    public Bitmap encode(String content) {
        if(content == null || content.length() == 0) {
            throw new IllegalArgumentException("Illegal encode content");
        }
        final Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, mCharset);
        hints.put(EncodeHintType.MARGIN, mMargin);
        hints.put(EncodeHintType.ERROR_CORRECTION, mErrorCorrectionLevel);
        BitMatrix result;
        try {
            result = mFormatWriter.encode(content, BarcodeFormat.QR_CODE, mWidth, mHeight, hints);
        } catch (Exception e) {
            Log.d(TAG, "Fail to encode content to QRCode bitmap", e);
            return null;
        }
        int tableW = result.getWidth();
        int tableH = result.getHeight();
        final int[] pixels = new int[tableW * tableH];
        for (int y = 0; y < tableH; y++) {
            int offset = y * tableW;
            for (int x = 0; x < tableW; x++) {
                pixels[offset + x] = result.get(x, y) ? mPointColor : mBackgroundColor;
            }
        }
        Bitmap bitmap = mPixelsProcessor.create(pixels, tableW, tableH);
        for (BitmapProcessor processor : mBitmapProcessors) {
            bitmap = processor.process(bitmap);
        }
        return bitmap;
    }


    public static class Builder {

        private int backgroundColor = DEF_BACKGROUND_COLOR;
        private int pointColor = DEF_POINT_COLOR;
        private int padding = 0;
        private int margin = 0;
        private int width = 500;
        private int height = 500;
        private ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.H;
        private Bitmap centerImage = null;
        private float ratio = 0.25f;
        private String charset = "UTF-8";
        private List<BitmapProcessor> processors = new ArrayList<>();
        private PixelsProcessor pixelsProcessor;

        public QRCodeEncoder build(){
            return new QRCodeEncoder(this);
        }

        public Builder backgroundColor(int color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder pointColor(int color) {
            this.pointColor = color;
            return this;
        }

        public Builder paddingPx(int paddingPx) {
            this.padding = paddingPx;
            return this;
        }

        public Builder marginPt(int marginPt) {
            this.margin = marginPt;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public Builder charset(PixelsProcessor processor) {
            this.pixelsProcessor = processor;
            return this;
        }

        public Builder errorCorrectionLevel(ErrorCorrectionLevel errorCorrectionLevel) {
            this.errorCorrectionLevel = errorCorrectionLevel;
            return this;
        }

        public Builder addBitmapProcessor(BitmapProcessor processor) {
            this.processors.add(processor);
            return this;
        }

        public Builder centerImage(Context context, int drawableResId) {
            return centerImage(BitmapFactory.decodeResource(context.getResources(), drawableResId));
        }

        public Builder centerImage(Bitmap centerImage) {
            this.centerImage = centerImage;
            return this;
        }

        public Builder centerImage(Context context, int drawableResId, int ratio) {
            return centerImage(BitmapFactory.decodeResource(context.getResources(), drawableResId), ratio);
        }

        public Builder centerImage(Bitmap centerImage, int ratio) {
            if(ratio > 40) {
                throw new IllegalArgumentException("Ratio of center image must be < 40");
            }
            this.centerImage = centerImage;
            this.ratio = ratio / 100f;
            return this;
        }

    }
}
