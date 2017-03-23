package com.mobisoft.MbsDemo;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mobisoft.MbsDemo.LitsViewActivity.ListActivity;
import com.mobisoft.MbsDemo.Loaction.KeepLiveReceiver;
import com.mobisoft.MbsDemo.Loaction.KeepLiveService;
import com.mobisoft.MbsDemo.Pull.PullWebActivity;
import com.mobisoft.mbswebplugin.proxy.server.ProxyBuilder;

/**
 * 主页
 */
public class NewActivity extends AppCompatActivity {
    private Button btn_html;
    private Button btn_location;
    private KeepLiveReceiver receiver;
    private Button btn_refresh;
    private Button btn_catch;
    private Button join_us;
    private Button btn_nwe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        btn_html = (Button) findViewById(R.id.btn_html);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        btn_location = (Button) findViewById(R.id.btn_location);
        btn_catch = (Button) findViewById(R.id.btn_down);
        join_us = (Button) findViewById(R.id.btn_join_us);
        btn_nwe = (Button) findViewById(R.id.btn_new);
        btn_nwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_catch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProxyBuilder.create()
                        .downCache();
//               ,"http://elearning.mobisoft.com.cn/mobile/cache.manifest",NewActivity.this);
            }
        });


        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openActivity(LocationActivity.class);
                openService(KeepLiveService.class);
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(PullWebActivity.class);
            }
        });
        btn_html.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ListActivity.class);

            }
        });
        registerReceiver();
        join_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinQQGroup("tCi9qYemPhCXnWJcTOLaywAoaJlYhRcN");
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm)
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 102);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 103);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm)
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 103);
        }
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        receiver = new KeepLiveReceiver();      // 创建广播接收者对象
        IntentFilter intentFilter = new IntentFilter();   // 创建IntentFilter对象，指定接收什么类型的广播
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);  // 屏幕点亮
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF); // 屏幕熄灭
        registerReceiver(receiver, intentFilter);  // 注册广播接收者
    }

    private void openActivity(Class cls) {
        startActivity(new Intent(this, cls));
    }

    private void openService(Class cls) {
        startService(new Intent(this, cls));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    /****************
     *
     * 发起添加群流程。群号：爱你们，死gay们(619978598) 的 key 为： tCi9qYemPhCXnWJcTOLaywAoaJlYhRcN
     * 调用 joinQQGroup(tCi9qYemPhCXnWJcTOLaywAoaJlYhRcN) 即可发起手Q客户端申请加群 爱你们，死gay们(619978598)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }
}
