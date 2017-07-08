package com.mobisoft.mbswebplugin.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.mobisoft.mbswebplugin.proxy.Cache.CacheManifest.TAG;


public class FileUtils {

	public static final String ROOT_DIRECTORY = "SaicMobile";
	public static final String VERDEOCACHE_DIRECTORY = "VideoCache";
	public static final String IMGCACHE_DIRECTORY = "ImgCache";
	private static StringBuilder stringBuilder;

	/**
	 * 判断SD卡是否可用/存在
	 * 
	 * @return
	 */
	public static boolean sdCardEnable() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SD卡的路径
	 *
	 * @return
	 */
	public static String sdCardPath() {
		return android.os.Environment.getExternalStorageDirectory().getPath()+"/underwriting/";
	}

	/**
	 * 取得指定的文件夹路径
	 *
	 * @return
	 */
	public static String getSaveImgPath(Activity mActivity, String fileName) {
		StringBuffer path = new StringBuffer("");
		if (FileUtils.sdCardEnable()) {
			path.append(FileUtils.sdCardPath());
		} else {
			path.append(FileUtils.getPackagePath(mActivity));

		}

//		path.append("/");
//		path.append(ROOT_DIRECTORY);
//		path.append("/");
		path.append(fileName);
		File file = new File(path.toString());
		if(!file.exists()){
			file.mkdir();
		}
		path.append("/");
		return path.toString();
	}

	/**
	 * 取得指定的文件夹路径
	 *
	 * @return
	 */
	public static String getSaveImgPath(Context context, String fileName) {
		StringBuffer path = new StringBuffer("");
		if (FileUtils.sdCardEnable()) {
			path.append(FileUtils.sdCardPath());
		} else {
			path.append(FileUtils.getPackagePath(context));

		}

		path.append("/");
		path.append(ROOT_DIRECTORY);
		path.append("/");
		path.append(fileName);
		path.append("/");
		return path.toString();
	}

	/**
	 * 得到/data/data/projiect目录
	 *
	 * @param mActivity
	 * @return
	 */
	public static String getPackagePath(Activity mActivity) {
		return mActivity.getFilesDir().toString();
	}

	/**
	 * 得到/data/data/projiect目录
	 *
	 * @return
	 */
	public static String getPackagePath(Context context) {
		return context.getFilesDir().toString();
	}

	/**
	 * 文件路径是否存在
	 *
	 * @return
	 */
	public static boolean filePathExist(File mDirectory) {
		return mDirectory.exists();
	}

	/**
	 * 创建文件夹
	 *
	 * @param path
	 */
	public static File createDirectoryPath(String path) {
		File mDirectory = new File(path);
		if (!filePathExist(mDirectory)) {
			mDirectory.mkdirs();
		}
		return mDirectory;
	}

