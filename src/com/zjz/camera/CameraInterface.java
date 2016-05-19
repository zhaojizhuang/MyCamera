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
 * 打开摄像头，预览，拍照，停止摄像头等功能
 * @author zjz3685
 *
 */
public class CameraInterface {
	private static final String TAG ="CameraInterface";
	//摄像头对象
	private Camera mCamera;
	//摄像头属性
	private Camera.Parameters mParams;
	//是否预览
	private boolean isPreviewing = false;
	//是否旋转
	private float mPreviwRate = -1f;
	//CameraInterface对象的引用，单例模式实现
	private static CameraInterface mCameraInterface;
	
	/**
	 * 摄像头回调接口，摄像头打开的时候调用public void cameraHasOpened();
	 * @author zjz3685
	 *
	 */
	public interface CamOpenOverCallback{
		public void cameraHasOpened();
	}
	public  CameraInterface(){
		
	}
	//单例模式
	public static synchronized CameraInterface getInstance(){
		if (mCameraInterface==null) {
			mCameraInterface=new CameraInterface();
		}
		return mCameraInterface;
	}
	/**打开Camera
	 * @param callback
	 */
	public void doOpenCamera(CamOpenOverCallback callback){
		Log.d(TAG, "Camera open....");
		//实例化camera
		mCamera = Camera.open();
		Log.d(TAG, "Camera open over....");
		callback.cameraHasOpened();
	}
	/**使用TextureView预览Camera
	 * @param surface
	 * @param previewRate
	 */
	public void doStartPreview(SurfaceTexture surface, float previewRate){
		
		Log.d(TAG, "doStartPreview...");
		//如果已经开启预览，先停止预览
		if(isPreviewing){
			mCamera.stopPreview();
			return;
		}
		//camera正常开启
		if(mCamera!=null){
			try {
				//必须startpreview之前调用
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
		//如果摄像头正常获取
		if(mCamera != null){
			mParams = mCamera.getParameters();
			//设置拍照后存储的图片格式
			mParams.setPictureFormat(PixelFormat.JPEG);
			// 根据设备支持的尺寸和 比率和最小的最小的尺寸进行
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
			//开启预览
			mCamera.startPreview();
			
			mPreviwRate = previewRate;

			mParams = mCamera.getParameters(); //重新get一次
			Log.d(TAG, "最终设置:PreviewSize--With = " + mParams.getPreviewSize().width
					+ "Height = " + mParams.getPreviewSize().height);
			Log.d(TAG, "最终设置:PictureSize--With = " + mParams.getPictureSize().width
					+ "Height = " + mParams.getPictureSize().height);
			
		}
	}
	/*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
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
				b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
				mCamera.stopPreview();
				isPreviewing = false;
			}
			
			//保存图片到sdcard
			if(null != b)
			{
				//设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
				//图片竟然不能旋转了，故这里要旋转下
				Bitmap rotaBitmap = ImageUtils.getRotateBitmap(b, 90.0f);
				FileUtil.saveBitmap(rotaBitmap);
			}
			//再次进入预览
			mCamera.startPreview();
			isPreviewing = true;
			
	
			
		}
	};
	
	
	
	

}
