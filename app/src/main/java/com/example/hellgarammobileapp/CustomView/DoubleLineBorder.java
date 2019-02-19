package com.example.hellgarammobileapp.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DoubleLineBorder extends View {
    private Paint linePaint;

    private int strokeWidth = 1; //선 굵기

    private int viewWidth;
    private int viewHeight;

    private int m = 8; // viewHeight를 m:n으로 내분
    private int n = 2; //

    private int gap = 5; // 두 line 사이 간격

    public DoubleLineBorder(Context context) {
        super(context);
        init(context);
    }

    public DoubleLineBorder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DoubleLineBorder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(strokeWidth);
        linePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, viewHeight * m / (m + n) + gap, viewWidth, viewHeight * n / (m + n) + gap, linePaint);
        canvas.drawLine(0, viewHeight * m / (m + n) - gap, viewWidth, viewHeight * n / (m + n) - gap, linePaint);
    }
}
