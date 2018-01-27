/* 
 * Copyright (c) 2015-2020 Founder Ltd. All Rights Reserved. 
 * 
 *zhx for  org
 * 
 * 
 */

package com.bxlt.customcamera.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/**
 * 
 * 希望有一天可以开源出来 org.zhx
 * 
 * @version 1.0, 2015-11-15 下午7:21:09
 * @author zhx
 */

public class ImageUtil {

	private static final String TAG = ImageUtil.class.getSimpleName();

	/**
	 *
	 * @param
	 * @return
	 * @throws Exception
	 * @author zhx
	 */
	public static Bitmap getRectBmp(Rect rect, Bitmap bm, Point p) {
		// TODO Auto-generated method stub
		int width = rect.right - rect.left;
		int hight = rect.bottom - rect.top;
		Bitmap bitmap = resizeImage(bm, p.x, p.y);
		Log.i(TAG, hight + "@" + width + "@" + bitmap.getHeight() + "@" + bitmap.getWidth());

			Bitmap rectbitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, width,
				hight);
		return rectbitmap;
	}

	//使用Bitmap加Matrix来缩放
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		BitmapOrg.recycle();
		return resizedBitmap;
	}


	/**
	 *
	 * @param
	 * @return
	 * @throws Exception
	 * @author zhx
	 */
	public static void recycleBitmap(Bitmap bitmap) {
		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}

}