	/**
	 * 删除指定文件
	 *
	 * @param path
	 */
	public static boolean deleteFile(String path) {
		boolean success = false;
		File file = new File(path);
		if (filePathExist(file)) {
			if (file.isFile()) {// 是文件，删除文件
				success = file.delete();
			}
		} else {
			success = true;// 如果文件或文件夹不存在返回为成功
		}
		return success;
	}
	/**
	 * 递归删除 文件/文件夹
	 *
	 * @param file 删除的文件
	 */
	public static void deleteFile(File file) {

		Log.i(TAG, "delete file path=" + file.getAbsolutePath());

		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		} else {
			Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
		}
	}
	/**
	 * 取得文件大小
	 *
	 * @param mActivity
	 * @param fileName
	 * @return
	 */
	public static long getFileSize(Activity mActivity, String saveUrl, String fileName) {
		String fileUrl = getSaveImgPath(mActivity, saveUrl);
		long size = 0L;
		File file = new File(fileUrl + fileName);
		if (file.exists()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * 获取本地图片文件
	 *
	 * @param mActivity
	 * @param imageName
	 * @return
	 */
	public static Bitmap getBitmapFromFile(Activity mActivity, String imageName, Bitmap def) {
		Bitmap bitmap = null;
		if (imageName != null) {
			File file = null;
			String real_path = "";
			try {
//				real_path = mActivity.getFilesDir()+"/"+FileUtils.IMGCACHE_DIRECTORY;

				real_path = FileUtils.getSaveImgPath(mActivity, FileUtils.IMGCACHE_DIRECTORY);
				file = new File(real_path, imageName);
				if (file.exists())
					bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

			} catch (Exception e) {
				e.printStackTrace();
				bitmap = def;
			}
		}
		return bitmap;
	}

	/**
	 * 根据网址获取图片名称
	 *
	 * @param url
	 * @return
	 */
	public static String getImageName(String url) {
//		return url.substring(url.lastIndexOf("/") + 1) + ".jpg";

		return url.substring(url.lastIndexOf("/") + 1) + (url.endsWith(".jpg")?"":".jpg");

	}

	/**
	 * 判断文件是否存在
	 *
	 * @param filepath
	 * @return
	 */
	public static boolean isFileExists(String filepath) {
		File file = new File(filepath);
		return file.exists();
	}


	/**
	 * copy Asset 资源到 私有文件目录files下
	 *
	 * @param context
	 * @param dirname
	 *            私有文件目录files
	 * @throws IOException
	 */
	public static void copyAssetDirToFiles(Context context, String dirname) throws IOException {

		 stringBuilder = new StringBuilder();
		stringBuilder.append(Environment.getExternalStorageDirectory().getPath());
		stringBuilder.append("/");
		stringBuilder.append("DCIM");
		stringBuilder.append("/QAS");
		File dir = new File(stringBuilder + "/" + dirname);
//		File dir = new File("file:///mnt/sdcard"  + "/" + dirname);
//		File dir = new File(Environment.getExternalStorageDirectory()
//				.getPath()  + "/" + dirname);

		Log.e("HTML", "dirname: "+dirname);
		if (!dir.exists()) {
			// 文件不存在进行创建
			dir.mkdir();
		}
		// assete管理类
		AssetManager assetManager = context.getAssets();
		String[] children = assetManager.list(dirname);
		Log.e("HTML", "子文件个数: "+children.length);
		for (String child : children) {
			child = dirname + '/' + child;
			String[] grandChildren = assetManager.list(child);
			// 通过子文件的数量是否为零 来判断是否为文件夹
			if (0 == grandChildren.length)
				copyAssetFileToFiles(context, child);
			else
				copyAssetDirToFiles(context, child);
		}
	}

	/**
	 * copy 指定文件 到资源到 私有文件目录files下
	 *
	 * @param context
	 * @param filename
	 *            文件名字
	 * @throws IOException
	 */
	public static void copyAssetFileToFiles(Context context, String filename) throws IOException {
		InputStream is = context.getAssets().open(filename);
		byte[] buffer = new byte[is.available()];
		is.read(buffer);
		is.close();

		Log.i("HTML", "filename: "+filename);
		File of = new File(stringBuilder + "/" + filename);
		of.createNewFile();
		FileOutputStream os = new FileOutputStream(of);
		os.write(buffer);
		os.flush();
		os.close();
	}

	/**
	 * 获取文件夹大小
	 * @param file File实例
	 * @return long 单位为M
	 * @throws Exception
	 */
	public static long getFolderSize(java.io.File file)throws Exception {
		long size = 0;
		java.io.File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++)
		{

			if (fileList[i].isDirectory())
			{
//				Log.e("oye", String.format("文件%s:路径 %s  名称:%s  大小：%s",i,fileList[i].getAbsolutePath(),fileList[i].getPath(),
//						setFileSize(fileList[i].length())));
				size = size + getFolderSize(fileList[i]);
			} else
			{
//				Log.i("oye", String.format("文件夹%s 名称:%s 大小：%s",i,fileList[i].getName(),
//						setFileSize(fileList[i].length())));
				size = size + fileList[i].length();
			}
		}
		//1048576
		return  size;
	}

	/**
	 * 文件大小单位转换
	 *
	 * @param size
	 * @return
	 */
	public static String setFileSize(long size) {
		DecimalFormat df = new DecimalFormat("###.##");
		Log.e("FileUtils","setFileSize:"+size);
		float f = ((float) size / (float) (1024 * 1024));

		if (f < 1.0) {
			float f2 = ((float) size / (float) (1024));

			return df.format(new Float(f2).doubleValue()) + "KB";

		} else {
			return df.format(new Float(f).doubleValue()) + "M";
		}

	}

	/**
	 * 获取文件大小
	 * @param file
	 * @return
     */
	public static String getFileSize(File file){

		try {
			return  setFileSize(getFolderSize(file));
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	/**
	 * 添加水印
	 *
	 * @param context      上下文
	 * @param bitmap       原图
	 * @param markText     水印文字
	 * @param markBitmapId 水印图片
	 * @return bitmap      打了水印的图
	 */
	public static Bitmap createWatermark(Context context, Bitmap bitmap, String markText, int markBitmapId) {

		// 当水印文字与水印图片都没有的时候，返回原图
		if (TextUtils.isEmpty(markText) && markBitmapId == 0) {
			return bitmap;
		}

		// 获取图片的宽高
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();

		// 创建一个和图片一样大的背景图
		Bitmap bmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		// 画背景图
		canvas.drawBitmap(bitmap, 0, 0, null);
		//-------------开始绘制文字-------------------------------

		// 文字开始的坐标,默认为左上角
		float textX = 0;
		float textY = 0;

		if (!TextUtils.isEmpty(markText)) {
			String   markDate = getTime(new Date(System.currentTimeMillis()));

			// 创建画笔
			Paint mPaint = new Paint();
			// 文字矩阵区域
			Rect textBounds = new Rect();
			// 获取屏幕的密度，用于设置文本大小
			//float scale = context.getResources().getDisplayMetrics().density;
			// 水印的字体大小
			//mPaint.setTextSize((int) (11 * scale));
			mPaint.setTextSize(20);
			// 文字阴影
			mPaint.setShadowLayer(0.5f, 0f, 1f, Color.BLACK);
			// 抗锯齿
			mPaint.setAntiAlias(true);
			// 水印的区域
			mPaint.getTextBounds(markDate, 0, markDate.length(), textBounds);
			// 水印的颜色
			mPaint.setColor(Color.WHITE);

			//当图片大小小于文字水印大小的3倍的时候，不绘制水印
//			if (textBounds.width() > bitmapWidth / 3 || textBounds.height() > bitmapHeight / 3) {
//				markText +="\n"+ getTime(new Date(System.currentTimeMillis()));
//			}

			// 文字开始的坐标
			textX = bitmapWidth - textBounds.width() - 10;//这里的-10和下面的+6都是微调的结果
			textY = bitmapHeight - textBounds.height() + 6;
			// 画文字
			canvas.drawText(markDate, textX, textY, mPaint);

			// 创建画笔
			Paint mPaint2 = new Paint();
			// 文字矩阵区域
			Rect textBounds2 = new Rect();
			// 获取屏幕的密度，用于设置文本大小
			//float scale = context.getResources().getDisplayMetrics().density;
			// 水印的字体大小
			//mPaint.setTextSize((int) (11 * scale));
			mPaint2.setTextSize(20);
			// 文字阴影
			mPaint2.setShadowLayer(0.5f, 0f, 1f, Color.BLACK);
			// 抗锯齿
			mPaint2.setAntiAlias(true);
			// 水印的区域
			mPaint2.getTextBounds(markText, 0, markText.length(), textBounds2);
			// 水印的颜色
			mPaint2.setColor(Color.WHITE);

			// 文字开始的坐标
			textX = bitmapWidth - textBounds2.width() - 10;//这里的-10和下面的+6都是微调的结果
			textY = bitmapHeight - textBounds2.height()-textBounds.height() + 6;
			// 画文字
			canvas.drawText(markText, textX, textY, mPaint);
		}

		//------------开始绘制图片-------------------------

		if (markBitmapId != 0) {
			// 载入水印图片
			Bitmap markBitmap = BitmapFactory.decodeResource(context.getResources(), markBitmapId);

			// 如果图片的大小小于水印的3倍，就不添加水印
			if (markBitmap.getWidth() > bitmapWidth / 3 || markBitmap.getHeight() > bitmapHeight / 3) {
				return bitmap;
			}

			int markBitmapWidth = markBitmap.getWidth();
			int markBitmapHeight = markBitmap.getHeight();

			// 图片开始的坐标
			float bitmapX = (float) (bitmapWidth - markBitmapWidth - 10);//这里的-10和下面的-20都是微调的结果
			float bitmapY = (float) (textY - markBitmapHeight - 20);

			// 画图
			canvas.drawBitmap(markBitmap, bitmapX, bitmapY, null);
		}

		//保存所有元素
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();

		return bmp;
	}
	public static boolean saveBitmap2file(Bitmap bmp, String filename)
	{
		Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try
		{
			stream = new FileOutputStream(filename);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return bmp.compress(format, quality, stream);
	}

	private static String getTime(Date date) {//可根据需要自行截取数据显示
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
}
