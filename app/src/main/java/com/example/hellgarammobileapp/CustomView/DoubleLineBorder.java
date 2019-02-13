package com.example.hellgarammobileapp.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DoubleLineBorder extends View{
    private Paint squPaint;

    private int strokeWidth = 1; //선 굵기

    private int viewWidth;
    private int viewHeight;

    private int m = 5; // viewHeight를 m:n으로 내분
    private int n = 3; //

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
        squPaint = new Paint();
        squPaint.setAntiAlias(true);
        squPaint.setColor(Color.RED);
        squPaint.setStrokeWidth(strokeWidth);
        squPaint.setStyle(Paint.Style.STROKE);
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

        canvas.drawRect(strokeWidth, strokeWidth, viewWidth - 40, viewHeight - 40, squPaint);
        canvas.drawRect(40, 40, viewWidth - strokeWidth, viewHeight - strokeWidth, squPaint);
    }
}
