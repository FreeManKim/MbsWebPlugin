package com.mobisoft.mbswebplugin.data;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobisoft.mbswebplugin.R;

import java.util.ArrayList;
import java.util.Calendar;

public class DataView extends LinearLayout {

	private static final int MIN_YEAR = 1970;
	private static final int MAX_YEAR = 2100;

	public static final int DIALOG_MODE_CENTER = 0;
	public static final int DIALOG_MODE_BOTTOM = 1;

	private WheelView wvYear;
	private WheelView wvMonth;
	private WheelView wvDay;

	private View vDialog;
	private View vDialogChild;
	private ViewGroup VDialogPicker;
	private TextView tvTitle;
	private TextView btnSure;
	private TextView btnCancel;

	private ArrayList<String> arry_years = new ArrayList<String>();
	private ArrayList<String> arry_months = new ArrayList<String>();
	private ArrayList<String> arry_days = new ArrayList<String>();
	private CalendarTextAdapter mYearAdapter;
	private CalendarTextAdapter mMonthAdapter;
	private CalendarTextAdapter mDaydapter;

	private int month;
	private int day;

	private int currentYear = getYear();
	private int currentMonth = getMonth();
	private int currentDay = getDay();

	private int maxTextSize = 24;
	private int minTextSize = 18;

	private boolean issetdata = false;

	private String selectYear;
	private String selectMonth;
	private String selectDay;

	private String strTitle = "选择日期";

	private OnDatePickTestListener OnDatePickTestListener;


	public DataView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public DataView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}


	public DataView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
