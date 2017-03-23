package com.mobisoft.mbswebplugin.dao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/12/3 0003.
 * 省市及其对应id的表
 */
public class AddressSQLiteOpenHelper extends SQLiteOpenHelper {

    /**
     * 数据库名称
     */
    public static final String DB_NAME = "area.db";
    /**
     * 数据库的版本号
     */
    public static final int DB_VERSION = 1;
    /**
     * 省的表名
     */
    public static final String ADDRESS_PROVINCE_NAME = "province";
    /**
     * 市的表名
     */
    public static final String ADDRESS_CITY_NAME = "city";
    /**
     * 县的表名
     */
    public static final String ADDRESS_COUNTY_NAME = "county";

    /**
     * SharedPreferences的文件名
     */
    public static final String GBF_INFO = "AREA_INFO";
    /**
     * 省表、市表、县表是否创建 的键
     */
    public static final String ADDRESS_TABLE = "ADDRESS_TABLE";

    public AddressSQLiteOpenHelper(Context context){
        this(context, DB_NAME, null, DB_VERSION);
    }
    public AddressSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ADDRESS_PROVINCE_NAME + " ( " +
                "province_id integer primary key,"+
                "name varchar(50)"+")" );
        db.execSQL("CREATE TABLE " + ADDRESS_CITY_NAME + " ( " +
                "city_id integer primary key,"+
                "name varchar(50)," +
                "province_name varchar(50)"+")");
        db.execSQL("CREATE TABLE " + ADDRESS_COUNTY_NAME + " ( " +
                "county_id integer primary key,"+
                "name varchar(50)," +
                "city_name varchar(50)"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
