package com.zhan.rippleview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import zhan.looperrippleview.LooperRippleView;

public class MainActivity extends AppCompatActivity implements LooperRippleView.RippleAnimationListener {

  private LooperRippleView mLooperRippleView;
  private TextView mTextView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();
    initListener();
  }

  private void initListener() {
    mLooperRippleView.setRippleStateListener(this);
  }

  private void initView() {
    mLooperRippleView = (LooperRippleView) findViewById(R.id.root_rv);
    mTextView = (TextView) findViewById(R.id.root_tv);
  }

  public void start(View v) {
    mLooperRippleView.startRipple();
  }

  public void stop(View v) {
    mLooperRippleView.stopRipple();
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
