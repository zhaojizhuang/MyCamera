package com.zjz.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtils {
	/**
	 * 对图片进行旋转操作
	 * @param b
	 * @param rotateDegree
	 * @return
	 */
	public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
		Bitmap bitmap=null;
		Matrix matrix= new Matrix();
		matrix.postRotate((float)rotateDegree);
		bitmap=Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),matrix, false);
		return bitmap;
	}
}
