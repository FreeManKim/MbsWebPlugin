package com.mobisoft.MbsDemo.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.Log;

import com.mobisoft.MbsDemo.R;

import java.util.HashMap;


/**
 * Author：Created by fan.xd on 2017/5/11.
 * Email：fang.xd@mobis.com.cn
 * Description：
 */

public class MBSTabLayout extends TabLayout {


    public static final String TAG = "OMG";
    private boolean isAlpha;
    private boolean isTransparent = false;
    private HashMap<Integer, Integer> alphaMap = new HashMap<>();

    public MBSTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MBSTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView(context);

    }

    private void initView(Context context) {
        setBackgroundAlpha(isTransparent);
        addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
               rollBackState(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MBSTabLayout);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }


    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.MBSTabLayout_isTransparent) {
            isTransparent = typedArray.getBoolean(attr, true);

        }
    }

    public void setTransparent(Boolean b) {
        isTransparent = b;
        setBackgroundAlpha(b);
    }

    private void setBackgroundAlpha(Boolean b) {
        if (b) {
            getBackground().setAlpha(0);
        } else {
            getBackground().setAlpha(255);
        }
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    public void setScrollChange(int alpha, int index) {
        alphaMap.put(index, alpha);
        if (isTransparent) {
            setToTransparent(alpha);
        } else {
            setTopOpaque(alpha);
        }
    }

    public void rollBackState(int index) {
        Integer alpha = alphaMap.get(index);
        if (alpha == null)
            alpha = 0;
        if (isTransparent) {
            setToTransparent(alpha);
        } else {

            setTopOpaque(alpha);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure");
    }

    /**
     * 滑动到顶部 TabLayout 不透明
     *
     * @param alpha
     */
    private void setTopOpaque(int alpha) {
        int alpha1 = (255 - alpha);
        Log.i("oye", "alpha1/ " + alpha1 + "? alpha/ " + alpha);

        if (alpha1 >= 240 && !isAlpha) {
            this.getBackground().setAlpha(255);
            this.setAlpha(1f);
            isAlpha = true;
            MBSTabLayout.this.setVisibility(VISIBLE);

        } else if (alpha1 <= 10 && isAlpha) {
            isAlpha = false;
//            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 0.2f, 0f);
//            animator.setDuration(500);
//            animator.start();
            this.setAlpha(0f);

            MBSTabLayout.this.setVisibility(INVISIBLE);


        } else if (alpha1 > 20 && alpha1 < 240) {
            this.getBackground().setAlpha(alpha1);
            MBSTabLayout.this.setVisibility(VISIBLE);
            this.setAlpha(1f);
        }
    }

    /**
     * 滑动到顶部 TabLayout透明
     *
     * @param alpha1
     */
    private void setToTransparent(int alpha1) {
        if (alpha1 >= 255) {
            this.getBackground().setAlpha(255);
        } else if (alpha1 <= 20) {
            this.getBackground().setAlpha(0);
        } else {
            int i = alpha1 - 50;
            this.getBackground().setAlpha(i > 0 ? i : 0);
        }
    }

}
