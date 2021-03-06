package com.zjz.camera.preview;

import com.zjz.camera.CameraInterface;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

public class CameraTextureView extends TextureView implements TextureView.SurfaceTextureListener {
	private static final String TAG = "CameraTextureView";
	Context mContext;
	SurfaceTexture mSurface;
	public CameraTextureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		this.setSurfaceTextureListener(this);
	}
	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onSurfaceTextureAvailable...");
		mSurface = surface;
//		CameraInterface.getInstance().doStartPreview(surface, 1.33f);
	}
	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onSurfaceTextureDestroyed...");
		CameraInterface.getInstance().doStopCamera();
		return true;
	}
	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onSurfaceTextureSizeChanged...");
	}
	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onSurfaceTextureUpdated...");
		
	}
	
	/* ��Activity�ܵõ�TextureView��SurfaceTexture
	 * @see android.view.TextureView#getSurfaceTexture()
	 */
	public SurfaceTexture _getSurfaceTexture(){
		return mSurface;
	}
}