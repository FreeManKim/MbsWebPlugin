package com.mobisoft.MbsDemo.Loaction;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mobisoft.amaplibrary.AmapLocation;

public class KeepLiveService extends Service {
    static KeepLiveService keepLiveService;

    public KeepLiveService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        keepLiveService = this;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AmapLocation.getInstance()
                .setContext(getApplicationContext())
                .setmLocationListener(new LocationListener(this))
                .init()
                .startLocation();
        startService(new Intent(this,InnerService.class));
        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public static class InnerService extends Service {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            KeepLiveManager.getInstance().setForeground(keepLiveService, this);
            return super.onStartCommand(intent, flags, startId);
        }
    }
}
