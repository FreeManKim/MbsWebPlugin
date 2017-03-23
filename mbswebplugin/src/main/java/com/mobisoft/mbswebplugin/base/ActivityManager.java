package com.mobisoft.mbswebplugin.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.LinkedList;

/**
 * Author：Created by fan.xd on 2017/3/14.
 * Email：fang.xd@mobisoft.com.cn
 * Description：activity 管理类，可以实现 任何线程下获取 栈顶activity
 */

public class ActivityManager implements Application.ActivityLifecycleCallbacks {
    private static ActivityManager manager = new ActivityManager();

    public static ActivityManager get() {
        return manager;
    }

    private LinkedList<Activity> stack = new LinkedList<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (!stack.contains(activity)) {
            stack.add(activity);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (stack.contains(activity)) {
            stack.remove(activity);
        }
    }

    /**
     * 获取栈顶activity
     * @return
     */
    public Activity topActivity() {
        Activity activity = null;
        if (!stack.isEmpty()) {
            activity = stack.getLast();
        }
        return activity;
    }



    /**
     * Register this to {@link Application}
     *  在 Application注册该类
     * @param context Application context
     */
    public void registerSelf(Context context) {
        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(ActivityManager.get());
    }
}
