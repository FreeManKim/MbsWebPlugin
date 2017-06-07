package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsRequestPermissionsListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsResultListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.UpLoadUtile;
import com.mobisoft.mbswebplugin.utils.Utils;
import com.mobisoft.mbswebplugin.view.ActionSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.mobisoft.mbswebplugin.base.AppConfing.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.mobisoft.mbswebplugin.base.AppConfing.NICK_REQUEST_CAMERA_CODE;
import static com.mobisoft.mbswebplugin.base.AppConfing.PICK_IMAGE_ACTIVITY_REQUEST_CODE;


/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 上传文件
 */

public class UploadFile extends DoCmdMethod implements MbsResultListener {
    private Context context;
    private String cmd;
    /**
     * 参数
     */
    private String mParamter;
    /**
     * 回掉方法
     */
    private String callBack;
    /**
     * 照片选择路径
     */
    private String picFileFullName;
    private Activity context1;

    @Override
    public String doMethod(HybridWebView webView, final Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        // TODO 上传文件，需要后续补上
        this.context = context;
        this.cmd = cmd;
        this.mParamter = params;
        this.callBack = callBack;
        context1 = (Activity) context;
//        ((MbsWebActivity) context).setResult(this);
        presenter.setResultListener(UploadFile.this);
        JSONObject json = null;
        try {
            json = new JSONObject(params);
            final String type = json.optString("type");
            presenter.setMbsRequestPermissionsResultListener(new MbsRequestPermissionsListener() {
                @Override
                public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                    if(requestCode==200){
                        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                            getPic(type);
                        }else {
                            ToastUtil.showShortToast(context,"缺少相关权限无法使此功能！");
                        }
                    }
                }
            });
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
                    getPic(type);                }

            } else {
                getPic(type);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onActivityResult(Context context, MbsWebPluginContract.View view, int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ACTIVITY_REQUEST_CODE:
                Uri uri = data.getData();
                if (uri != null) {
                    String realPath = Utils.getRealPathFromURI((Activity) context, uri);

                    String compress = UpLoadUtile.getInstance().compress(context, realPath);
                    File f = new File(compress);
                    UpLoadUtile.getInstance().postFileFile(
                            context, f, mParamter, callBack, view);

                } else {
                    Log.e("TAG", "从相册获取图片失败");
                }
                break;
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: // 启动相机拍照获取 照片
                Log.e("TAG", "获取图片成功，本地路径是：" + picFileFullName);
                //发送图片
                if (TextUtils.isEmpty(picFileFullName)) {
                    ToastUtil.showShortToast(context, "相机故障，请重试");
                    return;
                }
                Log.d("TAG", "图片的本地路径是：" + picFileFullName);
                String compress = UpLoadUtile.getInstance().compress(context, picFileFullName);
                File f = new File(compress);
                UpLoadUtile.getInstance().postFileFile(
                        context, f, mParamter, callBack, view);
                break;
            default:
                break;
        }
    }



    /**
     * @param type
     */
    public void getPic(String type) {
        if (type.equals("1")) {
            new ActionSheetDialog(context)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
//                                    uploadType = "picture";
                                    takePhoto();
                                }
                            })
                    .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
//                                    uploadType = "picture";
                                    selPhoto();
                                }
                            }).show();
        } else if (type.equals("2")) {
            new ActionSheetDialog(context)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
//                                    uploadType = "picture";
                                    takePhoto();

                                }
                            })
                    .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
//                                    uploadType = "picture";
                                    selPhoto();
                                }
                            })
                    .addSheetItem("语音", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
//                                    uploadType = "speech";
//                                    startActivity(new Intent(WebAppActivity.this, RecordActivity2.class));
//                                    registerBroadcastReceiver();
                                }
                            }).show();
        }

    }


    /**
     * 获取相册
     */
    public void selPhoto() {
        if (Build.VERSION.SDK_INT < 23) {
            System.out.println("sdk < 23");
            openAlbum();
        } else {
            //6.0
            System.out.println("sdk 6.0");
            if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //该权限已经有了
                System.out.println("权限已经有了");
                openAlbum();
            } else {
                //申请该权限
                System.out.println("申请该权限");
                ActivityCompat.requestPermissions((Activity) this.context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x2222);
            }
        }
    }

    /**
     * 打开本地相册
     */
    public void openAlbum() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        ((Activity) this.context).startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * 获取图片
     */
    public void takePhoto() {
        if (Build.VERSION.SDK_INT < 23) {
            System.out.println("sdk < 23");
            takePicture();
        } else {
            //6.0
            System.out.println("sdk 6.0");
            if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //该权限已经有了
                takePicture();
            } else {
                //申请该权限
                System.out.println("申请该权限");
                ((Activity) this.context).requestPermissions(new String[]{Manifest.permission.CAMERA}, NICK_REQUEST_CAMERA_CODE);
//                ActivityCompat.requestPermissions(WebAppActivity.this, new String[]{Manifest.permission.CAMERA}, 105);
            }
        }
    }

    /**
     * 拍照
     */
    public void takePicture() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File outDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
            picFileFullName = outFile.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            ((Activity) this.context).startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else {
            Log.e("TAG", "请确认已经插入SD卡");
        }
    }

}
