package com.zjz.util;

import android.hardware.Camera;
import android.util.Log;

public class CameraUtils {
	public static final String TAG="CameraUtils";
	public static Camera openFrontFacingCameraGingerbread() {
	      int cameraCount = 0;
	      Camera cam = null;
	      Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
	      cameraCount = Camera.getNumberOfCameras();
	      
	      for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
	          Camera.getCameraInfo(camIdx, cameraInfo);
	          if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	              try {
	                  cam = Camera.open(camIdx);
	              } catch (RuntimeException e) {
	                  Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
	              }
	          }
	      }

	      return cam;
	  }
}
