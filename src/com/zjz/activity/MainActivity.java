package com.zjz.activity;

import com.example.mycamera.R;
import com.zjz.camera.CameraInterface;
import com.zjz.camera.CameraInterface.CamOpenOverCallback;
import com.zjz.camera.preview.CameraTextureView;
import com.zjz.util.DisplayUtil;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener,CamOpenOverCallback{
	private static final String TAG = "MainActivity";
	float previewRate = -1f;
	Button takePicture ;
	CameraTextureView textureView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//在线程中开启摄像头
		Thread openThread = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				CameraInterface.getInstance().doOpenCamera(MainActivity.this);
			}
		};
		openThread.start();
		
		setContentView(R.layout.activity_main);
		initUI();
		//初始化摄像头的属性
		initViewParams();
		textureView.setAlpha(1.0f);
		takePicture.setOnClickListener(this);
	}
	private void initViewParams(){
		LayoutParams params = textureView.getLayoutParams();
		Point p = DisplayUtil.getScreenMetrics(this);
		params.width = p.x;
		params.height = p.y;
		previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
		textureView.setLayoutParams(params);

		//手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
//		LayoutParams p2 = textureView.getLayoutParams();
//		p2.width = DisplayUtil.dip2px(this, 380);
//		p2.height = DisplayUtil.dip2px(this, 380);;		
//		textureView.setLayoutParams(p2);	

	}
	private void initUI(){
		takePicture=(Button) findViewById(R.id.btn);
		textureView=(CameraTextureView) findViewById(R.id.camera_textureview);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn:
			CameraInterface.getInstance().doTakePicture();
			Log.d(TAG, "点击了拍照");
			break;

		default:
			break;
		}
	}
	@Override
	public void cameraHasOpened() {
		// TODO Auto-generated method stub
		SurfaceTexture surface = textureView._getSurfaceTexture();
		CameraInterface.getInstance().doStartPreview(surface, previewRate);
	}

}
