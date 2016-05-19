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
	 * ��ʼ���ļ�����·��
	 * @return
	 */
	private static String initPath(){
		if(storagePath.equals("")){
			storagePath = parentPath.getAbsolutePath()+"/"+DST_FOLDER_NAME;
			
			//����ļ����Ƿ���ڣ��������ڣ�����
			File file=new File(storagePath);
			if (!file.exists()) {
				file.mkdir();
			}
		}
		return storagePath;
	}
	/**����Bitmap��sdcard
	 * ������ʽ��ʱ��.jpg
	 * @param b
	 */
	public static void saveBitmap(Bitmap b){
		//��ʼ���ļ���
		String path=initPath();
		long dataTake = System.currentTimeMillis();
		String jpegName = path + "/" + dataTake +".jpg";
		try {
			FileOutputStream fileOutputStream=new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			Log.w(TAG, "saveBitmap�ɹ�");
			Toast.makeText(MyApplication.getContext(), "saveBitmap�ɹ�", Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w(TAG, "saveBitmapʧ��");
			Toast.makeText(MyApplication.getContext(), "saveBitmapʧ��", Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * ֪ͨý�����
	 */
	public static void scanSdCard(Context context){
        String file= storagePath;
        folderScan(context,file);
    }
    //ɨ��֪ͨ�����ļ� ��ý����и���
    private static void fileScan(Context context,String file){
    	context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
    	Log.w(TAG, "�ļ���"+file+"�������");
    }
    //Intent.ACTION_MEDIA_SCANNER_SCAN_FILEֻ��ɨ���ļ�����ɨ���ļ��У����Ե����г����ļ�
    private static void folderScan(Context context,String path){
        File file = new File(path);
        //file���ڣ��������ļ���
        if(file.exists() && file.isDirectory()){
        	//�г��ļ�
            File[] array = file.listFiles();
            
            for(int i=0;i<array.length;i++){
                File f = array[i];
                
                if(f.isFile()){//FILE TYPE
                    String name = f.getName();
                    
                    if(name.endsWith(".mp4") || name.endsWith(".mp3") || name.endsWith(".jpg")){
                    	//֪ͨ���µ����ļ�
                        fileScan(context,f.getAbsolutePath());
                    }
                }
                else {//FOLDER TYPE
                	//�ݹ�
                    folderScan(context,f.getAbsolutePath());
                }
            }
        }
    }
}
