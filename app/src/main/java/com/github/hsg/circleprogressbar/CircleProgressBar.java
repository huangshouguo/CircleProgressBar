package com.github.hsg.circleprogressbar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;


/**
 * Created by hsg on 9/19/16.
 */
public class CircleProgressBar extends View {
    // the default value of this view property
    private static final int DEFAULT_BACKGROUND_STROKE_COLOR = Color.parseColor("#dddddd");
    private static final int DEFAULT_PROGRESS_STROKE_START_COLOR = Color.parseColor("#00a2ff");
    private static final int DEFAULT_PROGRESS_STROKE_END_COLOR = Color.parseColor("#d175ec");
    private static final int DEFAULT_PROGRESS_TEXT_COLOR = Color.parseColor("#313131");
    private static final float DEFAULT_BACKGROUND_STROKE_WIDTH = 0.8f;
    private static final float DEFAULT_PROGRESS_STROKE_WIDTH = 1f;
    private static final float DEFAULT_PROGRESS_TEXT_SIZE = 4f;
    private static final int DEFAULT_PROGRESS_ANIMATE_DURATION = 1000; //unit: ms

    // this view property
    private float progress;
    private int backgroundStrokeColor;
    private int progressStrokeStartColor;
    private int progressStrokeEndColor;
    private int progressTextColor;
    private float backgroundStrokeWidth;
    private float progressStrokeWidth;
    private boolean progressTextShowEnable;
    private float progressTextSize;
    private String progressText;
    private boolean progressAnimateShowEnable;
    private int progressAnimateDuration;

    // helper property
    private int width;
    private int height;
    private Paint progressBackgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private RectF rectF;
    private ValueAnimator progressAnimator;


    public CircleProgressBar(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = (progress < 0.0f) ? 0.0f : ((progress > 1.0f) ? 1.0f : progress);
    }

    public int getBackgroundStrokeColor() {
        return backgroundStrokeColor;
    }

    public void setBackgroundStrokeColor(int backgroundStrokeColor) {
        this.backgroundStrokeColor = backgroundStrokeColor;
    }

    public int getProgressStrokeStartColor() {
        return progressStrokeStartColor;
    }

    public void setProgressStrokeStartColor(int progressStrokeStartColor) {
        this.progressStrokeStartColor = progressStrokeStartColor;
    }

    public int getProgressStrokeEndColor() {
        return progressStrokeEndColor;
    }

    public void setProgressStrokeEndColor(int progressStrokeEndColor) {
        this.progressStrokeEndColor = progressStrokeEndColor;
    }

    public int getProgressTextColor() {
        return progressTextColor;
    }

    public void setProgressTextColor(int progressTextColor) {
        this.progressTextColor = progressTextColor;
    }

    public float getBackgroundStrokeWidth() {
        return backgroundStrokeWidth;
    }

    public void setBackgroundStrokeWidth(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
    }

    public float getProgressStrokeWidth() {
        return progressStrokeWidth;
    }

    public void setProgressStrokeWidth(float progressStrokeWidth) {
        this.progressStrokeWidth = progressStrokeWidth;
    }

    public boolean isProgressTextShowEnable() {
        return progressTextShowEnable;
    }

    public void setProgressTextShowEnable(boolean progressTextShowEnable) {
        this.progressTextShowEnable = progressTextShowEnable;
    }

    public float getProgressTextSize() {
        return progressTextSize;
    }

    public void setProgressTextSize(float progressTextSize) {
        this.progressTextSize = progressTextSize;
    }

    public String getProgressText() {
        return progressText;
    }

    public void setProgressText(String progressText) {
        this.progressText = progressText;
    }

    public boolean isProgressAnimateShowEnable() {
        return progressAnimateShowEnable;
    }

    public void setProgressAnimateShowEnable(boolean progressAnimateShowEnable) {
        this.progressAnimateShowEnable = progressAnimateShowEnable;
    }

    public int getProgressAnimateDuration() {
        return progressAnimateDuration;
    }

