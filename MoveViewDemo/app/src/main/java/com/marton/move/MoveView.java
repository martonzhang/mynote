package com.marton.move;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by marton on 16/5/3.
 */
public class MoveView extends Button{

    public static final String TAG = "MoveView";

    private int mLastX;
    private int mLastY;

    public MoveView(Context context){
        super(context);
    }

    public MoveView(Context context,AttributeSet attributes){
        super(context,attributes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getRawX();
        int y = (int)event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                int destX = x - mLastX;
                int destY = y - mLastY;
                Log.d(TAG,"destX : " + destX + ", destY : " + destY);
                int translateX = (int)getTranslationX() + destX;
                int translateY = (int)getTranslationY() + destY;
                setTranslationX(translateX);
                setTranslationY(translateY);
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.onTouchEvent(event);
    }
}
