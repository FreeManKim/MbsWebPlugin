/**
 * Copyright (c) 2013, The Android Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobisoft.mbswebplugin.proxy.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.mobisoft.mbswebplugin.IProxyCallback;
import com.mobisoft.mbswebplugin.IProxyPortListener;
import com.mobisoft.mbswebplugin.proxy.DB.WebviewCaheDao;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;


/**
 *
 */
public class ProxyService extends Service {

    private ProxyServer server = null;

    /**
     * Keep these values up-to-date with PacManager.java
     */
    public static final String KEY_PROXY = "keyProxy";
    public static final String HOST = "localhost";
    // STOPSHIP This being a static port means it can be hijacked by other apps.
    public static int PORT = ProxyConfig.getConfig().getPORT();
    public static final String EXCL_LIST = "";
    /**
     * 数据库
     */
    private WebviewCaheDao dao;

    @Override
    public void onCreate() {
        super.onCreate();
        if (server == null) {
            dao = new WebviewCaheDao(getApplicationContext());
            server = new ProxyServer(ProxyConfig.getConfig().getCachePath(), getApplicationContext()
                    , dao);
            server.startServer();
        }
    }

    @Override
    public void onDestroy() {
        if (server != null) {
            server.stopServer();
            server = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new IProxyCallback.Stub() {
            @Override
            public void getProxyPort(IBinder callback) throws RemoteException {
                if (server != null) {
                    IProxyPortListener portListener = IProxyPortListener.Stub.asInterface(callback);
                    if (portListener != null) {
                        server.setCallback(portListener);
                    }
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
