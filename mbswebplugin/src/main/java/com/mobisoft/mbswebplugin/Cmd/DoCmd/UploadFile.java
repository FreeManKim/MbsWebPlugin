package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.mobisoft.mbswebplugin.Cmd.Working.DefaultUploadCreator;
import com.mobisoft.mbswebplugin.Cmd.Working.UploadCB;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsRequestPermissionsListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsResultListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.UpLoadUtile;
import com.mobisoft.mbswebplugin.utils.Utils;
import com.mobisoft.mbswebplugin.view.ActionSheetDialog;
import com.werb.pickphotoview.PickPhotoView;
import com.werb.pickphotoview.util.PickConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static com.mobisoft.mbswebplugin.base.AppConfing.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.mobisoft.mbswebplugin.base.AppConfing.NICK_REQUEST_CAMERA_CODE;
import static com.mobisoft.mbswebplugin.base.AppConfing.PICK_IMAGE_ACTIVITY_REQUEST_CODE;


/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 上传文件
 */

public class UploadFile extends DoCmdMethod implements MbsResultListener {
    private static final int REQUEST_CODE_CROP_IMAGE = 0x776;
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
    private boolean isCrop;
    private UploadCB uploadCB;
    private Uri cropImageUri;
    private File outCropFile;

    @Override
    public String doMethod(HybridWebView webView, final Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        // TODO 上传文件，需要后续补上
        this.context = context;
        this.cmd = cmd;
        this.mParamter = params;
        this.callBack = callBack;
        context1 = (Activity) context;
        uploadCB = new DefaultUploadCreator();
        uploadCB.create(context1, view);

//        ((MbsWebActivity) context).setResult(this);
        presenter.setResultListener(UploadFile.this);
        JSONObject json = null;
        try {
            json = new JSONObject(params);
            isCrop = json.optBoolean("isCrop", false);
            final int photoSize = json.optInt("size", 1);
            final int pickPhotoCount = json.optInt("pickPhotoCount", 1);
            presenter.setMbsRequestPermissionsResultListener(new MbsRequestPermissionsListener() {
                @Override
                public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                    if (requestCode == 200) {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            getPic(pickPhotoCount);
                        } else {
                            ToastUtil.showShortToast(context, context.getString(R.string.lack_camera_permiss));
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
//                    getPic(null);
                    getPic(pickPhotoCount);

                }

            } else {
//                getPic(null);
                getPic(pickPhotoCount);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param pickPhotoCount
     */
    private void getPic(final int pickPhotoCount) {
        new ActionSheetDialog(context)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem(
                        context.getString(R.string.taker_photo), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                takePhoto();
//                                uploadCB.onUploadStart(pickPhotoCount);

                            }
                        })
                .addSheetItem(context.getString(R.string.taker_album), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                new PickPhotoView.Builder((Activity) context)
                                        .setPickPhotoSize(pickPhotoCount)   //select max size
                                        .setShowCamera(false)   //is show camera
                                        .setSpanCount(3)       //SpanCount
                                        .setLightStatusBar(true)  // custom theme
                                        .setStatusBarColor("#ffffff")   // custom statusBar
                                        .setToolbarColor("#ffffff")   // custom toolbar
                                        .setToolbarIconColor("#000000")   // custom toolbar icon
                                        .start();
//                                uploadCB.onUploadStart(pickPhotoCount);

                            }
                        }).show();


    }

    @Override
    public void onActivityResult(Context context, MbsWebPluginContract.View view, int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ACTIVITY_REQUEST_CODE:
                Uri uri = data.getData();
                if (uri != null) {
                    String realPath = Utils.getRealPathFromURI((Activity) context, uri);

                    String compress = UpLoadUtile.getInstance().compress(context, realPath, 100);
                    File f = new File(compress);
                    UpLoadUtile.getInstance().postFileFile(
                            context, f, mParamter, callBack, uploadCB);

                } else {
                    Log.e("TAG", "从相册获取图片失败");
                }
                break;
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: // 启动相机拍照获取 照片
                Log.e("TAG", "获取图片成功，本地路径是：" + picFileFullName);
                //发送图片
                if (TextUtils.isEmpty(picFileFullName)) {
                    ToastUtil.showShortToast(context, context.getString(R.string.camera_failure));
                    return;
                }

                Log.d("TAG", "图片的本地路径是：" + picFileFullName);
                if (isCrop) {
                    cropImage();
                } else {
                    uploadCB.onUploadStart(1);
                    JSONObject json = null;
                    try {
                        json = new JSONObject(mParamter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int photoSize = json.optInt("size",100);
                    String compress = UpLoadUtile.getInstance().compress(context, picFileFullName, photoSize);
                    File f = new File(compress);
                    UpLoadUtile.getInstance().postFileFile(
                            context, f, mParamter, callBack, uploadCB);
                }


                break;
            case REQUEST_CODE_CROP_IMAGE: // 裁剪数据
//                Uri uri2 = data.getData();

//                String compress = UpLoadUtile.getInstance().compress(context, outCropFile.getAbsolutePath(), 100);
//                File f = new File(compress);
                uploadCB.onUploadStart(1);

                if (outCropFile.exists()) {
                    UpLoadUtile.getInstance().postFileFile(
                            context, outCropFile, mParamter, callBack, uploadCB);
                } else {
                    uploadCB.onUploadComplete(null);

                }


                break;
            case PickConfig.PICK_PHOTO_DATA:
                if (data == null) {
                    uploadCB.onUploadComplete(null);
                    return;
                }
                List<String> selectPaths = (List<String>) data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT);

                if (isCrop && selectPaths.size() == 1) {
                    picFileFullName = selectPaths.get(0);
                    cropImage();

                } else {
                    uploadCB.onUploadStart(selectPaths.size());

                    UpLoadUtile.getInstance().postFileFile(
                            context, selectPaths, uploadCB, mParamter, callBack);
                }

                break;
            case 0:
                uploadCB.onUploadComplete("");
                break;
            default:
                break;
        }
    }

    /**
     * 裁剪图片
     */
    private void cropImage() {
        Uri uri1 = Uri.fromFile(new File(picFileFullName));
        File cropName = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        outCropFile = new File(cropName, System.currentTimeMillis() + ".jpg");

        Intent intent = new Intent("com.android.camera.action.CROP");
        //可以选择图片类型，如果是*表明所有类型的图片
        intent.setDataAndType(uri1, "image/*");
        // 下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 800);
//        intent.putExtra("outputY", 800);
        //裁剪时是否保留图片的比例，这里的比例是1:1
        intent.putExtra("scale", true);
        //是否是圆形裁剪区域，设置了也不一定有效
        //intent.putExtra("circleCrop", true);
        //设置输出的格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outCropFile));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent = Intent.createChooser(intent, context.getString(R.string.vrop_image));


        context1.startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }


    /**
     * @param type
     */
    public void getPic(String type) {
        new ActionSheetDialog(context)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem(context.getString(R.string.taker_photo), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                takePhoto();

                            }
                        })
                .addSheetItem(context.getString(R.string.taker_album), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                selPhoto();
                            }
                        }).show();

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
