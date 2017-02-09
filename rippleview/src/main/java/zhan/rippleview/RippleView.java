package zhan.rippleview;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhan on 2017/2/10.
 *
 */
public class RippleView extends View
    implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

  /** 默认半径 dp */
  private static final int NORMAL_RADIUS = 60;
  private static final int NORMAL_DURATION = 3000;

  private static final float NORMAL_TWO_RIPPLE_TIMES = 1.5f;
  private static final float NORMAL_THREE_RIPPLE_TIMES = 2.0f;
  private static final float NORMAL_MAX_MORE_RADIUS_TIMES = 1.4f;

  /** 实心内圆画笔 */
  private Paint mInPaint = new Paint();
  /** 空心内圆画笔 */
  private Paint mInStrokePaint = new Paint();
  /** 空心外圆画笔 */
  private Paint mOutStrokePaint = new Paint();

  private ValueAnimator mValueAnimator;
  private List<RippleAnimationListener> mRippleAnimationListeners = new ArrayList<>();
  private TimeInterpolator mInterpolator = new AccelerateDecelerateInterpolator();

  /** 初始化半径 */
  private int mRadius;
  /** 变化的半径 */
  private int mChangeRadius;
  /** 园边线宽度 */
  private int mStrokeWidth;
  /** 动画时间 */
  private int mDuration;
  /** 动画次数 */
  private int mRepeatCount;
  /** 圆心x */
  private float mCx;
  /** 圆心y */
  private float mCy;

  private float mTwoRippleTimes;
  private float mThreeRippleTimes;
  private float mMaxMoreRadiusTimes;

  public RippleView(Context context) {
    this(context, null);
  }

  public RippleView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RippleView, 0, 0);
    mRadius = a.getInteger(R.styleable.RippleView_radius, dip2px(NORMAL_RADIUS));
    mStrokeWidth = a.getInteger(R.styleable.RippleView_stroke_width, 1);
    mDuration = a.getInteger(R.styleable.RippleView_duration, NORMAL_DURATION);
    mRepeatCount = a.getInteger(R.styleable.RippleView_repeat_count, 1);
    mTwoRippleTimes =
        a.getFloat(R.styleable.RippleView_two_ripple_times, NORMAL_TWO_RIPPLE_TIMES);
    mThreeRippleTimes =
        a.getFloat(R.styleable.RippleView_three_ripple_times, NORMAL_THREE_RIPPLE_TIMES);
    mMaxMoreRadiusTimes = a.getFloat(R.styleable.RippleView_max_more_radius_times,
        NORMAL_MAX_MORE_RADIUS_TIMES);
    a.recycle();

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

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    //初始化圆心
    mCx = getMeasuredWidth() / 2;
    mCy = getMeasuredHeight() / 2;
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (mValueAnimator != null) {
      mValueAnimator.cancel();
    }
    removeRippleStateListenerAll();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    //画半径变化的外圆
    canvas.drawCircle(mCx, mCy, mChangeRadius, mOutStrokePaint);

    if (mChangeRadius >= mRadius * mTwoRippleTimes) {
      canvas.drawCircle(mCx, mCy, mChangeRadius - (mRadius / 2), mOutStrokePaint);
    }

    if (mChangeRadius >= mRadius * mThreeRippleTimes) {
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
      mValueAnimator.setInterpolator(mInterpolator);
      mValueAnimator.setIntValues(mRadius,
          (int) (mCx > mCy ? mCx * mMaxMoreRadiusTimes : mCy * mMaxMoreRadiusTimes));
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

  @Override public void onAnimationUpdate(ValueAnimator animation) {
    mChangeRadius = (int) animation.getAnimatedValue();
    postInvalidate();
    for (RippleAnimationListener listener : mRippleAnimationListeners) {
      listener.onAnimationUpdate(animation);
    }
  }

  @Override public void onAnimationStart(Animator animation) {
    for (RippleAnimationListener listener : mRippleAnimationListeners) {
      listener.onAnimationStart(animation);
    }
  }

  @Override public void onAnimationEnd(Animator animation) {
    //动画结束，初始变化的半径
    mChangeRadius = mRadius;

    for (RippleAnimationListener listener : mRippleAnimationListeners) {
      listener.onAnimationEnd(animation);
    }
  }

  @Override public void onAnimationCancel(Animator animation) {
    mChangeRadius = mRadius;

    for (RippleAnimationListener listener : mRippleAnimationListeners) {
      listener.onAnimationCancel(animation);
    }
  }

  @Override public void onAnimationRepeat(Animator animation) {
    for (RippleAnimationListener listener : mRippleAnimationListeners) {
      listener.onAnimationRepeat(animation);
    }
  }

  /** 对外提供状态信息的接口 */
  public interface RippleAnimationListener {

    void onAnimationUpdate(ValueAnimator animation);

    void onAnimationStart(Animator animation);

    void onAnimationEnd(Animator animation);

    void onAnimationCancel(Animator animation);

    void onAnimationRepeat(Animator animation);
  }

  public void setRippleStateListener(RippleAnimationListener listener) {
    if (!mRippleAnimationListeners.contains(listener)) {
      mRippleAnimationListeners.add(listener);
    }
  }

  public void removeRippleStateListener(RippleAnimationListener listener) {
    if (mRippleAnimationListeners.contains(listener)) {
      mRippleAnimationListeners.remove(listener);
    }
  }

  public void removeRippleStateListenerAll() {
    mRippleAnimationListeners.clear();
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
  public void setCirclePoint(float x, float y) {
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

  public void setInterpolator(Interpolator interpolator) {
    mInterpolator = interpolator;
  }

  public void setTwoRippleTimes(float times) {
    mTwoRippleTimes = times;
  }

  public float getTwoRippleTimes() {
    return mTwoRippleTimes;
  }

  public void setThreeRippleTimes(float times) {
    mThreeRippleTimes = times;
  }

  public float getThreeRippleTimes() {
    return mThreeRippleTimes;
  }
}
