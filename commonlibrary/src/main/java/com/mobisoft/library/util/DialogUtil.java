package com.mobisoft.library.util;

import android.content.Context;
import android.view.View;

public class DialogUtil {
	private AlertDialog dialog = null;
	private static DialogUtil instance = null;
	private static Context mActivity = null;
	private static boolean isCancel=false;

	public static DialogUtil getInstance(Context mActivity) {
		return getInstance(mActivity, true);
	}

	public static DialogUtil getInstance(Context mActivity, boolean isCancel) {
		if (instance == null) {
			instance = new DialogUtil();
		}
		DialogUtil.mActivity = mActivity;
		DialogUtil.isCancel = isCancel;
		return instance;
	}

	public AlertDialog showDialog(String title, String message) {
		dialog = new AlertDialog(mActivity).builder().setMsg(message)
				.setTitle(title).setCancelable(isCancel)
				.setPositiveButton("确定", new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		return dialog;
	}
	public AlertDialog showDialog(String title, String message , String psoitveBtn ) {
		dialog = new AlertDialog(mActivity).builder().setMsg(message)
				.setTitle(title).setCancelable(isCancel)
				.setPositiveButton(psoitveBtn, new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		return dialog;
	}

	public AlertDialog showDialog(String title, String message, String neutralMsg, View.OnClickListener neutral) {
		dialog = new AlertDialog(mActivity).builder().setMsg(message).setTitle(title).setCancelable(isCancel)
				.setNegativeButton(neutralMsg, neutral);
		return dialog;
	}

	public AlertDialog showDialog(String title, String message, String positiveMsg, View.OnClickListener positive,
			String negativeMsg, View.OnClickListener negative) {
		dialog = new AlertDialog(mActivity).builder().setMsg(message).setTitle(title)
				.setPositiveButton(positiveMsg, positive).setNegativeButton(negativeMsg, negative)
				.setCancelable(isCancel);
		return dialog;
	}

}
