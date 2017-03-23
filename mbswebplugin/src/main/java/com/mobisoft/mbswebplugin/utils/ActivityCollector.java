package com.mobisoft.mbswebplugin.utils;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * activity 集合
 * @author Li Yong
 * @date 2016年6月16日 上午22:15:03
 * @version V1.0
 * @Description: TODO
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();
    private static boolean isFinish = false;

    /**
     * 添加Activity
     * @param activity
     */
    public static void addActivity(Activity activity){
        activities.add(activity);
        Log.i("LLL","activities= size = " + activities.size());
    }

    /**
     * 移除Activity
     * @param activity
     */
    public static void removeActivity(Activity activity){
        if(activities.contains(activity)) activities.remove(activity);

    }
    /**
     * 移除Activity
     * @param index
     */
    public static void clearTask(int index){
        for (int i = 0; i <activities.size() ; i++) {
            for (int j = 1; j <= index; j++) {
                if(i==j){
                    activities.get(activities.size()-1-i).finish();
                    activities.remove(activities.size()-1-i);
                }
            }
        }
        Log.i("LLL","activities= clearTask size = " + activities.size());
    }
    /**
     * 销毁全被Activity
     */
    public static void finishAll(){  

        if(!isFinish){
            isFinish= true;
            for(Activity activity : activities){
                if(!activity.isFinishing()){
                    activity.finish();
                }
            }
            activities.clear();
            isFinish = false;
        }



        Log.e("finishAll","控制 销毁@！");
//        System.exit(0);
    }

    /**
     * 销毁Activity,除了第一个
     */
    public static void goHomePage(){
        Activity homeActivity = activities.get(0);
        Log.i("LLL","activities= size = " + activities.size());
        activities.remove(0);
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
        activities.add(homeActivity);
//        System.exit(0);
    }
}  