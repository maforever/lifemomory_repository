package com.example.lifememory.db.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.lifememory.activity.model.BabyLuYin;
import com.example.lifememory.activity.model.PregnancyJiShiBen;
import com.example.lifememory.activity.model.PregnancyLuYin;
import com.example.lifememory.db.PregnancyDiaryOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BabyDiaryLuYinService {
	private SQLiteDatabase db = null;
	private PregnancyDiaryOpenHelper openHelper = null;
	public BabyDiaryLuYinService(Context context) {
		openHelper = new PregnancyDiaryOpenHelper(context);
		db = openHelper.getWritableDatabase();
	}
	
	//����idx����¼����Ϣ
	public BabyLuYin findItemById(int idx) {
		BabyLuYin item = new BabyLuYin();
		Cursor cursor = db.rawQuery("select * from baby_luyin where idx = ?", new String[]{String.valueOf(idx)});
		if(cursor.moveToFirst()) {
			item.setIdx(idx);
			item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			item.setAudioPath(cursor.getString(cursor.getColumnIndex("audiopath")));
			item.setCreateDate(cursor.getString(cursor.getColumnIndex("createdate")));
			item.setCreateYMD(cursor.getString(cursor.getColumnIndex("createymd")));
			item.setCreateYM(cursor.getString(cursor.getColumnIndex("createym")));
		}
		return item;
	}
	
	
	// ���ҽ��촴����¼��
	public ArrayList<BabyLuYin> getTodayItems(String todayYmd) {
		ArrayList<BabyLuYin> luyinItems = new ArrayList<BabyLuYin>();
		BabyLuYin item = null;
		Cursor cursor = db.rawQuery(
				"select * from baby_luyin where  createymd = ? order by createdate desc",
				new String[] { todayYmd});
		while (cursor.moveToNext()) {
			item = new BabyLuYin();
			item.setIdx(cursor.getInt(cursor.getColumnIndex("idx")));
			item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			item.setCreateDate(cursor.getString(cursor.getColumnIndex("createdate")));
			luyinItems.add(item);
		}
		return luyinItems;
	}
	
	//���������·�, ������������ӵ�����
	public List<String>  getMonths(String todayym) {
		List<String> months = new ArrayList<String>();
		String month = null;
		Cursor cursor = db.rawQuery("select createym from baby_luyin group by createym", null);
		while(cursor.moveToNext()) {
			month = cursor.getString(cursor.getColumnIndex("createym"));
			months.add(month);
		}
		return months;
	}
	
	// ��ʱ�䴴��expandablelistview��Ҫ��ʾ��group
	// ����ģ����ܣ����£�ÿ��
	// yyyy��MM��dd
	public List<BabyLuYin> findItemsByYM(String todayym, String todayYMD) {
		 List<BabyLuYin> luyinItems = new ArrayList<BabyLuYin>();
		 BabyLuYin item = null;
		 Cursor cursor = db.rawQuery("select * from baby_luyin  where createym = ? and createymd != ? order by createdate desc", new
		 String[]{todayym, todayYMD});
		 while(cursor.moveToNext()) {
				item = new BabyLuYin();
				item.setIdx(cursor.getInt(cursor.getColumnIndex("idx")));
				item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				item.setCreateDate(cursor.getString(cursor
						.getColumnIndex("createdate")));
				luyinItems.add(item);
		 }
		return luyinItems;
	}
	
	//����Idx�޸�¼��title
	public void updateItemByIdx(int idx, String title) {
		db.execSQL("update baby_luyin set title = ? where idx = ?", new String[]{title, String.valueOf(idx)});
	}
	
	//����Idx�˴�¼��
	public void deleteItemByIdx(int idx) {
		db.execSQL("delete from baby_luyin where idx = ?", new String[]{String.valueOf(idx)});
	}
	
	//���¼����Ϣ
	public void addLuYinItem(BabyLuYin item) {
		db.execSQL("insert into baby_luyin(title, audiopath, createdate, createymd, createym) values (?, ?, ?, ?, ?)", new String[]{item.getTitle(), item.getAudioPath(), item.getCreateDate(), item.getCreateYMD(), item.getCreateYM()});
	}
	
	//����idx����ɾ������
	public void deleteItemsByIdxs(List<Integer> idxs) {
		
		String audioPath = null;
		File file = null;
		if(idxs.size() > 0) {
			StringBuffer sb = new StringBuffer("(");
			for(Integer id : idxs) {
				sb.append(id).append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
//			Log.i("a", sb.toString());
			
			String sqlStr = "select audiopath from baby_luyin where idx in " + sb.toString();
			Cursor cursor = db.rawQuery(sqlStr, null);
			while(cursor.moveToNext()) {
				audioPath = cursor.getString(0);
				file = new File(audioPath);
				if(file.exists()) file.delete();
			}
			
			
			String sqlDelStr = "delete from baby_luyin where idx in " + sb.toString();
			db.execSQL(sqlDelStr);
			
		}
	}
	
	
	//ɾ�������ռ�
	public void deleteAll() {
		String audioPath = null;
		File file = null;
		Cursor cursor = db.rawQuery("select audiopath from baby_luyin", null);
		while(cursor.moveToNext()) {
			audioPath = cursor.getString(0);
			file = new File(audioPath);
			if(file.exists()) file.delete();
		}
		db.execSQL("delete from baby_luyin");
	}
	
	public void closeDB() {
		db.close();
	}
}
