package com.mobisoft.mbswebplugin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.adapter.SingleSelectionAdapter;

public class SingleSeletPopupWindow extends PopupWindow {

	private ListView mLv_single_selection;

	private View conentView;

	private Context context;

	private OnActionClickListener mOnActionClickListener;

	public SingleSelectionAdapter mSingleSelectionAdapter;

	public interface OnActionClickListener {
		void onSingleItemClickListener(AdapterView<?> parent, View view, int position, long id);
	}

	public void setOnActionClickListener(
			OnActionClickListener mOnActionClickListener) {
		this.mOnActionClickListener = mOnActionClickListener;
	}

	@SuppressLint("InflateParams")
	public SingleSeletPopupWindow(Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.popup_single_layout, null);

		// 设置SelectPicPopupWindow的View
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);

		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimationPreview);

		initViews();
		initEvents();

	}

	public void initViews() {
		mLv_single_selection = (ListView) conentView.findViewById(R.id.lv_single_selection);
		mSingleSelectionAdapter = new SingleSelectionAdapter(context);
		mLv_single_selection.setAdapter(mSingleSelectionAdapter);
	}

	public void initEvents() {
		mLv_single_selection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mOnActionClickListener.onSingleItemClickListener(parent,view,position,id);
			}
		});
	}

	/**
	 * 显示popupWindow
	 * @param parent
	 */
	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			// 以下拉方式显示popupwindow
//			this.showAsDropDown(parent, 0, -20);
			this.showAtLocation(parent, Gravity.CENTER, 0, 0); //设置layout在PopupWindow中显示的位置
		} else {
			this.dismiss();
		}
	}
	
}
