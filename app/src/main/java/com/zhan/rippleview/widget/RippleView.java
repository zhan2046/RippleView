package com.zhan.rippleview.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.zhan.rippleview.R;

/**
 * Created by hrz on 2016/7/2.
 * 水波纹View，可用于设备查询之类的View使用
 */
public class RippleView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    /** 默认半径 dp */
    private static final int NORMAL_RADIUS = 60;

    /** 实心内圆画笔 */
    private Paint mInPaint = new Paint();
    /** 空心内圆画笔 */
    private Paint mInStrokePaint = new Paint();
    /** 空心外圆画笔 */
    private Paint mOutStrokePaint = new Paint();

    private ValueAnimator mValueAnimator;
    private RippleStateListener mRippleStateListener;

    /** 初始化半径 */
    private int mRadius;
    /** 变化的半径 */
    private int mChangeRadius;
    /** 园边线宽度 */
    private int mStrokeWidth = 1;
    /** 动画时间 */
    private int mDuration = 3000;
    /** 动画次数 */
    private int mRepeatCount = 1;
    /** 圆心x */
    private float mCx;
    /** 圆心y */
    private float mCy;

    public RippleView(Context context) {
        super(context);
        init();
    }

    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRadius = dip2px(NORMAL_RADIUS);
        mChangeRadius = mRadius;

        //初始化实心内圆画笔
        mInPaint.setColor(getResources().getColor(R.color.white10));
        mInPaint.setAntiAlias(true);
        mInPaint.setStyle(Paint.Style.FILL);

        //初始化空心内圆画笔
        mInStrokePaint.setColor(getResources().getColor(R.color.white50));
        mInStrokePaint.setAntiAlias(true);
        mInStrokePaint.setStyle(Paint.Style.STROKE);
        mInStrokePaint.setStrokeWidth(dip2px(mStrokeWidth));

        //初始化空心外圆画笔
        mOutStrokePaint.setColor(getResources().getColor(R.color.white50));
        mOutStrokePaint.setAntiAlias(true);
        mOutStrokePaint.setStyle(Paint.Style.STROKE);
        mOutStrokePaint.setStrokeWidth(dip2px(mStrokeWidth));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //初始化圆心
        mCx = getMeasuredWidth() / 2;
        mCy = getMeasuredHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画半径变化的外圆
        canvas.drawCircle(mCx, mCy, mChangeRadius, mOutStrokePaint);

        if(mChangeRadius >= mRadius * 1.5) {
            canvas.drawCircle(mCx, mCy, mChangeRadius - (mRadius / 2) , mOutStrokePaint);
        }

        if(mChangeRadius >= mRadius * 2) {
            canvas.drawCircle(mCx, mCy, mChangeRadius - (mRadius), mOutStrokePaint);
        }

        //画实心内圆
        canvas.drawCircle(mCx, mCy, mRadius, mInPaint);
        //画空心内圆
        canvas.drawCircle(mCx, mCy, mRadius, mInStrokePaint);
    }

    /** 开启水波纹 */
    public void startRipple() {
        if (mValueAnimator == null) {
            mValueAnimator = new ValueAnimator();
            mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mValueAnimator.setIntValues(mRadius, (int) (mCx > mCy ? mCx * 1.4 : mCy * 1.4));
            mValueAnimator.setDuration(mDuration);
            mValueAnimator.setRepeatCount(mRepeatCount);
            mValueAnimator.addUpdateListener(this);
            mValueAnimator.addListener(this);
            mValueAnimator.start();
        } else {
            if (!mValueAnimator.isRunning()) {
                mValueAnimator.start();
            }
        }
    }

    /** 关闭水波纹 */
    public void stopRipple() {
        if (mValueAnimator != null) {
            mValueAnimator.end();
        }
    }

    public int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mChangeRadius = (int) animation.getAnimatedValue();
        postInvalidate();
        if (mRippleStateListener != null) {
            mRippleStateListener.onRippleUpdate(animation);
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (mRippleStateListener != null) {
            mRippleStateListener.startRipple();
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (mRippleStateListener != null) {
            mRippleStateListener.stopRipple();
        }
        //动画结束，初始变化的半径
        mChangeRadius = mRadius;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        mChangeRadius = mRadius;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    /** 对外提供状态信息的接口 */
    public interface RippleStateListener {

        /** 动画开始 */
        void startRipple();

        /** 动画结束 */
        void stopRipple();

        /** 每一贞动画回调 */
        void onRippleUpdate(ValueAnimator animation);
    }

    /** 水波纹状态接口对象注入 */
    public void setRippleStateListener(RippleStateListener listener) {
        mRippleStateListener = listener;
    }

    /** 设置圆半径 */
    public void setRadius(int radius) {
        mRadius = radius;
        mChangeRadius = radius;
    }

    /** 设置圆边线宽度 */
    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
    }

    /** 设置动画时间 */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    /** 设置动画次数 */
    public void setRepeatCount(int repeatCount) {
        mRepeatCount = repeatCount;
    }

    /** 设置圆心 */
    public void setCirclePoint(float x ,float y) {
        mCx = x;
        mCy = y;
    }

    /** 设置空心外圆颜色 */
    public void setOutStrokePaintColor(int color) {
        mOutStrokePaint.setColor(color);
    }

    /** 设置空心内圆颜色 */
    public void setInStrokePaintColor(int color) {
        mInStrokePaint.setColor(color);
    }

    /** 设置实心内圆颜色 */
    public void setInPaintColor(int color) {
        mInPaint.setColor(color);
    }
}
