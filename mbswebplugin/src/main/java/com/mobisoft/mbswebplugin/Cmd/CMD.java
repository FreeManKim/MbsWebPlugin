package com.mobisoft.mbswebplugin.Cmd;

/***
 * 命令库
 * action: 是拼接在http开头的url 后边 的，根据action 的不同执行不同的操作
 * cmd： 是kittaps 开头的url html可以通过发送命令 执行native操作
 */
public class CMD {

    /***
     * 弹出时间框  1日期
     * 标准格式
     */
    public final static String cmd_setDate = "setdate";// 弹出时间框  1日期

    /**
     * 开始转圈showhud ---showHud
     * 标准格式
     */
    public final static String cmd_showHud = "showhud";//开始转圈

    /***
     * 弹出时间框 0时间
     * 标准格式
     */
    public final static String cmd_setTime = "settime";// 弹出时间框 0时间


    /**
     * 设置右上角菜单
     * 标准格式
     **/
    public final static String cmd_setRightMenu = "setrightmenu"; // 设置右上角菜单

    /**
     * 发送消息
     */
    public final static String cmd_sendMessage = "sendmessage";// 发送消息
    /**
     * 接受消息
     */
    public final static String cmd_recvMessage = "recvmessage";// 接受消息

    /***
     * 确认的提示框
     * pagetips -- 显示showTips
     **/
    public final static String cmd_pagetips = "showtips"; // 确认的提示框
    public final static String cmd_confirm = "confirm"; // 确认的提示框
    /**
     * 禁止刷新
     */
    public final static String cmd_forbidRefresh = "forbidrefresh"; // 不刷新
    /**
     * 上传文件 TODO 没有测试
     */
    public final static String cmd_uploadFile = "uploadfile";//上传文件
    /**
     * 退出登录 TODO 没有测试
     */
    public final static String cmd_logout = "logout"; // 退出登录
    /**
     * 获取网络状态
     */
    public final static String cmd_getNetworkStatus = "getnetworkstatus";// 获取网络状态
    /**
     * 设置导航栏标题
     */
    public final static String cmd_setTitle = "settitle";// 设置导航栏标题
    /**
     * 显示toast弹窗
     */
    public final static String cmd_showToast = "showtoast";// 显示toast弹窗
    /**
     * 设置导航栏背景色
     * setTitleBarBg --->setNavigationBgColor
     */
    public final static String cmd_setNavigationBgColor = "setnavigationbgcolor";// 设置导航栏背景色
    /**
     * 存储数据库Key
     */
    public final static String cmd_setDatabase = "setdatabase"; // 存储数据Key

    /**
     * 读取数据Key
     */
    public final static String cmd_getDatabase = "getdatabase"; // 读取数据Key
    /**
     * 打开原生通讯录 TODO 未测试
     */
    public final static String cmd_openContacts = "opencontacts"; //打开原生通讯录
    /**
     * 打开视频
     */
    public final static String cmd_playVideo = "playvideo"; //打开视频
    /**
     * 获取app版本code
     */
    public final static String cmd_getVersion = "getversion"; // 获取app版本code

    /**
     * 检查并app版本是否需要更新
     */
    public final static String cmd_checkForUpdate = "checkforupdate"; // 检查并app版本是否需要更新
    /**
     * 关闭 指定 activity栈
     */
    public final static String cmd_clearTask = "cleartask";// 关闭 指定 activity栈
    /**
     * 下载视频
     */
    public final static String cmd_downloadFile = "downloadfile"; //下载

    /**
     * 获取视频绝对路径
     */
    public final static String cmd_getLocalFile = "getlocalfile";// 获取视频绝对路径

    /**
     * 获取位置
     */
    public final static String cmd_Location = "location";// 获取位置
    /*
     *  * cmd_db_get
     */
    public final static String cmd_db_get = "db_get";// db_get
    /*
     *  * 获取位置
     */
    public final static String cmd_db_set = "db_set";// 获取db_get位置
    /**
     * 发送邮件 更改 EMail
     */
    public final static String cmd_email = "email";//邮件
    /**
     * 设置底部菜单
     */
    public final static String cmd_setBottomMenu = "setbottommenu"; // 设置底部菜单

