package com.mobisoft.MbsDemo.LitsViewActivity;

/**
 * Author：Created by fan.xd on 2017/1/17.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.Serializable;
import java.util.List;


public class ResListMyInstalledMicroApp implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<MicroAppInfo> myApps;

    public ResListMyInstalledMicroApp() {
    }

    public List<MicroAppInfo> getMyApps() {
        return this.myApps;
    }

    public void setMyApps(List<MicroAppInfo> myApps) {
        this.myApps = myApps;
    }
}

