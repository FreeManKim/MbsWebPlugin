package com.mobisoft.mbswebplugin.Voide;


import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.mobisoft.mbswebplugin.Voide.util.MyUtil;


public class SurfaceOnTouchListener implements OnTouchListener {

	float startX = 0;
	float startY = 0;
	int currentAction = 0;
	int viewWidth = 0;
	int viewHeight = 0;
	private EduMediaPlayer mContext;
	int currentVolume  = 0;
	int currentLight = 0;
	int maxVolume = 0;
	int maxLight = 100;
	long maxLength = 0;
	long currentTime = -1;
	private long currentValue = -1;

	OnTouchChangeLister callBackListener;
	public SurfaceOnTouchListener(EduMediaPlayer mContext,OnTouchChangeLister callBackListener) {
		this.mContext = mContext;
		this.callBackListener = callBackListener;
		maxVolume = MyUtil.getMaxVolume(mContext);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {

		viewWidth = view.getWidth();
		viewHeight = view.getHeight();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				currentAction = 1;
				currentValue = -1;
				callBackListener.touchCallBack(currentAction, currentValue);
				startX = event.getX();
				startY = event.getY();
				if(!mContext.isProFlag()){
					return false;
				}
				isActionFlag = false;
				currentVolume = MyUtil.getCurrentVolume(mContext);
				currentLight = MyUtil.getScreenBrightness(mContext);
				currentTime = mContext.getCurrentTime();
				maxLength = mContext.getVideoLength();
				break;
			case MotionEvent.ACTION_MOVE:
				if(!mContext.isProFlag()){
					return false;
				}
				float moveX = event.getX();
				float moveY = event.getY();
				pdAction(moveX,moveY);
				break;
			case MotionEvent.ACTION_UP:
				if(!mContext.isProFlag()){
					return false;
				}
				isActionFlag = false;
				currentAction = 3;
				startX = 0;
				startY = 0;
				callBackListener.touchCallBack(currentAction, currentValue);
				currentValue = -1;
				break;
			case MotionEvent.ACTION_CANCEL:
				if(!mContext.isProFlag()){
					return false;
				}
				isActionFlag = false;
				currentAction = 3;
				startX = 0;
				startY = 0;
				callBackListener.touchCallBack(currentAction, currentValue);
				currentValue = -1;
				break;
			default:
				break;
		}
		return true;
	}

	//最小到判断距离
	int minDistance = 10;
	//动作锁
	boolean isActionFlag = false;

	private void pdAction(float moveX,float moveY) {
		float distanceX = moveX- startX;
		float distanceY = moveY- startY;
		if(Math.abs(distanceX) < minDistance && Math.abs(distanceY) < minDistance && !isActionFlag){
			//未到达最小到判断距离
			return;
		}
		if(!isActionFlag){	//未判断动作，先判读动作
			if(Math.abs(distanceX) > Math.abs(distanceY)){	//X轴动作
				//TODO   禁用快進（屏蔽快进）
//				currentAction = 4;	//x轴动作
				isActionFlag = true;
			}else{	//Y轴动作
				isActionFlag = true;
				if(Math.abs(startX) > viewWidth/2){	//声音（右边）
					System.out.println("值大于一半");
					currentAction = 5;
				}else{	//亮度 （左边）
					currentAction = 6;
				}
			}
		}
		showActionResult( moveX, moveY,distanceX,distanceY);

	}

	/**
	 * 执行在手指滑动过程中的动作
	 */
	private void showActionResult(float moveX,float moveY,float distanceX,float distanceY) {
		if(currentAction == 4){
			//			if(distanceX > 0){
			//				System.out.println("右滑+:" + distanceX);
			//			}else{
			//				System.out.println("左滑-:" + distanceX);
			//			}
			if(maxLength == 0){
				maxLength = mContext.getVideoLength();
				if(maxLength == 0){
					return;
				}
			}
			double percentValue = viewWidth / (double)100;	//直接将视频长度分为100份
			int num = (int)(distanceX / percentValue)*1000;	//转化为毫秒数
			System.out.println(num);
			if(currentTime == -1){
				currentTime = mContext.getCurrentTime();
			}
			currentValue = currentTime + num;
			if(currentValue > maxLength){
				currentValue = maxLength;
			}else if(currentValue < 0){
				currentValue = 0;
			}
			callBackListener.touchCallBack(currentAction, (int) currentValue);
		}else if(currentAction == 5){
			if(distanceY > 0){
				System.out.println("声音-" + distanceY);
			}else{
				System.out.println("声音+" + distanceY);
			}
			int percentValue = 30;
			int num = (int) (distanceY / percentValue);
			int currentValue = currentVolume - num;	//根据距离算出最终大小
			if(maxVolume == 0)
				maxVolume = MyUtil.getMaxVolume(mContext);
			if(currentValue > maxVolume){
				currentValue = maxVolume;
			}else if(currentValue < 0){
				currentValue = 0;
			}
			MyUtil.setVolume(mContext, currentValue);
			System.out.println(currentValue);

			callBackListener.touchCallBack(currentAction, currentValue);

		}else if(currentAction == 6){
			if(distanceY > 0){
				System.out.println("亮度-" + distanceY);
			}else{
				System.out.println("亮度+" + distanceY);
			}
			//这里的算法是让每一份滑动调节5个大小的亮度
			int percentLight = 51;
			int percentValue = (int) ((double)viewHeight / percentLight);
			int num = (int) (distanceY / percentValue)*5;
			int currentValue = currentLight - num;	//根据距离算出最终大小
			if(currentValue > maxLight){
				currentValue = maxLight;
			}else if(currentValue < 0){
				currentValue = 0;
			}

			MyUtil.saveScreenBrightness(mContext, currentValue);
			callBackListener.touchCallBack(currentAction, currentValue);
		}else{
			System.out.println("不晓得");
		}
	}



	public void setMaxLength(long maxLength) {
		this.maxLength = maxLength;
	}




	/**
	 * 用来回调显示当前各种调节状态的接口
	 * @author yantao
	 *
	 */
	public interface OnTouchChangeLister{

		void touchCallBack(int action, long value);

	}

}