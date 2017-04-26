package com.mobisoft.mbswebplugin.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mobisoft.mbswebplugin.dao.db.AddressDAO;
import com.mobisoft.mbswebplugin.dao.db.AddressSQLiteOpenHelper;
import com.mobisoft.mbswebplugin.utils.SharedPreferUtil;
import com.mobisoft.mbswebplugin.view.area.CharacterPickerView;
import com.mobisoft.mbswebplugin.view.area.CharacterPickerWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地址选择器
 *
 * @version 0.1 king 2015-10
 */
public class OptionsWindowHelper {

    private static List<String> options1Items = null;
    private static List<List<String>> options2Items = null;
    private static List<List<List<String>>> options3Items = null;

    public interface OnOptionsSelectListener {
        void onOptionsSelect(String province, String city, String area);
    }

    private OptionsWindowHelper() {
    }

    public static CharacterPickerWindow builder(Context context, final OnOptionsSelectListener listener) {
        //选项选择器
        CharacterPickerWindow mOptions = new CharacterPickerWindow(context);
        //初始化选项数据
        setPickerData(mOptions.getPickerView(),context);
        //设置默认选中的三级项目
        mOptions.setSelectOptions(0, 0, 0);
        //监听确定选择按钮
        mOptions.setOnoptionsSelectListener(new CharacterPickerWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                if (listener != null) {
                    String province = options1Items.get(options1);
                    String city = options2Items.get(options1).get(option2);
                    String area = options3Items.get(options1).get(option2).get(options3);
                    listener.onOptionsSelect(province, city, area);
                }
            }
        });
        return mOptions;
    }

    /**
     * 初始化选项数据
     */
    public static void setPickerData(CharacterPickerView view, Context context) {
        if (options1Items == null) {
            options1Items = new ArrayList();
            options2Items = new ArrayList<List<String>>();
            options3Items = new ArrayList();

            //    下标 String   下标 下标 String  下标 下标 下标 String
            //     0  湖南        0   0    岳阳     0   0    0   岳阳楼区
            for (int i = 0; i < AddressData.PROVINCES.length; i++) {
                List<String> shi2 = new ArrayList<>();
                List<List<String>> shi3 = new ArrayList<>();
                for (int j = 0; j < AddressData.CITIES[i].length; j++) {
                    shi2.add(j, AddressData.CITIES[i][j]);
                    List<String> qu = new ArrayList<>();
                    for (int k = 0; k < AddressData.COUNTIES[i][j].length; k++) {
                        Log.e("LLL","===I:" + i + "  J:" + j + "  K:" + k + " PROVINCES:" + AddressData.PROVINCES.length
                            + " CITIES:" + AddressData.CITIES[i].length + " COUNTIES:" + AddressData.COUNTIES[i][j].length );
                        qu.add(k, AddressData.COUNTIES[i][j][k]);
                    }
                    shi3.add(j,qu);
                }
                options1Items.add(i, AddressData.PROVINCES[i]);
                options2Items.add(i,shi2);
                options3Items.add(i,shi3);
            }

            //数据库拿数据
//            AddressDAO dao = new AddressDAO(context);
//            options1Items = dao.getProvinces();//获取所有省
//            ArrayList<String> cities;
//            ArrayList<String> counties;
//
//            for (int i = 0; i < options1Items.size(); i++) {
//                List<String> shi2 = new ArrayList<>();
//                List<List<String>> shi3 = new ArrayList<>();
//                cities = dao.getCities(options1Items.get(i));//获取某省下的所有市
//                for (int j = 0; j < cities.size(); j++) {
//                    shi2.add(j,cities.get(j));
//                    List<String> qu = new ArrayList<>();
//                    counties = dao.getCounties(cities.get(j));//获取某市下的所有县
//                    for (int k = 0; k < counties.size(); k++) {
//                        qu.add(k,counties.get(k));
//                    }
//                    shi3.add(j,qu);
//                }
//                options2Items.add(i,shi2);
//                options3Items.add(i,shi3);
//            }
//            dao.close();

        }

        //三级联动效果
        view.setPicker(options1Items, options2Items, options3Items);

    }

    private ArrayList<String> provinces;
    private ArrayList<String> cities;
    private ArrayList<String> counties;
    private int lastPosition = -1;

    /**
     * 省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 县
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * 当前省
     */
    protected String mCurrentProviceName = "";
    /**
     * 当前市
     */
    protected String mCurrentCityName = "";
    /**
     * 当前县
     */
    protected String mCurrentDistrictName = "";
    protected void initProvinceDatas(Context context) {
        AddressDAO dao = new AddressDAO(context);
        provinces = dao.getProvinces();//获取所有省
        mProvinceDatas = new String[provinces.size()];
        if (provinces != null && !provinces.isEmpty()) {
            //初始化第一条省市县
            mCurrentProviceName = provinces.get(0);
            ArrayList<String> cities = dao.getCities(provinces.get(0));
            if (cities != null && !cities.isEmpty()) {
                mCurrentCityName = cities.get(0);
                mCurrentDistrictName = dao.getCounties(dao.getCities(provinces.get(0)).get(0)).get(0);
            }
        }
        for (int i = 0; i < provinces.size(); i++) {
            String s1 = provinces.get(i);
            mProvinceDatas[i] = s1;
            cities = dao.getCities(s1);//获取某省下的所有市
            String[] citiesDatas = new String[cities.size()];
            for (int j = 0; j < cities.size(); j++) {
                String s2 = cities.get(j);
                citiesDatas[j] = s2;
                counties = dao.getCounties(s2);//获取某市下的所有县
                String[] countiesDatas = new String[counties.size()];
                for (int k = 0; k < counties.size(); k++) {
                    countiesDatas[k] = counties.get(k);
                }
                mDistrictDatasMap.put(s2, countiesDatas);
            }
            mCitisDatasMap.put(s1, citiesDatas);
        }
        dao.close();
    }

    /**
     * 建立数据库，创建省表/市表/县表，并添加好数据
     */
    public static void createAddressTable(Context context) {
        AddressDAO dao = new AddressDAO(context);
        SQLiteDatabase db = dao.getDb();
        db.beginTransaction();
        long l1 = System.currentTimeMillis();
        try {
            InputStream is1 = context.getAssets().open("province.txt");
            InputStream is2 = context.getAssets().open("city.txt");
            InputStream is3 = context.getAssets().open("county.txt");
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
            BufferedReader br3 = new BufferedReader(new InputStreamReader(is3));
            String s1 = null;
            String s2 = null;
            String s3 = null;
            Log.i("LLL","inputStream :" + is1.read());
            Log.i("LLL","BufferedReader :" +br1);
            Log.i("LLL","BufferedReader :" +br1.toString());
            Log.i("LLL","BufferedReader :" +new String(br1.readLine()));
            //省表
            while ((s1 = br1.readLine()) != null) {
                String[] split = s1.split("=");
                dao.addProvince(split[1], Integer.parseInt(split[0]));
            }
            //市表
            while ((s2 = br2.readLine()) != null) {
                String[] split = s2.split("=");
                dao.addCity(split[1], Integer.parseInt(split[0]), split[2]);
            }
            //县表
            while ((s3 = br3.readLine()) != null) {
                String[] split = s3.split("=");
                dao.addCounty(split[1], Integer.parseInt(split[0]), split[2]);
            }
            SharedPreferUtil.getInstance(context).setPrefBoolean(AddressSQLiteOpenHelper.ADDRESS_TABLE,true);
            db.setTransactionSuccessful();
            Log.i("LLL","===地址表初始化完毕!");
//            CommonUtils.getHandler().post(new Runnable() {
//                @Override
//                public void run() {
////                    SysoUtils.syso("地址表初始化完毕");
//                }
//            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 3. 结束事务
            db.endTransaction();
            long l2 = System.currentTimeMillis();
            long l = l2 - l1;
//            SysoUtils.syso("初始化数据库用时：" + l);
            dao.close();
        }
    }

}
