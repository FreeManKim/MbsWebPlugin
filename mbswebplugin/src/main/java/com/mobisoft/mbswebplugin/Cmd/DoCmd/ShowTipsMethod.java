package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mobisoft.mbswebplugin.Cmd.CMD;
import com.mobisoft.mbswebplugin.Cmd.CmdrBuilder;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.utils.UrlUtil;
import com.mobisoft.mbswebplugin.view.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mobisoft.mbswebplugin.proxy.Cache.CacheManifest.TAG;
import static com.mobisoft.mbswebplugin.utils.UrlUtil.parseUrl;

/**
 * Author：Created by fan.xd on 2017/2/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description： AlertDialog 弹出控件
 * 可以根据命令的不同显示不同的 样式的AlertDialog
 * {"title":"提示","content":"是否退出登录？","yes_action":"logout","no_action":"close"}入参描述：
 * title：提示框，头部标题 content: 提示框标语yes_action: 确认按钮，回掉方法 no_ation：取消按钮，回掉方法
 */

public class ShowTipsMethod extends DoCmdMethod {
    @Override
    public String doMethod(final HybridWebView webView, final Context context, final MbsWebPluginContract.View view, final MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {


        AlertDialog mAlertDialog = new AlertDialog(context).builder();

        try {
            JSONObject object = new JSONObject(params);
            final String title = object.optString("title");
            final String content = object.optString("content");
            final String confirm = object.optString("yes_action");
            final String cancel = object.optString("no_action");

            if (TextUtils.isEmpty(title)) {
                mAlertDialog.setTitle(context.getString(R.string.wen_xin_tips));
            } else {
                mAlertDialog.setTitle(title);
            }

            if (TextUtils.isEmpty(content)) {
                mAlertDialog.setTitle(context.getString(R.string.jing_gao));
            } else {
                mAlertDialog.setMsg(content);
            }

            if (!TextUtils.isEmpty(confirm))
                mAlertDialog.setPositiveButton(context.getString(R.string.que_ren), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 回调js方法
                        if (confirm.startsWith("http")) { // URL请求
                            Map<String, String> param = parseUrl(confirm);
                            if (param.containsKey("action")) {
                                String action = param.get("action");
                                if (action != null) {
                                    String value = action.toLowerCase();
                                    switch (value) {
                                        case CMD.action_nextPage:
                                            presenter.nextPage(confirm, value);
//                                            ((WebAppActivity) context).onNextPage(confirm, value);
                                            break;
                                        case CMD.action_closePage:
                                            presenter.onClosePage(confirm, value);

//                                            ((WebAppActivity) context).onClosePage(confirm, value);
                                            break;
                                        case CMD.action_closePageAndRefresh:
                                            presenter.onClosePageReturnMain(confirm, value);
//                                            ((WebAppActivity) context).onClosePageReturnMain(confirm, value);
                                            break;
                                    }
                                }
                            }
                        } else if (confirm.startsWith("kitapps")) { // 页面的功能函数
                            Map<String, String> param = parseUrl(confirm);
                            String parameter = param.get("para");
                            String function = param.get("callback");
                            Pattern p = Pattern.compile("\\//(.*?)\\?");//正则表达式，取=和|之间的字符串，不包括=和|
                            Matcher m = p.matcher(confirm);
                            String cmd = null;
                            while (m.find()) {
                                cmd = m.group();
                                break; // 菜单里的url带"?"的话会导致cmd取值不对，所以只拿第一次的cmd
                            }
                            if (cmd != null) {
                                cmd = cmd.substring(2, cmd.length() - 1);
                                CmdrBuilder.getInstance()
                                        .setContext(context)
                                        .setWebView(webView)
                                        .setCmd(cmd)
                                        .setContractView(view)
                                        .setPresenter(presenter)
                                        .setParameter(parameter)
                                        .setCallback(function)
                                        .doMethod();
//                                onCommand(cmd, parameter, function);
                            }
                        } else if (confirm.endsWith(")")) {
                            String json = "javascript:" + confirm;
                            Log.e(TAG, "==json:" + json);
                            webView.loadUrl(json);
                        } else if (TextUtils.equals(confirm, "close")) {// 关闭 mAlertDialog

                        } else {
//                            String json = String.format("javascript:" + confirm + "(%s)", "");
                            webView.loadUrl(UrlUtil.getFormatJs(confirm,""));
                        }
                    }
                });

            if (!TextUtils.isEmpty(cancel)) {
                mAlertDialog.setNegativeButton(context.getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ToastUtil.showLongToast(WebAppActivity.this, "取消！");
                    }
                });
            }

            mAlertDialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
