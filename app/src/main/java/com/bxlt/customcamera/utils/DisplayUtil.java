package com.bxlt.customcamera.utils;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 
 * 希望有一天可以开源出来 org.zhx
 * 
 * @version 1.0, 2015-11-15 下午5:23:57
 * @author zhx
 */
public class DisplayUtil {

	private static final String TAG = DisplayUtil.class.getSimpleName();

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 
	 * @param
	 * @return int
	 * @throws Exception
	 * @author zhx
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @author zhx
	 */
	public static Point getScreenMetrics(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
		Log.i(TAG, "Screen---Width = " + w_screen + " Height = " + h_screen
				+ " densityDpi = " + dm.densityDpi);
		return new Point(w_screen, h_screen);

	}

	/**
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @author zhx
	 */
	public static float getScreenRate(Context context) {
		Point P = getScreenMetrics(context);
		float H = P.y;
		float W = P.x;
		return (H / W);
	}

	/**
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @author zhx
	 */

	public static Rect createCenterScreenRect(Context context, Rect rect) {
		int x1 = DisplayUtil.dip2px(context, rect.left);
		int y1 = DisplayUtil.dip2px(context, rect.top);
		int x2 = DisplayUtil.getScreenMetrics(context).x
				- DisplayUtil.dip2px(context, rect.right);
		int y2 = DisplayUtil.getScreenMetrics(context).y
				- DisplayUtil.dip2px(context, rect.bottom);
		Log.i(TAG, x1 + "@" + y1 + "@" + x2 + "@" + y2);
		return new Rect(x1, y1, x2, y2);
	}
}
