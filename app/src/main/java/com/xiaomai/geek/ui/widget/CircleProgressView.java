package com.xiaomai.geek.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.DisplayUtils;

/**
 * Created by XiaoMai on 2017/8/1.
 */

public class CircleProgressView extends View {

    private int progressStartColor;
    private int progressEndColor;
    private int backgroundStartColor;
    private int backgroundEndColor;
    private float progress;
    private float progressStrokeWidth;
    private float startAngle;
    private float sweepAngle;
    private boolean showAnim;

    private RectF rectF = new RectF();

    private Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private boolean hasSetup;
    private int width;
    private int height;
    private float radius;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttributes(context, attrs);
    }

    private void setUp() {
        SweepGradient progressShader = new SweepGradient(0, 0, progressStartColor, progressEndColor);
        progressPaint.setShader(progressShader);
        progressPaint.setStrokeWidth(progressStrokeWidth);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        SweepGradient backgroundShader = new SweepGradient(0, getHeight(), backgroundStartColor, backgroundEndColor);
        backgroundPaint.setShader(backgroundShader);
        backgroundPaint.setStrokeWidth(progressStrokeWidth);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        hasSetup = true;
    }

    private void obtainAttributes(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);

        progressStartColor = typedArray.getColor(R.styleable.CircleProgressView_cpv_progress_start_color, DEFAULT_PROGRESS_START_COLOR);
        progressEndColor = typedArray.getColor(R.styleable.CircleProgressView_cpv_progress_end_color, progressStartColor);
        backgroundStartColor = typedArray.getColor(R.styleable.CircleProgressView_cpv_background_start_color, DEFAULT_BACKGROUND_START_COLOR);
        backgroundEndColor = typedArray.getColor(R.styleable.CircleProgressView_cpv_background_end_color, backgroundStartColor);
        progress = typedArray.getFloat(R.styleable.CircleProgressView_cpv_progress, 0);
        progressStrokeWidth = DisplayUtils.dip2px(context, typedArray.getFloat(R.styleable.CircleProgressView_cpv_progress_stroke_width, DEFAULT_PROGRESS_STROKE_WIDTH));
        startAngle = typedArray.getFloat(R.styleable.CircleProgressView_cpv_start_angle, DEFAULT_START_ANGLE);
        sweepAngle = typedArray.getFloat(R.styleable.CircleProgressView_cpv_sweep_angle, DEFAULT_SWEEP_ANGLE);
        showAnim = typedArray.getBoolean(R.styleable.CircleProgressView_cpv_show_anim, true);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getWidth() - getPaddingRight() - getPaddingLeft();
        height = getHeight() - getPaddingTop() - getPaddingBottom();
        radius = Math.min(width / 2, height / 2) - progressStrokeWidth / 2;

        rectF.set(-radius, -radius, radius, radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!hasSetup) {
            setUp();
        }

        canvas.translate(width / 2 + getPaddingLeft(), height / 2 + getPaddingTop());

        // 先画背景
        float backgroundStartAngle = startAngle + progress;
        float backgroundSweepAngle = sweepAngle - progress;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(-radius, -radius, radius, radius, backgroundStartAngle, backgroundSweepAngle, false, backgroundPaint);
            // 再画进度条
            canvas.drawArc(-radius, -radius, radius, radius, startAngle, progress, false, progressPaint);
        } else {
            canvas.drawArc(rectF, backgroundStartAngle, backgroundSweepAngle, false, backgroundPaint);
            // 再画进度条
            canvas.drawArc(rectF, startAngle, progress, false, progressPaint);
        }

//        if (Math.abs(currentProgress - progress) > 0.1) {
//            if (currentProgress < progress) {
//                if (currentProgress == 0) {
//                    currentProgress = 1;
//                } else {
//                    currentProgress *= 1.1;
//                }
//                postInvalidate();
//            } else if (currentProgress > progress) {
//                currentProgress -= 0.2;
//                postInvalidate();
//            }
//        }
    }

    @Keep
    public void setProgress(float progress) {
        this.progress = progress;
        postInvalidate();
    }

    public float getProgress() {
        return progress;
    }

    private static final int DEFAULT_PROGRESS_START_COLOR = Color.RED;
    private static final int DEFAULT_BACKGROUND_START_COLOR = Color.LTGRAY;
    private static final float DEFAULT_PROGRESS_STROKE_WIDTH = 15F;
    private static final float DEFAULT_START_ANGLE = 150F;
    private static final float DEFAULT_SWEEP_ANGLE = 240F;
}
