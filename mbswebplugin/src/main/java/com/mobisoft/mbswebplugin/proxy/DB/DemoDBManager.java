package com.mobisoft.mbswebplugin.proxy.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    synchronized public void saveUrlPath(String account, String key, String json) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            // 先删除再添加
            db.delete(WebviewCaheDao.TABLE_NAME, WebviewCaheDao.COLUMN_ACCOUNT + " = ?", new String[]{key});
            ContentValues values = new ContentValues();
            values.put(WebviewCaheDao.COLUMN_ACCOUNT, account);
            values.put(WebviewCaheDao.COLUMN_KEY, key);
            values.put(WebviewCaheDao.COLUMN_JSON, json);
            db.replace(WebviewCaheDao.TABLE_NAME, null, values);
        }
    }

    /**
     * 获取Webview的json
     *
     * @param key id
     */
    synchronized public String getUrlPath(String account, String key) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String json = null;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + WebviewCaheDao.TABLE_NAME + " where " + WebviewCaheDao.COLUMN_ACCOUNT + " = ?" + " and " + WebviewCaheDao.COLUMN_KEY + " = ?", new String[]{account, key});
            while (cursor.moveToNext()) {
                json = cursor.getString(cursor.getColumnIndex(WebviewCaheDao.COLUMN_JSON));
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
    synchronized public String getUrlPath(String key) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String json = null;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + WebviewCaheDao.TABLE_NAME + " where " + WebviewCaheDao.COLUMN_KEY + " = ?", new String[]{key});
            while (cursor.moveToNext()) {
                json = cursor.getString(cursor.getColumnIndex(WebviewCaheDao.COLUMN_JSON));
            }
            cursor.close();
        }
        return json;
    }

    /**
     * 获取表中总条数
     */
    synchronized public int getCount() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int json = 0;
        if (db.isOpen()) {

            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + WebviewCaheDao.TABLE_NAME, null);
            while (cursor.moveToNext()) {
                json = cursor.getInt(0);
            }
            cursor.close();
            return json;
        }
        return json;
    }

    /**
     * 删除Webview的数据库
     *
     * @param key id
     */
    synchronized public void deletWebviewList(String account, String key) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(WebviewCaheDao.TABLE_NAME, WebviewCaheDao.COLUMN_ACCOUNT + " = ?", new String[]{key});
        }
    }


    /**
     * 删除Webview的数据库
     *
     * @param key id
     */
    synchronized public void deletKey(String key) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(WebviewCaheDao.TABLE_NAME, WebviewCaheDao.COLUMN_KEY + " = ?", new String[]{key});
        }
    }

}