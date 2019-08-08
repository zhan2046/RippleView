package com.zhan.rippleview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import zhan.rippleview.RippleView;

public class MainActivity extends AppCompatActivity implements
        RippleView.RippleAnimationListener {

  private RippleView mRippleView;
  private TextView mTextView;

  @Override protected void onCreate(Bundle savedInstanceState) {
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

  @Override public void onAnimationUpdate(ValueAnimator animation) {
    float fraction = animation.getAnimatedFraction();
    int value = (int) (fraction * 100);
    mTextView.setText(String.valueOf(value) + "%");
  }

  @Override public void onAnimationStart(Animator animation) {
    mTextView.setText("0%");
  }

  @Override public void onAnimationEnd(Animator animation) {

  }

  @Override public void onAnimationCancel(Animator animation) {

  }

  @Override public void onAnimationRepeat(Animator animation) {

  }
}
