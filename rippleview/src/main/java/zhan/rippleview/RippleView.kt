package zhan.rippleview

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.core.content.ContextCompat
import java.util.*

class RippleView : View, ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private val mInPaint = Paint()
    private val mInStrokePaint = Paint()
    private val mOutStrokePaint = Paint()

    private var mValueAnimator: ValueAnimator? = null
    private val mRippleAnimationListeners = ArrayList<RippleAnimationListener>()
    private var mInterpolator: TimeInterpolator = AccelerateDecelerateInterpolator()

    private var mRadius: Int = 0
    private var mChangeRadius: Int = 0
    private var mStrokeWidth: Int = 0
    private var mDuration: Int = 0
    private var mRepeatCount: Int = 0
    private var mCx: Float = 0.toFloat()
    private var mCy: Float = 0.toFloat()

    private var twoRippleTimes: Float = 0.toFloat()
    private var threeRippleTimes: Float = 0.toFloat()
    private var mMaxMoreRadiusTimes: Float = 0.toFloat()

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RippleView,
                0, 0)
        mRadius = a.getInteger(R.styleable.RippleView_radius, dip2px(NORMAL_RADIUS.toFloat()))
        mStrokeWidth = a.getInteger(R.styleable.RippleView_stroke_width, 1)
        mDuration = a.getInteger(R.styleable.RippleView_duration, NORMAL_DURATION)
        mRepeatCount = a.getInteger(R.styleable.RippleView_repeat_count, 1)
        twoRippleTimes = a.getFloat(R.styleable.RippleView_two_ripple_times,
                NORMAL_TWO_RIPPLE_TIMES)
        threeRippleTimes = a.getFloat(R.styleable.RippleView_three_ripple_times,
                NORMAL_THREE_RIPPLE_TIMES)
        mMaxMoreRadiusTimes = a.getFloat(R.styleable.RippleView_max_more_radius_times,
                NORMAL_MAX_MORE_RADIUS_TIMES)
        a.recycle()

        mChangeRadius = mRadius

        mInPaint.color = ContextCompat.getColor(context, R.color.white10)
        mInPaint.isAntiAlias = true
        mInPaint.style = Paint.Style.FILL

        mInStrokePaint.color = ContextCompat.getColor(context, R.color.white50)
        mInStrokePaint.isAntiAlias = true
        mInStrokePaint.style = Paint.Style.STROKE
        mInStrokePaint.strokeWidth = dip2px(mStrokeWidth.toFloat()).toFloat()

        mOutStrokePaint.color = ContextCompat.getColor(context, R.color.white50)
        mOutStrokePaint.isAntiAlias = true
        mOutStrokePaint.style = Paint.Style.STROKE
        mOutStrokePaint.strokeWidth = dip2px(mStrokeWidth.toFloat()).toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCx = (measuredWidth / 2).toFloat()
        mCy = (measuredHeight / 2).toFloat()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mValueAnimator != null) {
            mValueAnimator!!.cancel()
        }
        removeRippleStateListenerAll()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(mCx, mCy, mChangeRadius.toFloat(), mOutStrokePaint)
        if (mChangeRadius >= mRadius * twoRippleTimes) {
            canvas.drawCircle(mCx, mCy, (mChangeRadius - mRadius / 2).toFloat(), mOutStrokePaint)
        }
        if (mChangeRadius >= mRadius * threeRippleTimes) {
            canvas.drawCircle(mCx, mCy, (mChangeRadius - mRadius).toFloat(), mOutStrokePaint)
        }
        canvas.drawCircle(mCx, mCy, mRadius.toFloat(), mInPaint)
        canvas.drawCircle(mCx, mCy, mRadius.toFloat(), mInStrokePaint)
    }

    fun startRipple() {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator()
            mValueAnimator!!.interpolator = mInterpolator
            mValueAnimator!!.setIntValues(mRadius,
                    (if (mCx > mCy) mCx * mMaxMoreRadiusTimes else mCy * mMaxMoreRadiusTimes).toInt())
            mValueAnimator!!.duration = mDuration.toLong()
            mValueAnimator!!.repeatCount = mRepeatCount
            mValueAnimator!!.addUpdateListener(this)
            mValueAnimator!!.addListener(this)
            mValueAnimator!!.start()
        } else {
            if (!mValueAnimator!!.isRunning) {
                mValueAnimator!!.start()
            }
        }
    }

    fun stopRipple() {
        if (mValueAnimator != null) {
            mValueAnimator!!.end()
        }
    }

    private fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        mChangeRadius = animation.animatedValue as Int
        postInvalidate()
        for (listener in mRippleAnimationListeners) {
            listener.onAnimationUpdate(animation)
        }
    }

    override fun onAnimationStart(animation: Animator) {
        for (listener in mRippleAnimationListeners) {
            listener.onAnimationStart(animation)
        }
    }

    override fun onAnimationEnd(animation: Animator) {
        mChangeRadius = mRadius
        for (listener in mRippleAnimationListeners) {
            listener.onAnimationEnd(animation)
        }
    }

    override fun onAnimationCancel(animation: Animator) {
        mChangeRadius = mRadius
        for (listener in mRippleAnimationListeners) {
            listener.onAnimationCancel(animation)
        }
    }

    override fun onAnimationRepeat(animation: Animator) {
        for (listener in mRippleAnimationListeners) {
            listener.onAnimationRepeat(animation)
        }
    }

    interface RippleAnimationListener {

        fun onAnimationUpdate(animation: ValueAnimator)

        fun onAnimationStart(animation: Animator)

        fun onAnimationEnd(animation: Animator)

        fun onAnimationCancel(animation: Animator)

        fun onAnimationRepeat(animation: Animator)
    }

    fun setRippleStateListener(listener: RippleAnimationListener) {
        if (!mRippleAnimationListeners.contains(listener)) {
            mRippleAnimationListeners.add(listener)
        }
    }

    fun removeRippleStateListener(listener: RippleAnimationListener) {
        if (mRippleAnimationListeners.contains(listener)) {
            mRippleAnimationListeners.remove(listener)
        }
    }

    fun removeRippleStateListenerAll() {
        mRippleAnimationListeners.clear()
    }

    fun setRadius(radius: Int) {
        mRadius = radius
        mChangeRadius = radius
    }

    fun setStrokeWidth(strokeWidth: Int) {
        mStrokeWidth = strokeWidth
    }

    fun setDuration(duration: Int) {
        mDuration = duration
    }

    fun setRepeatCount(repeatCount: Int) {
        mRepeatCount = repeatCount
    }

    fun setCirclePoint(x: Float, y: Float) {
        mCx = x
        mCy = y
    }

    fun setOutStrokePaintColor(color: Int) {
        mOutStrokePaint.color = color
    }

    fun setInStrokePaintColor(color: Int) {
        mInStrokePaint.color = color
    }

    fun setInPaintColor(color: Int) {
        mInPaint.color = color
    }

    fun setInterpolator(interpolator: Interpolator) {
        mInterpolator = interpolator
    }

    companion object {

        private const val NORMAL_RADIUS = 60
        private const val NORMAL_DURATION = 3000

        private const val NORMAL_TWO_RIPPLE_TIMES = 1.5f
        private const val NORMAL_THREE_RIPPLE_TIMES = 2.0f
        private const val NORMAL_MAX_MORE_RADIUS_TIMES = 1.4f
    }
}
