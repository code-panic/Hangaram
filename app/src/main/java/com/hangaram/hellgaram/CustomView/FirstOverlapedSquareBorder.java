package com.hangaram.hellgaram.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class FirstOverlapedSquareBorder extends View {
    private Paint squPaint;
    private Paint borderPaint;

    private int strokeWidth = 1; //선 굵기

    private int viewWidth;
    private int viewHeight;


    public FirstOverlapedSquareBorder(Context context) {
        super(context);
        init(context);
    }

    public FirstOverlapedSquareBorder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FirstOverlapedSquareBorder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        squPaint = new Paint();
        squPaint.setColor(Color.WHITE);
        squPaint.setStrokeWidth(strokeWidth);
        squPaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint();
        borderPaint.setColor(Color.RED);
        borderPaint.setStrokeWidth(strokeWidth);
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(viewWidth/8, strokeWidth, viewWidth * 7 / 8, viewHeight * 7 / 8, squPaint);
        canvas.drawRect(viewWidth/8, strokeWidth, viewWidth * 7 / 8, viewHeight * 7 / 8, borderPaint);
    }
}
