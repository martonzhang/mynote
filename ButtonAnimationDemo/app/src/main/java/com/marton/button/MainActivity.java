package com.marton.button;

import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener{

    private Button mExeBtn;
    private Button mGoalBtn;
    private Button mExeBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mExeBtn = (Button)findViewById(R.id.execute);
        mGoalBtn = (Button)findViewById(R.id.goal);
        mExeBtn2 = (Button)findViewById(R.id.execute2);
        mExeBtn.setOnClickListener(this);
        mExeBtn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.execute){
            ViewWrapper wrapper = new ViewWrapper(mGoalBtn);
            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(wrapper, "width", wrapper.getWitdh(), 800);
            objectAnimator.setDuration(4000);
            objectAnimator.setEvaluator(new IntEvaluator());
            objectAnimator.start();
        }else if (v.getId() == R.id.execute2){
            performAnimate(mGoalBtn,mGoalBtn.getWidth(),800);
        }
    }

    private void performAnimate(final View target, final int start, final int end){
        ValueAnimator animator = ValueAnimator.ofInt(1,100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                target.getLayoutParams().width = (int)(start + fraction * (end - start));
                target.requestLayout();
            }
        });
        animator.setDuration(3000).start();
    };

    private static class ViewWrapper{
        private View mTarget;

        public ViewWrapper(View target){
            mTarget = target;
        }

        public int getWitdh(){
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width){
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
