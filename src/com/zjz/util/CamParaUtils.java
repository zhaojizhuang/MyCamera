package com.zjz.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;

/**
 * camera���Թ�����
 * @author zjz3685
 *
 */
public class CamParaUtils {
	public static final String TAG="CamParaUtils";
	/**�����豸֧�ֵ�������ѡ����ʵĳߴ磬Ԥ���ߴ�����ճߴ�
	 * 
	 * @param list �豸֧�ֵĳߴ�
	 * @param th   Ҫ��� ����-����
	 * @param minWidth ��С�Ŀ��
	 * @return ���ص���Size
	 */
	public static Size getPropPreview_Picture_Size(List<Size> list,float th, int minWidth){
		//�Ƚ�������
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
			//�ҵ���һ�����ڵ���������ȣ����ұ��ʲ���
			if ((list.get(i).width>=minWidth) && equalRate(list.get(i), minWidth)) {
				return list.get(i);
			}
		}
		return list.get(0);
		
	}
	//�Ƚϳ������
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
	/**��ӡ֧�ֵ�previewSizes
	 * @param params
	 */
	public  static void printSupportPreviewSize(Camera.Parameters params){
		List<Size> previewSizes = params.getSupportedPreviewSizes();
		for(int i=0; i< previewSizes.size(); i++){
			Size size = previewSizes.get(i);
			Log.d(TAG, "previewSizes:width = "+size.width+" height = "+size.height);
		}
	
	}

	/**��ӡ֧�ֵ�pictureSizes
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
	/**��ӡ֧�ֵľ۽�ģʽ
	 * @param params
	 */
	public static void printSupportFocusMode(Camera.Parameters params){
		List<String> focusModes = params.getSupportedFocusModes();
		for(String mode : focusModes){
			Log.d(TAG, "focusModes--" + mode);
		}
	}
}
