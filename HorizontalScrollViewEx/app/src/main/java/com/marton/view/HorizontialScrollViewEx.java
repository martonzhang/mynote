package com.marton.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by marton on 16/5/15.
 */
public class HorizontialScrollViewEx extends ViewGroup {

    private int mLastInterceptedX;
    private int mLastInterceptedY;

    private int mLastX;
    private int mLastY;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    public HorizontialScrollViewEx(Context context) {
        super(context);
        init();
    }

    public HorizontialScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }

    private static int dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //无论HorizontialScrollViewEx的Mode是什么，都根据子view的宽高来决定HorizontialScrollViewEx的宽高；
        //因此这里不需要widthSpecSize和heightSpecSize
//        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
//        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int measureWidth = 0;
        int measureHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE){
                measureChildWithMargins(child,widthMeasureSpec,measureWidth,heightMeasureSpec,0);
                measureWidth += child.getMeasuredWidth();
                if (measureHeight < child.getMeasuredHeight()){
                    measureHeight = child.getMeasuredHeight();
                }
            }
        }

        setMeasuredDimension(measureWidth,measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int childLeft = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE){
                int childWidth = child.getMeasuredWidth();
                MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
                int left = childLeft + lp.leftMargin;
                int top = lp.topMargin + getPaddingTop();
                if (i == 0){
                    left += getPaddingLeft();
                }
                child.layout(left,top,left + childWidth,top + child.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = x - mLastX;
                scrollBy(-dx,0);
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                int dur = (int)(xVelocity / dp2Px(getContext(),10));
                dx = (int)(0.5 * dp2Px(getContext(),10) * dur *dur * 1000000);
                if (x - mLastX > 0){
                    smoothScrollBy(-dx,0,dur*1000);
                }else{
                    smoothScrollBy(dx,0,dur*1000);
                }
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private void smoothScrollBy(int dx ,int dy, int dur){
        mScroller.startScroll(getScrollX(),0,dx,dy,dur);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;

        int x = (int)ev.getX();
        int y = (int)ev.getY();
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                if (!mScroller.isFinished()){
                    mScroller.abortAnimation();
                    intercepted = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = x - mLastInterceptedX;
                int dy = y - mLastInterceptedY;
                intercepted = (Math.abs(dx) > Math.abs(dy)) ? true : false;
                break;
        }
        mLastInterceptedX = x;
        mLastInterceptedY = y;
        mLastX = x;
        mLastY = y;
        return intercepted;
    }

    /**
     * 解决MarginLayoutParams参数不匹配问题
     * @param p
     * @return
     */
    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    }

    public static class LayoutParams extends MarginLayoutParams{
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
