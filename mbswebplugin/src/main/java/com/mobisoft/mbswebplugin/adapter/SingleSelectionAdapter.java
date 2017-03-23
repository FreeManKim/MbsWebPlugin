package com.mobisoft.mbswebplugin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobisoft.mbswebplugin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 单选适配器
 * @author li.yong
 */
public class SingleSelectionAdapter extends BaseAdapter {
	/**
	 * 单选数据源
	 */
	private List<String> list = new ArrayList<String>();
	/**
	 * 上下文对象
	 */
	private Context context;
	/**
	 * 选中的索引
	 */
	private int indexSelection = -1;

	public SingleSelectionAdapter(Context context) {
		this.context = context;
	}

	public SingleSelectionAdapter(Context context, List<String> listTests) {
		this.context = context;
		this.list = listTests;
	}
	public void setData(List<String> projectstests) {
		this.list = projectstests;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder vHolder = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_single_selection_item, null);
			vHolder = new ViewHolder();
			vHolder.txt_single_selection_context = (TextView) view
					.findViewById(R.id.txt_single_selection_context);
			vHolder.iv_single_selection_ico = (ImageView) view
					.findViewById(R.id.iv_single_selection_ico);
			view.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) view.getTag();
		}
		vHolder.txt_single_selection_context.setText(list.get(position));
		if (indexSelection == position) {
			vHolder.txt_single_selection_context.setBackgroundColor(context.getResources()
					.getColor(R.color.actionsheet_red));
			vHolder.txt_single_selection_context.setTextColor(context.getResources()
					.getColor(R.color.color_light_orange));
		} else {
			vHolder.txt_single_selection_context.setBackgroundColor(context.getResources()
					.getColor(R.color.actionsheet_red));
			vHolder.txt_single_selection_context.setTextColor(context.getResources()
					.getColor(R.color.color_black));
		}
		return view;
	}

	private class ViewHolder {
		private TextView txt_single_selection_context;//
		private ImageView iv_single_selection_ico;//
	}

	public int getIndexSelection() {
		return indexSelection;
	}

	public void setIndexSelection(int indexSelection) {
		this.indexSelection = indexSelection;
		this.notifyDataSetChanged();
	}

}
