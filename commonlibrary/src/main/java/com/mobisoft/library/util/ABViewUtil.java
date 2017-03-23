package com.mobisoft.library.util;


import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;

import android.view.View;
import android.widget.ImageView;

public class ABViewUtil {

	/**
	 * view设置background drawable
	 *
	 * @param view
	 * @param drawable
	 */
	public static void setBackgroundDrawable(View view, Drawable drawable) {
		// if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
		view.setBackgroundDrawable(drawable);
		// } else {
		// view.setBackground(drawable);
		// }
	}

	/**
	 * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
	 *
	 * @param view
	 * @return
	 */
	public static int getViewMeasuredHeight(View view) {

		calcViewMeasure(view);
		return view.getMeasuredHeight();
	}

	/**
	 * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
	 *
	 * @param view
	 * @return
	 */
	public static int getViewMeasuredWidth(View view) {

		calcViewMeasure(view);
		return view.getMeasuredWidth();
	}

	/**
	 * 测量控件的尺寸
	 *
	 * @param view
	 */
	public static void calcViewMeasure(View view) {

		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
		view.measure(width, expandSpec);
	}

	/**
	 * 使用ColorFilter来改变亮度
	 *
	 * @param imageview
	 * @param brightness
	 */
	public static void changeBrightness(ImageView imageview, float brightness) {
		imageview.setColorFilter(getBrightnessMatrixColorFilter(brightness));
	}

	public static void changeBrightness(Drawable drawable, float brightness) {
		drawable.setColorFilter(getBrightnessMatrixColorFilter(brightness));
	}

	private static ColorMatrixColorFilter getBrightnessMatrixColorFilter(float brightness) {
		ColorMatrix matrix = new ColorMatrix();
		matrix.set(
				new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
		return new ColorMatrixColorFilter(matrix);
	}

}