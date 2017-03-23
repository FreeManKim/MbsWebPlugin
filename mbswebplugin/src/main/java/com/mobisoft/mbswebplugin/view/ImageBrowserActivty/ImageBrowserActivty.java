package com.mobisoft.mbswebplugin.view.ImageBrowserActivty;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobisoft.mbswebplugin.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

import static com.mobisoft.mbswebplugin.base.AppConfing.PERMISSIONS_REQUEST_CODE;


/**
 * 图片展示页面
 */
    public class ImageBrowserActivty extends Activity implements View.OnClickListener {

    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_RIGHTMEN = "name";
    public static final String DCIM = "DCIM";
    private ImageView iv_image_browser;
    private LinearLayout ll_head_layout;
    private LinearLayout ll_back;
    private TextView tv_head_title;
    private TextView tv_head_right;
    private String srcUrl;
    private StringBuilder stringBuilder;
    private Target target;
    private String _rigth_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        srcUrl = getIntent().getStringExtra(IMAGE_URL);
        _rigth_tv = getIntent().getStringExtra(IMAGE_RIGHTMEN);
        findView();
        initData(srcUrl);
        initEvent();

    }

    private void initEvent() {
        ll_back.setOnClickListener(this);
        tv_head_right.setOnClickListener(this);
    }

    /**
     * findView
     */
    private void findView() {
        iv_image_browser = (ImageView) findViewById(R.id.iv_image_browser);
//        ll_head_layout = (LinearLayout) findViewById(R.id.ll_head_layout);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);

    }

    /**
     * 初始化数据
     *
     * @param srcUrl
     */
        private void initData(String srcUrl) {
        stringBuilder = new StringBuilder();
        tv_head_title.setText("图片");
        tv_head_right.setText(TextUtils.isEmpty(_rigth_tv) ? "保存" : _rigth_tv);
//        initSystemBar(this, Color.parseColor("#037BFF"));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            initTarg();
            creatFilePath(DCIM);
        }// 大于6.0 权限检查
        if (!TextUtils.isEmpty(srcUrl)) Picasso.with(this).load(srcUrl).into(iv_image_browser);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_back) {//返回
            this.finish();
        } else if (v.getId() == R.id.tv_head_right) {
            DwonLoadImage();
        }
    }

    /**
     * 下载图片
     */
    private synchronized void DwonLoadImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)// 大于6.0 权限检查
            requestGalleryPermission();
        else {
            if (!TextUtils.isEmpty(srcUrl)) Picasso.with(this).load(srcUrl).into(target);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            initTarg();
            creatFilePath(DCIM);
            if (!TextUtils.isEmpty(srcUrl)) Picasso.with(this).load(srcUrl).into(target);
        } else {
            // Ask for one permission
           requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * 获取相册路径
     *
     * @param path
     * @param imageName
     * @return
     */
    private File getDCIMFile(String path, String imageName) {

        File image = new File(stringBuilder.toString() + imageName);

        return image;
    }

    /**
     * 创建基本路径
     *
     * @param path
     */
    private void creatFilePath(String path) {
        stringBuilder.append(Environment.getExternalStorageDirectory().getPath());
        stringBuilder.append("/");
        stringBuilder.append(path);
        stringBuilder.append("/QAS");
        File file = new File(stringBuilder.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        stringBuilder.append("/IMG_");
    }




    private void initTarg() {
        if (target == null)
            //Target
            target = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    String imageName = System.currentTimeMillis() + ".png";

                    File dcimFile = getDCIMFile(DCIM, imageName);

                    FileOutputStream ostream = null;
                    try {
                        ostream = new FileOutputStream(dcimFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(ImageBrowserActivty.this, "图片下载至:" + dcimFile, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
    }

}
