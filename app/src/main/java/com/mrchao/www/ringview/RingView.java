package com.mrchao.www.ringview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



/**
 * Created by MrChao on 2018/3/8.
 * 环行进度View
 */

public class RingView extends View implements ValueAnimator.AnimatorUpdateListener {


    /**
     * 圆环默认宽度。
     */
    private float defaultInnerStrokeWidth;
    private float defaultInnerStrokeUnfinishedWidth;
    private float defaultOuterStrokeWidth;
    private float defaultOuterStrokeUnfinishedWidth;
    /**
     * 默认不显示内部三个圆环
     */
    private boolean defaultInnerRingShow = false;
    private boolean innerFiserShow;
    private boolean innerSecondShow;
    private boolean innerThirdShow;
    /**
     * 未点击内部圆环宽度
     */
    private float innerStrokeWidth;
    /**
     * 内部圆环宽度
     */
    private float innerStrokeWidthUnfinished;
    /**
     * 最外圈圆环宽度
     */
    private float outerStrokeWidthUnfinished;
    /**
     * 未点击最外层圆环宽度
     */
    private float outerStrokeWidth;
    /**
     * 默认进度
     */
    private float defaultRingInnerFirstProgress;
    private float defaultRingInnerSecondProgress;
    private float defaultRingInnerThirdProgress;
    private float defaultRingOverallProgress;
    /**
     * 设置进度
     */
    private float ringInnerFirstProgress;
    private float ringInnerSecondProgress;
    private float ringInnerThirdProgress;
    private float ringOverallProgress;
    /**
     * 四个圆环颜色
     */
    private int ringInnerThirdColor;
    private int ringInnerSecondColor;
    private int ringInnerFirstColor;
    private int ringOverallColor;
    /**
     * 按压后其他圆环进度填充色
     */
    private int defaultRingFilledColor;
    /**
     * 默认圆环底色/按压后其他圆环进度填充色
     */
    private int defaultRingUnfinishedColor;
    /**
     * 圆环底色
     */
    private int ringUnfinishedColor;
    /**
     * 是否绘制底色
     */
    private boolean defaultIsDrawFilled = true;
    private boolean isDrawFilled;

    private boolean highlighted = false;
    private short highlightedRing = -1;
    /**
     * 起始角度，和旋转角度
     */
    private float startAngle = 90f;
    private float emptyArcAngle = 359.9f;

    /**
     * 圆环画笔
     */
    private Paint ringPaint;

    /**
     * 四个圆环对应 RectF
     */
    private RectF ringInnerFirst;
    private RectF ringInnerSecond;
    private RectF ringInnerThird;
    private RectF ringOverall;

    /**
     * 点击第四个圆环
     */
    public static final short RING_OVERALL = 1;

    /**
     * 点击第三个圆环
     */
    public static final short THIRD_INNER_RING = 2;

    /**
     * 点击第二个圆环
     */
    public static final short SECOND_INNER_RING = 3;

    /**
     * 点击第一个圆环
     */
    public static final short FIRST_INNER_RING = 4;

    /**
     * 圆环默认不可点击
     * 若可点击则不会调用\OnClickListener
     */
    private boolean defaultAreRingsClickable = false;
    private boolean areRingClickable;

    private ValueAnimator mAnimOver,mAnimFirst,mAnimSecond,mAnimThird;
    private float loadValue,thirdValue,secondVlaue,firstValue;

    public RingView(Context context) {
        this(context, null);
    }

    public RingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Default values initialization
        defaultInnerStrokeWidth = DenUtils.sp2px(2);
        defaultInnerStrokeUnfinishedWidth = DenUtils.sp2px(4);

        defaultOuterStrokeWidth = DenUtils.sp2px(2);
        defaultOuterStrokeUnfinishedWidth = DenUtils.sp2px(4);
        defaultRingUnfinishedColor = Color.GRAY;
        defaultRingFilledColor = Color.parseColor("#E6E6E6");