    /**
     * 打电话的命令
     */
    public final static String cmd_cellphone = "cellphone";// 打电话的命令

    /**
     * 打开手机自带浏览器
     */
    public final static String cmd_openBrowser = "openbrowser"; //打开手机自带浏览器

    /***
     * 获取图像，需要用户选择相册、相机
     */
    public final static String cmd_getImage = "getimage";//获取图像，需要用户选择相册、相机

    /***
     * 显示输入框
     */
    public final static String cmd_placeholder = "placeholder";//显示输入框
    /***
     * 打开图片浏览页面
     */
    public final static String cmd_ImageBrowse = "browseimage";//显示输入框
    /**
     *  是否使用缓存页面
     */
    public final static String cmd_OpenProxy = "openproxy";//是否使用缓存数据


    /**
     * 搜索界面
     */
    public final static String cmd_setSearchBar = "setSearchBar";// 搜索界面

    /**
     * 启动app
     */
    public final static String cmd_OpenApp = "openapp";// 获取位置

    /*************************分割线***************************/


    /**
     * 下一页
     */
    public final static String action_nextPage = "nextpage";//下一页
    /**
     * 返回首页
     **/
    public final static String action_homepage = "gohome";// 返回首页
    /**
     * 返回首页
     **/
    public final static String action_exit = "exit";// 返回首页
    /**
     * 返回上一页
     **/
    public final static String action_closePage = "closepage";// 返回上一页
    /**
     * 关闭本页并且刷新之前的页面
     **/
    public final static String action_closePageAndRefresh = "closepageandrefresh";// 关闭本页并且刷新之前的页面
    /****
     * 显示模式窗口(隐藏导航栏)
     */
    public final static String action_showModelPage = "showmodelpage";// 显示模式窗口(隐藏导航栏)
    /**
     * 显示模式化窗口进行搜索操作
     */
    public final static String action_showModelSearchPage = "showmodelsearchpage";// 显示模式化窗口进行搜索操作
    /***
     * 关闭模式化页面
     */
    public final static String action_closeModelPage = "closemodelpage";// 关闭模式化页面

    /**
     * 限制照片张数的命令
     */
    public final static String cmd_limitCount = "limitCount";//上传图片总数

    /**
     * 已经选择照片张数的命令
     */
    public final static String cmd_currCount = "currCount";// 已经选择数
    /***
     * closePageAndRefreshAndPop
     */
    public final static String action_closePageAndRefreshAndPop = "closepageandrefreshandpop"; //
    /***
     * closePageAndPop
     */
    public final static String action_closePageAndPop = "closepageandpop"; //
    /**
     * 在当前页面加载 新的url localPage
     */
    public final static String action_localPage = "localpage";// 在当前页面加载 新的url localPage
    /**
     * 加载轻量级webView
     */
    public final static String action_hideNavigation = "hidenavigation";// 加载轻量级webView


    //	public final static String cmd_getCamara = "getCamara";//打开相机相册 imageSourceType
    // ：0相册 1相机 2相机相册
       /***
     * //获取相机图片
     */
    public final static String cmd_getCamera = "getCamera";//获取相机图片
    /***
     * 获取相机图片
     */
    public final static String cmd_getPicture = "getPicutre";//获取相机图片


