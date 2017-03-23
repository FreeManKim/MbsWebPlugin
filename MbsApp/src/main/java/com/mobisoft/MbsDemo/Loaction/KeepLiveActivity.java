package com.mobisoft.MbsDemo.Loaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.mobisoft.MbsDemo.R;

/**
 * 进程保活一个像素得activity
 */
public class KeepLiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kepp_live);
        Window window = getWindow();
        window.setGravity(Gravity.LEFT |Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x=0;
        params.y=0;
        params.height=1;
        params.width=1;
        window.setAttributes(params);
        openService(KeepLiveService.class);


    }
    private void openService(Class cls) {
        startService(new Intent(this, cls));
    }
}
