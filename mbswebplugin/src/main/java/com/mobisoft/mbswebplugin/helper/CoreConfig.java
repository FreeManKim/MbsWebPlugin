package com.mobisoft.mbswebplugin.helper;

import android.content.Context;
import android.text.TextUtils;

import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.Cmd.CMD;


/**
 *CoreConfig
 *  启动混合式开发web View的配置
 */
public class CoreConfig {

    /**主题配置*/
    private ThemeConfig themeConfig;
    /***webApp的初始化设置*/
    private FunctionConfig functionConfig;
    /**转场动画*/
    private int animRes;
    /**html的地址*/
    private String url;
    /**当前工号*/
    private String account;
    /**是否隐藏头部导航*/
    private Boolean isHideNavigation;

    private CoreConfig(Builder builder) {
        this.themeConfig = builder.themeConfig;
        this.functionConfig = builder.functionConfig;
        this.account = builder.account;
        this.url = builder.url;
        if (builder.noAnimcation) {
            this.animRes = -1;
        } else {
            this.animRes = builder.animRes;
        }
        this.isHideNavigation = builder.isHideNavigation;
    }

    public static class Builder {
        private Context context;
        private ThemeConfig themeConfig;
        private FunctionConfig functionConfig;
        /**是否开启activity加载动画*/
        private boolean noAnimcation; //
        /**动画ID*/
        private int animRes;  //
        /*html地址*/
        private String url;
        /**工号*/
        private String account;
        /**是否隐藏 导航栏*/
        private Boolean isHideNavigation;

        public Builder(Context context, ThemeConfig themeConfig, FunctionConfig functionConfig) {
            this.context =context;
            this.functionConfig = functionConfig;
            this.themeConfig = themeConfig;
            this.animRes = R.anim.in_from_right;
        }

        /**
         * 设置activity的转场动画
         * @param animRes
         * @return
         */
        public Builder setAnimation(int animRes) {
            this.animRes = animRes;
            return this;
        }

        /**
         * 设置URL
         * @param url
         * @return
         */
        public Builder setURL(String url) {
            if(TextUtils.isEmpty(url))url="url is null";

            this.url = url;
            // 判断是否隐藏导航栏 后续优化 (加载轻量级webView)
            if (android.text.TextUtils.indexOf(url, CMD.action_hideNavigation)>=0) {
                this.url = url;
                setHideNavigation(true);
//                functionConfig = null;
//                functionConfig = new FunctionConfig.Builder().setIsRefreshEnable(true).build();
//                    themeConfig = new ThemeConfig.Builder().setsystemBarColor(Color.parseColor("#FFFFFF")).build();
            } else {
                setHideNavigation(false);
                this.url = url;
//                functionConfig = null;
//                functionConfig = FunctionConfig.DEFAULT_ACTIVITY;
            }

            return this;
        }

        /**
         * 设置工号
         * @param account
         * @return
         */
        public Builder setAccount(String account) {
            this.account = account;
            return this;
        }

        /**
         *  是否隐藏原生导航栏
         * @param isHideNavigation
         * @return
         */
        public Builder setHideNavigation(Boolean isHideNavigation) {
            this.isHideNavigation = isHideNavigation;
            return this;
        }

        /**
         * 禁止activity转场动画
         *
         * @return
         */
        public Builder setNoAnimcation(boolean noAnimcation) {
            this.noAnimcation = noAnimcation;
            return this;
        }

        public CoreConfig build() {
            return new CoreConfig(this);
        }
    }


    public ThemeConfig getThemeConfig() {
        return themeConfig;
    }

    public FunctionConfig getFunctionConfig() {
        return functionConfig;
    }

    public int getAnimRes() {
        return animRes;
    }

    public String getUrl() {
        return url;
    }

    public String getAccount() {
        return account;
    }

    public Boolean getHideNavigation() {
        return isHideNavigation;
    }
}
