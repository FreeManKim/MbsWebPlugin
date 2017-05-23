package com.mobisoft.mbswebplugin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobisoft.mbswebplugin.Entity.MeunItem;
import com.mobisoft.mbswebplugin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 单选适配器
 * @author li.yong
 */
public class TopMenuAdapter extends BaseAdapter {
    /**
     * 单选数据源
     */
    private List<MeunItem> list = new ArrayList<MeunItem>();
    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 选中的索引
     */
    private int indexSelection = -1;

    public TopMenuAdapter(Context context) {
        this.context = context;
    }

    public TopMenuAdapter(Context context, List<MeunItem> listTests) {
        this.context = context;
        this.list = listTests;
    }
    public void setData(List<MeunItem> projectstests) {
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
                    R.layout.top_menu_layout, null);
            vHolder = new ViewHolder();
            vHolder.txt_menu_item = (TextView) view
                    .findViewById(R.id.txt_menu_item);
            vHolder.img_menu_item = (ImageView) view
                    .findViewById(R.id.img_menu_item);
            view.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) view.getTag();
        }
        vHolder.txt_menu_item.setText(list.get(position).getName());
        if (indexSelection == position) {
            vHolder.txt_menu_item.setBackgroundColor(context.getResources()
                    .getColor(R.color.actionsheet_red));
            vHolder.txt_menu_item.setTextColor(context.getResources()
                    .getColor(R.color.color_black));
        } else {
            vHolder.txt_menu_item.setBackgroundColor(context.getResources()
                    .getColor(R.color.meu_right_bg_color));
            vHolder.txt_menu_item.setTextColor(context.getResources()
                    .getColor(R.color.white));
        }

        if(!TextUtils.isEmpty(list.get(position).getIcon())){
            vHolder.img_menu_item.setVisibility(View.VISIBLE);
            Picasso.with(context).load(list.get(position).getIcon()).into(vHolder.img_menu_item );
        }else{
            vHolder.img_menu_item.setVisibility(View.GONE);
        }
        return view;
    }

    private class ViewHolder {
        private TextView txt_menu_item;//
        private ImageView img_menu_item;//

    }

    public int getIndexSelection() {
        return indexSelection;
    }

    public void setIndexSelection(int indexSelection) {
        this.indexSelection = indexSelection;
        this.notifyDataSetChanged();
    }

}