        defaultRingInnerFirstProgress = 0;
        defaultRingInnerSecondProgress = 0;
        defaultRingInnerThirdProgress = 0;
        defaultRingOverallProgress = 0;

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Rings_View, defStyle, 0);
        initByAttributes(attributes);
        attributes.recycle();
        initPainters();
    }

    private void initPainters() {

        ringOverall = new RectF();
        mAnimOver = ValueAnimator.ofFloat(0, 1);
        mAnimOver.setDuration(2500);
        mAnimOver.setRepeatCount(0);
        mAnimOver.addUpdateListener(this);
//        mAnimOver.setInterpolator(new BounceInterpolator());

        if (innerThirdShow){
            ringInnerThird = new RectF();
            mAnimThird = ValueAnimator.ofFloat(0, 1);
            mAnimThird.setDuration(1500);
            mAnimThird.setRepeatCount(0);
            mAnimThird.setStartDelay(200);
            mAnimThird.addUpdateListener(this);
//            mAnimThird.setInterpolator(new BounceInterpolator());
        }
        if (innerSecondShow){
            ringInnerSecond = new RectF();
            mAnimSecond = ValueAnimator.ofFloat(0, 1);
            mAnimSecond.setDuration(1500);
            mAnimSecond.setRepeatCount(0);
            mAnimSecond.setStartDelay(400);
            mAnimSecond.addUpdateListener(this);
//            mAnimSecond.setInterpolator(new BounceInterpolator());
        }
        if (innerFiserShow){
            ringInnerFirst = new RectF();
            mAnimFirst = ValueAnimator.ofFloat(0, 1);
            mAnimFirst.setDuration(1500);
            mAnimFirst.setRepeatCount(0);
            mAnimFirst.setStartDelay(600);
            mAnimFirst.addUpdateListener(this);
//            mAnimFirst.setInterpolator(new BounceInterpolator());
        }
        // Ring Paint
        ringPaint = new Paint();
        ringPaint.setAntiAlias(true);
        ringPaint.setDither(true);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    private void initByAttributes(TypedArray attributes) {
        innerStrokeWidth = attributes.getDimension(R.styleable.Rings_View_inner_stroke_width, defaultInnerStrokeWidth);
        innerStrokeWidthUnfinished = attributes.getDimension(R.styleable.Rings_View_inner_stroke_width_unfinished, defaultInnerStrokeUnfinishedWidth);
        outerStrokeWidth = attributes.getDimension(R.styleable.Rings_View_outer_stroke_width, defaultOuterStrokeWidth);
        outerStrokeWidthUnfinished = attributes.getDimension(R.styleable.Rings_View_outer_stroke_width_unfinished, defaultOuterStrokeUnfinishedWidth);

        ringUnfinishedColor = attributes.getColor(R.styleable.Rings_View_unfinished_color, defaultRingUnfinishedColor);
        defaultRingFilledColor = attributes.getColor(R.styleable.Rings_View_default_filled_color, defaultRingFilledColor);

        ringInnerFirstColor = attributes.getColor(R.styleable.Rings_View_inner_first_color, defaultRingFilledColor);
        ringInnerSecondColor = attributes.getColor(R.styleable.Rings_View_inner_second_color, defaultRingFilledColor);
        ringInnerThirdColor = attributes.getColor(R.styleable.Rings_View_inner_third_color, defaultRingFilledColor);
        ringOverallColor = attributes.getColor(R.styleable.Rings_View_overall_color, defaultRingFilledColor);

        innerThirdShow = attributes.getBoolean(R.styleable.Rings_View_inner_third_show, defaultInnerRingShow);
        innerSecondShow = attributes.getBoolean(R.styleable.Rings_View_inner_second_show, defaultInnerRingShow);
        innerFiserShow = attributes.getBoolean(R.styleable.Rings_View_inner_first_show, defaultInnerRingShow);

        isDrawFilled = attributes.getBoolean(R.styleable.Rings_View_filled_show, defaultIsDrawFilled);
        areRingClickable = attributes.getBoolean(R.styleable.Rings_View_ring_is_clickable, defaultAreRingsClickable);

        setRingOverallProgress(attributes.getFloat(R.styleable.Rings_View_overall_progress, defaultRingOverallProgress));
        setRingInnerThirdProgress(attributes.getFloat(R.styleable.Rings_View_inner_third_progress, defaultRingInnerThirdProgress));
        setRingInnerSecondProgress(attributes.getFloat(R.styleable.Rings_View_inner_second_progress, defaultRingInnerSecondProgress));
        setRingInnerFirstProgress(attributes.getFloat(R.styleable.Rings_View_inner_first_progress, defaultRingInnerFirstProgress));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        ringOverall.set(
                outerStrokeWidthUnfinished,
                outerStrokeWidthUnfinished,
                MeasureSpec.getSize(widthMeasureSpec) - outerStrokeWidthUnfinished,
                MeasureSpec.getSize(heightMeasureSpec) - outerStrokeWidthUnfinished
        );
        if (innerThirdShow)
            ringInnerThird.set(
                    ringOverall.left + (outerStrokeWidthUnfinished + innerStrokeWidthUnfinished),
                    ringOverall.top + (outerStrokeWidthUnfinished + innerStrokeWidthUnfinished),
                    ringOverall.right - (outerStrokeWidthUnfinished + innerStrokeWidthUnfinished),
                    ringOverall.bottom - (outerStrokeWidthUnfinished + innerStrokeWidthUnfinished)
            );
        if (innerSecondShow)
            ringInnerSecond.set(
                    ringInnerThird.left + (outerStrokeWidthUnfinished + innerStrokeWidthUnfinished),
                    ringInnerThird.top + (outerStrokeWidthUnfinished + innerStrokeWidthUnfinished),
                    ringInnerThird.right - (outerStrokeWidthUnfinished + innerStrokeWidthUnfinished),
                    ringInnerThird.bottom - (outerStrokeWidthUnfinished + innerStrokeWidthUnfinished)
            );

        if (innerFiserShow)
            ringInnerFirst.set(
                    ringInnerSecond.left + (outerStrokeWidthUnfinished + innerStrokeWidthUnfinished),
                    ringInnerSecond.top + (outerStrokeWidthUnfinished + innerStrokeWidthUnfinished),
                    ringInnerSecond.right - (outerStrokeWidthUnfinished + innerStrokeWidthUnfinished),
                    ringInnerSecond.bottom - (outerStrokeWidthUnfinished + innerStrokeWidthUnfinished)
            );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置初始圆环宽度和颜色
        if (isDrawFilled) {
            ringPaint.setColor(ringUnfinishedColor);
            ringPaint.setStrokeWidth(outerStrokeWidthUnfinished);
            canvas.drawArc(ringOverall, startAngle, emptyArcAngle, false, ringPaint);
            ringPaint.setStrokeWidth(innerStrokeWidthUnfinished);
            if (innerThirdShow)
                canvas.drawArc(ringInnerThird, startAngle, emptyArcAngle, false, ringPaint);
            if (innerSecondShow)
                canvas.drawArc(ringInnerSecond, startAngle, emptyArcAngle, false, ringPaint);
            if (innerFiserShow)
                canvas.drawArc(ringInnerFirst, startAngle, emptyArcAngle, false, ringPaint);
        }

        if (!highlighted) {
            // 画 filled rings
            ringPaint.setStrokeWidth(outerStrokeWidth);
            ringPaint.setColor(ringOverallColor);
            canvas.drawArc(ringOverall, startAngle, getChartRingOverallProgress() * loadValue, false, ringPaint);
            ringPaint.setStrokeWidth(innerStrokeWidth);
            if (innerThirdShow) {
                ringPaint.setColor(ringInnerThirdColor);
                canvas.drawArc(ringInnerThird, startAngle, getChartRingSpeedProgress() * thirdValue, false, ringPaint);
            }
            if (innerSecondShow) {
                ringPaint.setColor(ringInnerSecondColor);
                canvas.drawArc(ringInnerSecond, startAngle, getChartRingBrakingProgress() * secondVlaue, false, ringPaint);
            }
            if (innerFiserShow) {
                ringPaint.setColor(ringInnerFirstColor);
                canvas.drawArc(ringInnerFirst, startAngle, getChartRingAccelerationProgress() * firstValue, false, ringPaint);
            }

        } else {
            switch (highlightedRing) {
                case RING_OVERALL:
                    // 画 filled rings
                    ringPaint.setStrokeWidth(outerStrokeWidth);
                    ringPaint.setColor(ringOverallColor);
                    canvas.drawArc(ringOverall, startAngle, getChartRingOverallProgress() * loadValue, false, ringPaint);
                    ringPaint.setStrokeWidth(innerStrokeWidth);
                    ringPaint.setColor(defaultRingFilledColor);
                    if (innerThirdShow)
                        canvas.drawArc(ringInnerThird, startAngle, getChartRingSpeedProgress() * thirdValue, false, ringPaint);
                    if (innerSecondShow)
                        canvas.drawArc(ringInnerSecond, startAngle, getChartRingBrakingProgress() * secondVlaue, false, ringPaint);
                    if (innerFiserShow)
                        canvas.drawArc(ringInnerFirst, startAngle, getChartRingAccelerationProgress() * firstValue, false, ringPaint);
                    break;
                case THIRD_INNER_RING:
                    // 画 filled rings
                    ringPaint.setStrokeWidth(innerStrokeWidth);
                    ringPaint.setColor(ringInnerThirdColor);
                    if (innerThirdShow)
                        canvas.drawArc(ringInnerThird, startAngle, getChartRingSpeedProgress() * thirdValue, false, ringPaint);
                    ringPaint.setColor(defaultRingFilledColor);
                    if (innerSecondShow)
                        canvas.drawArc(ringInnerSecond, startAngle, getChartRingBrakingProgress() * secondVlaue, false, ringPaint);
                    if (innerFiserShow)
                        canvas.drawArc(ringInnerFirst, startAngle, getChartRingAccelerationProgress() * firstValue, false, ringPaint);
                    ringPaint.setStrokeWidth(outerStrokeWidth);
                    canvas.drawArc(ringOverall, startAngle, getChartRingOverallProgress() * loadValue, false, ringPaint);
                    break;
                case SECOND_INNER_RING:
                    // 画 filled rings
                    ringPaint.setStrokeWidth(innerStrokeWidth);
                    ringPaint.setColor(ringInnerSecondColor);
                    if (innerSecondShow)
                        canvas.drawArc(ringInnerSecond, startAngle, getChartRingBrakingProgress() * secondVlaue, false, ringPaint);
                    ringPaint.setColor(defaultRingFilledColor);
                    if (innerThirdShow)
                        canvas.drawArc(ringInnerThird, startAngle, getChartRingSpeedProgress() * thirdValue, false, ringPaint);
                    if (innerFiserShow)
                        canvas.drawArc(ringInnerFirst, startAngle, getChartRingAccelerationProgress() * firstValue, false, ringPaint);
                    ringPaint.setStrokeWidth(outerStrokeWidth);
                    canvas.drawArc(ringOverall, startAngle, getChartRingOverallProgress() * loadValue, false, ringPaint);
                    break;
                case FIRST_INNER_RING:
                    // 画 filled rings
                    ringPaint.setStrokeWidth(innerStrokeWidth);
                    ringPaint.setColor(ringInnerFirstColor);
                    if (innerFiserShow)
                        canvas.drawArc(ringInnerFirst, startAngle, getChartRingAccelerationProgress() * firstValue, false, ringPaint);
                    ringPaint.setColor(defaultRingFilledColor);
                    if (innerThirdShow)
                        canvas.drawArc(ringInnerThird, startAngle, getChartRingSpeedProgress() * thirdValue, false, ringPaint);
                    if (innerSecondShow)
                        canvas.drawArc(ringInnerSecond, startAngle, getChartRingBrakingProgress() * secondVlaue, false, ringPaint);
                    ringPaint.setStrokeWidth(outerStrokeWidth);
                    canvas.drawArc(ringOverall, startAngle, getChartRingOverallProgress() * loadValue, false, ringPaint);
                    break;
                default:
                    throw new IllegalArgumentException("请选择需要高亮的圆环: FIRST_INNER_RING, SECOND_INNER_RING, THIRD_INNER_RING or RING_OVERALL");
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getRingsClickable()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (isOnRing(event, ringOverall, outerStrokeWidth) && isInSweep(event, ringOverall, startAngle, emptyArcAngle))
                        highlight(RING_OVERALL);
                    if (innerThirdShow && isOnRing(event, ringInnerThird, outerStrokeWidth) && isInSweep(event, ringInnerThird, startAngle, emptyArcAngle))
                        highlight(THIRD_INNER_RING);
                    if (innerSecondShow && isOnRing(event, ringInnerSecond, outerStrokeWidth) && isInSweep(event, ringInnerSecond, startAngle, emptyArcAngle))
                        highlight(SECOND_INNER_RING);
                    if (innerFiserShow && isOnRing(event, ringInnerFirst, outerStrokeWidth) && isInSweep(event, ringInnerFirst, startAngle, emptyArcAngle))
                        highlight(FIRST_INNER_RING);
                    break;
            }
            return true;
        }
        performClick();
        return false;
    }


    /**
     * 判断点击是否在圆环上
     *
     * @param event
     * @param bounds
     * @param strokeWidth
     * @return
     */
    private static boolean isOnRing(MotionEvent event, RectF bounds, float strokeWidth) {
        // Figure the distance from center point to touch point.
        final float distance = distance(event.getX(), event.getY(),
                bounds.centerX(), bounds.centerY());
        // Assuming square bounds to figure the radius.
        final float radius = bounds.width() / 2f;
        // The Paint stroke is centered on the circumference,
        // so the tolerance is half its width.
        final float halfStrokeWidth = strokeWidth / 2f;
        // Compare the difference to the tolerance.
        return Math.abs(distance - radius) <= halfStrokeWidth;
    }

    private static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private static boolean isInSweep(MotionEvent event, RectF bounds, float startAngle, float sweepAngle) {
        // Figure atan2 angle.
        final float at = (float) Math.toDegrees(Math.atan2(event.getY() - bounds.centerY(), event.getX() - bounds.centerX()));

        // Convert from atan2 to standard angle.
        final float angle = (at + 360) % 360;

        // Check if in sweep.
        return angle >= startAngle && angle <= startAngle + sweepAngle;
    }

    /**
     * 确定那个圆环被远中，高亮
     *
     * @param ring One of
     *             {@link #RING_OVERALL},
     *             {@link #THIRD_INNER_RING},
     *             {@link #SECOND_INNER_RING} ,
     *             {@link #FIRST_INNER_RING}.
     */
    public void highlight(short ring) {
        highlighted = true;
        highlightedRing = ring;
        startAnima();
    }

    /**
     * 取消圆环点击高亮
     */
    public void unhighlight() {
        highlighted = false;
        highlightedRing = -1;
        invalidate();
    }

    public short getHighlightedRing() {
        return highlightedRing;
    }


    /**
     * 设置第四圆环进度
     *
     * @param overAllProgress
     */
    public void setRingOverallProgress(float overAllProgress) {
        this.ringOverallProgress = (emptyArcAngle / 100f) * overAllProgress;
        invalidate();
    }

    public float getChartRingOverallProgress() {
        return ringOverallProgress;
    }

    /**
     * 设置第三圆环进度
     *
     * @param innerThirdProgress
     */
    public void setRingInnerThirdProgress(float innerThirdProgress) {
        this.ringInnerThirdProgress = (emptyArcAngle / 100f) * innerThirdProgress;
        invalidate();
    }

    public float getChartRingSpeedProgress() {
        return ringInnerThirdProgress;
    }


    /**
     * 设置第二圆环进度
     *
     * @param innerSecondProgress
     */
    public void setRingInnerSecondProgress(float innerSecondProgress) {
        this.ringInnerSecondProgress = (emptyArcAngle / 100f) * innerSecondProgress;
        invalidate();
    }

    public float getChartRingBrakingProgress() {
        return ringInnerSecondProgress;
    }


    /**
     * 设计第一圆环进度
     *
     * @param innerFirstProgress
     */
    public void setRingInnerFirstProgress(float innerFirstProgress) {
        this.ringInnerFirstProgress = (emptyArcAngle / 100f) * innerFirstProgress;
        invalidate();
    }

    public float getChartRingAccelerationProgress() {
        return ringInnerFirstProgress;
    }

    /**
     * 设置圆环是否可点击
     *
     * @param areRingsClickable
     */
    public void setRingsClickable(boolean areRingsClickable) {
        this.areRingClickable = areRingsClickable;
    }

    /**
     * 圆环是否可点击
     *
     * @return
     */
    public boolean getRingsClickable() {
        return this.areRingClickable;
    }

    public void startAnima() {
        loadValue = 0;
        thirdValue=0;
        secondVlaue=0;
        firstValue=0;
        mAnimOver.start();
        if (mAnimThird!=null){
            mAnimThird.start();
        }
        if (mAnimSecond!=null){
            mAnimSecond.start();
        }
        if (mAnimFirst!=null){
            mAnimFirst.start();
        }
    }


    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (mAnimOver == valueAnimator ){
            loadValue = (float) valueAnimator.getAnimatedValue();
        }else if (mAnimThird ==valueAnimator){
            thirdValue = (float) valueAnimator.getAnimatedValue();
        }else if (mAnimSecond ==valueAnimator){
            secondVlaue = (float) valueAnimator.getAnimatedValue();
        }else if (mAnimFirst ==valueAnimator){
            firstValue = (float) valueAnimator.getAnimatedValue();
        }
        invalidate();
    }


}
