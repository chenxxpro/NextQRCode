package com.github.yoojia.qrcode.qrcode;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public class QRCodeDecoder {

    public static final String TAG = "QRCodeDecoder";

    private final MultiFormatReader mFormatReader = new MultiFormatReader();

    private QRCodeDecoder(Builder builder) {
        final Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        final Collection<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        hints.put(DecodeHintType.CHARACTER_SET, builder.charset);
        mFormatReader.setHints(hints);
    }

    public String decode(Bitmap qrcodeBitmap) {
        final int width = qrcodeBitmap.getWidth();
        final int height = qrcodeBitmap.getHeight();
        final int[] pixels = new int[width * height];
        qrcodeBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        final RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            final Result rawResult = mFormatReader.decodeWithState(bitmap);
            return rawResult.getText();
        } catch (NotFoundException e) {
            Log.d(TAG, "Fail to decode bitmap to QRCode content", e);
            return null;
        }finally {
            mFormatReader.reset();
        }
    }

    public final static class Builder {

        private String charset = "UTF-8";

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public QRCodeDecoder build(){
            return new QRCodeDecoder(this);
        }
    }
}
