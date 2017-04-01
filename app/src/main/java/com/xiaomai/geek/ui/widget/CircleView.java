
package com.xiaomai.geek.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.DisplayUtils;

/**
 * Created by XiaoMai on 2016/12/19 14:28.
 */
public class CircleView extends ImageView {

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final int COLOR_DRAWABLE_DIMENSION = 2;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    // 默认边框的宽度为0
    private static final int DEFAULT_BORDER_WIDTH = 0;

    // 默认边框的颜色为黑色
    private static final int DEFAULT_BORDER_COLOR = Color.RED;

    // 默认的填充颜色为透明颜色
    private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;

    private static final boolean DEFAULT_BORDER_OVERLAY = false;

    private final RectF mDrawableRect = new RectF();

    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();

    private final Paint mBitmapPaint = new Paint();

    private final Paint mBorderPaint = new Paint();

    private final Paint mFillPaint = new Paint();

    private final Paint mTextPaint = new Paint();

    // 边框的宽度
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;

    // 边框的颜色
    private int mBorderColor = DEFAULT_BORDER_COLOR;

    // 填充的颜色
    private int mFillColor = DEFAULT_FILL_COLOR;

    private float mTextSize;

    private int mTextColor;

    private boolean mIsCircle;

    private String mText;

    private Bitmap mBitmap;

    private boolean mBorderOverlay = DEFAULT_BORDER_OVERLAY;

    private ColorFilter mColorFilter;

    // 是否准备好了
    private boolean mReady;

    // 是否等待设置中
    private boolean mSetupPending;

    // 位图渲染器
    private BitmapShader mBitmapShader;

    private int mBitmapHeight;

    private int mBitmapWidth;

    // 边框的半径
    private float mBorderRadius;

    // 圆形图片的半径
    private float mDrawableRadius;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView,
                defStyleAttr, 0);
        mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.CircleView_border_width,
                DEFAULT_BORDER_WIDTH);
        mBorderColor = typedArray.getColor(R.styleable.CircleView_border_color,
                DEFAULT_BORDER_COLOR);
        mBorderOverlay = typedArray.getBoolean(R.styleable.CircleView_border_overlay,
                DEFAULT_BORDER_OVERLAY);
        mFillColor = typedArray.getColor(R.styleable.CircleView_fill_color,
                DEFAULT_FILL_COLOR);
        mText = typedArray.getString(R.styleable.CircleView_text);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleView_textSize,
                DisplayUtils.dip2px(context, 15));
        mTextColor = typedArray.getColor(R.styleable.CircleView_textColor, Color.WHITE);
        mIsCircle = typedArray.getBoolean(R.styleable.CircleView_isCircle, false);
        typedArray.recycle();
        init();
    }

    private void init() {
        setScaleType(SCALE_TYPE);
        mReady = true;
        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    private void setup() {
        if (!mReady) {
            // 如果还没有准备好，就设置为等待设置中
            mSetupPending = true;
            return;
        }
        if (getWidth() == 0 && getHeight() == 0) {
            // 如果当前控件的宽度和高度都为0就返回
            return;
        }
        if (mBitmap == null) {
            // 如果bitmap为空，重绘
            invalidate();
            return;
        }
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, // 横向边缘拉伸
                Shader.TileMode.CLAMP); // 纵向边缘拉伸

        /********** Bitmap画笔start **********/
        // 设置抗锯齿，平滑效果
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);
        /********** Bitmap画笔end **********/

        /********** 边框画笔start **********/
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        /********** 边框画笔end **********/

        /********** 填充画笔start **********/
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);
        /********** 填充画笔end **********/

        /********** Text画笔end **********/
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        /********** Text画笔end **********/

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mBorderRect.set(calculateBounds());
        mBorderRadius = (mBorderRect.width() - mBorderWidth) / 2.0f;
        mDrawableRect.set(mBorderRect);
        if (!mBorderOverlay && mBorderWidth > 0) {
            mDrawableRect.inset(mBorderWidth - 1.0f, mBorderWidth - 1.0f);
        }

        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);

        applyColorFilter();
        updateShaderMatrix();
        invalidate();
    }

    /**
     * 这是圆形所在的矩形
     *
     * @return
     */
    private RectF calculateBounds() {
        // 真正的宽度
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        // 真正的高度
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        // 因为圆形的宽和高的长度都是直径的长度，所以边长（直径）为最短的边长
        int sideLength = Math.min(availableWidth, availableHeight);

        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

        return new RectF(left, top, left + sideLength, top + sideLength);
    }

    private void applyColorFilter() {
        if (mBitmapPaint != null) {
            mBitmapPaint.setColorFilter(mColorFilter);
        }
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }
        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left,
                (int) (dy + 0.5f) + mDrawableRect.top);
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(
                    String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }

        mColorFilter = cf;
        applyColorFilter();
        invalidate();
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    public void setText(String text) {
        this.mText = text;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mIsCircle) {
            super.onDraw(canvas);
            if (!TextUtils.isEmpty(mText)) {
                Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
                canvas.drawText(mText, mDrawableRadius - mTextPaint.measureText(mText) / 2,
                        mDrawableRadius - fontMetricsInt.descent
                                + (fontMetricsInt.bottom - fontMetricsInt.top) / 2,
                        mTextPaint);
            }
            return;
        }
        if (mBitmap == null) {
            return;
        }
        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius,
                    mFillPaint);
        }
        canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerX(), mDrawableRadius,
                mBitmapPaint);
        if (mBorderWidth > 0) {
            canvas.drawCircle(mBorderRect.centerX(), mBorderRect.centerY(), mBorderRadius,
                    mBorderPaint);
        }
        if (!TextUtils.isEmpty(mText)) {
            Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
            canvas.drawText(mText, mDrawableRect.centerX() - mTextPaint.measureText(mText) / 2,
                    mDrawableRect.centerY() - fontMetricsInt.descent
                            + (fontMetricsInt.bottom - fontMetricsInt.top) / 2,
                    mTextPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    @Override
    public ColorFilter getColorFilter() {
        return mColorFilter;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    private void initializeBitmap() {
        if (mIsCircle) {
            mBitmap = null;
        } else {
            mBitmap = getBitmapFromDrawable(getDrawable());
        }
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION,
                        BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }
            Canvas canvas = new Canvas(bitmap);
            // 设置边界
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setBorderColor(@ColorInt int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }
        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }
}
