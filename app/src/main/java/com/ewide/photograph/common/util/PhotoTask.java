package com.ewide.photograph.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;


import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * author：Taozebi
 * date：2019/4/8 11:26
 * describe：照片水印
 */

public class PhotoTask extends Thread {
	
	private String file;
	
	private Context context;


	private PhotoHintListener photoHintListener;

	public PhotoTask(Context context, String file) {
		this.file = file;
		this.context = context;
	}

	public void setPhotoHintListener(PhotoHintListener photoHintListener) {
		this.photoHintListener = photoHintListener;
	}

	@Override
	public void run() {
		BufferedOutputStream bos = null;
		Bitmap icon = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			options.inPurgeable = true;
			options.inInputShareable = true;
			BitmapFactory.decodeFile(file, options); // 此时返回bm为空
			
			float percent = options.outHeight > options.outWidth ? options.outHeight / 1920f:options.outWidth / 1920f;
			if (percent < 1) {
				percent = 1;
			}
			
			int width = (int) (options.outWidth / percent);
			int height = (int) (options.outHeight / percent);
			
			int angle = PhotoUtils.getBitmapDegree(file);
			if(angle == 90 || angle == 270){
				width = (int) (options.outHeight / percent);
				height = (int) (options.outWidth / percent);
			}
			
			icon = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

			// 初始化画布 绘制的图像到icon上
			Canvas canvas = new Canvas(icon);
			// 建立画笔
			Paint photoPaint = new Paint();
			// 获取跟清晰的图像采样
			photoPaint.setDither(true);
			// 过滤一些
			// photoPaint.setFilterBitmap(true);
			options.inJustDecodeBounds = false;
			
			FileInputStream fileInputStream = new FileInputStream(file);
//			Bitmap prePhoto = BitmapFactory.decodeFile(file); 
			Bitmap prePhoto = BitmapFactory.decodeFileDescriptor(fileInputStream.getFD(),null,options);
			fileInputStream.close();
			prePhoto = PhotoUtils.rotateBitmapByDegree(prePhoto, angle);
			if (percent > 1) {
				prePhoto = Bitmap.createScaledBitmap(prePhoto, width, height,true);
			}
			canvas.drawBitmap(prePhoto, 0, 0, photoPaint);
			if (prePhoto != null && !prePhoto.isRecycled()) {
				prePhoto.recycle();
				prePhoto = null;
				System.gc();
			}

//			 设置画笔
			Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DEV_KERN_TEXT_FLAG);
			// 字体大小
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			textPaint.setTextSize(16* dm.density);
			Typeface font = Typeface.create("宋体", Typeface.BOLD);
			textPaint.setTypeface(font);
			// 采用的颜色
			textPaint.setColor(Color.YELLOW);
			// 阴影设置
//			textPaint.setShadowLayer(3f, 1, 1, Color.DKGRAY);
//			ACache mCache = ACache.get(context);
//			String miid = "户号: "+ mCache.getAsString("miid");
//			String miid = "户号: "+ this.miid;
//			String username = "抄表员: "+ MRApplication.getInstance().getCurrentUser().getUserName();
//			SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 24小时制
//			String time = "时间: "+sdformat.format(new Date());
//			BDLocation location = MRApplication.getInstance().getCurrentBDLocation();
//			String locationStr = "地点: 未获取到位置信息";
//			if(location != null){
//				locationStr = "地点: "+ location.getLongitude()+", "+location.getLatitude();
//			}
//			Log.e(MRApplication.TAG,"水印信息:\n"+miid+"\n"+username+"\n"+time+"\n"+locationStr);
//			float textWidth1 = textPaint.measureText(username);
//			float textWidth2 = textPaint.measureText(time);
//			float textWidth3 = textPaint.measureText(locationStr);
			float textWidth = 0f;
//			if(textWidth1>textWidth2){
//				textWidth = textWidth1;
//			}else{
//				textWidth = textWidth2;
//			}
//			if(textWidth < textWidth3){
//				textWidth = textWidth3;
//			}

//			canvas.drawText(miid, width - textWidth - 20, height - 190,textPaint);
//			canvas.drawText(username, width - textWidth - 20, height - 140,textPaint);
////			textWidth = textPaint.measureText(time);
//			canvas.drawText(time, width - textWidth - 20, height - 82,textPaint);
////			textWidth = textPaint.measureText(location);
//			canvas.drawText(locationStr, width - textWidth - 20, height - 32,textPaint);

			bos = new BufferedOutputStream(new FileOutputStream(file));
			int quaility = (int) (100 / percent > 80 ? 80 : 100 / percent);
			icon.compress(CompressFormat.JPEG, quaility, bos);
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			if(photoHintListener != null){
				photoHintListener.onPhotoHintError();
			}
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
					if(photoHintListener != null){
						photoHintListener.onPhotoHintError();
					}
				}
			}
			if (icon != null && !icon.isRecycled()) {
				icon.recycle();
				icon = null;
				System.gc();
			}
			if(photoHintListener != null){
				photoHintListener.onPhotoHintSuccess();
			}
		}
	}
	
	public interface PhotoHintListener {
		
		public void onPhotoHintSuccess();

		public void onPhotoHintError();
	}
}
