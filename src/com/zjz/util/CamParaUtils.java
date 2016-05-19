package com.zjz.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;

/**
 * camera属性工具类
 * @author zjz3685
 *
 */
public class CamParaUtils {
	public static final String TAG="CamParaUtils";
	/**根据设备支持的类型来选择合适的尺寸，预览尺寸和拍照尺寸
	 * 
	 * @param list 设备支持的尺寸
	 * @param th   要求的 比率-长宽
	 * @param minWidth 最小的宽度
	 * @return 返回的是Size
	 */
	public static Size getPropPreview_Picture_Size(List<Size> list,float th, int minWidth){
		//先进行排序
		Collections.sort(list, new Comparator<Size>() {

			@Override
			public int compare(Size lhs, Size rhs) {
				// TODO Auto-generated method stub
				if(lhs.width == rhs.width){
					return 0;
				}
				else if(lhs.width > rhs.width){
					return 1;
				}
				else{
					return -1;
				}
			}
		});
		for (int i = 0; i < list.size(); i++) {
			//找到第一个大于等于所给宽度，并且比率差不多的
			if ((list.get(i).width>=minWidth) && equalRate(list.get(i), minWidth)) {
				return list.get(i);
			}
		}
		return list.get(0);
		
	}
	//比较长款比率
	public static boolean equalRate(Size s, float rate){
		float r = (float)(s.width)/(float)(s.height);
		if(Math.abs(r - rate) <= 0.03)
		{
			return true;
		}
		else{
			return false;
		}
	}
	/**打印支持的previewSizes
	 * @param params
	 */
	public  static void printSupportPreviewSize(Camera.Parameters params){
		List<Size> previewSizes = params.getSupportedPreviewSizes();
		for(int i=0; i< previewSizes.size(); i++){
			Size size = previewSizes.get(i);
			Log.d(TAG, "previewSizes:width = "+size.width+" height = "+size.height);
		}
	
	}

	/**打印支持的pictureSizes
	 * @param params
	 */
	public  static void printSupportPictureSize(Camera.Parameters params){
		List<Size> pictureSizes = params.getSupportedPictureSizes();
		for(int i=0; i< pictureSizes.size(); i++){
			Size size = pictureSizes.get(i);
			Log.d(TAG, "pictureSizes:width = "+ size.width
					+" height = " + size.height);
		}
	}
	/**打印支持的聚焦模式
	 * @param params
	 */
	public static void printSupportFocusMode(Camera.Parameters params){
		List<String> focusModes = params.getSupportedFocusModes();
		for(String mode : focusModes){
			Log.d(TAG, "focusModes--" + mode);
		}
	}
}
