package com.mobisoft.library.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mobisoft.library.AppManager;
import com.mobisoft.library.R;
import com.mobisoft.library.eventbus.EventCenter;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Log tag
     */
    protected static String TAG_LOG = null;


    /**
     * 枚举各种类型
     * overridePendingTransition mode
     */
    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.out_to_right);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.out_to_left);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
        super.onCreate(savedInstanceState);
        initSystemBar(this, getStatusBarTintResource(0));

        //注册EventBus
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }
        // log tag
        TAG_LOG = this.getClass().getSimpleName();

        AppManager.getAppManager().addActivity(this);

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        initContentView();
    }


    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            onEventComming(eventCenter);
        }
    }

    /**
     * when event comming
     *
     * @param eventCenter
     */
    protected abstract void onEventComming(EventCenter eventCenter);

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();

    /**
     * init all views
     */
    protected abstract void initViews();

    /**
     * init all data
     */
    protected abstract void initData();

    /**
     * init all events
     */
    protected abstract void initEvents();

    /**
     * toggle overridePendingTransition
     * 是否开启 启动动画
     *
     * @return false ：不开启转场动画， true：开启转场动画
     */
    protected abstract boolean toggleOverridePendingTransition();

    /**
     * get the overridePendingTransition mode
     * 动画效果
     */
    protected abstract TransitionMode getOverridePendingTransitionMode();

    /**
     * is bind eventBus
     * 是否绑定EVentBus
     *
     * @return false ：不绑定EVentBus， true：绑定EVentBus
     */
    protected abstract boolean isBindEventBusHere();

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getStatusBarTintResource(int color);


    protected void initContentView() {
        initViews();
        initData();
        initEvents();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

//	@Override
//	protected void onResume() {
//		super.onResume();
//	}


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
        Log.e(TAG_LOG,this.getClass().getName() + "  被销毁啦!");
    }

    @Override
    public void finish() {
        super.finish();
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }

    /**
     * 打开至指定的Activity
     *
     * @param pClass
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 打开至指定的Activity
     *
     * @param pClass
     * @param pBundle 传值
     */
    public void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * 打开activity
     *
     * @param pAction activity动作
     */
    public void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 打开activity
     *
     * @param pAction activity动作
     * @param pBundle 数据
     */
    public void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 返回activity ，绑定数据
     *
     * @param pClass
     */
    public void returnActivity(Class<?> pClass) {
        returnActivity(pClass, null);
    }

    /**
     * 返回activity ，绑定数据
     *
     * @param pClass
     * @param pBundle
     */
    public void returnActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        //如果这个activity已经启动了，就不产生新的activity，而只是把这个activity实例加到栈顶来就可以了
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    /**
     * 返回activity
     *
     * @param pAction activity动作
     */
    public void returnActivity(String pAction) {
        returnActivity(pAction, null);
    }

    /**
     * 返回activity
     *
     * @param pAction activity动作
     * @param pBundle 数据
     */
    public void returnActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }


    /**
     * 开启状态栏
     *
     * @param activity
     * @param on
     */
    @TargetApi(19)
    private void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();

        WindowManager.LayoutParams winParams = win.getAttributes();

        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {

            winParams.flags |= bits;

        } else {

            winParams.flags &= ~bits;

        }

        win.setAttributes(winParams);

    }


    /**
     * 状态栏管理
     */
    @TargetApi(19)
    public void initSystemBar(Activity activity, int setStatusBarTintResource) {

        if (setStatusBarTintResource == 0) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            setTranslucentStatus(activity, true);

        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);

        // 激活titleBar
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // 使用颜色资源
//        tintManager.setStatusBarTintResource(setStatusBarTintResource);
//        tintManager.setNavigationBarTintResource(setStatusBarTintResource);
        tintManager.setNavigationBarTintColor(setStatusBarTintResource);
        tintManager.setStatusBarTintColor(setStatusBarTintResource);
        //Color.parseColor("#990000FF")
//		tintManager.setTintColor(Color.parseColor("#00FFFFFF"));

    }

}
