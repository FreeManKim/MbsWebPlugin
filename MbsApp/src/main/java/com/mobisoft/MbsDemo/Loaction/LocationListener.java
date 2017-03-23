package com.mobisoft.MbsDemo.Loaction;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.mobisoft.common.gateway.Res;
import com.mobisoft.library.Constants;
import com.mobisoft.library.http.CustomHttp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author：Created by fan.xd on 2017/3/3.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class LocationListener implements AMapLocationListener {
    private Context context;

    public LocationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        final String msg = aMapLocation.getAddress() + "\n" ;

//              +  "getErrorInfo:" + aMapLocation.getErrorInfo() + "\n" +
//                "getErrorCode:" + aMapLocation.getErrorCode() + "\n" +
//                "getLongitude:" + aMapLocation.getLongitude() + "\n" +
//                "getLatitude:" + aMapLocation.getLatitude();
        Log.e("LocationListener", msg);

        final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "amap" + File.separator + "amap.txt");
        final File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "amap" + File.separator + "aResult.txt");
//        if(!file.exists()){
//            file.mkdir();
//        }
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
            final String data = dateFormat.format(new Date(System.currentTimeMillis()));
            final FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.write(msg.getBytes());
            fileOutputStream.write("\n".getBytes());
            fileOutputStream.close();
            ReqLoginByEmployeeID req = new ReqLoginByEmployeeID();
            req.setCmd("LoginByEmployeeID");
            req.setEmployee_id("8100458");
            req.setPasswd("a123456");
            Constants.ACCOUNT="8C89A41A3D86";
            CustomHttp.getInstance("http://ainewdev.cttq.com/account/mobile",context, req)
                    .setInterf(new CustomHttp.Callback() {
                        @Override
                        public void onFailure(String url, Object request, Throwable ex) {
                           Log.e("LocationListener",url);
                        }

                        @Override
                        public void onSuccess(String url, Res response) {
                            FileOutputStream fileOutputStream2 = null;
                            try {
                                fileOutputStream2 = new FileOutputStream(file2, true);
                                fileOutputStream2.write(data.getBytes());
                                fileOutputStream2.write("\n".getBytes());
//                                fileOutputStream2.write(response.getPayload().toString().getBytes());
//                                fileOutputStream2.write("\n".getBytes());
//                                fileOutputStream2.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Log.e("LocationListener","正确得"+response.getPayload().toString());
                        }
                    }).runGet();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
