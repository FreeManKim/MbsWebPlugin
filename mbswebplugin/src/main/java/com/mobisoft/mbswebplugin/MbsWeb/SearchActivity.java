package com.mobisoft.mbswebplugin.MbsWeb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.BaseWebActivity;
import com.mobisoft.mbswebplugin.utils.ToastUtil;


/**
 * 搜索
 * 
 * @author sam
 *
 */
public class SearchActivity extends BaseWebActivity {

	/**
	 * 设置一个变量
	 */
	private String state;
	/**
	 * Listview
	 */
	private ListView lv_search;
	/**
	 * 搜索
	 */
	private EditText et_searchs;
	private LinearLayout ll_cancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initViews();
		initData();
		initEvents();
	}

	protected void initViews() {
		lv_search = (ListView) findViewById(R.id.lv_search);
		et_searchs = (EditText) findViewById(R.id.et_search);
		ll_cancle = (LinearLayout) findViewById(R.id.ll_cancle);

	}

	protected void initData() {

	}

	protected void initEvents() {
		lv_search.setOnItemClickListener(onitemclick);
		ll_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		et_searchs.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() == 0) {
					lv_search.setVisibility(View.GONE);
				} 
//				else {
//					lv_search.setVisibility(View.VISIBLE);
//					state = et_searchs.getText().toString();
//					searchFriend(state);
//				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		
		et_searchs.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					((InputMethodManager) et_searchs.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					
					lv_search.setVisibility(View.VISIBLE);
					state = et_searchs.getText().toString();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
//							searchadapter.setKeword(state);
						}
					});
					
					return true;
				}
				return false;
			}
		});
	}

	OnItemClickListener onitemclick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			ToastUtil.showLongToast(SearchActivity.this, "你点不到我！");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

//	public void search(String string) {
//		arrayListlist.clear();
//		for (int i = 0; i < sStrings.length; i++) {
//
//			if (sStrings[i].contains(string)) {
//				arrayListlist.add(sStrings[i]);
//			}
//		}
//		// 只在当前的listview上进行搜索
//		lv_search.setAdapter(new SearchAdapter(SearchActivity.this, arrayListlist));
//	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

}
