package com.zjz.camera;

import java.io.IOException;
import java.util.List;



import com.zjz.util.CamParaUtils;
import com.zjz.util.ImageUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.MediaScannerConnection;
import android.util.Log;

/**
 * ������ͷ��Ԥ�������գ�ֹͣ����ͷ�ȹ���
 * @author zjz3685
 *
 */
public class CameraInterface {
	private static final String TAG ="CameraInterface";
	//����ͷ����
	private Camera mCamera;
	//����ͷ����
	private Camera.Parameters mParams;
	//�Ƿ�Ԥ��
	private boolean isPreviewing = false;
	//�Ƿ���ת
	private float mPreviwRate = -1f;
	//CameraInterface��������ã�����ģʽʵ��
	private static CameraInterface mCameraInterface;
	
	/**
	 * ����ͷ�ص��ӿڣ�����ͷ�򿪵�ʱ�����public void cameraHasOpened();
	 * @author zjz3685
	 *
	 */
	public interface CamOpenOverCallback{
		public void cameraHasOpened();
	}
	public  CameraInterface(){
		
	}
	//����ģʽ
	public static synchronized CameraInterface getInstance(){
		if (mCameraInterface==null) {
			mCameraInterface=new CameraInterface();
		}
		return mCameraInterface;
	}
	/**��Camera
	 * @param callback
	 */
	public void doOpenCamera(CamOpenOverCallback callback){
		Log.d(TAG, "Camera open....");
		//ʵ����camera
		mCamera = Camera.open();
		Log.d(TAG, "Camera open over....");
		callback.cameraHasOpened();
	}
	/**ʹ��TextureViewԤ��Camera
	 * @param surface
	 * @param previewRate
	 */
	public void doStartPreview(SurfaceTexture surface, float previewRate){
		
		Log.d(TAG, "doStartPreview...");
		//����Ѿ�����Ԥ������ֹͣԤ��
		if(isPreviewing){
			mCamera.stopPreview();
			return;
		}
		//camera��������
		if(mCamera!=null){
			try {
				//����startpreview֮ǰ����
				mCamera.setPreviewTexture(surface);
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			initCamera(previewRate);
		}
	}
	private void initCamera(float previewRate) {
		// TODO Auto-generated method stub
		//�������ͷ������ȡ
		if(mCamera != null){
			mParams = mCamera.getParameters();
			//�������պ�洢��ͼƬ��ʽ
			mParams.setPictureFormat(PixelFormat.JPEG);
			// �����豸֧�ֵĳߴ�� ���ʺ���С����С�ĳߴ����
			Size pictureSize=CamParaUtils.getPropPreview_Picture_Size(
					mParams.getSupportedPictureSizes(),
					previewRate, 
					800);
			mParams.setPictureSize(pictureSize.width, pictureSize.height);
			
			Size previewSize=CamParaUtils.getPropPreview_Picture_Size(
					mParams.getSupportedPreviewSizes(), 
					previewRate, 
					800);
			mParams.setPreviewSize(pictureSize.width, pictureSize.height);
			
			CamParaUtils.printSupportFocusMode(mParams);
			
			List<String> focusModes = mParams.getSupportedFocusModes();
			if(focusModes.contains("continuous-video")){
				mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}
			mCamera.setDisplayOrientation(90);
			
			mCamera.setParameters(mParams);	
			//����Ԥ��
			mCamera.startPreview();
			
			mPreviwRate = previewRate;

			mParams = mCamera.getParameters(); //����getһ��
			Log.d(TAG, "��������:PreviewSize--With = " + mParams.getPreviewSize().width
					+ "Height = " + mParams.getPreviewSize().height);
			Log.d(TAG, "��������:PictureSize--With = " + mParams.getPictureSize().width
					+ "Height = " + mParams.getPictureSize().height);
			
		}
	}
	/*Ϊ��ʵ�����յĿ������������ձ�����Ƭ��Ҫ���������ص�����*/
	ShutterCallback mShutterCallback =new ShutterCallback() {
		
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			Log.d(TAG, "mShutterCallback:onShutter()");
		}
	};
	PictureCallback mJpegPictureCallback =new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.d(TAG, "myJpegCallback:onPictureTaken...");
			Bitmap b = null;
			if(null != data){
				b = BitmapFactory.decodeByteArray(data, 0, data.length);//data���ֽ����ݣ����������λͼ
				mCamera.stopPreview();
				isPreviewing = false;
			}
			
			//����ͼƬ��sdcard
			if(null != b)
			{
				//����FOCUS_MODE_CONTINUOUS_VIDEO)֮��myParam.set("rotation", 90)ʧЧ��
				//ͼƬ��Ȼ������ת�ˣ�������Ҫ��ת��
				Bitmap rotaBitmap = ImageUtils.getRotateBitmap(b, 90.0f);
				FileUtil.saveBitmap(rotaBitmap);
			}
			//�ٴν���Ԥ��
			mCamera.startPreview();
			isPreviewing = true;
			
	
			
		}
	};
	
	
	
	

}
