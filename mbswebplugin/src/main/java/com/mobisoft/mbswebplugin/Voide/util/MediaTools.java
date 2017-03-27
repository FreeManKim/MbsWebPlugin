package com.mobisoft.mbswebplugin.Voide.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaTools {

	public static final int ACTION_VOICE = 0011;	//   声音
	
	public static final int ACTION_PROGRESS = 0012;	//   进度
	
	public static final int ACTION_LIGHT = 0013;	//  亮度
	
	public static final int ACTION_NORMAL = 0000;
	

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_MOBILE = 0x02;
	
	
	/**
	 * 修改画布的大小，和画布可视区大小
	 * @param type
	 */
	public static void changeSize(Activity context, SurfaceView surfaceView, int type) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mSurfaceViewWidth = dm.widthPixels;
		int mSurfaceViewHeight = dm.heightPixels;
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		lp.width = mSurfaceViewWidth;
		lp.height = mSurfaceViewHeight;
//		if(type == 0){	//横屏
//			lp.height = mSurfaceViewHeight;
//		}else{	//竖屏
//			int dp2px = DataTools.dip2px(context,300);
//			lp.height = dp2px;
//		}
		surfaceView.setLayoutParams(lp);
		surfaceView.getHolder().setFixedSize(mSurfaceViewWidth,
				mSurfaceViewHeight);
	}
	
	
	
	
	
	/**
	 * 将时间好描述转化为xx:xx格式
	 * @param str
	 * @return
	 */
	public static String simpleDate(long str) {
		SimpleDateFormat dateformat = new SimpleDateFormat("mm:ss");
		String a2 = dateformat.format(new Date(str));
		return a2;
	}

	

	/**
     * 检测网络是否可用
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：手机网络
     */
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }        
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
        	netType = NETTYPE_MOBILE;
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }
	
}
