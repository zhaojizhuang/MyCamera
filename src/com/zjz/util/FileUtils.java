package com.zjz.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.zjz.application.MyApplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class FileUtils {
	private static final String TAG = "FileUtil";
	private static final File parentPath = Environment.getExternalStorageDirectory();
	private static String storagePath = "";
	private static final String DST_FOLDER_NAME = "PlayCamerazjz";
	/**
	 * 初始化文件保存路径
	 * @return
	 */
	private static String initPath(){
		if(storagePath.equals("")){
			storagePath = parentPath.getAbsolutePath()+"/"+DST_FOLDER_NAME;
			
			//检查文件夹是否存在，若不存在，则创新
			File file=new File(storagePath);
			if (!file.exists()) {
				file.mkdir();
			}
		}
		return storagePath;
	}
	/**保存Bitmap到sdcard
	 * 命名方式，时间.jpg
	 * @param b
	 */
	public static void saveBitmap(Bitmap b){
		//初始化文件夹
		String path=initPath();
		long dataTake = System.currentTimeMillis();
		String jpegName = path + "/" + dataTake +".jpg";
		try {
			FileOutputStream fileOutputStream=new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			Log.w(TAG, "saveBitmap成功");
			Toast.makeText(MyApplication.getContext(), "saveBitmap成功", Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w(TAG, "saveBitmap失败");
			Toast.makeText(MyApplication.getContext(), "saveBitmap失败", Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * 通知媒体更新
	 */
	public static void scanSdCard(Context context){
        String file= storagePath;
        folderScan(context,file);
    }
    //扫描通知单个文件 对媒体进行更新
    private static void fileScan(Context context,String file){
    	context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
    	Log.w(TAG, "文件："+file+"更新完毕");
    }
    //Intent.ACTION_MEDIA_SCANNER_SCAN_FILE只会扫面文件不会扫描文件夹，所以得罗列出来文件
    private static void folderScan(Context context,String path){
        File file = new File(path);
        //file存在，并且是文件夹
        if(file.exists() && file.isDirectory()){
        	//列出文件
            File[] array = file.listFiles();
            
            for(int i=0;i<array.length;i++){
                File f = array[i];
                
                if(f.isFile()){//FILE TYPE
                    String name = f.getName();
                    
                    if(name.endsWith(".mp4") || name.endsWith(".mp3") || name.endsWith(".jpg")){
                    	//通知更新单个文件
                        fileScan(context,f.getAbsolutePath());
                    }
                }
                else {//FOLDER TYPE
                	//递归
                    folderScan(context,f.getAbsolutePath());
                }
            }
        }
    }
}
