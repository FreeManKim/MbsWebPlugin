package com.mobisoft.mbswebplugin.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobisoft.mbswebplugin.Entity.Task;
import com.mobisoft.mbswebplugin.R;
import com.tencent.smtt.sdk.WebView;

import java.util.List;

/**
 * Author：Created by fan.xd on 2017/2/16.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 任务弹窗适配器
 */

public class TaskAdapter extends BaseAdapter {
    /**
     * 环境
     */
    private final Context mContext;
    /**
     * webView
     */
    private final WebView mWebView;
    /**
     * 任务弹窗
     */
    private final Dialog dialog;
    String function;
    private List<Task> list ;

    public TaskAdapter(String function, List<Task> list, Context context
            , WebView webView, Dialog dialog) {
        this.function = function;
        this.list= list;
        this.mContext = context;
        this.mWebView =webView;
        this.dialog =dialog;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_task_listview, null);

//            Picasso.with(DialogActivity.this).load(list.get(position).getImages_url()).into(viewHold.img_left);
        TextView title = (TextView) convertView.findViewById(R.id.text_title);
        TextView text_month = (TextView) convertView.findViewById(R.id.text_month);
        TextView text_task = (TextView) convertView.findViewById(R.id.text_task);
        title.setText(list.get(position).getHeader_title());
        text_month.setText(list.get(position).getDate() + "");

        text_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("function", function + "==" + list.get(position).getUrl());
                String josn1 = String.format("javascript:" + function + "(" + "'%s')", list.get(position).getUrl());
                mWebView.loadUrl(josn1);
                dialog.cancel();
            }
        });

        return convertView;
    }


}