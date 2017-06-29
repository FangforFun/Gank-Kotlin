package com.gkzxhn.gank_kotlin.ui.wegit;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.wingsofts.dragphotoview.DragPhotoView;

import uk.co.senab.photoview.PhotoView;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by 方 on 2017/6/28.
 */

public class MyDragPhotoView extends PhotoView{
    private Paint mPaint;
    private float mDownX;
    private float mDownY;
    private float mTranslateY;
    private float mTranslateX;
    private float mScale;
    private int mWidth;
    private int mHeight;
    private float mMinScale;
    private int mAlpha;
    private static final int MAX_TRANSLATE_Y = 500;
    private static final long DURATION = 300L;
    private boolean canFinish;
    private boolean isAnimate;
    private boolean isTouchEvent;
    private MyDragPhotoView.OnTapListener mTapListener;
    private MyDragPhotoView.OnExitListener mExitListener;
    private boolean afterMove; //是否刚action_move

    public MyDragPhotoView(Context context) {
        this(context, (AttributeSet)null);
    }

    public MyDragPhotoView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public MyDragPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        this.mScale = 1.0F;
        this.mMinScale = 0.5F;
        this.mAlpha = 255;
        this.canFinish = false;
        this.isAnimate = false;
        this.isTouchEvent = false;
        this.mPaint = new Paint();
        this.mPaint.setColor(-16777216);
    }

    protected void onDraw(Canvas canvas) {
        this.mPaint.setAlpha(this.mAlpha);
        canvas.drawRect(0.0F, 0.0F, 2000.0F, 2000.0F, this.mPaint);
        canvas.translate(this.mTranslateX, this.mTranslateY);
        canvas.scale(this.mScale, this.mScale, (float)(this.mWidth / 2), (float)(this.mHeight / 2));
        super.onDraw(canvas);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        float scale = 0;
        try {
            scale = this.getScale();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(scale == 1.0F) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    this.onActionDown(event);
                    this.canFinish = !this.canFinish;
                    break;
                case MotionEvent.ACTION_UP:
                    if(event.getPointerCount() == 1) {
                        this.onActionUp(event);
                        this.isTouchEvent = false;
                        this.postDelayed(new Runnable() {
                            public void run() {
                                if(MyDragPhotoView.this.mTranslateX == 0.0F && MyDragPhotoView.this.mTranslateY == 0.0F && MyDragPhotoView.this.canFinish && MyDragPhotoView.this.mTapListener != null) {
                                    MyDragPhotoView.this.mTapListener.onTap(MyDragPhotoView.this);
                                }

                                MyDragPhotoView.this.canFinish = false;
                            }
                        }, 300L);
                        Log.i(TAG, "dispatchTouchEvent: canfinish " + canFinish);
                        Log.i(TAG, "dispatchTouchEvent: afterMove " + afterMove);
                        afterMove = false;
                    }
                    break;
                case MotionEvent.ACTION_MOVE :
                    afterMove = true;
                    if(this.mTranslateY == 0.0F && this.mTranslateX != 0.0F && !this.isTouchEvent) {
                        this.mScale = 1.0F;
                        return super.dispatchTouchEvent(event);
                    }

                    if(this.mTranslateY >= 0.0F && event.getPointerCount() == 1) {
                        this.onActionMove(event);
                        if(this.mTranslateY != 0.0F) {
                            this.isTouchEvent = true;
                        }

                        return true;
                    }

                    if(this.mTranslateY >= 0.0F && (double)this.mScale < 0.95D) {
                        return true;
                    }
            }
        }else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN :
                    canFinish = !canFinish;
                    break;

                case MotionEvent.ACTION_UP :
                    if (!canFinish || afterMove) {
                        afterMove = false;
                        canFinish = false;
                        return super.dispatchTouchEvent(event);
                    }
                    if(event.getPointerCount() == 1) {
                        Runnable runnable = new Runnable() {
                            public void run() {
                                if (MyDragPhotoView.this.mTranslateX == 0.0F && MyDragPhotoView.this.mTranslateY == 0.0F && MyDragPhotoView.this.canFinish && MyDragPhotoView.this.mTapListener != null) {
                                    MyDragPhotoView.this.mTapListener.onTap(MyDragPhotoView.this);
                                }
                                MyDragPhotoView.this.canFinish = false;
                            }
                        };
                        this.postDelayed(runnable, 300L);
                        return super.dispatchTouchEvent(event);
                    }
                    afterMove = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    afterMove = true;
                    return super.dispatchTouchEvent(event);
            }
        }

        return super.dispatchTouchEvent(event);
    }

    private void onActionUp(MotionEvent event) {
        if(this.mTranslateY > 200.0F) {
            if(this.mExitListener == null) {
                throw new RuntimeException("DragPhotoView: onExitLister can\'t be null ! call setOnExitListener() ");
            }

            this.mExitListener.onExit(this, this.mTranslateX, this.mTranslateY, (float)this.mWidth, (float)this.mHeight);
        } else {
            this.performAnimation();
        }

    }

    private void onActionMove(MotionEvent event) {
        float moveY = event.getY();
        float moveX = event.getX();
        this.mTranslateX = moveX - this.mDownX;
        this.mTranslateY = moveY - this.mDownY;
        if(this.mTranslateY < 0.0F) {
            this.mTranslateY = 0.0F;
        }

        float percent = this.mTranslateY / 500.0F;
        if(this.mScale >= this.mMinScale && this.mScale <= 1.0F) {
            this.mScale = 1.0F - percent;
            this.mAlpha = (int)(255.0F * (1.0F - percent));
            if(this.mAlpha > 255) {
                this.mAlpha = 255;
            } else if(this.mAlpha < 0) {
                this.mAlpha = 0;
            }
        }

        if(this.mScale < this.mMinScale) {
            this.mScale = this.mMinScale;
        } else if(this.mScale > 1.0F) {
            this.mScale = 1.0F;
        }

        this.invalidate();
    }

    private void performAnimation() {
        this.getScaleAnimation().start();
        this.getTranslateXAnimation().start();
        this.getTranslateYAnimation().start();
        this.getAlphaAnimation().start();
    }

    private ValueAnimator getAlphaAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(new int[]{this.mAlpha, 255});
        animator.setDuration(300L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                MyDragPhotoView.this.mAlpha = ((Integer)valueAnimator.getAnimatedValue()).intValue();
            }
        });
        return animator;
    }

    private ValueAnimator getTranslateYAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{this.mTranslateY, 0.0F});
        animator.setDuration(300L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                MyDragPhotoView.this.mTranslateY = ((Float)valueAnimator.getAnimatedValue()).floatValue();
            }
        });
        return animator;
    }

    private ValueAnimator getTranslateXAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{this.mTranslateX, 0.0F});
        animator.setDuration(300L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                MyDragPhotoView.this.mTranslateX = ((Float)valueAnimator.getAnimatedValue()).floatValue();
            }
        });
        return animator;
    }

    private ValueAnimator getScaleAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{this.mScale, 1.0F});
        animator.setDuration(300L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                MyDragPhotoView.this.mScale = ((Float)valueAnimator.getAnimatedValue()).floatValue();
                MyDragPhotoView.this.invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animator) {
                MyDragPhotoView.this.isAnimate = true;
            }

            public void onAnimationEnd(Animator animator) {
                MyDragPhotoView.this.isAnimate = false;
                animator.removeAllListeners();
            }

            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }
        });
        return animator;
    }

    private void onActionDown(MotionEvent event) {
        this.mDownX = event.getX();
        this.mDownY = event.getY();
    }

    public float getMinScale() {
        return this.mMinScale;
    }

    public void setMinScale(float minScale) {
        this.mMinScale = minScale;
    }

    public void setOnTapListener(MyDragPhotoView.OnTapListener listener) {
        this.mTapListener = listener;
    }

    public void setOnExitListener(MyDragPhotoView.OnExitListener listener) {
        this.mExitListener = listener;
    }

    public void finishAnimationCallBack() {
        this.mTranslateX = (float)(-this.mWidth / 2) + (float)this.mWidth * this.mScale / 2.0F;
        this.mTranslateY = (float)(-this.mHeight / 2) + (float)this.mHeight * this.mScale / 2.0F;
        this.invalidate();
    }

    public interface OnExitListener {
        void onExit(MyDragPhotoView var1, float var2, float var3, float var4, float var5);
    }

    public interface OnTapListener {
        void onTap(MyDragPhotoView var1);
    }
}
