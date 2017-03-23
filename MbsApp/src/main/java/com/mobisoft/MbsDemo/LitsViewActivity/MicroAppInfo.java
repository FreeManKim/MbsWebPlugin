package com.mobisoft.MbsDemo.LitsViewActivity;

/**
 * Author：Created by fan.xd on 2017/1/17.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

import java.io.Serializable;


public class MicroAppInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String account_owner;
    private String description;
    private String icon_no;
    private String icon_url;
    private Integer installed;
    private Boolean is_delete = Boolean.valueOf(false);
    private Boolean is_install = Boolean.valueOf(false);
    private Boolean is_online = Boolean.valueOf(false);
    private String keepalive_url;
    private String microapp_no;
    private String name;
    private Boolean need_monitor = Boolean.valueOf(false);
    private String notify_type;
    private String notify_url;
    private Long order_no;
    private String url;
    private Long version_code = Long.valueOf(0L);
    private String version_no;
    private String role_name;
    private String role_code;

    public MicroAppInfo() {
    }

    public static long getSerialversionuid() {
        return 1L;
    }

    public String getRole_code() {
        return this.role_code;
    }

    public void setRole_code(String role_code) {
        this.role_code = role_code;
    }

    public String getRole_name() {
        return this.role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getAccount_owner() {
        return this.account_owner;
    }

    public String getDescription() {
        return this.description;
    }

    public String getIcon_no() {
        return this.icon_no;
    }

    public String getIcon_url() {
        return this.icon_url;
    }

    public Integer getInstalled() {
        return this.installed;
    }

    public Boolean getIs_delete() {
        return this.is_delete;
    }

    public Boolean getIs_install() {
        return this.is_install;
    }

    public Boolean getIs_online() {
        return this.is_online;
    }

    public String getKeepalive_url() {
        return this.keepalive_url;
    }

    public String getMicroapp_no() {
        return this.microapp_no;
    }

    public String getName() {
        return this.name;
    }

    public Boolean getNeed_monitor() {
        return this.need_monitor;
    }

    public String getNotify_type() {
        return this.notify_type;
    }

    public String getNotify_url() {
        return this.notify_url;
    }

    public Long getOrder_no() {
        return this.order_no;
    }

    public String getUrl() {
        return this.url;
    }

    public Long getVersion_code() {
        return this.version_code;
    }

    public String getVersion_no() {
        return this.version_no;
    }

    public void setAccount_owner(String account_owner) {
        this.account_owner = account_owner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon_no(String icon_no) {
        this.icon_no = icon_no;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public void setInstalled(Integer installed) {
        this.installed = installed;
    }

    public void setIs_delete(Boolean is_delete) {
        this.is_delete = is_delete;
    }

    public void setIs_install(Boolean is_install) {
        this.is_install = is_install;
    }

    public void setIs_online(Boolean is_online) {
        this.is_online = is_online;
    }

    public void setKeepalive_url(String keepalive_url) {
        this.keepalive_url = keepalive_url;
    }

    public void setMicroapp_no(String microapp_no) {
        this.microapp_no = microapp_no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNeed_monitor(Boolean need_monitor) {
        this.need_monitor = need_monitor;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public void setOrder_no(Long order_no) {
        this.order_no = order_no;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVersion_code(Long version_code) {
        this.version_code = version_code;
    }

    public void setVersion_no(String version_no) {
        this.version_no = version_no;
    }

    public String toString() {
        return "MicroAppInfo [name=" + this.name + ", description=" + this.description + ", url=" + this.url + ", keepalive_url=" + this.keepalive_url + ", icon_no=" + this.icon_no + ", version_no=" + this.version_no + ", version_code=" + this.version_code + ", microapp_no=" + this.microapp_no + ", need_monitor=" + this.need_monitor + ", is_online=" + this.is_online + ", account_owner=" + this.account_owner + ", is_delete=" + this.is_delete + "]";
    }
}
