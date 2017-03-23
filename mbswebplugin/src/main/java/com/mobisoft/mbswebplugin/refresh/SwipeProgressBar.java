package com.mobisoft.mbswebplugin.refresh;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by 仇杰 on 2015/3/30
 */
final class SwipeProgressBar {
	private static final int FILP = 0x1;
	private static final int ROTATE = 0x0;
	private static final int PULL_TO_REFRESH = 0x0;
	private static final int RELEASE_TO_REFRESH = 0x1;
	private static final int FINISHED = 0x2;
	
    private final Paint mPaint = new Paint();
    private long mStartTime;
    private long mFinishTime;
    private boolean mRunning;
    private int direction;
    
    private View mParent;

    private Rect mBounds = new Rect();
    
    private Bitmap bitmap;
    
    private Bitmap bitmapFlip;
    
    private Bitmap bitmapFinished;
    private Bitmap dstbmp = null;
    private String tips;
    private String processingTips;
    private String latestRefreshTime;
    
    private int width;
    
    private float textSize;
    private int textColor;
    private int textMarginTop;
    private int imageId;
    private int ptr_flip_drawable;
    private int finished_drawable;
    private static final int DEFAULT_DRAWABLE_MARGIN_LEFT = 30;
    private int drawable_margin_left;
    
    /**
	 * 0:rotate 1:flip
	 */
    private int type;
    
    private int state;
    
    public SwipeProgressBar(View parent, float textSize, int textColor, int imageId, int ptr_flip_drawable, int finished_drawable, int type) {
        mParent = parent;
        float density = parent.getResources().getDisplayMetrics().density;
        this.textSize = textSize;
        this.textColor = textColor;
        this.imageId = imageId;
        this.ptr_flip_drawable = ptr_flip_drawable;
        this.finished_drawable = finished_drawable;
        this.textMarginTop = (int) (5 * density);
        this.drawable_margin_left = (int) (DEFAULT_DRAWABLE_MARGIN_LEFT * density);
        this.type = type;
        getBitmap();
        getFinishedBitmap();
    }
    
    void setProperty(String text, String latestRefreshTime, int direction, int state) {
    	mStartTime = 0;
    	this.tips = text;
    	this.latestRefreshTime = latestRefreshTime;
    	this.direction = direction;
    	this.state = state;
      ViewCompat.postInvalidateOnAnimation(mParent);
    }
    
    void setState(int state) {
    	this.state = state;
    }
    
    void setWidth(int width) {
    	this.width = width;
    }
    
    void setProcessingTips(String tips) {
    	this.processingTips = tips;
    }
    
    /**
     * Start showing the progress animation.
     */
    void start() {
        if (!mRunning) {
            mStartTime = AnimationUtils.currentAnimationTimeMillis();
            mRunning = true;
            mParent.postInvalidate();
        }
    }

    /**
     * Stop showing the progress animation.
     */
    void stop() {
        if (mRunning) {
            //mFinishTime = AnimationUtils.currentAnimationTimeMillis();
            mRunning = false;
            tips = "刷新成功!";
            state = FINISHED;
            mParent.postInvalidate();
            
        }
    }

    boolean isRunning() {
        return mRunning || mFinishTime > 0;
    }

    void draw(Canvas canvas) {
//        final int width = mBounds.width();
        final int height = mBounds.height();
        
        int restoreCount = canvas.save();
        canvas.clipRect(mBounds);
        if (mRunning || (mFinishTime > 0)) {
        	if(!mRunning) {
        		mFinishTime = 0;
        		return;
        	}
        	this.tips = processingTips;
        	drawBar(canvas, height);
            // Keep running until we finish out the last cycle.
            ViewCompat.postInvalidateOnAnimation(mParent);
        } else {
        	if(height > 0) {
        		drawBar(canvas, height);
        	}
        	
        }
        if(state != FINISHED) {
        	canvas.restoreToCount(restoreCount);
        }
        
    }

