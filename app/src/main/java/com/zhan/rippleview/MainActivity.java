package com.zhan.rippleview;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zhan.rippleview.widget.RippleView;

public class MainActivity extends AppCompatActivity implements RippleView.RippleStateListener {

    private RippleView mRippleView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {
        mRippleView.setRippleStateListener(this);
    }

    private void initView() {
        mRippleView = (RippleView) findViewById(R.id.root_rv);
        mTextView = (TextView) findViewById(R.id.root_tv);
    }

    public void start(View v) {
        mRippleView.startRipple();
    }

    public void stop(View v) {
        mRippleView.stopRipple();
    }

    @Override
    public void startRipple() {
        mTextView.setText("0%");
    }

    @Override
    public void stopRipple() {

    }

    @Override
    public void onRippleUpdate(ValueAnimator animation) {
        float fraction = animation.getAnimatedFraction();
        int value = (int) (fraction * 100);
        mTextView.setText(String.valueOf(value) + "%");
    }
}
