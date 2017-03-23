package com.mobisoft.mbswebplugin.base;

/**
 * Created by fan on 2016/6/15.
 * 网络环境配置
 */
public class AppConfing {

    /**定位获取的地址Province*/
    public static String LOCATION_Province;
    /**定位获取的地址city*/
    public static String LOCATION_City;

    /**权限申请 code*/
    public static final int PERMISSIONS_REQUEST_CODE = 10010;

    /**
     * 聊天历史 界面 返回eventCode
     */
    public static final int CHTA_BACK = 110;
    /**
     * 工号ACCount；
     */
    public static String ACCOUNT = "";

    /**
     * MainActivity 的finsh方法   eventCode
     */
    public static final int MAIN_FINSH = 111;
    /**
     * MainActivity 的backDwon方法  eventCode
     */
    public static final int MAIN_BACK_DOWN = 112;

    /**
     * 改变状态栏的颜色  eventCode
     */
    public static final int STATUS_BAR = 113;


    /**
     * 群组聊天邀请好友  eventCode
     */
    public static final int INVITE_GROUP = 116;

    /**
     * 单聊天邀请好友  eventCode
     */
    public static final int INVITE_SINGLE = 117;

    /**
     * 传递单聊好友工号 eventCode
     */
    public static final int INVITE_SINGLE_MEMBRER = 118;

    /**
     * 待办数量
     */
    public static int CTTQ_WAITTASK_NUM = 0;

    /**
     * 获取轮播图的Url
     */
    public static final String BANNER_URL = "http://cmsdev2.cttq.com/tx/json/sy/gdtp/";


    /***************************** * 分割线 正大天晴 DEV环境 2016/11/18新增*******************************************/
//   /**
//     * 个人信息
//    */
//    public static final String CTTQ_URL = "http://aidev.cttq.com";
//
//    /**
//    *
//     * 正大天晴 基本接口 新DEV环境
//     */
//    public static final String CTTQ_BASE_URL = "http://ainewdev.cttq.com";
//    /**
//     * 待办事项
//     */
//    public static final String CTTQ_WAITTASK = CTTQ_BASE_URL + "/waitTask/waitCount";
//
//    /**
//     * html页面 地址拼接
//     */
//    public static final String CTTQ_WEB_PATH = "/tianxin";
//    /**
//     * 升级接口 测试环境 DEV环境
//     *
//    https://ainewqas.cttq.com:1443/install/install.xml
//     */
//
//    public static final String ULR_UP = "https://ainewdev.cttq.com:1443/install/install.xml";
//    /**
//     * 基本接口 测试环境 DEV环境
//     */
//    public static final String BASE_URL = CTTQ_BASE_URL + ":80/microapp/mobile";
//    /**
//     * 工号登陆接口 测试环境 DEV环境
//     */
//    public static final String LOGIN_URL = CTTQ_BASE_URL + ":80/account/mobile";


    /****************************** 分割线  UAT环境*******************************************/
    /**
     *   正大天晴 基本接口 UAT环境
     */
    public static final String CTTQ_URL = "http://ainewqas.cttq.com";
    /**
     * 正大天晴 基本接口 UAT环境
     */
    public static final String CTTQ_BASE_URL = "http://ainewqas.cttq.com";

    /**
     * 待办事项
     */
    public static final String CTTQ_WAITTASK = CTTQ_BASE_URL + "/waitTask/waitCount";
    /**
     * html页面 地址拼接
     */
    public static final String CTTQ_WEB_PATH = "/tianxin";
    /**
     * 升级接口  正式环境 UAT环境
     */
    public static final String ULR_UP = "https://ainewqas.cttq.com:1443/install/install.xml";
    /**
     * 工号登陆接口  正式环境UAT
     */
    public static final String LOGIN_URL = CTTQ_BASE_URL + "/account/mobile";
    /**
     * 基本接口 正式环境UAT
     */
    public static final String BASE_URL = CTTQ_BASE_URL + "/microapp/mobile";


    /******************************分割线  PRD环境******************************************/
//    /**
//     *   正大天晴 基本接口 PRD环境
//     */
//    public static final String CTTQ_URL = "http://ainewprd.cttq.com";
//    /**
//     * 正大天晴 基本接口 PRD环境
//     */
//    public static final String CTTQ_BASE_URL = "http://ainewprd.cttq.com";
//
//    /**
//     * 待办事项
//     */
//    public static final String CTTQ_WAITTASK = CTTQ_BASE_URL + "/waitTask/waitCount";
//    /**
//     * html页面 地址拼接
//     */
//    public static final String CTTQ_WEB_PATH = "/tianxin";
//    /**
//     * 升级接口  正式环境 PRD环境
//     */
//    public static final String ULR_UP = "https://ainewprd.cttq.com:1443/install/install.xml";
//    /**
//     * 工号登陆接口  正式环境PRD
//     */
//    public static final String LOGIN_URL = CTTQ_BASE_URL + "/account/mobile";
//    /**
//     * 基本接口 正式环境PRD
//     */
//    public static final String BASE_URL = CTTQ_BASE_URL + "/microapp/mobile";
}
