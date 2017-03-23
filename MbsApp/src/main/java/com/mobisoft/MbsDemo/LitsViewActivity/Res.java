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


public class Res implements Serializable {
    public static DummySuccess success = new DummySuccess();
    private long t1 = 0L;
    private long t2 = 0L;
    private long t3 = 0L;
    private String ts;
    private String cmd;
    private Object payload;
    private Boolean result = Boolean.valueOf(false);
    private String error;

    public Res() {
    }

    public long getT1() {
        return this.t1;
    }

    public void setT1(long t1) {
        this.t1 = t1;
    }

    public long getT2() {
        return this.t2;
    }

    public void setT2(long t2) {
        this.t2 = t2;
    }

    public long getT3() {
        return this.t3;
    }

    public void setT3(long t3) {
        this.t3 = t3;
    }

    public Object getPayload() {
        return this.payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTs() {
        return this.ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getCmd() {
        return this.cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Boolean getResult() {
        return this.result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}

