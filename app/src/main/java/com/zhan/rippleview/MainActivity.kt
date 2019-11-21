package com.zhan.rippleview

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import zhan.rippleview.RippleView

class MainActivity : AppCompatActivity(), RippleView.RippleAnimationListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListener()
    }

    private fun initListener() {
        rippleView.setRippleStateListener(this)
    }

    fun start(view: View) {
        rippleView.startRipple()
    }

    fun stop(view: View) {
        rippleView.stopRipple()
    }

    @SuppressLint("SetTextI18n")
    override fun onAnimationUpdate(animation: ValueAnimator) {
        val fraction = animation.animatedFraction
        val value = (fraction * 100).toInt()
        root_tv.text = "$value%"
    }

    override fun onAnimationStart(animation: Animator) {
        root_tv.text = "0%"
    }

    override fun onAnimationEnd(animation: Animator) {
        // do nothing
    }

    override fun onAnimationCancel(animation: Animator) {
        // do nothing
    }

    override fun onAnimationRepeat(animation: Animator) {
        // do nothing
    }
}
