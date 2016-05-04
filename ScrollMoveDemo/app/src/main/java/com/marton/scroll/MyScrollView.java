package com.marton.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by marton on 16/5/3.
 */
public class MyScrollView extends TextView {


    private Scroller mScroller;

    public MyScrollView(Context context){
        super(context);
        mScroller = new Scroller(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public void smoothScrollTo(int deltX, int deltY){
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int dx = deltX - scrollX;
        int dy = deltY - scrollY;
        mScroller.startScroll(scrollX,scrollY,dx,dy,3000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }
}
