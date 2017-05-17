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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;

import com.mobisoft.ActionTopDialog;
import com.mobisoft.MbsDemo.LitsViewActivity.ListActivity;
import com.mobisoft.MbsDemo.Loaction.KeepLiveReceiver;
import com.mobisoft.MbsDemo.Loaction.KeepLiveService;
import com.mobisoft.MbsDemo.Pull.PullWebActivity;
import com.mobisoft.Reciver.TestReceiver;
import com.mobisoft.bannerlibrary.BGABanner;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebApp;
import com.mobisoft.mbswebplugin.helper.FunctionConfig;
import com.mobisoft.mbswebplugin.helper.ThemeConfig;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyBuilder;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

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
    private Button btn_register;
    private TestReceiver testReceiver;
    public static final String INDEX_URL = "http://euat.idoutec.cn/HyTestDdemo/index.html";
    private Button btn_pull_sheet;
    private BGABanner banner_guide_content;
    private Toolbar toolbar_1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        btn_html = (Button) findViewById(R.id.btn_html);
//        toolbar_1 = (Toolbar)findViewById(R.id.toolbar_1);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        btn_location = (Button) findViewById(R.id.btn_location);
        btn_catch = (Button) findViewById(R.id.btn_down);
        join_us = (Button) findViewById(R.id.btn_join_us);
        btn_nwe = (Button) findViewById(R.id.btn_new);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_pull_sheet = (Button) findViewById(R.id.btn_pull_sheet);
        banner_guide_content = (BGABanner) findViewById(R.id.banner_guide_content);
//        setSupportActionBar(toolbar_1);
        banner_guide_content.setData(Arrays.asList("http://i8.download.fd.pchome.net/t_540x960/g1/M00/06/09/oYYBAFJJJaeIFh1GAAL4Tp2c4g0AAA6pAPTLhsAAvhm339.jpg",
                "http://img-download.pchome.net/download/1k0/k0/2k/o6faou-146b.jpg",
                "http://missever.com/wp-content/uploads/2016/07/1435027856_TyIjXaxW.jpg"), Arrays.asList("提示文字1", "提示文字2", "提示文字3"));
        banner_guide_content.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Picasso.with(NewActivity.this)
                        .load(model)
                        .placeholder(R.drawable.uoko_guide_background_1)
                        .error(R.drawable.uoko_guide_background_1)
                        .into(itemView);
            }

        });

        ViewStub viewStub = (ViewStub) findViewById(R.id.view_stup);
        viewStub.inflate();
        testReceiver = new TestReceiver();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Intent intent = new Intent("com.fxd.test.user");
                            intent.putExtra("text", "receiver");
//                            intent.addCategory(Intent.CATEGORY_DEFAULT);
// 也可以使用sendBroadcast(intent);进行发送
                            sendBroadcast(intent, "com.fxd.test.user.precession");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        btn_nwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FunctionConfig functionConfig = new FunctionConfig.Builder()
                        .setIsLeftIconShow(true)
                        .build();

                ThemeConfig.Builder builder = new ThemeConfig.Builder();
                builder.settitleBgColor(0);
                ThemeConfig themeConfig = builder.build();
                com.mobisoft.mbswebplugin.helper.CoreConfig coreConfig =
                        new com.mobisoft.mbswebplugin.helper.CoreConfig.Builder(
                                NewActivity.this, themeConfig, functionConfig)
                                .setURL(INDEX_URL)
                                .setAccount("8100458")//
                                .setNoAnimcation(false)
                                .build();
                HybridWebApp.init(coreConfig).startWebActivity(NewActivity.this, com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebActivity.class);

//                startActivity(new Intent(NewActivity.this, TableActivity.class));
            }
        });
        btn_catch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProxyBuilder.create().downCache();
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
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 104);
        }

        btn_pull_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActionTopDialog(NewActivity.this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("aaa", ActionTopDialog.SheetItemColor.Blue, new ActionTopDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                            }
                        }).show();
            }
        });
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

        IntentFilter intentFilter2 = new IntentFilter();   // 创建IntentFilter对象，指定接收什么类型的广播
        intentFilter2.addAction("com.fxd.test.user");  // 添加action
        registerReceiver(testReceiver, intentFilter2, "com.fxd.test.user.precession", null);  // 注册广播接收者

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
        if (testReceiver != null)
            unregisterReceiver(testReceiver);
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

    public void alipay(View view) {

    }
}
