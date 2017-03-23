package com.mobisoft.mbswebplugin.Entity;

/**
 * Created by Administrator on 2016/9/24.
 */
public class Task {
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImages_url() {
        return images_url;
    }

    public void setImages_url(String images_url) {
        this.images_url = images_url;
    }

    public String getHeader_title() {
        return header_title;
    }

    public void setHeader_title(String header_title) {
        this.header_title = header_title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String url;
    public String images_url;
    public String header_title;
    public String date;

    @Override
    public String toString() {
        return "Task{" +
                "url='" + url + '\'' +
                ", images_url='" + images_url + '\'' +
                ", header_title='" + header_title + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
