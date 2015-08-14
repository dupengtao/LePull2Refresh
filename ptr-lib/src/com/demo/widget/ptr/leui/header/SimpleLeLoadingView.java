package com.demo.widget.ptr.leui.header;

import android.animation.*;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

/**
 * Created by dupengtao on 15-5-12.
 */
public class SimpleLeLoadingView extends View implements ValueAnimator.AnimatorUpdateListener {

    private static final int ROTATE_DURATION = 900;
    private static final int DISAPPEAR_DURATION = 300;
    private static final int DURATION = 1000;
    private static final int BALL_NUM = 6;
    private static int PERCENT_OFFSET = DURATION / 6 *2;
    private static int EVERY_DURATION = DURATION / 6 - PERCENT_OFFSET / 6;
    //private static int EVERY_DURATION = DURATION / 6;
    private float mBallRadius, mViewSize, mViewRadius;
    private ArrayList<BallsLoadingShapeHolder> mBalls = new ArrayList<>(6);
    private ArrayList<Integer> mColorList = new ArrayList<>(6);
    private AnimatorSet mDisappearAnim;
    private ObjectAnimator[] mAppearAnimators;
    private long mLastPercent;
    private ObjectAnimator mRotateAnim;

    //new
    private float rot;

    public SimpleLeLoadingView(Context context) {
        this(context, null);
    }

    public SimpleLeLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int[] attrsArray = new int[]{
                android.R.attr.layout_width, // 0
                android.R.attr.layout_height // 1
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        int layout_width = ta.getDimensionPixelSize(0, ViewGroup.LayoutParams.MATCH_PARENT);
        int layout_height = ta.getDimensionPixelSize(1, ViewGroup.LayoutParams.MATCH_PARENT);
        ta.recycle();
        init(layout_width, layout_height);

    }


    private void init(int layout_width, int layout_height) {
        mColorList.addAll(getDefaultColorList());
        prepare(layout_width, layout_height);
        preAnim();
    }

    public ArrayList<Integer> getDefaultColorList() {
        ArrayList<Integer> colorList = new ArrayList<>(6);
        colorList.add(Color.parseColor("#ed1e20"));
        colorList.add(Color.parseColor("#8c50e7"));
        colorList.add(Color.parseColor("#1ab1eb"));
        colorList.add(Color.parseColor("#80cb17"));
        colorList.add(Color.parseColor("#ffd200"));
        colorList.add(Color.parseColor("#ff8400"));
        return colorList;
    }

    private void initBall() {
        float angleUnit = 360f / BALL_NUM;
        float drawRadius = mViewRadius - mBallRadius;
        for (int i = 0; i < BALL_NUM; i++) {
            PointF pointF = new PointF();
            pointF.set((float) (mViewSize / 2 + drawRadius * Math.sin(i * angleUnit * Math.PI / 180)),
                    (float) (mViewSize / 2 - drawRadius * Math.cos(i * angleUnit * Math.PI / 180)));
            mBalls.add(addBall(pointF.x, pointF.y, mColorList.get(i)));
        }
    }


    private BallsLoadingShapeHolder addBall(float x, float y, int color) {
        OvalShape circle = new OvalShape();
        circle.resize(mBallRadius, mBallRadius);
        ShapeDrawable drawable = new ShapeDrawable(circle);
        BallsLoadingShapeHolder shapeHolder = new BallsLoadingShapeHolder(drawable);
        shapeHolder.setX(x);
        shapeHolder.setY(y);
        Paint paint = drawable.getPaint();
        paint.setColor(color);
        shapeHolder.setPaint(paint);
        shapeHolder.setAlpha(0f);
        return shapeHolder;
    }


    private void preAnim() {
        preAppearAnim();
        mRotateAnim = preRotateAnim2();
        preDisappearAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mRotateAnim.isRunning()) {
            canvas.rotate(rot, canvas.getWidth() / 2, canvas.getHeight() / 2);
        }

        for (BallsLoadingShapeHolder ball : mBalls) {
            if (ball.getShape().getAlpha() <= 0) {
                continue;
            }
            canvas.translate(ball.getX() - mBallRadius / 2, ball.getY() - mBallRadius / 2);
            ball.getShape().draw(canvas);
            canvas.translate(-ball.getX() + mBallRadius / 2, -ball.getY() + mBallRadius / 2);
        }
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    private void prepare(int w, int h) {
        //int h = getMeasuredHeight();
        //int w = getMeasuredWidth();
        int size = h >= w ? h : w;
        mViewSize = size;
        mBallRadius = size / (192 / 24);
        mViewRadius = size / (192 / 96);
        initBall();
    }

