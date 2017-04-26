package com.mobisoft.mbswebplugin.Voide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.Voide.util.DataTools;
import com.mobisoft.mbswebplugin.Voide.util.MediaTools;
import com.mobisoft.mbswebplugin.Voide.util.MyUtil;
import com.mobisoft.mbswebplugin.Voide.util.StringUtil;
import com.mobisoft.mbswebplugin.utils.NetworkUtils;
import com.mobisoft.mbswebplugin.utils.ToastUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 播放视频
 */
public class EduMediaPlayer extends Activity implements MediaListerHelper.CompletionCallBack, OnClickListener {


    private SurfaceView mSurfacesView;
    private MediaPlayer mMediaPlayer;
    private RelativeLayout mediaGroup;
    private ImageView play_control;
    private TextView text_currentTime;
    private TextView text_playerLength;
    private SeekBar seekbar_progress;
    private TextView TipsView;
    private View playController;
    private View titleController;
    private ImageView location;
    private ProgressBar probar;
    private ImageView ImgLock;
    private LinearLayout layoutRight;
    private ImageView ImgCheck;


    private EduMediaPlayer mContext;

    private String path1 = "https://s3.cn-north-1.amazonaws.com.cn/nikedev/course/7FF054F587E9/jiechu.mp4";

    private String https = "https://s3.cn-north-1.amazonaws.com.cn/nikedev/course/59C17E0323C4/yundongyuanxingxiang.mp4";