    private int getFontHeight(float fontSize)
    {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    private void drawBar(Canvas canvas, int height) {
    	mPaint.setAntiAlias(true);
    	Bitmap bitmap = null;
    	if(state == FINISHED) {
    		bitmap = getFinishedBitmap();
    	} else {
    		bitmap = getBitmap();
    	}
    	
    	Matrix matrix = new Matrix();
    	matrix.setTranslate(bitmap.getWidth() >> 1, bitmap.getHeight() >> 1);
    	
    	if(mRunning) {
    		long now = AnimationUtils.currentAnimationTimeMillis();
            matrix.postRotate((now - mStartTime) % 360);  
    	} else {
    		if(type == ROTATE) {
    			if(state != FINISHED) {
    				matrix.postRotate(height % 360); 
    			}
    		} else {
    			if(direction == 0) {
    				if(state == RELEASE_TO_REFRESH) {
    					matrix.postRotate(180);
    				} 
    				
    			} else if(direction == 1) {
    				if(state == PULL_TO_REFRESH) {
    					matrix.postRotate(180);
    				}
    			}
    		}
    		 
    	}
    	
    	dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);  
    	int originalWidth = bitmap.getWidth();
    	int originalHeight = bitmap.getHeight();
    	
    	int dstWidth = dstbmp.getWidth();
    	int dstHeight = dstbmp.getHeight();
    	
    	int fontHeight = getFontHeight(textSize);
    	mPaint.setTextSize(textSize);
    	mPaint.setColor(textColor);
    	if(direction == 0) {
    		//canvas.drawBitmap(dstbmp, (width >> 1) - ((int)mPaint.measureText(latestRefreshTime) >> 1) - drawable_margin_left - ((dstWidth >> 1) - (originalWidth >> 1)), (mBounds.bottom - (originalHeight >> 1)) + ((originalHeight >> 1) - (dstHeight >> 1)) - (fontHeight << 1) , mPaint);
    		canvas.drawBitmap(dstbmp, drawable_margin_left - ((dstWidth >> 1) - (originalWidth >> 1)), (mBounds.bottom - (originalHeight >> 1)) + ((originalHeight >> 1) - (dstHeight >> 1)) - (fontHeight << 1) , mPaint);
        	mPaint.setFakeBoldText(true);
        	
        	canvas.drawText(tips, (width >> 1) - ((int)mPaint.measureText(tips) >> 1), (mBounds.bottom - (fontHeight << 1)), mPaint);
        	mPaint.setFakeBoldText(false);
        	canvas.drawText(latestRefreshTime, (width >> 1) - ((int)mPaint.measureText(latestRefreshTime) >> 1), (mBounds.bottom - fontHeight), mPaint);
    	} else {
    		float y = mBounds.top + ((textMarginTop + (fontHeight << 1)) >> 1) + textMarginTop - (dstHeight >> 1);
//    		canvas.drawBitmap(dstbmp, (width >> 1) - ((int)mPaint.measureText(latestRefreshTime) >> 1) - drawable_margin_left - ((dstWidth >> 1) - (originalWidth >> 1)), y , mPaint);
    		canvas.drawBitmap(dstbmp, drawable_margin_left - ((dstWidth >> 1) - (originalWidth >> 1)), y , mPaint);
    		mPaint.setFakeBoldText(true);
        	canvas.drawText(tips, (width >> 1) - ((int)mPaint.measureText(tips) >> 1), (mBounds.top + textMarginTop + fontHeight), mPaint);
        	mPaint.setFakeBoldText(false);
        	canvas.drawText(latestRefreshTime, (width >> 1) - ((int)mPaint.measureText(latestRefreshTime) >> 1), (mBounds.top  + (fontHeight << 1) + (textMarginTop << 1)), mPaint);
    	}
    	
    }

    /**
     * Set the drawing bounds of this SwipeProgressBar.
     */
    void setBounds(int left, int top, int right, int bottom) {
        mBounds.left = left;
        mBounds.top = top;
        mBounds.right = right;
        mBounds.bottom = bottom;
    }
    
    private Bitmap getBitmap() {
    	
    	if(type == ROTATE) {
    		if(bitmap != null && !bitmap.isRecycled()) {
        		return bitmap;
        	} else {
        		bitmap = BitmapFactory.decodeResource(mParent.getResources(), imageId);
        		return bitmap;
        	}
    	} else if(type == FILP){
    		
    		if(mRunning) {
    			if(bitmap != null && !bitmap.isRecycled()) {
            		return bitmap;
            	} else {
            		bitmap = BitmapFactory.decodeResource(mParent.getResources(), imageId);
            		return bitmap;
            	}
    			
    		} else {
    			if(bitmapFlip != null && !bitmapFlip.isRecycled()) {
            		return bitmapFlip;
            	} else {
            		bitmapFlip = BitmapFactory.decodeResource(mParent.getResources(), ptr_flip_drawable);
            		return bitmapFlip;
            	}
    		}
    		
    	}
    	
    	return null;
    }
    
    private Bitmap getFinishedBitmap() {
    	if(null != bitmapFinished && !bitmapFinished.isRecycled()) {
    		return bitmapFinished;
    	} else {
    		bitmapFinished = BitmapFactory.decodeResource(mParent.getResources(), finished_drawable);
    		return bitmapFinished;
    	}
    }
    
    void destory() {
    	if(null != bitmap && !bitmap.isRecycled()) {
    		bitmap.recycle();
    		bitmap = null;
    	}
    	if(null != bitmapFlip && !bitmapFlip.isRecycled()) {
    		bitmapFlip.recycle();
    		bitmapFlip = null;
    	}
    	if(null != bitmapFinished && !bitmapFinished.isRecycled()) {
    		bitmapFinished.recycle();
    		bitmapFinished = null;
    	}
    	
    	if(null != dstbmp && !dstbmp.isRecycled()) {
    		dstbmp.recycle();
    		dstbmp = null;
    	}
    	
    }
}