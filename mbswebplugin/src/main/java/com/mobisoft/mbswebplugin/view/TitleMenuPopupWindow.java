package com.mobisoft.mbswebplugin.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.adapter.TopMenuAdapter;

/**
 * title菜单
 */
public class TitleMenuPopupWindow extends PopupWindow {

	private ListView lv_basic_pop;

	private View conentView;

	private Context context;

	private int width;
	private int height;

	private OnActionClickListener mOnActionClickListener;

//	public SingleSelectionAdapter mSingleSelectionAdapter;

	// title 菜单适配器
	public TopMenuAdapter mTopMenuAdapter;


	public interface OnActionClickListener {
		void onSingleItemClickListener(AdapterView<?> parent, View view, int position, long id);
	}

	public void setOnActionClickListener(
			OnActionClickListener mOnActionClickListener) {
		this.mOnActionClickListener = mOnActionClickListener;
	}

	@SuppressLint("InflateParams")
	public TitleMenuPopupWindow(Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.activity_toppopupwindow, null);
		DisplayMetrics metric = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;  // 宽度（PX）
		height = metric.heightPixels;  // 高度（PX）
		Log.i("LLL","width = " + width + "，height = " + height);


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
		lv_basic_pop = (ListView) conentView.findViewById(R.id.lv_basic_pop);
		mTopMenuAdapter = new TopMenuAdapter(context);
		lv_basic_pop.setAdapter(mTopMenuAdapter);

	}

	public void initEvents() {
		lv_basic_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
			this.showAsDropDown(parent,width*4/15,0);
		} else {
			this.dismiss();
		}
	}

}
