package com.mobisoft.mbswebplugin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.adapter.SingleSelectionAdapter;


public class SeletAreaPopupWindow extends PopupWindow {

	private View conentView;

	private ListView mLv_province;
	private GridView mGv_city;
	private GridView mGv_district;

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
	public SeletAreaPopupWindow(Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.popup_select_area_layout, null);

		// 设置SelectPicPopupWindow的View
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
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
		initData();
		initEvents();

	}

	public void initViews() {
		// 选择省市相关
		mLv_province = (ListView) conentView.findViewById(R.id.lv_province);
		mGv_city = (GridView) conentView.findViewById(R.id.gv_city);
		mGv_district = (GridView) conentView.findViewById(R.id.gv_district);
	}

	public void initData() {
		mSingleSelectionAdapter = new SingleSelectionAdapter(context);
		mLv_province.setAdapter(mSingleSelectionAdapter);
	}

	public void initEvents() {
		mLv_province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
	}

	/**
	 * 显示popupWindow
	 * 
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
