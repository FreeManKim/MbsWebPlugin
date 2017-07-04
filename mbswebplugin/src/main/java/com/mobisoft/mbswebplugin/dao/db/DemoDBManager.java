package com.mobisoft.mbswebplugin.dao.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class DemoDBManager {
    static private DemoDBManager dbMgr = new DemoDBManager();
    private DbOpenHelper dbHelper;

    void onInit(Context context) {
        dbHelper = DbOpenHelper.getInstance(context);
    }

    public static synchronized DemoDBManager getInstance() {
        return dbMgr;
    }

    /**
     * 保存Webview的数据库
     *
     * @param key  id
     * @param json 传入json
     */
    synchronized public void saveWebviewJson(String account, String key, String json) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            // 先删除再添加
            db.delete(WebViewDao.TABLE_NAME, WebViewDao.COLUMN_KEY + " = ?", new String[]{key});
            ContentValues values = new ContentValues();
            values.put(WebViewDao.COLUMN_ACCOUNT, account);
            values.put(WebViewDao.COLUMN_KEY, key);
            values.put(WebViewDao.COLUMN_JSON, json);
            db.replace(WebViewDao.TABLE_NAME, null, values);
        }
    }

    /**
     * 获取Webview的json
     *
     * @param key id
     */
    synchronized public String getWebviewJson(String account, String key) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String json = null;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + WebViewDao.TABLE_NAME + " where " + WebViewDao.COLUMN_ACCOUNT + " = ?" + " and " + WebViewDao.COLUMN_KEY + " = ?", new String[]{account, key});
            while (cursor.moveToNext()) {
                json = cursor.getString(cursor.getColumnIndex(WebViewDao.COLUMN_JSON));
            }
            cursor.close();
        }
        return json;
    }

    /**
     * 获取Webview的json
     *
     * @param key id
     */
    synchronized public String getWebviewJson(String key) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String json = null;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + WebViewDao.TABLE_NAME + " where " + WebViewDao.COLUMN_KEY + " = ?", new String[]{key});
            while (cursor.moveToNext()) {
                json = cursor.getString(cursor.getColumnIndex(WebViewDao.COLUMN_JSON));
            }
            cursor.close();
        }
        return json;
    }

    /**
     * 删除Webview的数据库
     *
     * @param key id
     */
    synchronized public int deletWebviewList(String account, String key) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int n = 0;
        if (db.isOpen()) {
            if (TextUtils.isEmpty(account)) {
                n = db.delete(WebViewDao.TABLE_NAME, WebViewDao.COLUMN_KEY + " = ?", new String[]{key});

            } else {
                n = db.delete(WebViewDao.TABLE_NAME, WebViewDao.COLUMN_KEY + " = ?"+ WebViewDao.COLUMN_ACCOUNT +" = ?" , new String[]{key,account});

            }
        }
        return n;

    }


}