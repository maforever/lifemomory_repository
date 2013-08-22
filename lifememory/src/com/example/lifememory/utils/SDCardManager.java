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
		// �ж��Ƿ����SD����������ڣ��ͽ��ļ��д�����SD���ϣ���������ڣ��������õĿռ�
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {

			File sdDir = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ¼
			path = sdDir.getPath() + File.separator + homeFileName + File.separator + "pregnancy_recode";
		} else {
			// ��ȡ���ô洢�ռ�·��
			File sd = Environment.getRootDirectory();
			path = sd.getPath() + File.separator + homeFileName + File.separator + "pregnancy_recode";
		}

		// ���Ŀ¼���ھͲ������������ھʹ���
		Log.i("a", "¼���ļ�·����" + path);
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

}
