package com.mobisoft.mbswebplugin.MbsWeb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mobisoft.mbswebplugin.helper.CoreConfig;
import com.mobisoft.mbswebplugin.helper.FunctionConfig;
import com.mobisoft.mbswebplugin.helper.ThemeConfig;


/**
 * 启动WebAppActivity的 配置信息
 */
public class HybridWebApp {
    static final int REQUEST_CODE = 1001;
    /**
     * activity状态的初始化设置
     */
    private static FunctionConfig mFunctionConfig;
    /**
     * activity主题设置
     */
    private static ThemeConfig mThemeConfig;
    /**
     * 启动混合式开发web View的配置
     */
    private static CoreConfig mCoreConfig;
    private static int mRequestCode = REQUEST_CODE;

    public HybridWebApp(CoreConfig coreConfig) {
        mCoreConfig = coreConfig;
        mThemeConfig = coreConfig.getThemeConfig();
        mFunctionConfig = coreConfig.getFunctionConfig();
    }

    public static HybridWebApp init(CoreConfig coreConfig) {
        return new HybridWebApp(coreConfig);
    }

    public void startWebActivity(Context context, Class mclass) {


        Intent intent = new Intent(context, mclass);
        intent.putExtra(WebAppActivity.URL, mCoreConfig.getUrl());
        intent.putExtra(WebAppActivity.ACCOUNT, mCoreConfig.getAccount());
        intent.putExtra(WebAppActivity.ANIMRES, mCoreConfig.getAnimRes());
        intent.putExtra(WebAppActivity.IS_HIDENAVIGATION, mCoreConfig.getHideNavigation());


        intent.putExtra(WebAppActivity.TITLECOLOR, mThemeConfig.getTitleBgColor());
        intent.putExtra(WebAppActivity.SYSTEM_BAR_COLOR, mThemeConfig.getSystemBarColor());


        intent.putExtra(WebAppActivity.ICON_BACK, mThemeConfig.getIconBack());
        intent.putExtra(WebAppActivity.ICON_TITLE_RIGHT, mThemeConfig.getIconTitleRight());
        intent.putExtra(WebAppActivity.ICON_TITLE_CENTER, mThemeConfig.getIconTitleCenter());
        intent.putExtra(WebAppActivity.TITLE_LEFT_TEXT_COLOR, mThemeConfig.getTitleLeftTextColor());
        intent.putExtra(WebAppActivity.TITLE_RIGHT_TEXT_COLOR, mThemeConfig.getTitleRightTextColor());
        intent.putExtra(WebAppActivity.TITLE_CENTER_TEXT_COLOR, mThemeConfig.getTitleCenterTextColor());

        intent.putExtra(WebAppActivity.SHOWMOUDLE, mFunctionConfig.getIsLeftAndRightMenu());
        intent.putExtra(WebAppActivity.SHOWMOUDLESEARCHPAGE, mFunctionConfig.getIsNoTilte());
        intent.putExtra(WebAppActivity.IS_LEFT_ICON_SHOW, mFunctionConfig.getIsLeftIconShow());
        intent.putExtra(WebAppActivity.IS_LEFT_TEXT_SHOW, mFunctionConfig.getIsLeftTextShow());
        intent.putExtra(WebAppActivity.IS_SYSTEM_BAR_SHOW, mFunctionConfig.getIsSystemBarShow());
        intent.putExtra(WebAppActivity.IS_REFRESH_ENABLE, mFunctionConfig.getIsRefreshEnable());
        intent.putExtra(WebAppActivity.IS_TRANSITION_MODE_ENABLE, mFunctionConfig.getIsTransitionModeEnable());
        intent.putExtra(WebAppActivity.IS_TRANSITION_MODE, mFunctionConfig.getIsTransitionMode().name());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        /**
         * mvp模式下的改进方法
         */
        Bundle bundle = new Bundle();
        bundle.putString(WebAppActivity.URL, mCoreConfig.getUrl());
        bundle.putString(WebAppActivity.ACCOUNT, mCoreConfig.getAccount());
        bundle.putInt(WebAppActivity.ANIMRES, mCoreConfig.getAnimRes());
        bundle.putBoolean(WebAppActivity.IS_HIDENAVIGATION, mCoreConfig.getHideNavigation());

        bundle.putInt(WebAppActivity.TITLECOLOR, mThemeConfig.getTitleBgColor());
        bundle.putInt(WebAppActivity.SYSTEM_BAR_COLOR, mThemeConfig.getSystemBarColor());

        bundle.putInt(WebAppActivity.ICON_BACK, mThemeConfig.getIconBack());
        bundle.putInt(WebAppActivity.ICON_TITLE_RIGHT, mThemeConfig.getIconTitleRight());
        bundle.putInt(WebAppActivity.ICON_TITLE_CENTER, mThemeConfig.getIconTitleCenter());
        bundle.putInt(WebAppActivity.TITLE_LEFT_TEXT_COLOR, mThemeConfig.getTitleLeftTextColor());
        bundle.putInt(WebAppActivity.TITLE_RIGHT_TEXT_COLOR, mThemeConfig.getTitleRightTextColor());
        bundle.putInt(WebAppActivity.TITLE_CENTER_TEXT_COLOR, mThemeConfig.getTitleCenterTextColor());

        bundle.putBoolean(WebAppActivity.SHOWMOUDLE, mFunctionConfig.getIsLeftAndRightMenu());
        bundle.putBoolean(WebAppActivity.SHOWMOUDLESEARCHPAGE, mFunctionConfig.getIsNoTilte());
        bundle.putBoolean(WebAppActivity.IS_LEFT_ICON_SHOW, mFunctionConfig.getIsLeftIconShow());
        bundle.putBoolean(WebAppActivity.IS_LEFT_TEXT_SHOW, mFunctionConfig.getIsLeftTextShow());
        bundle.putBoolean(WebAppActivity.IS_SYSTEM_BAR_SHOW, mFunctionConfig.getIsSystemBarShow());
        bundle.putBoolean(WebAppActivity.IS_REFRESH_ENABLE, mFunctionConfig.getIsRefreshEnable());
        bundle.putBoolean(WebAppActivity.IS_TRANSITION_MODE_ENABLE, mFunctionConfig.getIsTransitionModeEnable());
        bundle.putString(WebAppActivity.IS_TRANSITION_MODE, mFunctionConfig.getIsTransitionMode().name());


        intent.putExtras(bundle);


        context.startActivity(intent);
    }
    public void startHomeActivity(Context context, Class mclass) {


        Intent intent = new Intent(context, mclass);
        intent.putExtra(WebAppActivity.URL, mCoreConfig.getUrl());
        intent.putExtra(WebAppActivity.ACCOUNT, mCoreConfig.getAccount());
        intent.putExtra(WebAppActivity.ANIMRES, mCoreConfig.getAnimRes());
        intent.putExtra(WebAppActivity.IS_HIDENAVIGATION, mCoreConfig.getHideNavigation());


        intent.putExtra(WebAppActivity.TITLECOLOR, mThemeConfig.getTitleBgColor());
        intent.putExtra(WebAppActivity.SYSTEM_BAR_COLOR, mThemeConfig.getSystemBarColor());


        intent.putExtra(WebAppActivity.ICON_BACK, mThemeConfig.getIconBack());
        intent.putExtra(WebAppActivity.ICON_TITLE_RIGHT, mThemeConfig.getIconTitleRight());
        intent.putExtra(WebAppActivity.ICON_TITLE_CENTER, mThemeConfig.getIconTitleCenter());
        intent.putExtra(WebAppActivity.TITLE_LEFT_TEXT_COLOR, mThemeConfig.getTitleLeftTextColor());
        intent.putExtra(WebAppActivity.TITLE_RIGHT_TEXT_COLOR, mThemeConfig.getTitleRightTextColor());
        intent.putExtra(WebAppActivity.TITLE_CENTER_TEXT_COLOR, mThemeConfig.getTitleCenterTextColor());

        intent.putExtra(WebAppActivity.SHOWMOUDLE, mFunctionConfig.getIsLeftAndRightMenu());
        intent.putExtra(WebAppActivity.SHOWMOUDLESEARCHPAGE, mFunctionConfig.getIsNoTilte());
        intent.putExtra(WebAppActivity.IS_LEFT_ICON_SHOW, mFunctionConfig.getIsLeftIconShow());
        intent.putExtra(WebAppActivity.IS_LEFT_TEXT_SHOW, mFunctionConfig.getIsLeftTextShow());
        intent.putExtra(WebAppActivity.IS_SYSTEM_BAR_SHOW, mFunctionConfig.getIsSystemBarShow());
        intent.putExtra(WebAppActivity.IS_REFRESH_ENABLE, mFunctionConfig.getIsRefreshEnable());
        intent.putExtra(WebAppActivity.IS_TRANSITION_MODE_ENABLE, mFunctionConfig.getIsTransitionModeEnable());
        intent.putExtra(WebAppActivity.IS_TRANSITION_MODE, mFunctionConfig.getIsTransitionMode().name());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        /**
         * mvp模式下的改进方法
         */
        Bundle bundle = new Bundle();
        bundle.putString(WebAppActivity.URL, mCoreConfig.getUrl());
        bundle.putString(WebAppActivity.ACCOUNT, mCoreConfig.getAccount());
        bundle.putInt(WebAppActivity.ANIMRES, mCoreConfig.getAnimRes());
        bundle.putBoolean(WebAppActivity.IS_HIDENAVIGATION, mCoreConfig.getHideNavigation());

        bundle.putInt(WebAppActivity.TITLECOLOR, mThemeConfig.getTitleBgColor());
        bundle.putInt(WebAppActivity.SYSTEM_BAR_COLOR, mThemeConfig.getSystemBarColor());

        bundle.putInt(WebAppActivity.ICON_BACK, mThemeConfig.getIconBack());
        bundle.putInt(WebAppActivity.ICON_TITLE_RIGHT, mThemeConfig.getIconTitleRight());
        bundle.putInt(WebAppActivity.ICON_TITLE_CENTER, mThemeConfig.getIconTitleCenter());
        bundle.putInt(WebAppActivity.TITLE_LEFT_TEXT_COLOR, mThemeConfig.getTitleLeftTextColor());
        bundle.putInt(WebAppActivity.TITLE_RIGHT_TEXT_COLOR, mThemeConfig.getTitleRightTextColor());
        bundle.putInt(WebAppActivity.TITLE_CENTER_TEXT_COLOR, mThemeConfig.getTitleCenterTextColor());

        bundle.putBoolean(WebAppActivity.SHOWMOUDLE, mFunctionConfig.getIsLeftAndRightMenu());
        bundle.putBoolean(WebAppActivity.SHOWMOUDLESEARCHPAGE, mFunctionConfig.getIsNoTilte());
        bundle.putBoolean(WebAppActivity.IS_LEFT_ICON_SHOW, mFunctionConfig.getIsLeftIconShow());
        bundle.putBoolean(WebAppActivity.IS_LEFT_TEXT_SHOW, mFunctionConfig.getIsLeftTextShow());
        bundle.putBoolean(WebAppActivity.IS_SYSTEM_BAR_SHOW, mFunctionConfig.getIsSystemBarShow());
        bundle.putBoolean(WebAppActivity.IS_REFRESH_ENABLE, mFunctionConfig.getIsRefreshEnable());
        bundle.putBoolean(WebAppActivity.IS_TRANSITION_MODE_ENABLE, mFunctionConfig.getIsTransitionModeEnable());
        bundle.putString(WebAppActivity.IS_TRANSITION_MODE, mFunctionConfig.getIsTransitionMode().name());


        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 目前还没有完善，请勿使用
     */
//    public void startWebActivityForResult(int requestCode) {
//        Intent intent = new Intent(mCoreConfig.getContext(), WebAppActivity.class);
//        intent.putExtra(WebAppActivity.URL, mCoreConfig.getUrl());
//        intent.putExtra(WebAppActivity.ACCOUNT, mCoreConfig.getAccount());
//        intent.putExtra(WebAppActivity.ANIMRES, mCoreConfig.getAnimRes());
//        intent.putExtra(WebAppActivity.TITLECOLOR, mThemeConfig.getTitleBgColor());
//        intent.putExtra(WebAppActivity.SYSTEM_BAR_COLOR, mThemeConfig.getSystemBarColor());
//
//        intent.putExtra(WebAppActivity.ICON_BACK, mThemeConfig.getIconBack());
//        intent.putExtra(WebAppActivity.ICON_TITLE_RIGHT, mThemeConfig.getIconTitleRight());
//        intent.putExtra(WebAppActivity.ICON_TITLE_CENTER, mThemeConfig.getIconTitleCenter());
//        intent.putExtra(WebAppActivity.TITLE_LEFT_TEXT_COLOR, mThemeConfig.getTitleLeftTextColor());
//        intent.putExtra(WebAppActivity.TITLE_RIGHT_TEXT_COLOR, mThemeConfig.getTitleRightTextColor());
//        intent.putExtra(WebAppActivity.TITLE_CENTER_TEXT_COLOR, mThemeConfig.getTitleCenterTextColor());
//
//        intent.putExtra(WebAppActivity.SHOWMOUDLE, mFunctionConfig.getIsLeftAndRightMenu());
//        intent.putExtra(WebAppActivity.SHOWMOUDLESEARCHPAGE, mFunctionConfig.getIsNoTilte());
//        intent.putExtra(WebAppActivity.IS_LEFT_ICON_SHOW, mFunctionConfig.getIsLeftIconShow());
//        intent.putExtra(WebAppActivity.IS_LEFT_TEXT_SHOW, mFunctionConfig.getIsLeftTextShow());
//        intent.putExtra(WebAppActivity.IS_SYSTEM_BAR_SHOW, mFunctionConfig.getIsSystemBarShow());
//        intent.putExtra(WebAppActivity.IS_REFRESH_ENABLE, mFunctionConfig.getIsRefreshEnable());
//
//        ((Activity) mCoreConfig.getContext()).startActivityForResult(intent, requestCode);
//    }

    public Fragment getWebAppFragment() {
        WebAppFragment mWebAppFragment = new WebAppFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(WebAppActivity.URL, mCoreConfig.getUrl());
        mBundle.putString(WebAppActivity.ACCOUNT, mCoreConfig.getAccount());
        mBundle.putInt(WebAppActivity.ANIMRES, mCoreConfig.getAnimRes());
        mBundle.putInt(WebAppActivity.TITLECOLOR, mThemeConfig.getTitleBgColor());
        mBundle.putInt(WebAppActivity.SYSTEM_BAR_COLOR, mThemeConfig.getSystemBarColor());

        mBundle.putInt(WebAppActivity.ICON_BACK, mThemeConfig.getIconBack());
        mBundle.putInt(WebAppActivity.ICON_TITLE_RIGHT, mThemeConfig.getIconTitleRight());
        mBundle.putInt(WebAppActivity.ICON_TITLE_CENTER, mThemeConfig.getIconTitleCenter());
        mBundle.putInt(WebAppActivity.TITLE_LEFT_TEXT_COLOR, mThemeConfig.getTitleLeftTextColor());
        mBundle.putInt(WebAppActivity.TITLE_RIGHT_TEXT_COLOR, mThemeConfig.getTitleRightTextColor());
        mBundle.putInt(WebAppActivity.TITLE_CENTER_TEXT_COLOR, mThemeConfig.getTitleCenterTextColor());

        mBundle.putBoolean(WebAppActivity.SHOWMOUDLE, mFunctionConfig.getIsLeftAndRightMenu());
        mBundle.putBoolean(WebAppActivity.SHOWMOUDLESEARCHPAGE, mFunctionConfig.getIsNoTilte());
        mBundle.putBoolean(WebAppActivity.IS_LEFT_ICON_SHOW, mFunctionConfig.getIsLeftIconShow());
        mBundle.putBoolean(WebAppActivity.IS_LEFT_TEXT_SHOW, mFunctionConfig.getIsLeftTextShow());
        mBundle.putBoolean(WebAppActivity.IS_SYSTEM_BAR_SHOW, mFunctionConfig.getIsSystemBarShow());
        mBundle.putBoolean(WebAppActivity.IS_REFRESH_ENABLE, mFunctionConfig.getIsRefreshEnable());

        mWebAppFragment.setArguments(mBundle);
        return mWebAppFragment;
    }

    public static CoreConfig getCoreConfig() {
        return mCoreConfig;
    }

    public static FunctionConfig getFunctionConfig() {
        return mFunctionConfig;
    }

    public static int getRequestCode() {
        return mRequestCode;
    }
}