    /***
     * 数据缓存本地数据库
     */
    public final static String cmd_setValue = "setValue";//数据缓存本地数据库
    /**
     * 获取本地缓存
     */
    public final static String cmd_getValue = "getValue";//获取本地缓存
    /**
     * 清除缓存
     */
    public final static String cmd_cleanCacheAndCookie = "cleanCacheAndCookie"; //清除本地缓存
    /**
     * 保存数据，html5的缓存版本
     */
    public final static String cmd_setValueH5 = "setValueH5";//保存数据，html5的缓存版本
    /**
     * 获取本地数据库，html5缓存版本
     */
    public final static String cmd_getValueH5 = "getValueH5";//获取本地数据库，html5缓存版本
    /**
     * 区
     */
    public final static String cmd_setCity = "getCity"; // 地区
    /**
     * 地区
     */
    public final static String cmd_setAddress = "getAddress"; // 地区
    /**
     * 显示进度条
     */
    public final static String cmd_showProgress = "showProgress"; // 显示进度条
    /**
     * 显示进度条带比例
     */
    public final static String cmd_showProgressRatio = "showProgressRatio"; // 显示进度条带比例
    /**
     * 影藏进度条
     */
    public final static String cmd_hideProgress = "hideProgress"; // 影藏进度条

    /**
     * 设置右上角菜单
     */
    public final static String cmd_setLeftRightMenu = "setLeftRightMenu"; // 设置右上角菜单
    /**
     * 设置title菜单
     */
    public final static String cmd_setCenterMenu = "setCenterMenu"; // 设置title菜单
    /**
     * 显示选择器，需要用户选择内容
     **/
    public final static String cmd_showSelect = "showSelect"; // 显示选择器，需要用户选择内容
    /**
     * 显示消息
     */
    public final static String cmd_showMessage = "showMessage"; // 显示消息
    /**
     * 显示需要确认的消息
     */
    public final static String cmd_showConfirmMessage = "showConfirmMessage"; // 显示需要确认的消息


    /**
     * 创建手势密码
     */
    public final static String cmd_GestureDrawing = "GestureDrawing"; // 创建手势密码
    /**
     * 创建手势密码
     */
    public final static String cmd_initGesture = "initGesture"; // 创建手势密码
    /**
     * 开启刷新
     */
    public final static String cmd_openRefresh = "openRefresh"; // 开启刷新
    /**
     * 显示大图
     */
    public final static String cmd_imgBrowse = "imgBrowse"; // 显示大图
    /**
     * 关闭也面前 检查
     */
    public final static String cmd_checkClose = "isFinish"; // 关闭也面前 检查
    /**
     * 微信分享页面
     */
    public final static String cmd_wechatShare = "shareWechat"; //  微信分享页面
    /***
     * 极光推送 设置别名
     */
    public final static String cmd_setAlias = "setAlias";// 极光推送 设置别名
    /**
     * 极光推送 设置标签
     */
    public final static String cmd_setTags = "setTags";// 极光推送 设置标签
    /**
     * 极光推送 同时设置别名
     */
    public final static String cmd_setAliasAndTags = "setAliasAndTags";// 极光推送 同时设置别名与标签
    /**
     * 极光推送 设置badge值
     */
    public final static String cmd_setBadgeNumber = "setBadgeNumber";// 极光推送 设置badge值
    /**
     * 极光推送 获取badge值
     */
    public final static String cmd_getBadgeNumber = "getBadgeNumber";// 极光推送 获取badge值
    /**
     * 极光推送  清除所有通知
     */
    public final static String cmd_clearAllNotifications = "clearAllNotifications";// 极光推送  清除所有通知
    /**
     * 极光推送  open关闭
     */
    public final static String cmd_stopOrResumePush = "stopOrResumePush";// 极光推送  open关闭 或 close开启


    /**
     * 关闭页面
     */
    public final static String action_close = "close";// 关闭页面

    /**
     * 二维码
     */
    public final static String cmd_scanQrcode = "scanQrcode"; // 二维码
    /**
     * 薪资查询解密
     */
    public final static String cmd_salary_dec = "setjiemi"; // 薪资查询解密


    /**
     * 删除数据
     */
    public final static String cmd_delKey = "delDatabase"; // 删除数据Key
    /**
     * 提交64位字符串
     */
    public final static String cmd_uploadFile_Base64 = "uploadFileBase64";//上传文件
    /**
     * 页面下拉刷新
     */
    public final static String cmd_setHeaderRefreshing = "setHeaderRefreshing";//页面设置下拉刷新



