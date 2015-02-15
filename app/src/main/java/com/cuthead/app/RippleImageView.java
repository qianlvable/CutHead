package com.cuthead.app;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;


/**
 * TODO: document your custom view class.
 */
public class RippleImageView extends ImageView{

    private Paint backgroundPaint = new Paint();
    private static final Interpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();
    private static final long ANIMATION_TIME = 530;
    private static ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private float radius = 0;
    private float paintX = 0;
    private float paintY = 0;
    private Handler myHandler = new Handler();
    private Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };
    private Property<RippleImageView,Float> mRadiusProperty = new Property<RippleImageView, Float>(Float.class,"radius") {
        @Override
        public Float get(RippleImageView rippleImageView) {
            return rippleImageView.radius;
        }

        @Override
        public void set(RippleImageView object, Float value) {
            object.radius = value;
            invalidate();
        }
    };

    private Property<RippleImageView,Integer> mBackgroundColorProperty = new Property<RippleImageView, Integer>(Integer.class,"bg_color") {
        @Override
        public Integer get(RippleImageView rippleImageView) {
            return rippleImageView.backgroundPaint.getColor();
        }

        @Override
        public void set(RippleImageView object, Integer value) {
            object.backgroundPaint.setColor(value);
        }
    };

    public RippleImageView(Context context) {
        super(context);
    }

    public RippleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawCircle(paintX,paintY,radius,backgroundPaint);
        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            // 记录坐标
            paintX = event.getX();
            paintY = event.getY();
            // 启动动画
            startAnimator();
        }

        myHandler.postDelayed(delayRunnable, 100);
        return super.onTouchEvent(event);
    }

    private void startAnimator() {
        float start, end, height, width;
        long time = (long) (ANIMATION_TIME * 1.85);

        //Height Width
        height = getHeight();
        width = getWidth();

        //Start End
        if (height < width) {
            start = height;
            end = width;
        } else {
            start = width;
            end = height;
        }

        float startRadius = (start / 2 > paintY ? start - paintY : paintY) * 1.15f;
        float endRadius = (end / 2 > paintX ? end - paintX : paintX) * 0.85f;

        //If The approximate square approximate square
        if (startRadius > endRadius) {
            startRadius = endRadius * 0.6f;
            endRadius = endRadius / 0.8f;
            time = (long) (time * 0.5);
        }

        int startColor = 0xff40C4FF;
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(this, mRadiusProperty, startRadius, endRadius),
                ObjectAnimator.ofObject(this, mBackgroundColorProperty, argbEvaluator, Color.WHITE, Color.TRANSPARENT)
        );
        // set Time
        set.setDuration((long) (time / end * endRadius));
        set.setInterpolator(ANIMATION_INTERPOLATOR);
        set.start();
    }
}
