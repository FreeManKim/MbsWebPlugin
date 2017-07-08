package com.mobisoft.mbswebplugin.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.mobisoft.mbswebplugin.Cmd.CMD;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.dao.OptionsWindowHelper;
import com.mobisoft.mbswebplugin.data.DatePickerDialog;
import com.mobisoft.mbswebplugin.data.TimeAndDataDialog;
import com.mobisoft.mbswebplugin.data.TimeDialog;
import com.mobisoft.mbswebplugin.data.TimePickerDialog;
import com.mobisoft.mbswebplugin.view.ActionSheetDialog;
import com.mobisoft.mbswebplugin.view.area.CharacterPickerWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import static com.mobisoft.mbswebplugin.base.AppConfing.PERMISSIONS_REQUEST_CODE;
import static com.mobisoft.mbswebplugin.utils.FileUtils.deleteFile;


public class Utils {
    public static String isSuccess = "";

    public static final String TAG = "Utils";


    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final String TEMP_IMAGE_CAMERA = "temp_camera.jpg";
    public static final String TEMP_IMAGE_CROP = "temp_crop.jpg";
    public static final int CROP_AVATAR_HEIGHT = 240;
    public static final int CROP_AVATAR_WIDTH = 240;
    public static final int IMAGE_FROM_CAMERA = 0x0a1;
    public static final int IMAGE_FROM_PHOTOS = 0xfe2;
    public static final int IMAGE_CROP_RESULT = 0xaf3;// 结果

    public static final String IMAGE_SELECT_PHOTOS = "0";//相册
    public static final String IMAGE_SELECT_CAMERA = "1";//相机
    public static final String IMAGE_SELECT_CAMERA_AND_PHOTOS = "2";//相机 和 相册

    // 0时间 1日期 2日期时间
    public static final String DATA_SELECT_TIME = "0";//时间
    public static final String DATA_SELECT_DATA = "1";//日期
    public static final String DATA_SELECT_DATA_AND_TIME = "2";//日期时间
    public static final String DATA_SELECT_TIME_AND_DATA = "3";//时间日期

    //入参
    public static final String IN_PARAMETER_FOR_DATE = "date";//日期
    public static final String IN_PARAMETER_FOR_TIME = "time";//时间
    public static final String IN_PARAMETER_FOR_CITY = "city";//市
    public static final String IN_PARAMETER_FOR_PROV = "prov";//省
    public static final String IN_PARAMETER_FOR_AREA = "area";//区
    public static final String IN_PARAMETER_FOR_NAME = "name";//名字
    public static final String IN_PARAMETER_FOR_CODE = "code";//编码
    public static final String IN_PARAMETER_FOR_ADDR = "addr";//位置
    public static final String IN_PARAMETER_FOR_LAT = "lat";//经度
    public static final String IN_PARAMETER_FOR_LON = "lon";//纬度
    public static final String IN_PARAMETER_FOR_IMAGE = "images";//图片

    /**
     * 图片压缩宽高
     */
    private static float width = 480;
    private static float height = 800;

    /**
     * 放置资源的文件夹htmlsrc
     */
    public static String HTML_PATH = "Acfan";

    /**
     * 放置图片的的文件夹temp
     */
    public static String HTML_PHTOT_PATH = "temp";
    /**
     * 图片的的文件夹images
     */
    public static String HTML_IMAGES_PATH = "images";


    // 获得参数
    public static String getCommand(String command) {
//        String commandStr = command.replaceAll("/", "").replaceAll("/?", "").trim();
        String commandStr = command.substring(2, command.length() - 1);
        Log.i("LLL", "commandStr:" + commandStr);
        return commandStr;
    }

