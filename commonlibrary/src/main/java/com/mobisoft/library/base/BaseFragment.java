package com.mobisoft.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobisoft.library.eventbus.EventCenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/***
 * base fragment
 */
public abstract class BaseFragment extends Fragment {
    /**
     * context
     */
    protected Context mContext = null;
    /**
     * Log tag
     */
    protected static String TAG_LOG = null;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG_LOG = this.getClass().getSimpleName();
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            return inflater.inflate(getContentViewLayoutID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        initViews(view);
        initEvents();
        initData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * is bind eventBus
     *
     * @return false ：不绑定EVentBus， true：绑定EVentBus
     */
    protected abstract boolean isBindEventBusHere();


    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();

    /**
     * when event comming
     *
     * @param eventCenter
     */
    protected abstract void onEventComming(EventCenter eventCenter);

    /**
     * get the support fragment manager
     *
     * @return getActivity().getSupportFragmentManager()
     */
    protected FragmentManager getSupportFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 初始化 view
     *
     * @param view
     */
    public abstract void initViews(View view);

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化事件
     */
    public abstract void initEvents();


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
        Intent intent = new Intent(getActivity(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
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
        Intent intent = new Intent(getActivity(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
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

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            onEventComming(eventCenter);
        }
    }
}
