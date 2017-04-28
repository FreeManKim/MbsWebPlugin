package com.mobisoft.mbswebplugin.helper;

import android.graphics.Color;

import com.mobisoft.mbswebplugin.R;

import java.io.Serializable;

/**
 * activity主题设置
 */
public class ThemeConfig implements Serializable {

    /***默认主题****/
    public static ThemeConfig DEFAULT = new ThemeConfig.Builder().build();
    /**黑色主题**/
    public static ThemeConfig DARK = new ThemeConfig.Builder()
            .settitleRightTextColor(Color.WHITE)
            .settitleLeftTextColor(Color.WHITE)
            .settitleCenterTextColor(Color.WHITE)
            .settitleBgColor(Color.BLACK)
            .setsystemBarColor(Color.BLACK)
            .build();
    /**蓝色主题*/
    public static ThemeConfig BLUE = new ThemeConfig.Builder()
            .settitleRightTextColor(Color.WHITE)
            .settitleLeftTextColor(Color.WHITE)
            .settitleCenterTextColor(Color.WHITE)
            .settitleBgColor(Color.BLUE)
            .setsystemBarColor(Color.BLUE)
            .build();
    /**红色主题*/
    public static ThemeConfig RED = new ThemeConfig.Builder()
            .settitleRightTextColor(Color.WHITE)
            .settitleLeftTextColor(Color.WHITE)
            .settitleCenterTextColor(Color.WHITE)
            .settitleBgColor(Color.RED)
            .setsystemBarColor(Color.RED)
            .build();
    /**绿色主题*/
    public static ThemeConfig GREEN = new ThemeConfig.Builder()
            .settitleRightTextColor(Color.WHITE)
            .settitleLeftTextColor(Color.WHITE)
            .settitleCenterTextColor(Color.WHITE)
            .settitleBgColor(Color.GREEN)
            .setsystemBarColor(Color.GREEN)
            .build();
    /**青绿色主题*/
    public static ThemeConfig CYAN = new ThemeConfig.Builder()
            .settitleRightTextColor(Color.WHITE)
            .settitleLeftTextColor(Color.WHITE)
            .settitleCenterTextColor(Color.WHITE)
            .settitleBgColor(Color.CYAN)
            .setsystemBarColor(Color.CYAN)
            .build();

    /**
     *
     */
    private int titleLeftTextColor;
    private int titleRightTextColor;
    private int titleCenterTextColor;
    private int titleBgColor;
    private int systemBarColor;

    private int iconBack;
    private int iconTitleCenter;
    private int iconTitleRight;

    private ThemeConfig(Builder builder) {
        this.titleLeftTextColor = builder.titleLeftTextColor;
        this.titleRightTextColor = builder.titleRightTextColor;
        this.titleCenterTextColor = builder.titleCenterTextColor;
        this.titleBgColor = builder.titleBgColor;
        this.systemBarColor = builder.systemBarColor;
        this.iconBack = builder.iconBack;
        this.iconTitleCenter = builder.iconTitleCenter;
        this.iconTitleRight = builder.iconTitleRight;
    }

    public static class Builder {
        /**头部导航栏左边文字显示颜色**/
        private int titleLeftTextColor = Color.WHITE;
        /**导航栏中间title的颜色**/
        private int titleCenterTextColor = Color.WHITE;
        /**导航栏右边 文字  颜色**/
        private int titleRightTextColor = Color.WHITE;
        /**默认导航栏背景颜色**/
//        private int titleBgColor = Color.parseColor("#8c9eff");
        private int titleBgColor = 0;
        /**沉浸式状态栏的颜色**/
        private int systemBarColor = 0;
//        private int systemBarColor = Color.parseColor("#8c9eff");
        /**导航栏的左边返回图标**/
        private int iconBack = R.drawable.back;
        /**导航栏中间title 默认图标**/
        private int iconTitleCenter = R.drawable.ic_gf_triangle_arrow;
        /**导航栏右边默认图标**/
        private int iconTitleRight = R.drawable.ic_add_black_48dp;

        public Builder settitleLeftTextColor(int intColor) {
            this.titleLeftTextColor = intColor;
            return this;
        }

        public Builder settitleCenterTextColor(int intColor) {
            this.titleCenterTextColor = intColor;
            return this;
        }

        public Builder settitleRightTextColor(int intColor) {
            this.titleRightTextColor = intColor;
            return this;
        }

        public Builder settitleBgColor(int intColor) {
            this.titleBgColor = intColor;
            return this;
        }

        public Builder setsystemBarColor(int intColor) {
            this.systemBarColor = intColor;
            return this;
        }

        public Builder seticonBack(int intIcon) {
            this.iconBack = intIcon;
            return this;
        }

        public Builder seticonTitleCenter(int intIcon) {
            this.iconTitleCenter = intIcon;
            return this;
        }

        public Builder seticonTitleRight(int intIcon) {
            this.iconTitleRight = intIcon;
            return this;
        }

        public ThemeConfig build() {
            return new ThemeConfig(this);
        }
    }

    public int getTitleLeftTextColor() {
        return titleLeftTextColor;
    }

    public int getTitleRightTextColor() {
        return titleRightTextColor;
    }

    public int getTitleCenterTextColor() {
        return titleCenterTextColor;
    }

    public int getTitleBgColor() {
        return titleBgColor;
    }

    public int getSystemBarColor() {
        return systemBarColor;
    }

    public int getIconBack() {
        return iconBack;
    }

    public int getIconTitleCenter() {
        return iconTitleCenter;
    }

    public int getIconTitleRight() {
        return iconTitleRight;
    }
}