    MediaListerHelper listenerHelper;
    private List<String> pathList;
    private SurfaceOnTouchListener onTocuchListener;
    // 从上次退出时间播放
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (getIntent().getIntExtra("currentTime", 0) > 0) {
                    ToastUtil.showShortToast(EduMediaPlayer.this, "继续上次播放！");
                    mMediaPlayer.seekTo(getIntent().getIntExtra("currentTime", 0));

                    Log.e("tag", mSurfacesView.getWidth() + "==" + mSurfacesView.getHeight());
                }


            }
        }
    };


    /**
     * 是否已经准备就绪
     */
    volatile boolean proFlag = false;

    /**
     * 当前播放到了第几个视频
     */
    int position = 0;
    //返回按钮
    TextView play_back;
    //网络链接
    private String url_path = "";
    //本地文件
    private String native_path = "";
    //是否第一次播放
    private Boolean studyState;
    private int currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edumediaplayer);
        mContext = EduMediaPlayer.this;
        //获取播放连接
        url_path = getIntent().getStringExtra("url");
        native_path = getIntent().getStringExtra("filePath");
        studyState = getIntent().getBooleanExtra("studyState", true);
        currentTime = getIntent().getIntExtra("currentTime", 0);
        Log.e("playVideo", "播放地址" + url_path + "===" + native_path + "是否第一次播放" + studyState + "上次播放时常" + currentTime);


        //保持屏幕长亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        initData();
        initView();
        initMediaandSurface();
        if (studyState) {
            seekbar_progress.setVisibility(View.VISIBLE);
        } else {
            seekbar_progress.setVisibility(View.INVISIBLE);
        }


    }

    /**
     * 初始化数据
     */
    private void initData() {
        pathList = new ArrayList<String>();

        if (!TextUtils.isEmpty(native_path)) {
            pathList.add(native_path + "");

        } else {
            pathList.add(url_path + "");
        }
//		pathList.add(PPP);
    }


    public boolean isProFlag() {
        return proFlag;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("tag", mSurfacesView.getWidth() + "==" + mSurfacesView.getHeight());
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mSurfacesView = (SurfaceView) findViewById(R.id.acivity_edumedia_surface);
        mediaGroup = (RelativeLayout) findViewById(R.id.activity_edumedia_group);
        play_control = (ImageView) findViewById(R.id.activity_edumedia_imgbtn_paly);
        text_currentTime = (TextView) findViewById(R.id.activity_edumedia_text_time);
        text_playerLength = (TextView) findViewById(R.id.activity_edumedia_text_length);
        seekbar_progress = (SeekBar) findViewById(R.id.activity_edumedia_seekbar);
        TipsView = (TextView) findViewById(R.id.activity_edumedia_text_tips);
        playController = findViewById(R.id.activity_edumedia_controller);
        titleController = findViewById(R.id.activity_edumedia_titlecontroller);
        location = (ImageView) findViewById(R.id.activity_edumedia_location);
        probar = (ProgressBar) findViewById(R.id.activity_edumedia_progressbar);
        ImgLock = (ImageView) findViewById(R.id.activity_edumedia_img_lock);
        layoutRight = (LinearLayout) findViewById(R.id.activity_edumedia_layout_right);
        ImgCheck = (ImageView) findViewById(R.id.activity_edumedia_img_check);
        play_back = (TextView) findViewById(R.id.play_back);

        play_control.setOnClickListener(this);
        location.setOnClickListener(this);
        ImgLock.setOnClickListener(this);
        ImgCheck.setOnClickListener(this);
        seekbar_progress.setOnSeekBarChangeListener(new mySeekBarChangeListener());
        play_back.setOnClickListener(this);
        /**
         * 初始化控件的值
         */
        text_currentTime.setText("00:00");
        text_playerLength.setText("00:00");
    }

    public void readCourse() {
        Intent tent = new Intent("readCourse");// 广播的标签，一定要和需要接受的一致。
        double currentPosition = mMediaPlayer.getCurrentPosition();
        double ma = currentPosition / videoLength;
        String format = new DecimalFormat("#.00").format(ma);
        tent.putExtra("totaltime", format);
        tent.putExtra("time", currentPosition);
        sendBroadcast(tent);// 发送广播
        Log.e("Broadcast", "发送时间" + mMediaPlayer.getCurrentPosition() + "百分百" + format);
    }


    surfaceCallBack surfacecallback;

    /**
     * 初始化播放器组件和画布
     */
    private void initMediaandSurface() {
        MediaTools.changeSize(this, mSurfacesView, 1);    //默认为竖屏
        mMediaPlayer = new MediaPlayer();
        mSurfacesView.getHolder().setKeepScreenOn(true);
        mSurfacesView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //设置回调
        surfacecallback = new surfaceCallBack();
        mSurfacesView.getHolder().addCallback(surfacecallback);
        //实例化mediaplayer的回调帮助类
        listenerHelper = new MediaListerHelper(mContext, pathList);
        mMediaPlayer.setOnCompletionListener(listenerHelper);
        mMediaPlayer.setOnErrorListener(listenerHelper);
        mMediaPlayer.setOnSeekCompleteListener(listenerHelper);
        mMediaPlayer.setOnBufferingUpdateListener(listenerHelper);
        mMediaPlayer.setOnPreparedListener(listenerHelper);
        onTocuchListener = new SurfaceOnTouchListener(mContext, callBackListener);

        mSurfacesView.setOnTouchListener(onTocuchListener);
    }


    private SurfaceOnTouchListener.OnTouchChangeLister callBackListener = new SurfaceOnTouchListener.OnTouchChangeLister() {

        @Override
        public void touchCallBack(int action, long value) {
            switch (action) {
                case 1:    //按下
                    showController(false);
                    if (layoutRight.getVisibility() == View.VISIBLE) {
                        layoutRight.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    if (TipsView.getVisibility() == View.VISIBLE)
                        TipsView.setVisibility(View.GONE);
                    if (value != -1) {
                        mMediaPlayer.seekTo((int) value);
                        probar.setVisibility(View.VISIBLE);
                    }
                    break;
                case 5:
                    if (TipsView.getVisibility() == View.GONE)
                        TipsView.setVisibility(View.VISIBLE);
                    TipsView.setText("声音：" + value);
                    break;
                case 6:
                    if (TipsView.getVisibility() == View.GONE)
                        TipsView.setVisibility(View.VISIBLE);
                    TipsView.setText("亮度：" + value);
                    break;
//				case 4:
//					if(TipsView.getVisibility() == View.GONE)
//						TipsView.setVisibility(View.VISIBLE);
//					String currentStr = StringUtil.simpleTime(value);
//					String maxStr =  StringUtil.simpleTime(videoLength);
//					TipsView.setText("进度："+currentStr+"/"+maxStr);
//					break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取当前播放器时间
     *
     * @return
     */
    public long getCurrentTime() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }


    /**
     * 画布创建完成的回调
     *
     * @author yantao
     */

    private class surfaceCallBack implements Callback {

        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
                                   int arg3) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder sufaceholder) {
            //创建画布完成，开始播放视频
            startPlayMedia();
            System.out.println("---------------surfaceCreated");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder sufaceholder) {
            if (mMediaPlayer != null) {
                //如果画布被销毁，把播放器停止
                mMediaPlayer.stop();
                System.out.println("---------------surfaceDestroyed");
            }
        }
    }


    private class mySeekBarChangeListener implements OnSeekBarChangeListener {

        boolean flag = false;

        @Override
        public void onProgressChanged(SeekBar seekbar, int position, boolean isPerson) {
            if (mMediaPlayer != null && proFlag) {
                this.flag = isPerson;
                if (isPerson) {
                    probar.setVisibility(View.VISIBLE);
                    long length = getVideoLength();
                    double percent = position / (double) seekbar.getMax();
                    int value = (int) (length * percent);
                    mMediaPlayer.seekTo(value);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar arg0) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar arg0) {

        }

    }

    /**
     * e
     * 显隐视图开始
     *
     * @params 是否是当前所在容器的操作
     */
    private void showController(boolean flag) {
        if (flag) {
            viewHandler.removeMessages(2);
            viewHandler.sendEmptyMessage(1);
            viewHandler.sendEmptyMessageDelayed(2, 5000);
        } else {
            if (playController.getVisibility() == View.GONE) {
                viewHandler.removeMessages(2);
                viewHandler.sendEmptyMessage(1);
                viewHandler.sendEmptyMessageDelayed(2, 5000);
            } else {
                viewHandler.sendEmptyMessage(2);
            }
        }
    }

    private Handler viewHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (playController.getVisibility() == View.GONE) {
                    playController.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.bottom_in));
                    titleController.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.top_in));
                    titleController.setVisibility(View.VISIBLE);
                    playController.setVisibility(View.VISIBLE);
                }
            } else if (msg.what == 2) {
                if (playController.getVisibility() == View.VISIBLE) {
                    playController.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.bottom_out));
                    playController.setVisibility(View.GONE);
                    titleController.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.top_out));
                    titleController.setVisibility(View.GONE);
                }
            }
        }

    };
    /**
     * 显隐视图结束
     */


    /**
     * 播放视频
     */
    private void startPlayMedia() {
        try {
            showController(true);
            boolean netflag = NetworkUtils.isNetworkConnected(mContext);
            if (netflag) {
                if (position < pathList.size()) {
//					Toast.makeText(mContext, "开始播放第"+ (position+1) +"个！", Toast.LENGTH_SHORT).show();
                    String urlPath = pathList.get(position);
                    mMediaPlayer.reset();        //idle
                    mMediaPlayer.setDataSource(urlPath);    //初始化状态
                    mMediaPlayer.setDisplay(mSurfacesView.getHolder());
                    mMediaPlayer.prepareAsync();// 进行缓冲处理(异步的方式去缓冲) prepare()方法阻塞主线程
                    probar.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(mContext, "播放结束！", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(mContext, "当前网络不可用！", Toast.LENGTH_LONG).show();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("报错：", ex.getMessage() + ";" + ex.getClass());
            Toast.makeText(mContext, "加载视频失败！", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("报错：", e.getMessage() + ";" + e.getClass());
            Toast.makeText(mContext, "加载视频失败！", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        if (isOnPause && mMediaPlayer != null) {
            startPlayMedia();
        } else if (isOnPause && mMediaPlayer == null) {
            initMediaandSurface();
        }
        isOnPause = false;
        super.onResume();
    }

    boolean isOnPause = false;
    //断点播放得位置
    long lastPosition = 0;

    @Override
    protected void onPause() {
        readCourse();
        if (mMediaPlayer != null) {
            lastPosition = mMediaPlayer.getCurrentPosition();
            isOnPause = true;
            mMediaPlayer.release();    //处于End状态
            mMediaPlayer = null;
        }

        super.onPause();
    }


    @Override
    protected void onDestroy() {
        if (mMediaPlayer != null) {
            System.out.println("---------------onDestroy");
            mHandler = null;
            mMediaPlayer.release();    //处于End状态
            mMediaPlayer = null;
        }

        super.onDestroy();
    }


    @Override
    public void playNext() {
        proFlag = false;
        position++;
        startPlayMedia();
    }

    long videoLength = 0;

    @Override
    public void OnPrepared(MediaPlayer mediaPlaer) {
        if (mMediaPlayer == null)
            this.mMediaPlayer = mediaPlaer;
        proFlag = true;
        try {
            //显示视频的总长度
            videoLength = mMediaPlayer.getDuration();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Message m = new Message();
        m.what = 1;
        handler.sendMessage(m);

        String str = StringUtil.simpleTime(videoLength);
        text_playerLength.setText(str);
        onTocuchListener.setMaxLength(videoLength);
        probar.setVisibility(View.GONE);
        if (lastPosition != 0) {
            mMediaPlayer.seekTo((int) lastPosition);
            //更新进度条
            double pro = lastPosition / (double) videoLength;
            int pro_cu = (int) (pro * seekbar_progress.getMax());
            seekbar_progress.setProgress(pro_cu);
            proFlag = false;
            probar.setVisibility(View.VISIBLE);
        } else {
            //开始线程请求当前的播放进度
            requestProgress();
        }
        lastPosition = 0;
    }

    public long getVideoLength() {
        return videoLength;
    }


    /**
     * 请求视频进度开始
     */
    private void requestProgress() {
        if (proFlag && mHandler != null) {
            mHandler.sendEmptyMessage(1);
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestProgress();
                    }
                }, 1000);
                if (mMediaPlayer != null && proFlag) {
                    //更新显示数字
                    long currentposition = 0;
                    try {
                        currentposition = mMediaPlayer.getCurrentPosition();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                    String text = StringUtil.simpleTime(currentposition);
                    text_currentTime.setText(text);
                    //更新进度条
                    double pro = currentposition / (double) videoLength;
                    int pro_cu = (int) (pro * seekbar_progress.getMax());
                    seekbar_progress.setProgress(pro_cu);
                }
            }
        }

    };
    /**
     * 请求视频进度结束
     */
    //是否锁定屏幕
    boolean lockFlag = false;
    boolean landeceflag = false;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.play_back) {
            finish();
        } else if (id == R.id.activity_edumedia_imgbtn_paly) {
            showController(true);
            pauseMedia(view);
        } else if (id == R.id.activity_edumedia_location) {
            landeceflag = !landeceflag;
            new MyUtil().setScreenMode(EduMediaPlayer.this, landeceflag);
        } else if (id == R.id.activity_edumedia_img_lock) {
            showController(true);
            lockFlag = !lockFlag;
            new MyUtil().setScreenLock(EduMediaPlayer.this, lockFlag);
            if (lockFlag) {
                ImgLock.setImageResource(R.drawable.locked);
            } else {
                ImgLock.setImageResource(R.drawable.lock);
            }
        } else if (id == R.id.activity_edumedia_img_check) {
            if (layoutRight.getVisibility() == View.GONE) {
                showController(false);
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                int screenWidth = wm.getDefaultDisplay().getWidth();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutRight.getLayoutParams();
                params.width = screenWidth / 2;
                layoutRight.setVisibility(View.VISIBLE);
            }
        }
//		switch (view.getId()) {
//			case R.id.activity_edumedia_imgbtn_paly:
//				showController(true);
//				pauseMedia(view);
//				break;
//			case R.id.activity_edumedia_location:
//				landeceflag = !landeceflag;
//				new MyUtil().setScreenMode(EduMediaPlayer.this,landeceflag);
//				break;
//			case R.id.activity_edumedia_img_lock:
//				showController(true);
//				lockFlag = ! lockFlag;
//				new MyUtil().setScreenLock(EduMediaPlayer.this,lockFlag);
//				if(lockFlag){
//					ImgLock.setImageResource(R.drawable.locked);
//				}else{
//					ImgLock.setImageResource(R.drawable.lock);
//				}
//				break;
//			case R.id.activity_edumedia_img_check:
//				if(layoutRight.getVisibility() == View.GONE){
//					showController(false);
//					WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//					int screenWidth = wm.getDefaultDisplay().getWidth();
//					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutRight.getLayoutParams();
//					params.width = screenWidth/2;
//					layoutRight.setVisibility(View.VISIBLE);
//				}
//				break;
//			default:
//				break;
//		}
    }

    /**
     * 暂停，继续
     *
     * @param view
     */
    private void pauseMedia(View view) {
        if (mMediaPlayer == null) {
            return;
        }
        if (!proFlag) {
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            play_control.setImageResource(R.drawable.btn_play);
        } else {
            mMediaPlayer.start();
            play_control.setImageResource(R.drawable.btn_pause);
        }
    }


    /**
     * 转屏检测，设置surfaceView和其父容器的宽高
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        // 检测屏幕的方向：纵向或横向
        LayoutParams params = mediaGroup.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 当前为横屏， 在此处添加额外的处理代码
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            MediaTools.changeSize(EduMediaPlayer.this, mSurfacesView, 0);
            if (layoutRight.getVisibility() == View.VISIBLE) {//如果浮动层显示，就隐藏
                layoutRight.setVisibility(View.GONE);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 当前为竖屏， 在此处添加额外的处理代码
            int dp2px = DataTools.dip2px(EduMediaPlayer.this, 300);
            params.height = dp2px;
            MediaTools.changeSize(EduMediaPlayer.this, mSurfacesView, 1);
            if (layoutRight.getVisibility() == View.VISIBLE) {    //如果浮动层显示，就隐藏
                layoutRight.setVisibility(View.GONE);
            }
        }
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void OnSeekCommit() {
        probar.setVisibility(View.GONE);
        if (!proFlag) {
            proFlag = true;
            //开始线程请求当前的播放进度
            requestProgress();
        }
    }


}