    public void setProgressAnimateDuration(int progressAnimateDuration) {
        this.progressAnimateDuration = progressAnimateDuration;
    }

    public void startAnimationAction(final Animator.AnimatorListener animatorListener) {
        if (this.progressAnimator != null) {
            this.progressAnimator.cancel();
        }

        this.progressAnimator = ValueAnimator.ofFloat(0, getProgress());
        this.progressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationCancel(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationRepeat(animation);
                }
            }
        });
        this.progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                setProgress(progress);
                invalidate();
            }
        });
        this.progressAnimator.setDuration(getProgressAnimateDurationImpl());
        this.progressAnimator.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        this.width = Math.min(height, width);
        this.height = this.width;

        final float leftTop = 0 + getProgressStrokeWidthImpl() / 2.0f;
        final float rightBottom = this.width - getProgressStrokeWidthImpl() / 2.0f;
        this.rectF.set(leftTop, leftTop, rightBottom, rightBottom);

        setMeasuredDimension(width, height);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgressBackground(canvas);
        drawProgress(canvas);
        drawText(canvas);
    }

    //--------------------------------------------------------------------------------------------//
    // helper
    //--------------------------------------------------------------------------------------------//
    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0);
        this.progress = typedArray.getFloat(R.styleable.CircleProgressBar_cpb_progress, 0f);
        this.backgroundStrokeColor = typedArray.getColor(R.styleable.CircleProgressBar_cpb_background_stroke_color, DEFAULT_BACKGROUND_STROKE_COLOR);
        this.progressStrokeStartColor = typedArray.getColor(R.styleable.CircleProgressBar_cpb_progress_stroke_start_color, DEFAULT_PROGRESS_STROKE_START_COLOR);
        this.progressStrokeEndColor = typedArray.getColor(R.styleable.CircleProgressBar_cpb_progress_stroke_end_color, DEFAULT_PROGRESS_STROKE_END_COLOR);
        this.progressTextColor = typedArray.getColor(R.styleable.CircleProgressBar_cpb_progress_text_color, DEFAULT_PROGRESS_TEXT_COLOR);
        this.backgroundStrokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_cpb_background_stroke_width, getSizeInPixel(DEFAULT_BACKGROUND_STROKE_WIDTH));
        this.progressStrokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_cpb_progress_stroke_width, getSizeInPixel(DEFAULT_PROGRESS_STROKE_WIDTH));
        this.progressTextShowEnable = typedArray.getBoolean(R.styleable.CircleProgressBar_cpb_progress_text_show_enable, false);
        this.progressTextSize = typedArray.getDimension(R.styleable.CircleProgressBar_cpb_progress_text_size, getSizeInPixel(DEFAULT_PROGRESS_TEXT_SIZE));
        this.progressText = typedArray.getString(R.styleable.CircleProgressBar_cpb_progress_text);
        this.progressAnimateShowEnable = typedArray.getBoolean(R.styleable.CircleProgressBar_cpb_progress_animate_show_enable, false);
        this.progressAnimateDuration = typedArray.getInt(R.styleable.CircleProgressBar_cpb_progress_animate_duration, DEFAULT_PROGRESS_ANIMATE_DURATION);
        typedArray.recycle();

        this.rectF = new RectF();
    }

    private void drawProgressBackground(Canvas canvas) {
        canvas.drawArc(getRectF(), 0, 360, false, this.progressBackgroundPaint);
    }

    private void drawProgress(Canvas canvas) {
        float sweepAngle = getProgress() * 360;
        canvas.drawArc(getRectF(), 0, sweepAngle, false, this.progressPaint);
    }

    private void drawText(Canvas canvas) {
        Rect textRect = new Rect();
        textPaint.getTextBounds(getTextStringImpl(), 0, getTextStringImpl().length(), textRect);
        canvas.drawText(getTextStringImpl(), getCenterX(), getCenterY() + textRect.height() / 2, textPaint);
    }

    private RectF getRectF() {
        return this.rectF;
    }

    private float getProgressBackgroundStrokeWidthImpl() {
        return getSizeInPixel(getBackgroundStrokeWidth() > 0.0f ? getBackgroundStrokeWidth() : DEFAULT_BACKGROUND_STROKE_WIDTH);
    }

    private float getProgressStrokeWidthImpl() {
        return getSizeInPixel(getProgressStrokeWidth() > 0.0f ? getProgressStrokeWidth() : DEFAULT_PROGRESS_STROKE_WIDTH);
    }

    private int getBackgroundStrokeColorImpl() {
        return getBackgroundStrokeColor();
    }

    private int getProgressStrokeStartColorImpl() {
        return getProgressStrokeStartColor();
    }

    private int getProgressStrokeEndColorImpl() {
        return getProgressStrokeEndColor();
    }

    private int getProgressTextColorImpl() {
        return getProgressTextColor();
    }

    private String getTextStringImpl() {
        return TextUtils.isEmpty(getProgressText()) ? String.format("%d%%", (int) (getProgress() * 100)) : getProgressText();
    }

    private float getTextSizeImpl() {
        final float textSize = getProgressTextSize();
        return getSizeInPixel(textSize <= 0f ? DEFAULT_PROGRESS_TEXT_SIZE : textSize);
    }

    private int getProgressAnimateDurationImpl() {
        return (int) (((getProgressAnimateDuration() <= 0) ? DEFAULT_PROGRESS_ANIMATE_DURATION : getProgressAnimateDuration()) * getProgress());
    }

    private float getCenterX() {
        return this.width / 2.0f;
    }

    private float getCenterY() {
        return this.height / 2.0f;
    }

    private void initPaint() {
        // 进度背景画笔初始化
        this.progressBackgroundPaint = new Paint();
        this.progressBackgroundPaint.setStrokeWidth(this.getProgressBackgroundStrokeWidthImpl());
        this.progressBackgroundPaint.setColor(getBackgroundStrokeColorImpl());
        this.initPaintCommon(this.progressBackgroundPaint);


        // 进度画笔初始化
        this.progressPaint = new Paint();
        this.progressPaint.setStrokeWidth(this.getProgressStrokeWidthImpl());

        //line
        //this.progressPaint.setShader(new LinearGradient(0, 0, 0, this.height, getProgressStrokeStartColorImpl(), getProgressStrokeEndColorImpl(), Shader.TileMode.CLAMP));

        //radial
//        this.progressPaint.setShader(new RadialGradient(this.width/2,
//                this.height/2,
//                (this.width - getProgressStrokeWidthImpl())/2,
//                getProgressStrokeStartColorImpl(),
//                getProgressStrokeEndColorImpl(),
//                Shader.TileMode.CLAMP));

        float radius = (this.width - getProgressStrokeWidthImpl()) / 2;
        float radian = (float) (getProgressStrokeWidthImpl() / Math.PI * 2.0f / radius);
        float rotateDegrees = (float) (0 - Math.toDegrees(radian));
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegrees, getCenterX(), getCenterY());
        Shader shader = new SweepGradient(getCenterX(),
                getCenterY(),
                new int[]{getProgressStrokeStartColorImpl(), getProgressStrokeEndColorImpl()},
                new float[]{0f, 1f});
        shader.setLocalMatrix(matrix);
        this.progressPaint.setShader(shader);
        this.initPaintCommon(this.progressPaint);

        // 文字画笔初始化
        this.textPaint = new Paint();
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.textPaint.setTextSize(getTextSizeImpl());
        this.textPaint.setColor(getProgressTextColorImpl());
        this.initPaintCommon(this.textPaint);
    }

    private void initPaintCommon(Paint paint) {
        if (paint != null) {
            paint.setAntiAlias(true);//抗锯齿
            paint.setDither(true); //防抖动
            paint.setStyle(Paint.Style.STROKE);
            paint.setAlpha(255);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
        }
    }

    private float getSizeInPixel(final float inputValue) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, inputValue, dm);
    }
}

