package com.mobisoft.library.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.mobisoft.library.log.TLog;
/**
 *全局的Taost
 * @author Fan xuedong
 * @date 2016年5月12日 下午9:15:03 
 * @version V1.0
 * @Description: TODO
 */
public class ToastPUtil {
	private static final String TAG = "ToastUtil";
	private static Toast toast;
	private static View view;

	private ToastPUtil() {
	}

	@SuppressLint("ShowToast")
	private static void getToast(Context context) {
		if (toast == null) {
			toast = new Toast(context);
		}
		if (view == null) {
			view = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
		}
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
	}

	public static void showShortToast(Context context, CharSequence msg) {
		showToast(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
	}

	public static void showShortToast(Context context, int resId) {
		showToast(context.getApplicationContext(), resId, Toast.LENGTH_SHORT);
	}

	public static void showLongToast(Context context, CharSequence msg) {
		showToast(context.getApplicationContext(), msg, Toast.LENGTH_LONG);
	}

	public static void showLongToast(Context context, int resId) {
		showToast(context.getApplicationContext(), resId, Toast.LENGTH_LONG);
	}

	private static void showToast(Context context, CharSequence msg,
			int duration) {
		try {
			getToast(context);
			toast.setText(msg);
			toast.setDuration(duration);
			toast.show();
		} catch (Exception e) {
			TLog.d(TAG, e.getMessage());
		}
	}

	private static void showToast(Context context, int resId, int duration) {
		try {
			if (resId == 0) {
				return;
			}
			getToast(context);
			toast.setText(resId);
			toast.setDuration(duration);
			toast.show();
		} catch (Exception e) {
			TLog.d(TAG, e.getMessage());
		}
	}

}
