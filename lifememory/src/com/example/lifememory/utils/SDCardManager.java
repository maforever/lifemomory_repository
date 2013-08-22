package com.example.lifememory.utils;

import java.io.File;

import android.os.Environment;
import android.util.Log;

public class SDCardManager {
	private static String homeFileName = ".recall4everything";
	public static SDCardManager instance = null;

	public SDCardManager() {
	};

	public static SDCardManager getInstance() {
		if (instance == null) {
			instance = new SDCardManager();
		}
		return instance;
	}

	public boolean getSDCardState() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public String getPregnancyRecodePath() {

		String path;
		// 判断是否存在SD卡，如果存在，就将文件夹创建在SD卡上，如果不存在，就用内置的空间
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {

			File sdDir = Environment.getExternalStorageDirectory();// 获取根目录
			path = sdDir.getPath() + File.separator + homeFileName + File.separator + "pregnancy_recode";
		} else {
			// 获取内置存储空间路径
			File sd = Environment.getRootDirectory();
			path = sd.getPath() + File.separator + homeFileName + File.separator + "pregnancy_recode";
		}

		// 如果目录存在就不创建，不存在就创建
		Log.i("a", "录音文件路径：" + path);
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

}
