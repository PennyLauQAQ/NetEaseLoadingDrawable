package com.cn.sinodata.circledrawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.animation.LinearInterpolator;

public class CircleDrawable extends Drawable implements IAnimatable {


    private static final int DURATION = 1500;
    private static final int BASE_PAINT_WIDTH = 5;
    private static final int PAINT_UNIT = 20;
    private static final int DEFAULT_SIZE = 100;
    private Paint mPaint;
    private float mWidth, mHeight;
    private ValueAnimator mValueAnimator;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;

    private float scale = 1.0f;
    private int count = 0;
    private RectF mRectF;
    private int[] mColors;


    public CircleDrawable(final int[] colors) {
        mWidth = DEFAULT_SIZE;
        mHeight = DEFAULT_SIZE;
        mRectF = new RectF();
        mPaint = new Paint();
        mPaint.setStrokeWidth(BASE_PAINT_WIDTH);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mColors = colors;

        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setDuration(DURATION);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                count = 0;
                if (BuildConfig.DEBUG) {
                    Log.e("TAG", "onAnimationEnd");
                }

            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                count = 0;
                if (BuildConfig.DEBUG) {
                    Log.e("TAG", "onAnimationStart");
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                //0 or 1
                count ^= 1;
                if (BuildConfig.DEBUG) {
                    Log.e("TAG", "onAnimationRepeat");
                }
            }
        });
        mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale = (Float) animation.getAnimatedValue();
                invalidateSelf();
            }
        };
    }

    @Override
    public int getIntrinsicWidth() {
        //default width when wrap_content
        return (int) mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        //default height when wrap_content
        return (int) mHeight;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mWidth = bounds.width();
        mHeight = bounds.height();
        if (BuildConfig.DEBUG) {
            Log.e("TAG", "bound changed:" + bounds.toShortString());
        }
    }

    public void setScale(float scale) {
        if (this.scale != scale) {
            this.scale = scale;
            invalidateSelf();
        }
    }

    public void setColors(int[] colors){
        mColors = colors;
        invalidateSelf();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float realWidth = BASE_PAINT_WIDTH * (1 + scale);
        float realPadding = BASE_PAINT_WIDTH + (PAINT_UNIT * scale);
        Rect bound = getBounds();
        mRectF.set(bound.left + realPadding, bound.top + realPadding, bound.right - realPadding, bound.bottom - realPadding);
        int unit = 180 / mColors.length;
        //rotate
        float dtAngle = count == 0 ? 360 * scale : 360 * (1 - scale);
        for (int i = 0; i < mColors.length; i++) {
            mPaint.setColor(mColors[i]);
            mPaint.setStrokeWidth(realWidth);
            canvas.drawArc(mRectF, unit * 2 * i + dtAngle, unit, false, mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void start() {
        mValueAnimator.addUpdateListener(mAnimatorUpdateListener);
        mValueAnimator.start();
    }

    @Override
    public void stop() {
        mValueAnimator.removeUpdateListener(mAnimatorUpdateListener);
        mValueAnimator.end();
    }

    @Override
    public boolean isRunning() {
        return mValueAnimator.isRunning();
    }
}
