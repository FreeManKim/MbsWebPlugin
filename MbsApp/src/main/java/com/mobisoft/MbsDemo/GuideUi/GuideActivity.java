package com.mobisoft.MbsDemo.GuideUi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.mobisoft.MbsDemo.R;
import com.mobisoft.MbsDemo.ScrollingActivity;
import com.mobisoft.bannerlibrary.BGABanner;
import com.squareup.picasso.Picasso;

import java.util.Arrays;


public class GuideActivity extends Activity {
    private static final String TAG = GuideActivity.class.getSimpleName();
    private BGABanner mBackgroundBanner;
    private BGABanner mForegroundBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        setListener();
        processLogic();
    }

    private void initView() {
        setContentView(R.layout.activity_guide);
        mBackgroundBanner = (BGABanner) findViewById(R.id.banner_guide_background);
        mForegroundBanner = (BGABanner) findViewById(R.id.banner_guide_foreground);
    }

    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                startActivity(new Intent(GuideActivity.this, ScrollingActivity.class));
                finish();
            }
        });
    }

    private void processLogic() {
        mBackgroundBanner.setData(Arrays.asList("http://i8.download.fd.pchome.net/t_540x960/g1/M00/06/09/oYYBAFJJJaeIFh1GAAL4Tp2c4g0AAA6pAPTLhsAAvhm339.jpg",
                "http://img-download.pchome.net/download/1k0/k0/2k/o6faou-146b.jpg",
                "http://missever.com/wp-content/uploads/2016/07/1435027856_TyIjXaxW.jpg"), Arrays.asList("提示文字1", "提示文字2", "提示文字3"));

        // 设置数据源
        mBackgroundBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Picasso.with(GuideActivity.this)
                        .load(model)
                        .placeholder(R.drawable.uoko_guide_background_1)
                        .error(R.drawable.uoko_guide_background_1)
                        .into(itemView);
            }

        });
//        mBackgroundBanner.setData(R.drawable.uoko_guide_background_1, R.drawable.uoko_guide_background_2, R.drawable.uoko_guide_background_3);
        mForegroundBanner.setData(Arrays.asList(R.drawable.uoko_guide_foreground_1, R.drawable.uoko_guide_foreground_2, R.drawable.uoko_guide_foreground_3), Arrays.asList("", "", ""));

        mForegroundBanner.setAdapter(new BGABanner.Adapter<ImageView ,Integer>() {

            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, Integer model, int position) {
                Picasso.with(GuideActivity.this)
                        .load(model)
                        .into(itemView);
            }
        });
//        mForegroundBanner.setData(R.drawable.uoko_guide_foreground_1, R.drawable.uoko_guide_foreground_2, R.drawable.uoko_guide_foreground_3);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
    }
}