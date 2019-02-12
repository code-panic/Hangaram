package com.example.hellgarammobileapp.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.hellgarammobileapp.support.TimeGiver;

public class TitleBar extends View {
    private Paint noPaint;
    private Paint linePaint;
    private Paint textPaint;
    private Paint squPaint;

    private int noTextSize = 50;
    private String noText = "No." + TimeGiver.getMonth() + "." + TimeGiver.getDate();
    private int offsetBetweenNoAndParent = 10; //No과 오른쪽 벽과의 거리
    private int offsetBetweenNoAndLine = 15; //No과 Line 사이 거리
    private int offsetBetweenLineAndSqu = 30; //Line과 Squares사이의 거리
    private int strokeWidth = 1; //선 굵기

    private int gearTextX = 4;
    private int gearTextY = 15;

    private int viewWidth;
    private int viewHeight;

    private char[] mealArr = {'급', '식', '정', '보'};

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        noPaint = new Paint();
        noPaint.setAntiAlias(true);
        noPaint.setColor(Color.RED);
        noPaint.setTextSize(noTextSize);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(strokeWidth);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);

        squPaint = new Paint();
        squPaint.setAntiAlias(true);
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

        canvas.drawText(noText, viewWidth - noTextSize * noText.length()/2 - offsetBetweenNoAndParent, noTextSize, noPaint);  //No.01.02
        canvas.drawLine(0, noTextSize + offsetBetweenNoAndLine, viewWidth, noTextSize + offsetBetweenNoAndLine, linePaint); // ----------

        int squSize = viewHeight - noTextSize -offsetBetweenNoAndLine - offsetBetweenLineAndSqu;

        textPaint.setTextSize(squSize);

        for (int i = 0; i * squSize < viewWidth; i++) {
            canvas.drawRect(strokeWidth + squSize * i, noTextSize + offsetBetweenLineAndSqu, strokeWidth + squSize * (i + 1), viewHeight - strokeWidth, squPaint);

            if (i < mealArr.length) {
                canvas.drawText(mealArr,i,1,strokeWidth + squSize * i + gearTextX,viewHeight + strokeWidth - gearTextY,textPaint);
            }
        }
    }
}