//		LayoutInflater.from(context).inflate(R.layout.dialog_picker_center, this, true); 
		init(context);
	}

	private void init(final Context context) {
		View.inflate(context, R.layout.dialog_picker_center, this);
		VDialogPicker = (ViewGroup) findViewById(R.id.ly_dialog_picker);
		LayoutParams lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f);
		// 此处相当于布局文件中的Android:layout_gravity属性
		lp.gravity = Gravity.CENTER_VERTICAL;

		wvYear = new WheelView(context);
		wvYear.setLayoutParams(lp);
		VDialogPicker.addView(wvYear);

		wvMonth = new WheelView(context);
		wvMonth.setLayoutParams(lp);
		VDialogPicker.addView(wvMonth);

		wvDay = new WheelView(context);
		wvDay.setLayoutParams(lp);
		VDialogPicker.addView(wvDay);

		vDialog = findViewById(R.id.ly_dialog);
		vDialogChild = findViewById(R.id.ly_dialog_child);
		tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
		btnSure = (TextView) findViewById(R.id.btn_dialog_sure);
		btnCancel = (TextView) findViewById(R.id.btn_dialog_cancel);
		btnSure.setOnClickListener(click);
		if (!issetdata) {
			initData();
		}
		
		initYears();
		mYearAdapter = new CalendarTextAdapter(context, arry_years,
				setYear(currentYear), maxTextSize, minTextSize);
		wvYear.setVisibleItems(5);
		wvYear.setViewAdapter(mYearAdapter);
		wvYear.setCurrentItem(setYear(currentYear));

		initMonths(month);
		mMonthAdapter = new CalendarTextAdapter(context, arry_months,
				setMonth(currentMonth), maxTextSize, minTextSize);
		wvMonth.setVisibleItems(5);
		wvMonth.setCyclic(true);
		wvMonth.setViewAdapter(mMonthAdapter);
		wvMonth.setCurrentItem(setMonth(currentMonth));

		initDays(day);
		mDaydapter = new CalendarTextAdapter(context, arry_days,
				currentDay - 1, maxTextSize, minTextSize);
		wvDay.setVisibleItems(5);
		wvDay.setCyclic(true);
		wvDay.setViewAdapter(mDaydapter);
		wvDay.setCurrentItem(currentDay - 1);

		wvYear.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mYearAdapter.getItemText(wheel
						.getCurrentItem());
				selectYear = currentText;
				setTextviewSize(currentText, mYearAdapter);
				currentYear = Integer.parseInt(currentText);
				setYear(currentYear);
				initMonths(month);
				mMonthAdapter = new CalendarTextAdapter(context, arry_months,
						0, maxTextSize, minTextSize);
				wvMonth.setVisibleItems(5);
				wvMonth.setViewAdapter(mMonthAdapter);
				wvMonth.setCurrentItem(0);
			}
		});

		wvYear.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mYearAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mYearAdapter);
			}
		});

		wvMonth.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel
						.getCurrentItem());
				selectMonth = currentText;
				setTextviewSize(currentText, mMonthAdapter);
				setMonth(Integer.parseInt(currentText));
				initDays(day);
				mDaydapter = new CalendarTextAdapter(context, arry_days, 0,
						maxTextSize, minTextSize);
				wvDay.setVisibleItems(5);
				wvDay.setViewAdapter(mDaydapter);
				wvDay.setCurrentItem(0);
			}
		});

		wvMonth.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mMonthAdapter);
			}
		});

		wvDay.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mDaydapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mDaydapter);
				selectDay = currentText;
			}
		});

		wvDay.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mDaydapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, mDaydapter);
			}
		});

	}

	public void initData() {
		setDate(getYear(), getMonth(), getDay());
	}

	public void initYears() {
		for (int i = MIN_YEAR; i < MAX_YEAR; i++) {
			arry_years.add(i + "");
		}
	}

	public void initMonths(int months) {
		arry_months.clear();
		for (int i = 1; i <= months; i++) {
			arry_months.add(i + "");
		}
	}

	public void initDays(int days) {
		arry_days.clear();
		for (int i = 1; i <= days; i++) {
			arry_days.add(i + "");
		}
	}

	public void setTitle(String title) {
		this.strTitle = title;
	}


	public void setDatePickListener01(OnDatePickTestListener OnDatePickTestListener) {
		this.OnDatePickTestListener = OnDatePickTestListener;
	}

	public interface OnDatePickTestListener {
		void onClick(String year, String month, String day);
	}

	OnClickListener click =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			OnDatePickTestListener.onClick(selectYear, selectMonth, selectDay);
		}
	};
	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText,
			CalendarTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxTextSize);
			} else {
				textvew.setTextSize(minTextSize);
			}
		}
	}

	
	
	public int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public int getMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1;
	}

	public int getDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE);
	}

	/**
	 * 设置年月日
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setDate(int year, int month, int day) {
		selectYear = year + "";
		selectMonth = month + "";
		selectDay = day + "";
		issetdata = true;
		this.currentYear = year;
		this.currentMonth = month;
		this.currentDay = day;
		this.month = 12;
		calDays(year, month);
	}

	/**
	 * 设置年份
	 * 
	 * @param year
	 */
	private int setYear(int year) {
		int yearIndex = 0;
		for (int i = MIN_YEAR; i < MAX_YEAR; i++) {
			if (i == year) {
				return yearIndex;
			}
			yearIndex++;
		}
		return yearIndex;
	}

	/**
	 * 设置月份
	 * 
	 * @param month
	 * @return
	 */
	private int setMonth(int month) {
		int monthIndex = 0;
		calDays(currentYear, month);
		for (int i = 1; i < this.month; i++) {
			if (month == i) {
				return monthIndex;
			} else {
				monthIndex++;
			}
		}
		return monthIndex;
	}

	/**
	 * 计算每月多少天
	 * 
	 * @param month
	 */
	public void calDays(int year, int month) {
		boolean leayyear = false;
        leayyear = year % 4 == 0 && year % 100 != 0;
		for (int i = 1; i <= 12; i++) {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				this.day = 31;
				break;
			case 2:
				if (leayyear) {
					this.day = 29;
				} else {
					this.day = 28;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				this.day = 30;
				break;
			}
		}
		if (year == getYear() && month == getMonth()) {
			this.day = getDay();
		}
	}

	private class CalendarTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected CalendarTextAdapter(Context context, ArrayList<String> list,
									  int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem,
					maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}
	
	/**
	 * 获取时间
	 */
	public void getDataTiem(OnDatePickTestListener OnDatePickTestListener){
		OnDatePickTestListener.onClick(selectYear, selectMonth, selectDay);
	}
}
