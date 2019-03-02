package com.hangaram.hellgaram.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hangaram.hellgaram.support.ConvertUnit;
import com.hangaram.hellgaram.support.FontGiver;
import com.hangaram.hellgaram.support.TimeGiver;

public class TitleBar extends View implements ViewPager.OnPageChangeListener {
    private static String log = "TitleBar";

    private Paint noPaint;
    private Paint linePaint;
    private Paint textPaint;
    private Paint squPaint;

    private int noTextSize = ConvertUnit.convertSpToPixel(20,getContext());
    private String noText = "No." + TimeGiver.getMonth() + "." + TimeGiver.getDate();
    private int offsetBetweenNoAndParent = ConvertUnit.convertDpToPixel(5,getContext()); //No과 오른쪽 벽과의 거리
    private int offsetBetweenNoAndLine = ConvertUnit.convertDpToPixel(5,getContext()); //No과 Line 사이 거리
    private int offsetBetweenLineAndSqu = ConvertUnit.convertDpToPixel(5,getContext()); //Line과 Squares사이의 거리
    private int strokeWidth = 1; //선 굵기

    private int gearTextX = ConvertUnit.convertDpToPixel(7,getContext());
    private int gearTextY = ConvertUnit.convertDpToPixel(10,getContext());

    private int gearTextSize  = ConvertUnit.convertDpToPixel(10,getContext()); // (실제 사이즈) = (정사각형 변의 길이) - gearTextSizw

    private int viewWidth;
    private int viewHeight;

    private char[] mealArr = {'오','늘','급', '식'};
    private char[] timetableArr = {'시', '간', '표'};
    private char[] busArr = {'버', '스', '도', '착', '정', '보'};
    private char[] weatherArr = {'오', '늘', '날', '씨'};
    private char[] settingArr = {'설','정'};

    private char[] textarr = mealArr;

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
        noPaint.setTypeface(FontGiver.getNanumMyeongjo(this.getContext()));

        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(strokeWidth);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(FontGiver.getNanumMyeongjo(this.getContext()));

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

        canvas.drawText(noText, viewWidth - noTextSize * noText.length() / 2 - offsetBetweenNoAndParent, noTextSize, noPaint);  //No.01.02
        canvas.drawLine(0, noTextSize + offsetBetweenNoAndLine, viewWidth, noTextSize + offsetBetweenNoAndLine, linePaint); // ----------

        int squSize = viewHeight - noTextSize - offsetBetweenNoAndLine - offsetBetweenLineAndSqu - strokeWidth;

        textPaint.setTextSize(squSize - gearTextSize);

        for (int i = 0; i * squSize < viewWidth; i++) {
            canvas.drawRect(strokeWidth + (squSize - strokeWidth) * i, viewHeight - squSize, strokeWidth + (squSize - strokeWidth) * (i + 1), viewHeight - strokeWidth, squPaint);

            if (i < textarr.length) {
                canvas.drawText(textarr, i, 1, (squSize - strokeWidth) * i + gearTextX, viewHeight + strokeWidth - gearTextY, textPaint);
            }
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                textarr = mealArr;
                break;
            case 1:
                textarr = timetableArr;
                break;
            case 2:
                textarr = busArr;
                break;
            case 3:
                textarr = weatherArr;
                break;
            case 4:
                textarr = settingArr;
                break;
            default:
                break;
        }
        Log.d(log,textarr.length+"");
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
