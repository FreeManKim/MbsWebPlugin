package com.mobisoft.MbsDemo.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

/**
 * Author：Created by fan.xd on 2017/5/10.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class MBSScrollView extends ScrollView {
    public static final int ALPHA = 255;
    private boolean isScrolledToTop = true;
    // 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;
    private ISmartScrollChangedListener mSmartScrollChangedListener;

    /**
     * 定义监听接口
     */
    public interface ISmartScrollChangedListener {
        void onScrolledToBottom();

        void onScrolledToTop();

        void onScrolledChange(int alpha);
    }


    public MBSScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY == 0) {
            isScrolledToTop = clampedY;
            isScrolledToBottom = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = clampedY;
        }
        notifyScrollChangedListeners();

    }

    //
    @Override
    protected void onScrollChanged(int ScrollX, int mScrollY, int oldX, int oldY) {
        super.onScrollChanged(ScrollX, mScrollY, oldX, oldY);
        if (getScrollY() == 0) {
            isScrolledToTop = true;
            isScrolledToBottom = false;
        } else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() == getChildAt(0).getHeight()) {
            isScrolledToBottom = true;
            isScrolledToTop = false;
        } else {
            isScrolledToBottom = false;
            isScrolledToTop = false;
        }
        notifyScrollChangedListeners();
        if (mSmartScrollChangedListener != null) {
            Log.e("oye", "/  mScrollY  / " + mScrollY + "/  oldY  / " + oldY + "// ");
            mSmartScrollChangedListener.onScrolledChange(mScrollY);
        }


    }

    public void setScanScrollChangedListener(ISmartScrollChangedListener listener) {
        this.mSmartScrollChangedListener = listener;
    }

    private void notifyScrollChangedListeners() {
        if (isScrolledToTop) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToTop();
            }
        } else if (isScrolledToBottom) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToBottom();
            }
        }
    }

    public boolean isScrolledToTop() {
        return isScrolledToTop;
    }

    public boolean isScrolledToBottom() {
        return isScrolledToBottom;
    }
}
