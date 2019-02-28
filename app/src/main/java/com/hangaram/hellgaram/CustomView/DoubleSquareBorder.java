package com.hangaram.hellgaram.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hangaram.hellgaram.support.ConvertUnit;

public class DoubleSquareBorder extends View {

    private Paint squPaint;

    private int strokeWidth = 1; //선 굵기

    private int viewWidth;
    private int viewHeight;

    private float gap = ConvertUnit.convertDpToPixel(8,getContext());

    public DoubleSquareBorder(Context context) {
        super(context);
        init(context);
    }

    public DoubleSquareBorder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DoubleSquareBorder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        squPaint = new Paint();
        squPaint.setColor(Color.RED);
        squPaint.setStrokeWidth(strokeWidth);
        squPaint.setStyle(Paint.Style.STROKE);
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

        canvas.drawRect(strokeWidth, strokeWidth, viewWidth - gap, viewHeight - gap, squPaint);
        canvas.drawRect(gap, gap, viewWidth - strokeWidth, viewHeight - strokeWidth, squPaint);
    }
}