    private ObjectAnimator preRotateAnim() {
        PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation",
                0, 360);
        ObjectAnimator rotateAnim = ObjectAnimator.ofPropertyValuesHolder(this, rotation).setDuration(ROTATE_DURATION);
        rotateAnim.setRepeatCount(-1); // -1:Infinite loop
        rotateAnim.setInterpolator(new LinearInterpolator());
        return rotateAnim;
    }

    private ObjectAnimator preRotateAnim2() {
        PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rot",
                0, 360);
        //PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation",
        //        0, 360);
        ObjectAnimator rotateAnim = ObjectAnimator.ofPropertyValuesHolder(this, rotation).setDuration(ROTATE_DURATION);
        rotateAnim.setRepeatCount(-1); // -1:Infinite loop
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.addUpdateListener(this);
        //rotateAnim.addListener(new AnimatorListenerAdapter() {
        //    @Override
        //    public void onAnimationStart(Animator animation) {
        //        super.onAnimationStart(animation);
        //        SimpleLeLoadingView.this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        //    }
        //
        //    @Override
        //    public void onAnimationEnd(Animator animation) {
        //        super.onAnimationEnd(animation);
        //        SimpleLeLoadingView.this.setLayerType(View.LAYER_TYPE_NONE, null);
        //    }
        //
        //    @Override
        //    public void onAnimationCancel(Animator animation) {
        //        super.onAnimationCancel(animation);
        //        SimpleLeLoadingView.this.setLayerType(View.LAYER_TYPE_NONE, null);
        //    }
        //});
        return rotateAnim;
    }

    /**
     * 0 - normal
     */
    private ObjectAnimator getZero2Normal(BallsLoadingShapeHolder ball, int orderId) {
        PropertyValuesHolder pvhW = PropertyValuesHolder.ofFloat("width",
                0, ball.getWidth());
        PropertyValuesHolder pvhH = PropertyValuesHolder.ofFloat("height",
                0, ball.getHeight());
        PropertyValuesHolder pvTX = PropertyValuesHolder.ofFloat("x",
                ball.getX() + mBallRadius / 2, ball.getX());
        PropertyValuesHolder pvTY = PropertyValuesHolder.ofFloat("y",
                ball.getY() + mBallRadius / 2, ball.getY());
        ObjectAnimator z2nAnim = ObjectAnimator.ofPropertyValuesHolder(
                ball, pvhW, pvhH, pvTX, pvTY);

        z2nAnim.setDuration(EVERY_DURATION);
        //z2nAnim.setDuration(DURATION/BALL_NUM*orderId);
        z2nAnim.setInterpolator(new OvershootInterpolator());
        //z2nAnim.addUpdateListener(this);
        return z2nAnim;
    }

    private void preAppearAnim() {
        mAppearAnimators = new ObjectAnimator[mBalls.size()];
        for (int i = 0, j = mBalls.size(); i < j; i++) {
            //0-normal
            mAppearAnimators[i] = getZero2Normal(mBalls.get(i), i);
            mAppearAnimators[i].setTarget(mBalls.get(i));
            //mAppearAnimators[i].addListener(new EmptyAnimatorListener() {
            //    @Override
            //    public void onAnimationStart(Animator animation) {
            //        ObjectAnimator objectAnimator = (ObjectAnimator) animation;
            //        BallsLoadingShapeHolder holder = (BallsLoadingShapeHolder) objectAnimator.getTarget();
            //        if (holder != null) {
            //            holder.setAlpha(1f);
            //        }
            //    }
            //});
        }
        //appearAnim = new AnimatorSet();
        //appearAnim.playTogether(mAppearAnimators);
        //return appearAnim;
    }


    /**
     * normal - 0
     */
    private ObjectAnimator getNormal2Zero(BallsLoadingShapeHolder ball, int orderId) {
        PropertyValuesHolder pvhW = PropertyValuesHolder.ofFloat("width",
                ball.getWidth(), 0);
        PropertyValuesHolder pvhH = PropertyValuesHolder.ofFloat("height",
                ball.getHeight(), 0);
        PropertyValuesHolder pvTX = PropertyValuesHolder.ofFloat("x", ball.getX(),
                mViewSize / 2);
        PropertyValuesHolder pvTY = PropertyValuesHolder.ofFloat("y", ball.getY(),
                mViewSize / 2);
        ObjectAnimator z2nAnim = ObjectAnimator.ofPropertyValuesHolder(
                ball, pvhW, pvhH, pvTX, pvTY).setDuration(DISAPPEAR_DURATION);
        //z2nAnim.setStartDelay(75 * orderId);
        z2nAnim.setInterpolator(new AccelerateInterpolator());
        z2nAnim.addUpdateListener(this);
        return z2nAnim;
    }



    private void preDisappearAnim() {

        if (mDisappearAnim == null) {
            final ObjectAnimator[] mAnimators = new ObjectAnimator[mBalls.size()];
            for (int i = 0, j = mBalls.size(); i < j; i++) {
                //0-normal
                mAnimators[i] = getNormal2Zero(mBalls.get(i), i);
                mAnimators[i].setTarget(mBalls.get(i));
                mAnimators[i].addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ObjectAnimator objectAnimator = (ObjectAnimator) animation;
                        BallsLoadingShapeHolder holder = (BallsLoadingShapeHolder) objectAnimator.getTarget();
                        if (holder != null) {
                            holder.setAlpha(0f);
                        }
                    }
                });
            }
            mDisappearAnim = new AnimatorSet();
            mDisappearAnim.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mRotateAnim.pause();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    //mRotateAnim.cancel();
                    resetOriginals();
                }
            });
            mDisappearAnim.playTogether(mAnimators);
        }
        //mDisappearAnim.start();
    }


    public void setPercent(long percent) {
        if (percent < PERCENT_OFFSET) {
            return;
        }
        percent -= PERCENT_OFFSET;
        boolean isDown = getDirection(percent);
        if (percent <= EVERY_DURATION) {
            cancelRotateAnim();
            mAppearAnimators[0].setCurrentPlayTime(percent);
            mBalls.get(0).setAlpha(1);
            if (!isDown) {
                makeCurPlayTime(mAppearAnimators[1], 0);
                makeCurPlayTime(mAppearAnimators[2], 0);
                makeCurPlayTime(mAppearAnimators[3], 0);
                makeCurPlayTime(mAppearAnimators[4], 0);
                makeCurPlayTime(mAppearAnimators[5], 0);
            }
            invalidate();
        } else if (percent <= EVERY_DURATION * 2) {
            cancelRotateAnim();
            mAppearAnimators[1].setCurrentPlayTime(percent - EVERY_DURATION);
            mBalls.get(1).setAlpha(1);
            if (isDown) {
                makeCurPlayTime(mAppearAnimators[0], EVERY_DURATION);
            } else {
                makeCurPlayTime(mAppearAnimators[2], 0);
                makeCurPlayTime(mAppearAnimators[3], 0);
                makeCurPlayTime(mAppearAnimators[4], 0);
                makeCurPlayTime(mAppearAnimators[5], 0);
            }
            invalidate();
        } else if (percent <= EVERY_DURATION * 3) {
            cancelRotateAnim();
            mAppearAnimators[2].setCurrentPlayTime(percent - EVERY_DURATION * 2);
            mBalls.get(2).setAlpha(1);
            if (isDown) {
                makeCurPlayTime(mAppearAnimators[0], EVERY_DURATION);
                makeCurPlayTime(mAppearAnimators[1], EVERY_DURATION);
            } else {
                makeCurPlayTime(mAppearAnimators[5], 0);
                makeCurPlayTime(mAppearAnimators[4], 0);
                makeCurPlayTime(mAppearAnimators[3], 0);
            }
            invalidate();
        } else if (percent <= EVERY_DURATION * 4) {
            cancelRotateAnim();
            mAppearAnimators[3].setCurrentPlayTime(percent - EVERY_DURATION * 3);
            mBalls.get(3).setAlpha(1);
            if (isDown) {
                makeCurPlayTime(mAppearAnimators[0], EVERY_DURATION);
                makeCurPlayTime(mAppearAnimators[1], EVERY_DURATION);
                makeCurPlayTime(mAppearAnimators[2], EVERY_DURATION);

            } else {
                makeCurPlayTime(mAppearAnimators[5], 0);
                makeCurPlayTime(mAppearAnimators[4], 0);
            }
            invalidate();
        } else if (percent <= EVERY_DURATION * 5) {
            cancelRotateAnim();
            mAppearAnimators[4].setCurrentPlayTime(percent - EVERY_DURATION * 4);
            mBalls.get(4).setAlpha(1);
            if (isDown) {
                makeCurPlayTime(mAppearAnimators[0], EVERY_DURATION);
                makeCurPlayTime(mAppearAnimators[1], EVERY_DURATION);
                makeCurPlayTime(mAppearAnimators[2], EVERY_DURATION);
                makeCurPlayTime(mAppearAnimators[3], EVERY_DURATION);
            } else {
                makeCurPlayTime(mAppearAnimators[5], 0);
            }
            invalidate();
        } else if (percent < EVERY_DURATION * 6) {
            cancelRotateAnim();
            mAppearAnimators[5].setCurrentPlayTime(percent - EVERY_DURATION * 5);
            mBalls.get(5).setAlpha(1);
            if (isDown) {
                makeCurPlayTime(mAppearAnimators[0], EVERY_DURATION);
                makeCurPlayTime(mAppearAnimators[1], EVERY_DURATION);
                makeCurPlayTime(mAppearAnimators[2], EVERY_DURATION);
                makeCurPlayTime(mAppearAnimators[3], EVERY_DURATION);
                makeCurPlayTime(mAppearAnimators[4], EVERY_DURATION);
            }
            invalidate();
        } else {
            mBalls.get(0).setAlpha(1);
            mBalls.get(1).setAlpha(1);
            mBalls.get(2).setAlpha(1);
            mBalls.get(3).setAlpha(1);
            mBalls.get(4).setAlpha(1);
            mBalls.get(5).setAlpha(1);
            makeCurPlayTime(mAppearAnimators[0], EVERY_DURATION);
            makeCurPlayTime(mAppearAnimators[1], EVERY_DURATION);
            makeCurPlayTime(mAppearAnimators[2], EVERY_DURATION);
            makeCurPlayTime(mAppearAnimators[3], EVERY_DURATION);
            makeCurPlayTime(mAppearAnimators[4], EVERY_DURATION);
            makeCurPlayTime(mAppearAnimators[5], EVERY_DURATION);
            invalidate();

            if (mRotateAnim.isPaused()) {
                mRotateAnim.resume();
            } else if (!mRotateAnim.isRunning()) {
                mRotateAnim.start();
            }

            //if (!mRotateAnim.isRunning() || mRotateAnim.isPaused()) {
            //    mRotateAnim.start();
            //}
        }

    }

    public void completeAnim(){
        if(mDisappearAnim !=null){
            mDisappearAnim.start();
        }
    }

    private void cancelRotateAnim() {
        if (mRotateAnim.isRunning()) {
            mRotateAnim.cancel();
            //mRotateAnim.pause();
        }
    }

    private void makeCurPlayTime(ObjectAnimator appearAnimator, int time) {
        long currentPlayTime = appearAnimator.getCurrentPlayTime();
        if (currentPlayTime != time) {
            appearAnimator.setCurrentPlayTime(time);
        }
    }

    private boolean getDirection(long curPer) {
        boolean isDown = curPer > mLastPercent;
        mLastPercent = curPer;
        return isDown;
    }

    public void resetOriginals() {
        if (mRotateAnim != null) {
            mRotateAnim.cancel();
            setRotation(0);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //invalidate();
        int x = (int) getX()+1;
        int y = (int) getY()+1;
        invalidate(x, y, x + 672, y +672);
    }

    public float getRot() {
        return rot;
    }

    public void setRot(float rot) {
        this.rot = rot;
    }

    private void restInit(){
        initBall();
    }

    public void setEachColor4Balls(int color1,int color2,int color3,int color4,int color5,int color6){
        mColorList.clear();
        mColorList.add(color1);
        mColorList.add(color2);
        mColorList.add(color3);
        mColorList.add(color4);
        mColorList.add(color5);
        mColorList.add(color6);
        for (int i=0;i<BALL_NUM;i++){
            BallsLoadingShapeHolder shapeHolder = mBalls.get(i);
            shapeHolder.getPaint().setColor(mColorList.get(i));
        }
    }

    public void autoPull2RefreshAnim() {
        for(BallsLoadingShapeHolder holder : mBalls){
            holder.setAlpha(1f);
        }
        if (mRotateAnim.isPaused()) {
            mRotateAnim.resume();
        } else if (!mRotateAnim.isRunning()) {
            mRotateAnim.start();
        }
    }

    public void cancelAutoPull2RefreshAnim(){
        if(mRotateAnim.isRunning()){
            //mRotateAnim.pause();
            mRotateAnim.cancel();
        }
        setAllBallsAlpha(0f);
    }

    public void setAllBallsAlpha(float alpha) {
        for(BallsLoadingShapeHolder holder : mBalls){
            holder.setAlpha(alpha);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mRotateAnim!=null){
            mRotateAnim.cancel();
        }
    }
}
