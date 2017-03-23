package com.mobisoft.MbsDemo.Loaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class KeepLiveReceiver extends BroadcastReceiver {
    public KeepLiveReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("KeepLiveReceiver",action);
        if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            // 结束
            KeepLiveManager.getInstance().startKeepLiveActivity();

        } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
            //关闭
            KeepLiveManager.getInstance().finishKeepLiveActivity();

        }

//        // TODO: This method is called when the BroadcastReceiver is receiving
//
//        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
    }


}
