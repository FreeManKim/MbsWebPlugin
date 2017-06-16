package com.mobisoft.mbswebplugin.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * url解析工具类
 */
public class UrlUtil {
    /***
     *  利用正则表达式来解析url
     *  取得 param 参数 action 命令 cmd命令
     * @param url 地址
     * @return Map
     */
    public static Map<String, String> parseUrl(String url) {
        String param = url.substring(url.indexOf("?") + 1);
        param = param.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        String[] list = param.split("&");
        Map<String, String> result = new HashMap<String, String>();
        for (String str : list) {
            int first = str.indexOf("=");
            if (first != -1) {
                String[] keyval = new String[]{str.substring(0, first), str.substring(first + 1, str.length())};
                if (keyval.length == 2) {
                    try {
                        String pram = URLDecoder.decode(keyval[1], "UTF-8");
                        result.put(keyval[0], pram);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (Map.Entry<String, String> entry : result.entrySet()) {
            Log.i("parseUrl", "parseUrl()===key:" + entry.getKey() + "  value:" + entry.getValue());
        }

        return result;
    }


    /**
     * 判断是否为网址
     *
     * @param pInput
     * @return Boolean
     */
    public static Boolean isNewUrl(String pInput) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher1 = patt.matcher(pInput);
        boolean isMatch = matcher1.matches();
        return isMatch;
    }

    /**
     * 正则表达式匹配
     */
    private static Pattern ACTION = Pattern.compile("\\baction=(\\w+)\\b");

    /**
     * 截取action
     *
     * @param url
     * @return Param
     */
    private static String getActionParam(String url) {
        Matcher matcher = ACTION.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * 格式化 javascript
     *
     * @param function 回掉方法
     * @param paramter 参数
     * @return javascript方法
     */
    public static String getFormatJavascript(String function, String paramter) {
        return String.format("javascript:" + function + "('%s')", paramter);
    }

    /**
     * 格式化 javascript
     *
     * @param callBack 回掉方法
     * @param json     参数
     * @return javascript方法
     */
    public static String getFormatJs(String callBack, String json) {
        String newFunction = callBack;

        if (callBack.contains("#result#")) {
            newFunction = callBack.replace("#result#", TextUtils.isEmpty(json) ? "" : json);
        } else if (callBack.endsWith("(")) {
            newFunction = callBack.substring(0, callBack.length() - 1);
        } else {
            String json2 = String.format("javascript:" + newFunction + "('%s')", json);
            return json2;
        }
//		String function = callBack.replace("#result#", TextUtils.isEmpty(json)?"":json);
        String json1 = String.format("javascript:%s", newFunction);
        return json1;
    }

    /**
     * 格式化 javascript
     *
     * @param callBack 回掉方法
     * @param object     参数
     * @return javascript方法
     */
    public static String getFormatObj(String callBack, Object object) {
        String newFunction = callBack;

        if (callBack.contains("#result#")) {
            newFunction = callBack.replace("#result#", object==null ? "" : object.toString());
        } else if (callBack.endsWith("(")) {
            newFunction = callBack.substring(0, callBack.length() - 1);

        } else {
            String json2 = String.format("javascript:" + newFunction + "('%s')", object);
            return json2;
        }
//		String function = callBack.replace("#result#", TextUtils.isEmpty(json)?"":json);
        String json1 = String.format("javascript:%s", newFunction);
        return json1;
    }

}
