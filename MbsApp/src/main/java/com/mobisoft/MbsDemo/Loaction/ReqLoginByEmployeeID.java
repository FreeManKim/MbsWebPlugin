//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mobisoft.MbsDemo.Loaction;

import com.mobisoft.common.gateway.Parameter;

public class ReqLoginByEmployeeID extends Parameter {
    private static final long serialVersionUID = 1L;
    private String employee_id;
    private String passwd;

    public ReqLoginByEmployeeID() {
    }

    public String getEmployee_id() {
        return this.employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public static long getSerialversionuid() {
        return 1L;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
