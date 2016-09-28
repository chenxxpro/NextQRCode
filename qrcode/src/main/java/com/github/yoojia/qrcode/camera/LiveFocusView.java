package com.github.yoojia.qrcode.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public class LiveFocusView extends View{

    public LiveFocusView(Context context) {
        super(context);
        init();
    }

    public LiveFocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveFocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LiveFocusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private final Paint mRectPaint = new Paint();
    private final Point mRectTB = new Point();
    private final Point mRectLR = new Point();

    private void init(){
        mRectPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int wh = Math.min(width, height);
        final int size = (int) (wh * 0.7f);
        final int left = (width - size)/2;
        final int right = left + size;
        mRectLR.set(left, right);
        final int bottom = (height - size)/2;
        final int top = bottom + size;
        mRectTB.set(top, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Center box
//        canvas.drawRect(mRectLR.x, mRectTB.x, mRectLR.y, mRectTB.y, mRectPaint);
        canvas.drawRect(0, 100, 100, 0, mRectPaint);
    }

}
