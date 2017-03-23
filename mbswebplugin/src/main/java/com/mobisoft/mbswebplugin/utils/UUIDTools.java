package com.mobisoft.mbswebplugin.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.UUID;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Author：Created by fan.xd on 2017/1/5.
 * Email：fang.xd@mobisoft.com.cn
 * 所需权限
 *     <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 <uses-permission android:name="android.permission.INTERNET"/>
 <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
 <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
 * Description：
 */

public class UUIDTools {
    private static final String TAG = UUIDTools.class.getName();
    private static UUIDTools UUIDTools;

    /**
     * 获取当前实例
     *
     * @return UUIDTools
     */
    public static UUIDTools getInstance() {
        if (UUIDTools == null)
            UUIDTools = new UUIDTools();
        return UUIDTools;
    }

    /**
     * 获取UUID
     * @param mContext
     * @return 获取UUID
     */
    public String getUuid(Context mContext){
        try {
            StringBuilder sb=new StringBuilder();
            String imei = getIMEI(mContext);
            String androidID = getAndroidID(mContext.getContentResolver());
            String macAddress = getMacAddress(mContext);
            if(!TextUtils.isEmpty(imei))
                sb.append(imei);
            if(!TextUtils.isEmpty(androidID))
                sb.append(androidID);
            if(!TextUtils.isEmpty(macAddress))
                sb.append(macAddress);
            if(TextUtils.isEmpty(sb.toString()))
                new RuntimeException("getUuid failure!获取uuid失败请重新申请！");
            return Md5(sb.toString());
        }catch (Exception e){

        }
        return "";
    }

    /**
     * mmad5加密
     * @param request
     * @return
     */
    private String Md5(String request) {

        String md5Str = request;
        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }

        md5.reset();

        md5.update(md5Str.getBytes());
        byte[] b = md5.digest();
        String s = makeString(b);
        // String b64result = Base64.encodeToString(s.getBytes(),
        // Base64.DEFAULT);
        return s;
    }
    private String makeString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(String.format("%02x", b[i]));
        }
        return sb.toString();
    }
    /**
     * 获取Mac地址
     *
     * @param mContext 环境
     * @return Mac地址
     */
    public String getMacAddress(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Enumeration<NetworkInterface> interfaces = null;
            try {
                interfaces = NetworkInterface.getNetworkInterfaces();
            } catch (SocketException e) {
                e.printStackTrace();
            }

            while (interfaces.hasMoreElements()) {

                NetworkInterface iF = interfaces.nextElement();
                byte[] addr = new byte[0];
                try {
                    addr = iF.getHardwareAddress();
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                if (addr == null || addr.length == 0) {
                    continue;
                }
                StringBuilder buf = new StringBuilder();
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                String mac = buf.toString();
                Log.d(TAG, "interfaceName=" + iF.getName() + ", mac=" + mac);
                if (TextUtils.equals("wlan0", iF.getName())) {
                    return mac;
                }
            }
            return "";
        } else {
            String macStr = "";
            WifiManager wifiManager = (WifiManager) mContext
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getMacAddress() != null) {
                macStr = wifiInfo.getMacAddress();// MAC地址
            }
            return macStr;
        }

    }

    /**
     * 获取DeviceID
     *
     * @param mContext
     */
    public String getDevice(Context mContext) {

        final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        Log.d(TAG, "deviceId=" + deviceId);
        return deviceId;
    }

    /**
     * 获取手机IMEI号
     *
     * @param context
     * @return getDeviceId
     */
    public String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        return tm.getDeviceId();//String
    }

    /**
     * 获取android_ID
     * @param contentResolver
     * @return  android_ID
     */
    public String getAndroidID(ContentResolver contentResolver){
        return  Settings.System.getString(contentResolver, Settings.System.ANDROID_ID);
    }
}