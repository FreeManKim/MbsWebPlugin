package com.mobisoft.mbswebplugin.Voide;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.widget.Toast;

import java.util.List;

public class MediaListerHelper implements OnErrorListener,OnSeekCompleteListener
			,OnBufferingUpdateListener,OnCompletionListener,OnPreparedListener {

	private EduMediaPlayer mContext;

	private List<String> pathList;
	int position = 0;

	public MediaListerHelper(EduMediaPlayer mContext,List<String> pathList) {
		this.mContext = mContext;
		this.pathList = pathList;
	}

	//缓冲进度 这里的进度position指的应该就是百分比，因为我实验的时候，他最大的值就是100
	@Override
	public void onBufferingUpdate(MediaPlayer mediaPlaer, int position) {
		System.out.println("缓冲进度："+position);
	}

	//调用seekto方法完成之后，这里的完成代表的是当前播放进度已经调整到了要指定的时间，并且调整之后mediaPlayer开始播放了！
//	//Ps:这里就应该可以最加载中进度条的隐藏判断
	@Override
	public void onSeekComplete(MediaPlayer mediaPlaer) {
		mContext.OnSeekCommit();
	}

	//	出现错误的时候的监听
	@Override
	public boolean onError(MediaPlayer mediaPlaer, int i, int j) {
//		Toast.makeText(mContext, "报错了！"+i+":::::"+j, Toast.LENGTH_LONG).show();
		if(i == 1){
			Toast.makeText(mContext, "视频地址是一个不可到达的地址！",  Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(mContext, "未知错误",  Toast.LENGTH_LONG).show();
		}
		mediaPlaer.reset();
		return true;
	}

	@Override
	public void onCompletion(MediaPlayer media) {
		mContext.playNext();
	}

	//播放器已经准备就绪
	@Override
	public void onPrepared(MediaPlayer mediaPlaer) {
		if(!mediaPlaer.isPlaying())
			mediaPlaer.start();
		mContext.OnPrepared(mediaPlaer);
	}


	public interface CompletionCallBack{

		/**
		 * 视频播放完成，开始播放下一个
		 */
		void playNext();
		/**
		 * 视频流缓冲完成，开始播放
		 */
		void OnPrepared(MediaPlayer mediaPlaer);

		void OnSeekCommit();
	}


}
