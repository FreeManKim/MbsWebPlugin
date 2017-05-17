package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsResultListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.base.AppConfing;
import com.mobisoft.mbswebplugin.utils.Base64Util;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.Utils;
import com.mobisoft.mbswebplugin.view.ActionSheetDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.mobisoft.mbswebplugin.utils.Utils.FILE_PATH;

/**
 * Author：Created by fan.xd on 2017/4/28.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class AlbumOrCamera extends DoCmdMethod implements MbsResultListener {
    public static final int OPEN_GALLERY_CODE = 2001;
    public static final String ACTION = "xiangce.test.ccc.fxd.image";

    private MbsWebPluginContract.Presenter presenter;
    private Activity context1;
    /**
     * h5传递参数
     */
    private String params;
    /**
     * h5回掉方法
     */
    private String callBack;
    /**
     * 图片名称
     */
    private String filename;


    @Override
    public String doMethod(HybridWebView webView, final Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

        context1 = (Activity) context;
        this.presenter = presenter;
        this.params = params;
        this.callBack = callBack;
        presenter.setResultListener(AlbumOrCamera.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)// 大于6.0 权限检查
        {
            if (ContextCompat.checkSelfPermission(context1,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permission = new String[]{
                        android.Manifest.permission.CAMERA
                };
                ActivityCompat.requestPermissions(context1, permission, 200);
            } else {
                showSelectDialog(context, context1);
            }

        } else {
            showSelectDialog(context, context1);
        }

        return null;
    }

    @Override
    public void onActivityResult(final Context context, final MbsWebPluginContract.View view, final int requestCode, int resultCode, final Intent data) {
//            final String path = Utils.getAbsoluteImagePath(context, data.getData());
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 选则相册
                 */
                if (requestCode == AppConfing.PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
                    Uri uri = data.getData();
                    final String path2 = Utils.copyPhotoToTemp(context1, uri);

                    setImageSrc(path2, view, context);
                } else if (requestCode == AppConfing.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                    /**
                     * 选择 相机拍摄
                     */
                    File file = new File(FILE_PATH + filename + ".jpg");
                    try {
                        Uri u = Uri.parse(MediaStore.Images.Media.insertImage(context1.getContentResolver(),
                                file.getAbsolutePath(), null, null));
                        final String path3 = Utils.copyPhotoToTemp(context1, u);
                        setImageSrc(path3, view, context);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();
    }

    /**
     * 给webView设置图片
     *
     * @param path2
     * @param view
     * @param context
     */
    private void setImageSrc(String path2, final MbsWebPluginContract.View view, final Context context) {
        String s = null;
        try {
            s = Base64Util.encodeBase64File(path2);
            final String finalS = s;
            Utils.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    String josn = String.format("javascript:" + callBack + "(%s)", finalS);
                    view.loadUrl(josn);
                    ToastUtil.showLongToast(context.getApplicationContext(), finalS);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示打开相机相册窗口
     *
     * @param context
     * @param context1
     */
    private void showSelectDialog(final Context context, final Activity context1) {
        new ActionSheetDialog(context)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HH_mmss");
                                Date date = new Date(System.currentTimeMillis());
                                filename = format.format(date);
                                Intent intent = new Intent();
                                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                File file = new File(FILE_PATH + filename + ".jpg");
                                if (file.exists()) {
                                    file.delete();
                                }
                                Uri uri;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                    uri = FileProvider.getUriForFile(context, "com.mobisoft.mbswebplugin.fileprovider", file);
                                else
                                    uri = Uri.fromFile(file);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                (context1).startActivityForResult(intent, AppConfing.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                            }
                        }).addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {

                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        (context1).startActivityForResult(intent, AppConfing.PICK_IMAGE_ACTIVITY_REQUEST_CODE);

                    }
                }).show();
    }


}
