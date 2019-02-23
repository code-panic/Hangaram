package com.example.hellgarammobileapp.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class OverlapedSquareBorder extends View {
    private Paint squPaint;
    private Paint overlapedPaint;

    private int strokeWidth = 1; //선 굵기

    private int viewWidth;
    private int viewHeight;


    public OverlapedSquareBorder(Context context) {
        super(context);
        init(context);
    }

    public OverlapedSquareBorder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OverlapedSquareBorder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        squPaint = new Paint();
        squPaint.setColor(Color.RED);
        squPaint.setStrokeWidth(strokeWidth);
        squPaint.setStyle(Paint.Style.STROKE);

        overlapedPaint = new Paint();
        overlapedPaint.setColor(Color.RED);
        overlapedPaint.setStrokeWidth(strokeWidth);
        overlapedPaint.setStyle(Paint.Style.STROKE);
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

        canvas.drawRect(strokeWidth, strokeWidth, viewWidth * 3 / 4, viewHeight * 3 / 4, squPaint);

        Path path = new Path();
        path.moveTo(viewWidth / 4, viewHeight * 3 / 4);
        path.lineTo(viewWidth / 4, viewHeight - strokeWidth);
        path.lineTo(viewWidth - strokeWidth, viewHeight - strokeWidth);
        path.lineTo(viewWidth - strokeWidth, viewHeight / 4);
        path.lineTo(viewWidth * 3 / 4, viewHeight / 4);

        canvas.drawPath(path, overlapedPaint);
    }
}
