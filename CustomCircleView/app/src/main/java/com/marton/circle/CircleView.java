package com.marton.circle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by marton on 16/5/15.
 */
public class CircleView extends View {

    private Paint mPaint;
    private int mColor = Color.RED;

    private Context mContext;

    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;

    private static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public CircleView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray circleArray = context.obtainStyledAttributes(attrs, R.styleable.circle);
        mColor = circleArray.getColor(R.styleable.circle_circle_color, Color.BLACK);
        circleArray.recycle();
        init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMeasureSpecMode == MeasureSpec.AT_MOST){
            widthMeasureSpecSize = dp2px(mContext,DEFAULT_WIDTH) ;
        }

        if (heightMeasureSpecMode == MeasureSpec.AT_MOST){
            heightMeasureSpecSize = dp2px(mContext,DEFAULT_HEIGHT);
        }

        setMeasuredDimension(widthMeasureSpecSize,heightMeasureSpecSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int drawWidth = getWidth() - paddingLeft - paddingRight;
        int drawHeight = getHeight() - paddingTop - paddingBottom;
        float radius = Math.min(drawWidth,drawHeight) / 2 * 1f;
        canvas.drawCircle(drawWidth * 1.0f / 2 + paddingLeft, drawHeight * 1.0f / 2 + paddingTop,radius,mPaint);
    }
}
