package com.mobisoft.mbswebplugin.proxy.Cache;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MyHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            // 信任所有host，直接返回true
            return true;
        }
    }