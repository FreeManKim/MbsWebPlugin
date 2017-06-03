package com.mobisoft.MbsDemo;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.mobisoft.ActionTopDialog;
import com.mobisoft.MbsDemo.LitsViewActivity.ListActivity;
import com.mobisoft.MbsDemo.Loaction.KeepLiveReceiver;
import com.mobisoft.MbsDemo.Loaction.KeepLiveService;
import com.mobisoft.MbsDemo.Pull.PullWebActivity;
import com.mobisoft.MbsDemo.alipay.AuthResult;
import com.mobisoft.MbsDemo.alipay.OrderInfoUtil2_0;
import com.mobisoft.Reciver.TestReceiver;
import com.mobisoft.bannerlibrary.BGABanner;
import com.mobisoft.mbsmsgview.MBSMsgView;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebApp;
import com.mobisoft.mbswebplugin.helper.FunctionConfig;
import com.mobisoft.mbswebplugin.helper.ThemeConfig;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyBuilder;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;
import com.mobisoft.mbswebplugin.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import static com.mobisoft.MbsDemo.cmd.aliPayAuth.APPID;
import static com.mobisoft.MbsDemo.cmd.aliPayAuth.PID;
import static com.mobisoft.MbsDemo.cmd.aliPayAuth.RSA2_PRIVATE;
import static com.mobisoft.MbsDemo.cmd.aliPayAuth.RSA_PRIVATE;
import static com.mobisoft.MbsDemo.cmd.aliPayAuth.TARGET_ID;

public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_html;
    private Button btn_location;
    private KeepLiveReceiver receiver;
    private Button btn_refresh;
    private Button btn_catch;
    private Button join_us;
    private Button btn_nwe;
    private Button btn_register;
    private TestReceiver testReceiver;
//    public static final String INDEX_URL = "http://euat.idoutec.cn/HyTestDdemo/index.html";
//    public static final String INDEX_URL = "http://test.mobisoft.com.cn/cathay/index.html";
    public static final String INDEX_URL = "http://nikegcuat.mobisoft.com.cn:81/mobile/login.html";
    private Button btn_pull_sheet;
    private BGABanner banner_guide_content;
    private AppBarLayout app_bar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private Button btn_tab;
    private final int SDK_AUTH_FLAG = 2;
    private MBSMsgView tipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        initViews();
        setEvents();
//        toolbar.setBackgroundResource(R.color.colorPrimary);

//        collapsingToolbarLayout.setBackgroundResource(R.color.actionsheet_red);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                app_bar.setBackgroundColor();
                int alpha = 255 + verticalOffset;
//                Log.e("oye", "verticalOffset/:/" + verticalOffset + "//" + alpha);

//                collapsingToolbarLayout.setAlpha(alpha);
//                toolbar.setAlpha(alpha);
//                app_bar.setAlpha(alpha);
            }
        });
    }

    private void setEvents() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        banner_guide_content.setData(Arrays.asList("http://i8.download.fd.pchome.net/t_540x960/g1/M00/06/09/oYYBAFJJJaeIFh1GAAL4Tp2c4g0AAA6pAPTLhsAAvhm339.jpg",
                "http://img-download.pchome.net/download/1k0/k0/2k/o6faou-146b.jpg",
                "http://missever.com/wp-content/uploads/2016/07/1435027856_TyIjXaxW.jpg"), Arrays.asList("提示文字1", "提示文字2", "提示文字3"));
        banner_guide_content.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Picasso.with(ScrollingActivity.this)
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
                                ScrollingActivity.this, themeConfig, functionConfig)
                                .setURL(INDEX_URL)
                                .setAccount("8100458")//
                                .setNoAnimcation(false)
                                .build();
                HybridWebApp.init(coreConfig).startWebActivity(ScrollingActivity.this, com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebActivity.class);

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
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm)
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm)
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 102);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm)
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 103);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm)
                requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE}, 104);
        }

        btn_pull_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActionTopDialog(ScrollingActivity.this)
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
        btn_tab.setOnClickListener(this);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tipView.getLayoutParams();
        DisplayMetrics dm = tipView.getResources().getDisplayMetrics();
        tipView.setStrokeWidth(0);
        tipView.setText("");

        lp.width = (int) (8 * dm.density);
        lp.height = (int) (8 * dm.density);
        tipView.setLayoutParams(lp);
    }

    private void initViews() {
        btn_html = (Button) findViewById(R.id.btn_html);
        app_bar = (AppBarLayout) findViewById(R.id.app_bar);
//        toolbar_1 = (Toolbar)findViewById(R.id.toolbar_1);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        btn_location = (Button) findViewById(R.id.btn_location);
        btn_catch = (Button) findViewById(R.id.btn_down);
        join_us = (Button) findViewById(R.id.btn_join_us);
        btn_nwe = (Button) findViewById(R.id.btn_new);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_pull_sheet = (Button) findViewById(R.id.btn_pull_sheet);
        btn_tab = (Button) findViewById(R.id.btn_tab);
        banner_guide_content = (BGABanner) findViewById(R.id.banner_guide_content);
        tipView = (MBSMsgView) findViewById(R.id.hebo_msg_tip);

    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_table, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                File file = new File(ProxyConfig.getConfig().getCachePath());
                FileUtils.deleteFile(file);
                return true;

            default:
                return super.onOptionsItemSelected(item);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tab:
                Intent intent = new Intent();
                intent.setClass(ScrollingActivity.this, TableActivity.class);
                startActivity(intent);
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_AUTH_FLAG:
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(ScrollingActivity.this.getApplicationContext(),
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(ScrollingActivity.this.getApplicationContext(),
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_LONG).show();

                    }
                    break;
                default:
            }
        }
    };
    ;

    public void alipay(View view) {
        int[] a = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] b = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        TestMove.rcr(a, 5);
        TestMove.rcr(b, 5);
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(ScrollingActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();

    }
}