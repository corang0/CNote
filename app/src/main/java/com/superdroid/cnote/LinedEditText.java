package com.superdroid.cnote;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class LinedEditText extends AppCompatEditText {
    private Paint mPaint = new Paint();

    public LinedEditText(Context context) {
        super(context);
        initPaint(context, null);
    }

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context, attrs);
    }

    public LinedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint(context, attrs);
    }

    private void initPaint(Context context, AttributeSet attrs) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0x80FFD7D7);

        if(attrs != null) {
            final TypedArray customAttrs = context.obtainStyledAttributes(attrs, R.styleable.LinedEditText);
            final int lineColor = customAttrs.getColor(R.styleable.LinedEditText_lineColor, Color.WHITE);
            mPaint.setColor(lineColor);
        }
    }

    @Override protected void onDraw(Canvas canvas) {
        int left = getLeft();
        int right = getRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int height = getHeight();
        int lineHeight = getLineHeight();
        int count = (height-paddingTop-paddingBottom)/lineHeight;

        for (int i = 0; i < count; i++) {
            int baseline = lineHeight * (i+1) + paddingTop;
            canvas.drawLine(left+paddingLeft, baseline, right-paddingRight, baseline, mPaint);
        }

        super.onDraw(canvas);
    }

    public void setLineColor(int color) {
        mPaint.setColor(color);
        this.invalidate();
    }
}