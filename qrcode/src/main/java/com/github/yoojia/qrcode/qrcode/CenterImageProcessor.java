package com.github.yoojia.qrcode.qrcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
class CenterImageProcessor implements BitmapProcessor {

    private final Bitmap mCenterImage;
    private final float mRatio;

    CenterImageProcessor(Bitmap centerImage, float ratio) {
        this.mCenterImage = centerImage;
        this.mRatio = ratio;
    }

    @Override
    public Bitmap process(Bitmap src) {
        final int resizeW = (int) (src.getWidth() * mRatio);
        final int resizeH = (int) (src.getHeight() * mRatio);
        final Bitmap newCenterImage = Bitmap.createScaledBitmap(mCenterImage, resizeW, resizeH, true);
        final int x = (src.getWidth() - resizeW) / 2;
        final int y = (src.getHeight() - resizeH) / 2;
        final Canvas canvas = new Canvas(src);
        canvas.drawBitmap(newCenterImage, x, y, null);
        return src;
    }

}
