package com.example.lifememory.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class CopyFileFromData {
	public static CopyFileFromData instance = new CopyFileFromData();
	public CopyFileFromData(){};
	public static CopyFileFromData getInstance() {
		return instance;
	}
	
	public  void copyDatabase(Context context, String dbName) {
		try {
			File dbFile = context.getDatabasePath(dbName);
			String dbPath = dbFile.getPath();
			Log.i("a", "dbPath = " + dbPath);
			if(dbFile.exists()) {
				Log.i("a", "pregnancy_diary.db 存在");
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					File homeFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "点滴忆文件夹");
					if(!homeFile.exists()) 
						{
						homeFile.mkdirs();
						}
					else {
						Log.i("a", "homeFile = " + homeFile.getPath());
					}
					File dbFile2 = new File(homeFile, "pregnancy_diary.db");
					FileInputStream fis = new FileInputStream(dbFile);
					FileOutputStream fos = new FileOutputStream(dbFile2);
					int len = -1;
					byte[] buffer = new byte[1024];
					while( (len = fis.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					fis.close();
					fos.close();
				}
			}else {
				Log.i("a", "pregnancy_diary.db 不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
