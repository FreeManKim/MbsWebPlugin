package com.mobisoft.mbswebplugin.Cmd.DoCmd;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Entity.AddressComponent;
import com.mobisoft.mbswebplugin.Entity.LocationMap;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsRequestPermissionsListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsResultListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.LocationUtils;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.UrlUtil;

import cz.msebera.android.httpclient.Header;

import static com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebFragment.TAG;

/**
 * Author：Created by fan.xd on 2017/3/3.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 获取当前位置经纬度
 */

/**
 * Author：Created by fan.xd on 2017/6/16.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class Location extends DoCmdMethod implements MbsResultListener, LocationListener {

    public static final int REQUEST_CODE_LOCTION = 0X654;
    private LocationManager locationManager;
    private Context mContext;
    private MbsWebPluginContract.View presenterView;
    private String callBack;

    @Override
    public String doMethod(HybridWebView hybridWebView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String s, String s1, String s2) {
        presenter.setResultListener(this);
        mContext = context;
        presenterView = view;
        callBack = s2;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permission = new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            };
            ActivityCompat.requestPermissions((Activity) mContext, permission, 200);
        } else {
            getLocationManger(context);

        }
        presenter.setMbsRequestPermissionsResultListener(new MbsRequestPermissionsListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] strings, @NonNull int[] grantResults) {
                if (requestCode == 200) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        getLocationManger(mContext);

                    } else {
                        ToastUtil.showShortToast(mContext, "缺少相关权限无法使此功能！");
                    }
                }
            }
        });


        return null;
    }

    /**
     * 获取定位
     *
     * @param context
     */
    private void getLocationManger(Context context) {
        if (!LocationUtils.isLocationEnabled(context)) {
            ToastUtil.showShortToast(context, "请打开网络或GPS定位功能!");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            ((Activity) context).startActivityForResult(intent, REQUEST_CODE_LOCTION);
        } else {
            startLocation();
        }

    }

    /**
     * 定位
     *
     */
    private void startLocation() {
        try {
            LocationManager mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            String provider = mLocationManager.getBestProvider(LocationUtils.getCriteria(), true);
            android.location.Location location = mLocationManager.getLastKnownLocation(provider);

            updateView(location);
//            mLocationManager.requestLocationUpdates(provider, 0, 0, this);

//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, this);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
//       boolean isSuccess = LocationUtils.register(mContext,0, 0, mOnLocationChangeListener);
    }

    @Override
    public void onActivityResult(Context context, MbsWebPluginContract.View view, int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CODE_LOCTION:
                getLocationManger(context);
                break;
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        updateView(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


        } else {
            android.location.Location location = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, "onProviderDisabled.location = " + location);
            updateView(location);
        }

    }

    private void updateView(android.location.Location location) {

//
        if (location != null) {
//                location.getLatitude(), location.getLongitude();
            String url = String.format("http://restapi.amap.com/v3/geocode/regeo?key=b11808c6d42b56091cb79705d0a3929a&location=%s,%s", location.getLongitude(), location.getLatitude());
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    LocationMap locationMap = JSON.parseObject(new String(responseBody), LocationMap.class);
                    final AddressComponent component = locationMap.getRegeocode().getAddressComponent();
                    presenterView.loadUrl(UrlUtil.getFormatJs(callBack, JSON.toJSON(component).toString()));


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });

        }
    }


    private LocationUtils.OnLocationChangeListener

            mOnLocationChangeListener = new LocationUtils.OnLocationChangeListener() {


        @Override
        public void getLastKnownLocation(android.location.Location location) {
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            String country = LocationUtils.getCountryName(mContext, Double.parseDouble(latitude), Double.parseDouble(longitude));
            String locality = LocationUtils.getLocality(mContext, Double.parseDouble(latitude), Double.parseDouble(longitude));
            String street = LocationUtils.getStreet(mContext, Double.parseDouble(latitude), Double.parseDouble(longitude));
            AddressComponent component = new AddressComponent();
            component.setProvince(country);
            component.setDistrict(locality);
            presenterView.loadUrl(UrlUtil.getFormatJs(callBack, JSON.toJSON(component).toString()));


        }

        @Override
        public void onLocationChanged(android.location.Location location) {
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());

            String country = LocationUtils.getCountryName(mContext, Double.parseDouble(latitude), Double.parseDouble(longitude));
            String locality = LocationUtils.getLocality(mContext, Double.parseDouble(latitude), Double.parseDouble(longitude));
            String street = LocationUtils.getStreet(mContext, Double.parseDouble(latitude), Double.parseDouble(longitude));
            AddressComponent component = new AddressComponent();
            component.setProvince(country);
            component.setDistrict(locality);
            presenterView.loadUrl(UrlUtil.getFormatJs(callBack, JSON.toJSON(component).toString()));

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

}