    /**
     * 封装打电话的方法
     *
     * @param context
     * @param paramter
     */
    public static void getPhone(Context context, String paramter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)// 大于6.0 权限检查
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                cellPhone(context, paramter);
            } else {
                // Ask for one permission
                ((Activity) context).requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            cellPhone(context, paramter);
        }
    }
    /**
     * 封装打电话的方法
     *
     * @param context
     * @param paramter
     */
    public static void getSMS(Context context, String paramter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)// 大于6.0 权限检查
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                doSendSMSTo(context, paramter);
            } else {
                // Ask for one permission
                ((Activity) context).requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            doSendSMSTo(context, paramter);
        }
    }
    /**
     * 拨打电话
     *
     * @param context
     * @param paramter
     */
    public static void cellPhone(Context context, String paramter) {
        JSONObject mJSONObject = null;
        String phoneNumber = null;
        try {
            mJSONObject = new JSONObject(paramter);
            phoneNumber = mJSONObject.getString(CMD.cmd_cellphone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }
    /**
     * @param context
     * @param paramter
     * 短信
     */
    public static void doSendSMSTo(Context context, String paramter){
        JSONObject mJSONObject = null;
        JSONArray phoneNumber = null;
        String message = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            mJSONObject = new JSONObject(paramter);
            phoneNumber = mJSONObject.getJSONArray("phoneNum");
            message = mJSONObject.getString("message");
            int length = phoneNumber.length();
            for (int i = 0; i < length; i++) {
                stringBuilder.append(phoneNumber.get(i));
                if(i==length-1){
                }else {
                    stringBuilder.append(";");
                }

            }
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+stringBuilder.toString()));
            intent.putExtra("sms_body", message);
            context.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    /**
     * 解析H5返回图片张数
     */

    public static int getlimitCount(Context context, String paramter) {

        JSONObject mjsonObject = null;
        int limtCount = 0;

        try {
            mjsonObject = new JSONObject(paramter);
            limtCount = mjsonObject.getInt("limitCount");// 图片总数

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return limtCount;
    }

    /**
     * 解析H5返回图片张数
     */

    public static int getSelectCount(Context context, String paramter) {

        JSONObject mjsonObject = null;
        int selectCount = 0;

        try {
            mjsonObject = new JSONObject(paramter);
            selectCount = mjsonObject.getInt("currCount");// 图片总数

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return selectCount;
    }

    /**
     * 发送邮件
     */
    public static void sendEmail(Context context, String email) {
        JSONObject mJSONObject = null;
        String mailto = null;
        try {
            mJSONObject = new JSONObject(email);
            mailto = mJSONObject.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:" + mailto));
        data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
        data.putExtra(Intent.EXTRA_TEXT, "这是内容");
        context.startActivity(data);
    }

    //封装调用相机的方法
    public static void getPic(Context context, String paramter) {
        if (IMAGE_SELECT_PHOTOS.equals(paramter)) getFromPhotos(context);
        if (IMAGE_SELECT_CAMERA.equals(paramter)) openTakePhoto(context);
    }

    /**
     * 打开相机
     *
     * @param context
     * @param param   0相册 1相机 2相机相册
     */
    public static void getPicDialog(final Context context, final String param) {
//        try {
//            JSONObject object = new JSONObject(param);
//            final String typeStr = object.getString("imageSourceType");
        if (Utils.IMAGE_SELECT_CAMERA.equals(param)) {
            new ActionSheetDialog(context)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    getPic(context, param);
                                }
                            }).show();
        } else if (Utils.IMAGE_SELECT_PHOTOS.equals(param)) {
            new ActionSheetDialog(context)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    getPic(context, param);
                                }
                            }).show();
        } else if (Utils.IMAGE_SELECT_CAMERA_AND_PHOTOS.equals(param)) {
            new ActionSheetDialog(context)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    getPic(context, Utils.IMAGE_SELECT_CAMERA);
                                }
                            })
                    .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    getPic(context, Utils.IMAGE_SELECT_PHOTOS);
                                }
                            }).show();
        }