    /**
     * 科大讯飞语音识别
     */
    public final static String cmd_speechRecognition = "speechRecognition"; // 科大讯飞语音识别


    /**
     * 保存图片
     */
    public final static String cmd_savePicture = "savePicture"; // 保存图片



    /***
     * 停止转圈
     */
    public final static String cmd_finishIntercept = "finishIntercept";//停止转圈
    /**
     * 停止转圈
     */
    public final static String cmd_setFooterRefreshing = "setFooterRefreshing";//停止转圈
    /***
     * 返回并刷新
     */
    public final static String cmd_closeandrefresh = "closeandrefresh";//返回并刷新
    /**
     * 单选框
     */
    public final static String cmd_boxtype = "boxtype"; // 单选框
    /**
     * 弹出消息框
     */
    public final static String cmd_message = "message"; // 弹出消息框
    /**
     * 确定框
     */
    public final static String cmd_comfirm = "comfirm"; // 确定框
    /**
     * 打开文本
     */
    public final static String cmd_openfile = "openfile";// 打开文本
    /***
     * 通知跳转nativi页面
     */
    public final static String cmd_noticenativi = "noticenativi";//通知跳转nativi页面


    /**
     * 退出登录
     */
    public final static String cmd_exitout = "exitout"; // 直接退出登录

    /**
     * 强制横屏
     */
    public final static String cmd_allowLandscape = "allowLandscape";//强制横屏

    /**
     * 清除缓存
     */
    public final static String cmd_clearCache = "clearCache";//清除缓存
    /**
     * 获取uuid
     */
    public final static String cmd_getudid = "getudid";//获取uuid
    /**
     * 获取缓存大小
     */
    public final static String cmd_getCache = "getCache";//获取缓存大小

    /**
     * 显示任务窗
     */
    public final static String cmd_showTaskView = "showTaskView";// 显示任务窗

    /**
     * 跳转缓存界面
     */
    public final static String cmd_notice_native = "notice_native";// 跳转缓存界面
    /**
     * 自定义页面
     */
    public final static String cmd_setSegment = "setSegment";//自定义页面
    /**
     * 屏幕常亮
     */
    public final static String cmd_disableBlankScreen = "disableBlankScreen";// 屏幕常亮
    /**
     * 关闭屏幕常亮
     */
    public final static String cmd_canBlankScreen = "canBlankScreen";// 关闭屏幕常亮

    /**
     * 不能刷新
     */
    public final static String cmd_disableRefresh = "disableRefresh";//不能刷新

    /**
     * 两个按钮的弹出框
     */
    public final static String cmd_pageConfirm = "pageConfirm";//两个按钮的弹出框


    //nike
    public final static String action_getNetworkStatus = "getNetworkStatus";//获取网络状态
    public final static String action_logout = "logout";//退出

    public final static String action_setTitle = "setTitle";//设置标题
    public final static String action_recvMessage = "recvMessage";//接收信息
    public final static String action_sendMessage = "sendMessage";//发送信息
    public final static String action_pagetips = "pagetips";//一个按钮的弹出框
    public final static String action_uploadFile = "uploadFile";//头像上传
    public final static String action_updatePageData = "updatePageData";//更细用户信息
    public final static String action_browseImage = "browseImage";//预览图片

    /**
     * 发送广播
     */
    public final static class BroadcastAction {
        public final static String setGesture = "com.mobisoft.qas.activity.setGesture"; // 设置手势

    }

    /**
     * 收回键盘
     */


    /**
     * 获取h5自带标题
     */
    public final static int type_h5Title = 0;
    /**
     * 获取h5命令中的标题 (优先级高于 {@value type_h5Title} )
     * 同一个页面内 设置此标题后 无法设置 {@value type_h5Title} 标题
     */
    public final static int type_kitappsTitle = 1;
}
