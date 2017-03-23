package com.mobisoft.MbsDemo.LitsViewActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobisoft.MbsDemo.R;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebApp;
import com.mobisoft.mbswebplugin.helper.FunctionConfig;
import com.mobisoft.mbswebplugin.helper.ThemeConfig;

import java.util.ArrayList;

/**
 * Author：Created by fan.xd on 2017/1/17.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class ListAppAdapter extends RecyclerView.Adapter<ListAppAdapter.ListHolder> {
    private  ArrayList<MicroAppInfo> mAppList;
    private Context context;

    public ListAppAdapter(ArrayList<MicroAppInfo> mAppList, Context context) {
        this.mAppList = mAppList;
        this.context = context;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ListHolder(layout);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        holder.tv.setText(mAppList.get(position).getName());
        holder.tv2.setText(mAppList.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }

    class ListHolder extends RecyclerView.ViewHolder{
        TextView tv;
        TextView tv2;
        public ListHolder(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.item);
            tv2 = (TextView)itemView.findViewById(R.id.item2);
            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // webAitivity配置 主页面子应用跳转的逻辑
                    com.mobisoft.mbswebplugin.helper.CoreConfig coreConfig =
                            new com.mobisoft.mbswebplugin.helper.CoreConfig.Builder(
                            context, ThemeConfig.DEFAULT, FunctionConfig.DEFAULT_ACTIVITY)
                            .setURL(tv2.getText().toString())
                            .setAccount("8100458")//
                            .setNoAnimcation(false)
                            .build();
                    HybridWebApp.init(coreConfig).startWebActivity(context,com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebActivity.class);
                }
            });
        }
    }
}
