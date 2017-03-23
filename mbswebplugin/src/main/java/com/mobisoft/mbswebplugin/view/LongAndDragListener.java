package com.mobisoft.mbswebplugin.view;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/6/14.
 *
 * xiaonuo
 *
 * 图标的长按拖拽的监听
 */
public class LongAndDragListener {
    private static final String TAG =null;

    /**
     * 实现将一个视图拖动到另一个视图所在的位置
     * @param viewFrom   起始View
     * @param viewTo     终点View
     * @throws Exception
     */
    public void clickLongAndDrag(View viewFrom, View viewTo) throws Exception {
        //获得视图View中手机屏幕上的绝对x、y坐标
        final int[] location = new int[2];
        final int[] location2 = new int[2];
        viewFrom.getLocationOnScreen(location);
        viewTo.getLocationOnScreen(location2);

        float xStart=location[0];
        float yStart=location[1];

        float xStop=location2[0];
        float yStop=location2[1];
        Log.i(TAG, "xStart:"+ String.valueOf(xStart));
        Log.i(TAG, "yStart:" + String.valueOf(yStart));
        Log.i(TAG, "xStop:"+ String.valueOf(xStop));
        Log.i(TAG, "yStop:"+ String.valueOf(yStop));

        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        try{
            MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xStart+10f, yStart+10f, 0);
//            inst.sendPointerSync(event);
            //event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xStart+10f+1.0f, yStart+10f+1.0f, 0);
            //inst.sendPointerSync(event);
            //Thread.sleep(1000);
            //延迟一秒，模拟长按操作
            eventTime = SystemClock.uptimeMillis() + 1000;
            //xStop加了10点坐标，获得的View坐标需根据应用实际情况稍做一点调整
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xStop+10f, yStop+50f, 0);
//            inst.sendPointerSync(event);
            eventTime = SystemClock.uptimeMillis() + 1000;
            //又再小小移动了一次，不这么做的话可以无法激活被测应用状态，导致View移动后又回复到原来位置
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xStop+10f, yStop+10f, 0);
//            inst.sendPointerSync(event);
            eventTime = SystemClock.uptimeMillis() + 1000;
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xStop+10f, yStop+10f, 0);
//            inst.sendPointerSync(event);
        }catch (Exception ignored) {
            // Handle exceptions if necessary
        }
    }

}
