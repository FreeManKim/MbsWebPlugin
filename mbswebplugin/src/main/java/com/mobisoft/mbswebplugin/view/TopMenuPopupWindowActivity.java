package com.mobisoft.mbswebplugin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.adapter.TopMenuAdapter;


/**
 * 顶部菜单
 */

public class TopMenuPopupWindowActivity extends PopupWindow {

	private ListView lv_basic_pop;

	private View conentView;

	private Context context;

	private OnActionClickListener mOnActionClickListener;

//	public SingleSelectionAdapter mSingleSelectionAdapter;

	// top 菜单适配器
	public TopMenuAdapter mTopMenuAdapter;


	public interface OnActionClickListener {
		void onSingleItemClickListener(AdapterView<?> parent, View view, int position, long id);
	}

	public void setOnActionClickListener(
			OnActionClickListener mOnActionClickListener) {
		this.mOnActionClickListener = mOnActionClickListener;
	}

	@SuppressLint("InflateParams")
	public TopMenuPopupWindowActivity(Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.activity_toppopupwindow, null);

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
//		mSingleSelectionAdapter = new SingleSelectionAdapter(context);
//		lv_basic_pop.setAdapter(mSingleSelectionAdapter);
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
			// 以下拉方式显示popupwindow
			this.showAsDropDown(parent, 0, 0);
//			this.showAtLocation(parent, Gravity.TOP, 0, 0); //设置layout在PopupWindow中显示的位置
		} else {
			this.dismiss();
		}
	}



}
