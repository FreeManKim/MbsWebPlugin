package com.mobisoft.mbswebplugin.helper;

/**
 * webAPP 状态的初始化设置
 */
public class FunctionConfig{

    /***是否显示左标题*/
    private boolean isLeftTextShow;
    /***是否显示左标题图片*/
    private boolean isLeftIconShow;
    /***左边菜单选项卡*/
    private boolean isLeftAndRightMenu;
    /***是否显示中间标题*/
    private boolean isNoTilte;
    /***是否设置状态栏*/
    private boolean isSystemBarShow;
    /**是否可已下拉刷新*/
    private boolean isRefreshEnable;
    /***是否开启转场动画*/
    private boolean isTransitionModeEnable;
    /**转场动画类型*/
    private TransitionMode isTransitionMode;

    /***
     *      默认配置
     *    isSystemBarShow = true;// 显示沉浸式菜单栏
     *    isLeftTextShow = false;// 返回文字，左边标题默认不显示
     *    isLeftIconShow = false;// 返回图标，左边标题默认不显示
     *    isLeftAndRightMenu = false;// title左右两边菜单模式 ，菜单模式默认不显示
     *    isNoTilte = false;// 没有title模式
     *    isRefreshEnable = false;// 支持支持刷新功能
     *    isTransitionModeEnable = true;// activity是否支持打开方式，支持转场动画
     *    isTransitionMode = TransitionMode.RIGHT;// activity打开方式 ，左进右出模式
     */
    public static FunctionConfig DEFAULT_ACTIVITY = new FunctionConfig.Builder().build();

    public static FunctionConfig DEFAULT_FRAGMENT = new FunctionConfig.Builder()
            .setIsSystemBarShow(true)
            .setIsLeftIconShow(true)
            .setIsLeftTextShow(true)
            .build();

    private FunctionConfig(final Builder builder) {
        this.isSystemBarShow = builder.isSystemBarShow;
        this.isLeftTextShow = builder.isLeftTextShow;
        this.isLeftIconShow = builder.isLeftIconShow;
        this.isLeftAndRightMenu = builder.isLeftAndRightMenu;
        this.isNoTilte = builder.isNoTilte;
        this.isRefreshEnable = builder.isRefreshEnable;
        this.isTransitionModeEnable = builder.isTransitionModeEnable;
        this.isTransitionMode = builder.isTransitionMode;
    }

    public static class Builder {
        private boolean isSystemBarShow = true;// 沉浸式菜单栏
        private boolean isLeftTextShow = false;// 返回文字
        private boolean isLeftIconShow = false;// 返回图标
        private boolean isLeftAndRightMenu = false;// title左右两边菜单模式
        private boolean isNoTilte = false;// 没有title模式
        private boolean isRefreshEnable = false;// 是否支持刷新功能
        private boolean isTransitionModeEnable = true;// activity是否支持打开方式
        private TransitionMode isTransitionMode = TransitionMode.RIGHT;// activity打开方式

        public Builder setIsSystemBarShow(boolean enable) {
            this.isSystemBarShow = enable;
            return this;
        }

        public Builder setIsLeftTextShow(boolean enable) {
            this.isLeftTextShow = enable;
            return this;
        }

        public Builder setIsLeftIconShow(boolean enable) {
            this.isLeftIconShow = enable;
            return this;
        }

        public Builder setIsLeftAndRightMenu(boolean enable) {
            this.isLeftAndRightMenu = enable;
            return this;
        }

        public Builder setIsNoTilte(boolean enable) {
            this.isNoTilte = enable;
            return this;
        }

        public Builder setIsRefreshEnable(boolean enable) {
            this.isRefreshEnable = enable;
            return this;
        }

        public Builder setIsTransitionModeEnable(boolean enable) {
            this.isTransitionModeEnable = enable;
            return this;
        }

        public Builder setIsTransitionModeEnable(TransitionMode enable) {
            this.isTransitionMode = enable;
            return this;
        }

        public FunctionConfig build() {
            return new FunctionConfig(this);
        }
    }

    /**
     * overridePendingTransition mode
     */
    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    public boolean getIsLeftTextShow() {
        return isLeftTextShow;
    }

    public boolean getIsLeftIconShow() {
        return isLeftIconShow;
    }

    public boolean getIsLeftAndRightMenu() {
        return isLeftAndRightMenu;
    }

    public boolean getIsNoTilte() {
        return isNoTilte;
    }

    public boolean getIsSystemBarShow() {
        return isSystemBarShow;
    }

    public boolean getIsRefreshEnable() {
        return isRefreshEnable;
    }

    public boolean getIsTransitionModeEnable() {
        return isTransitionModeEnable;
    }

    public TransitionMode getIsTransitionMode() {
        return isTransitionMode;
    }
}