//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }


    /**
     * 选择地区对话框
     */
    public static void showAreaWindow(final HybridWebView mHybridWebView, View prent, final Context context, String paramter, final String function) {
        //初始化
        final CharacterPickerWindow window = OptionsWindowHelper.builder(context, new OptionsWindowHelper.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(String province, String city, String area) {
                Log.e("main", province + "," + city + "," + area);
                ToastUtil.showLongToast(context, province + "," + city + "," + area);
                mHybridWebView.excuteJSFunction(function, IN_PARAMETER_FOR_CITY, "123," + province, "123," + city, "123," + area);
            }
        });
        // 弹出
        window.showAtLocation(prent, Gravity.BOTTOM, 0, 0);

    }

    /*
      * 判断是否为整数
      * @param str 传入的字符串
      * @return 是整数返回true,否则返回false
    */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 打开时间选择框
     *
     * @param context
     * @param paramter 0时间 1日期 2日期时间 默认是日期
     */
    public static void getTimePickerDialog(final HybridWebView mHybridWebView, Context context, String TIMETYPE, final String function, String paramter) {
        JSONObject jsonObject = null;
        String time = "";
        String year = "";
        try {
            jsonObject = new JSONObject(paramter);
            time = jsonObject.optString("time");// 图片url
            year = jsonObject.optString("date");// 右上角菜单名称
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 设置时间
        if (Utils.DATA_SELECT_TIME.equals(TIMETYPE)) {
            TimeDialog mTimeDialog;
            if (!TextUtils.isEmpty(time)) {
                String[] str = time.split(":");
                if (!isInteger(str[0]))
                    mTimeDialog = new TimeDialog(context);
                else
                    mTimeDialog = new TimeDialog(context, Integer.parseInt(str[0]), Integer.parseInt(str[1]));
            } else {
                mTimeDialog = new TimeDialog(context);
            }
            mTimeDialog.setDialogMode(TimePickerDialog.DIALOG_MODE_BOTTOM);

            mTimeDialog.show();
            mTimeDialog.setTimePickListener(new TimeDialog.OnTimePickListener() {
                @Override
                public void onClick(String hour, String minute) {
                    Log.e("LLL", hour + ":" + minute);
                    mHybridWebView.excuteJSFunction(function, IN_PARAMETER_FOR_TIME, hour + ":" + minute);
                }
            });


        }
        if (Utils.DATA_SELECT_DATA.equals(TIMETYPE)) { // 设置日期 年月日
            DatePickerDialog mDatePickerDialog = null;
            if (!TextUtils.isEmpty(year)) {//设置指定日期
                String[] dates = year.split("-");
                if (dates.length == 3) {// 当获取日期为年月日的时候
                    mDatePickerDialog = new DatePickerDialog(context, Integer.parseInt(dates[0]),
                            Integer.parseInt(dates[1]),
                            Integer.parseInt(dates[2]));
                    mDatePickerDialog.setDialogMode(TimePickerDialog.DIALOG_MODE_BOTTOM);

//                    mDatePickerDialog  = new DatePickerDialog(context);
//                    mDatePickerDialog.setDialogMode(TimePickerDialog.DIALOG_MODE_BOTTOM);

                } else {
                    mDatePickerDialog = new DatePickerDialog(context);
                    mDatePickerDialog.setDialogMode(TimePickerDialog.DIALOG_MODE_BOTTOM);
                }
            } else {
                mDatePickerDialog = new DatePickerDialog(context);
                mDatePickerDialog.setDialogMode(TimePickerDialog.DIALOG_MODE_BOTTOM);
            }


            mDatePickerDialog.show();
            mDatePickerDialog.setDatePickListener(new DatePickerDialog.OnDatePickListener() {
                @Override
                public void onClick(String year, String month, String day) {
                    Log.e("LLL", year + "-" + month + "-" + day);
                    mHybridWebView.excuteJSFunction(function, IN_PARAMETER_FOR_DATE, year + "-" + month + "-" + day);
                }
            });
        }
        if (Utils.DATA_SELECT_DATA_AND_TIME.equals(TIMETYPE)) { // 设置时间和日期
            TimePickerDialog mTimePickerDialog = new TimePickerDialog(context);
            // mTimePickerDialog.setDate(2015, 03, 29);
            mTimePickerDialog.setDialogMode(TimePickerDialog.DIALOG_MODE_BOTTOM);
//		c = Calendar.getInstance();
//		c.setTimeInMillis(System.currentTimeMillis());
            mTimePickerDialog.show();
            mTimePickerDialog.setTimePickListener(new TimePickerDialog.OnTimePickListener() {
                @Override
                public void onClick(int year, int month, int day, String hour, String minute) {
                    Log.e("LLL", year + "-" + month + "-" + day + " " + hour + ":" + minute);
                }

            });

        }
        if (Utils.DATA_SELECT_TIME_AND_DATA.equals(TIMETYPE)) {
            TimeAndDataDialog mTimeAndDataDialog = new TimeAndDataDialog(context);
            mTimeAndDataDialog.setDialogMode(TimePickerDialog.DIALOG_MODE_BOTTOM);
            mTimeAndDataDialog.show();
            mTimeAndDataDialog.setTimePickListener(new TimeAndDataDialog.OnTimePickListener() {
                @Override
                public void onClick(String hour, String minute, int year, int month, int day) {
                    Log.e("LLL", hour + ":" + minute + " " + year + "-" + month + "-" + day);
                }

            });
        }

    }

    /**
     * 打开时间选择框
     *
     * @param context
     * @param paramter 0时间 1日期 2日期时间 默认是日期
     */
    public static String getTimePickerDialog(Context context, String paramter) {

        final String[] endtime = {""};
//        try {
//            JSONObject object = new JSONObject(paramter);
//            final String typeStr = object.getString("dateType");
        if (DATA_SELECT_TIME.equals(paramter)) {
            TimeDialog mTimeDialog = new TimeDialog(context);
            mTimeDialog.setDialogMode(TimePickerDialog.DIALOG_MODE_BOTTOM);
            mTimeDialog.show();
            mTimeDialog.setTimePickListener(new TimeDialog.OnTimePickListener() {
                @Override
                public void onClick(String hour, String minute) {
                    endtime[0] = hour + ":" + minute;
                    Log.e("LLL", hour + ":" + minute);
                }
            });
        }
        if (DATA_SELECT_DATA.equals(paramter)) {
            DatePickerDialog mDatePickerDialog = new DatePickerDialog(context);
            mDatePickerDialog.setDialogMode(TimePickerDialog.DIALOG_MODE_BOTTOM);
            mDatePickerDialog.show();
            mDatePickerDialog.setDatePickListener(new DatePickerDialog.OnDatePickListener() {
                @Override
                public void onClick(String year, String month, String day) {
                    endtime[0] = year + "-" + month + "-" + day;
                }
            });
        }
        if (DATA_SELECT_DATA_AND_TIME.equals(paramter)) {
            TimePickerDialog mTimePickerDialog = new TimePickerDialog(context);
            // mTimePickerDialog.setDate(2015, 03, 29);

            mTimePickerDialog.setDialogMode(TimePickerDialog.DIALOG_MODE_BOTTOM);
//		c = Calendar.getInstance();
//		c.setTimeInMillis(System.currentTimeMillis());
            mTimePickerDialog.show();
            mTimePickerDialog.setTimePickListener(new TimePickerDialog.OnTimePickListener() {
                @Override
                public void onClick(int year, int month, int day, String hour, String minute) {
                    endtime[0] = year + "-" + month + "-" + day + " " + hour + ":" + minute;

                }

            });

        }
        if (DATA_SELECT_TIME_AND_DATA.equals(paramter)) {
            TimeAndDataDialog mTimeAndDataDialog = new TimeAndDataDialog(context);
            mTimeAndDataDialog.setDialogMode(TimePickerDialog.DIALOG_MODE_BOTTOM);
            mTimeAndDataDialog.show();
            mTimeAndDataDialog.setTimePickListener(new TimeAndDataDialog.OnTimePickListener() {
                @Override
                public void onClick(String hour, String minute, int year, int month, int day) {
                    endtime[0] = hour + ":" + minute + " " + year + "-" + month + "-" + day;

                }

            });
        }

//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        return endtime[0];
    }

    /**
     * 调用相机
     */
    private static void openTakePhoto(Context context) {
        /**
         * 在启动拍照之前最好先判断一下sdcard是否可用
         */
        String state = Environment.getExternalStorageState(); // 拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)) { // 如果可用

//			File dir = new File(Environment.getExternalStorageDirectory() + "/" + "ideaTemp");
//			if (!dir.exists())
//				dir.mkdirs();
            File dir = new File(Environment.getExternalStorageDirectory() + "/" + "ideaTemp",
                    TEMP_IMAGE_CAMERA);
            dir.getParentFile().mkdirs();

            ((Activity) context).startActivityForResult(
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(dir)),
                    IMAGE_FROM_CAMERA);

//			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//			File f = new File(dir, BasicDetailInformationTestActivity.TEMP_IMAGE_CAMERA);// localTempImgDir和localTempImageFileName是自己定义的名字
//			Uri u = Uri.fromFile(f);
//			intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
//			startActivityForResult(intent, BasicDetailInformationTestActivity.IMAGE_FROM_CAMERA);
        } else {
            ToastUtil.showLongToast(context, "sdcard不可用");
        }
    }

    /**
     * 调用相册
     */
    private static void getFromPhotos(Context context) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        ((Activity) context).startActivityForResult(intent, IMAGE_FROM_PHOTOS);
    }

    /**
     * 获取绝对路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static  String getAbsoluteImagePath(Context context, Uri uri) {
        return URIUtils.getPath(context, uri);
    }



    /**
     * 图片暂存地址
     */
    public static String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "DCIM/MBS" + File.separator;

    /**
     * copy照片 到私有目录temp下
     *
     * @param context
     * @param fileUrl 文件名字
     */
    public static String copyPhotoToTemp(Context context, Uri fileUrl) {
        String isSuccess = "";
        File of = null;
        try {
//            File filesDir = context.getFilesDir();
//            String filesDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + HTML_PATH + "/" + HTML_IMAGES_PATH + "/" + HTML_PHTOT_PATH;
            String filesDir = FILE_PATH + HTML_PHTOT_PATH;
            File dir = new File(filesDir);
            if (!dir.exists()) {
                // 文件不存在进行创建
                dir.mkdirs();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            String imagePath = getAbsoluteImagePath(context, fileUrl);
            BitmapFactory.decodeFile(imagePath, options);

            options.inSampleSize = calculateInSampleSize(options);

            options.inJustDecodeBounds = false;


            File file = new File(imagePath);
            if (!file.exists()) {
                return isSuccess;
            }


            isSuccess = System.currentTimeMillis() + "";
            // 将图片放到制定文件夹下面
            of = new File(filesDir + "/" + isSuccess + ".jpg");

            FileOutputStream os = new FileOutputStream(of);

            if (BitmapFactory.decodeFile(imagePath, options).compress(Bitmap.CompressFormat.JPEG, 50, os)) {
                os.flush();
                os.close();
            }

            Log.i("bitmap", "压缩后:" + (of.length() / 1024) + "KB");


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isSuccess = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isSuccess = null;
        }
//        Log.i("bitmap", "路径：" + HTML_IMAGES_PATH + "/" + HTML_PHTOT_PATH + "/" + isSuccess + ".jpg");
        // 返回路径
        if (TextUtils.isEmpty(isSuccess)) {// 失败的情况下
            return "";
        }
//        return HTML_IMAGES_PATH + "/" + HTML_PHTOT_PATH + "/" + isSuccess + ".jpg";
        return of.getAbsolutePath();
    }

    /**
     * 压缩bitmap
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 图片压缩方法
     *
     * @param context
     * @param fileUrl
     * @return
     */
    public static Bitmap transImage(Context context, Uri fileUrl) {
        Bitmap resizeBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(getAbsoluteImagePath(context, fileUrl), options);
        options.inSampleSize = calculateInSampleSize(options);
        options.inJustDecodeBounds = false;
//		resizeBitmap =BitmapFactory.decodeFile(getAbsoluteImagePath(context, fileUrl), options);


        try {
            return BitmapFactory.decodeFile(getAbsoluteImagePath(context, fileUrl), options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片压缩方法
     *
     * @param context
     * @param path
     * @return
     */
    public static Bitmap transImage(Context context, String path) {

        Bitmap resizeBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options);
        options.inJustDecodeBounds = false;
//		resizeBitmap =BitmapFactory.decodeFile(path, options);
//		BitmapFactory.decodeFileDescriptor()
        try {
            return BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断压缩的比例
     *
     * @param options
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > Utils.height || width > Utils.width) {
            final int heightRatio = Math.round((float) height / Utils.height);
            final int widthRatio = Math.round((float) width / Utils.width);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        Log.i("bitmap", width + "宽 :搞 " + height);
        Log.i("bitmap", inSampleSize + "质量 ");
        return inSampleSize;
    }

    /**
     * json2entity: json字符串转换为entity.
     */
    public static <T> T json2entity(String json, Class<T> clazz) {

        if (json == null || TextUtils.isEmpty(json)) {
            return null;
        }
        T entity = JSON.parseObject(json, clazz);
        return entity;

    }

    /**
     * 格式化 js方法
     *
     * @return
     */
    public static String functionFormat(String function, Object myJsonObject) {
        String newFunction = function;
        if (function.endsWith("(")) {
            newFunction = function.substring(0, function.length() - 1);
        } else if (!function.contains("(")) {
            String josn = String.format("javascript:" + newFunction + "(%s)", myJsonObject);

            return josn;
        }
        String josn = String.format("javascript:" + newFunction + "%s)", myJsonObject);

        return josn;
    }

    /**
     * 格式化 js方法
     *
     * @return
     */
    public static String functionFormat(String function, String param) {
        String newFunction = function;
        if (function.endsWith("(")) {
            newFunction = function.substring(0, function.length() - 1);
        } else if (!function.contains("(")) {
            String josn = String.format("javascript:(" + newFunction + "%s)", param);

            return josn;
        }
        String josn = String.format("javascript:" + newFunction + "%s)", param);

        return josn;
    }


    /**
     * uri 获取字符地址
     *
     * @param context
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Activity context, Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            // Do not call Cursor.close() on a cursor obtained using this
            // method,
            // because the activity will do that for you at the appropriate time
            Cursor cursor = context.managedQuery(contentUri, proj, null, null,
                    null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    /**
     * 清除WebView缓存
     */
    public static void clearWebViewCache(Context context, String app_cacahe_dirname) {

        //清理Webview缓存数据库
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
            context.deleteDatabase("web_cache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(context.getFilesDir().getAbsolutePath() + app_cacahe_dirname);
        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath() + "/webviewCache");
        Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }


    /**
     * 获取 缓存的大小
     *
     * @param absolutePath       缓存的绝对路径
     * @param app_cacahe_dirname 缓目录
     * @param PackageName        包名
     * @return 缓存大小
     * @throws Exception
     */
    public static String getCacherSize(String absolutePath, String app_cacahe_dirname, String PackageName) throws Exception {
        long size1 = FileUtils.getFolderSize(new File(absolutePath + app_cacahe_dirname));
        long size2 = FileUtils.getFolderSize(new File(absolutePath + "/webviewCache"));
        long size5 = FileUtils.getFolderSize(new File(Environment.getExternalStorageDirectory() + File.separator + PackageName + File.separator + "Images"
                + File.separator));
        long sum = size1 + size2 + size5;
        return FileUtils.setFileSize(sum);

    }

    private static Handler handler;

    public static Handler getMainHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }
}
