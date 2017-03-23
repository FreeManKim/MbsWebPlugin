package com.mobisoft.mbswebplugin.dao.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/3 0003.
 * 操作省市表的DAO层
 */
public class AddressDAO {
    private SQLiteDatabase db;
    public SQLiteDatabase getDb() {
        return db;
    }
    public AddressDAO(Context context){
        AddressSQLiteOpenHelper asoh = new AddressSQLiteOpenHelper(context);
        db = asoh.getWritableDatabase();
    }

    /**
     * 向省表添加省名和id
     * @param name
     * @param id
     */
    public void addProvince(String name, int id){
        ContentValues values = new ContentValues();
        values.put("province_id",id);
        values.put("name",name);
        db.insert(AddressSQLiteOpenHelper.ADDRESS_PROVINCE_NAME,null,values);
    }
    /**
     * 向市表添加市名、id、所在省
     * @param name
     * @param id
     */
    public void addCity(String name, int id, String provinceName){
        ContentValues values = new ContentValues();
        values.put("city_id",id);
        values.put("name", name);
        values.put("province_name", provinceName);
        db.insert(AddressSQLiteOpenHelper.ADDRESS_CITY_NAME, null, values);
    }

    /**
     * 向县表添加县名、id、所在市
     * @param name
     * @param id
     */
    public void addCounty(String name, int id, String cityName){
        ContentValues values = new ContentValues();
        values.put("county_id",id);
        values.put("name", name);
        values.put("city_name", cityName);
        db.insert(AddressSQLiteOpenHelper.ADDRESS_COUNTY_NAME, null, values);
    }
    /**
     * 获取所有省
     * @return
     */
    public ArrayList<String> getProvinces(){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.query(AddressSQLiteOpenHelper.ADDRESS_PROVINCE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(name);
        }
        cursor.close();
        return list;
    }
    /**
     * 根据省名，查询省的id
     * @param province
     * @return
     */
    public int getProvinceId(String province){
        Cursor cursor = db.query(AddressSQLiteOpenHelper.ADDRESS_PROVINCE_NAME, new String[]{"province_id"}, "name=?",new String[]{province}, null, null, null);
        String[] columnNames = cursor.getColumnNames();
        int id = -1;
        if(cursor.moveToFirst())
            id = cursor.getInt(cursor.getColumnIndex("province_id"));
        cursor.close();
        return id;
    }

    /**
     * 根据省id，查询省名
     * @param id
     * @return
     */
    public String getProvinceName(String id){
        Cursor cursor = db.query(AddressSQLiteOpenHelper.ADDRESS_PROVINCE_NAME, new String[]{"name"}, "province_id=?",new String[]{id}, null, null, null);
        String[] columnNames = cursor.getColumnNames();
        String provinceName = "";
        if(cursor.moveToFirst())
            provinceName = cursor.getString(cursor.getColumnIndex("name"));
        cursor.close();
        return provinceName;
    }
    /**
     * 根据省，查询该省所有市的名称
     * @param provinceName
     * @return
     */
    public ArrayList<String> getCities(String provinceName){
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = db.query(AddressSQLiteOpenHelper.ADDRESS_CITY_NAME, new String[]{"name"}, "province_name=?", new String[]{provinceName}, null, null, null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(name);
        }
        cursor.close();
        return list;
    }
    /**
     * 根据市名，查询市的id
     * @param city
     * @return
     */
    public int getCityId(String city){
        Cursor cursor = db.query(AddressSQLiteOpenHelper.ADDRESS_CITY_NAME, new String[]{"city_id"}, "name=?", new String[]{city}, null, null, null);
        int id =-1;
        if(cursor.moveToFirst())
            id = cursor.getInt(cursor.getColumnIndex("city_id"));
        cursor.close();
        return id;
    }
    /**
     * 根据市id，查询市名
     * @param id
     * @return
     */
    public String getCityName(String id){
        Cursor cursor = db.query(AddressSQLiteOpenHelper.ADDRESS_CITY_NAME, new String[]{"name"}, "city_id=?",new String[]{id}, null, null, null);
        String[] columnNames = cursor.getColumnNames();
        String cityName = "";
        if(cursor.moveToFirst())
            cityName = cursor.getString(cursor.getColumnIndex("name"));
        cursor.close();
        return cityName;
    }
    /**
     * 根据市名，查询其下的所有县的名称
     * @param cityName
     * @return
     */
    public ArrayList<String> getCounties(String cityName){
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = db.query(AddressSQLiteOpenHelper.ADDRESS_COUNTY_NAME, new String[]{"name"}, "city_name=?", new String[]{cityName}, null, null, null);
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(name);
        }
        cursor.close();
        return list;
    }

    /**
     * 关闭数据库，释放资源
     */
    public void close(){
        db.close();
    }
}
