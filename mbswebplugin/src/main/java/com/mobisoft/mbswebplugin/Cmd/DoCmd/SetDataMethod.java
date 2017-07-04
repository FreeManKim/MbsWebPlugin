package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.mobisoft.mbswebplugin.utils.Utils.IN_PARAMETER_FOR_DATE;

/**
 * Author：Created by fan.xd on 2017/2/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 设置日期
 * 入参： "date":  "2016-10-22"  日期及格式、
 * 出参：{date:'2016-09-09',result:true}
 */

public class SetDataMethod extends DoCmdMethod {
    private TimePickerView pvTime;
    private Context context;
    String time = "";
    String year = "";
    private String endDate;
    private String startDate;
    private TimePickerView.Builder newBuilder;
    private String callBack;
    private HybridWebView webView;
    private MbsWebPluginContract.View view;

    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
//        Utils.getTimePickerDialog( webView, context, Utils.DATA_SELECT_DATA, callBack, params);
        this.context = context;
        this.webView = webView;
        this.view = view;
        this.callBack = callBack;
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(params);
            time = jsonObject.optString("time");// 图片url
            year = jsonObject.optString("date");// 日期
            endDate = jsonObject.optString("endDate");// 结束日期
            startDate = jsonObject.optString("startDate");// 开始日期
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initTimePicker();
        pvTime.show();
        return null;
    }

    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11

        Calendar selectedDate = Calendar.getInstance();
        if (!TextUtils.isEmpty(year)) {
            String[] dates = year.split("-");
            selectedDate.set(Integer.valueOf(dates[0]), Integer.valueOf(dates[1]) - 1, Integer.valueOf(dates[2]));
        }

        //时间选择器
        newBuilder = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
//                webView.excuteJSFunction(callBack, IN_PARAMETER_FOR_DATE, getTime(date));
                JSONObject myJsonObject = null;
                try {
                    //将字符串转换成jsonObject对象
                    myJsonObject = new JSONObject();
                    myJsonObject.put(IN_PARAMETER_FOR_DATE,getTime(date));
                    view.loadUrl(UrlUtil.getFormatJs(callBack, myJsonObject.toString()));
                } catch (JSONException e) {

                }
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null);


        Calendar startDates = Calendar.getInstance();

        if (!TextUtils.isEmpty(startDate)) {
            String[] dates = startDate.split("-");

            startDates.set(Integer.valueOf(dates[0]), Integer.valueOf(dates[1]) - 1, Integer.valueOf(dates[2]));
        } else {
            startDates.set(1970, 0, 1);
        }
        Calendar endDates = Calendar.getInstance();

        if (!TextUtils.isEmpty(endDate)) {
            String[] dates = endDate.split("-");
            endDates.set(Integer.valueOf(dates[0]), Integer.valueOf(dates[1]) - 1, Integer.valueOf(dates[2]));
        } else {
            startDates.set(2100, 0, 1);
        }
        newBuilder.setRangDate(startDates, endDates);
        pvTime = newBuilder.build();

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
