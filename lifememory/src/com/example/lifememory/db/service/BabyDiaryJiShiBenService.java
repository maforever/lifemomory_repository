package com.example.lifememory.db.service;

import java.util.ArrayList;
import java.util.List;

import com.example.lifememory.activity.model.BabyJiShiBen;
import com.example.lifememory.activity.model.PregnancyJiShiBen;
import com.example.lifememory.db.PregnancyDiaryOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class BabyDiaryJiShiBenService {
	private SQLiteDatabase db = null;

	public BabyDiaryJiShiBenService(Context context) {
		PregnancyDiaryOpenHelper helper = new PregnancyDiaryOpenHelper(context);
		db = helper.getWritableDatabase();
	}

	/*
	 * ������ڼ��±��ռ� int id; String title; String content; int textcolorindex; int
	 * textsizeindex; String date; int imageid;
	 */
	public void addPregnancyDiaryJiShiBen(BabyJiShiBen item) {
		db.execSQL(
				"insert into baby_jishiben (title, content, textcolorindex, textsizeindex, createdate, createymd, createym, ismodyfied, imageid) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new String[] { item.getTitle(), item.getContent(),
						String.valueOf(item.getTextColorIndex()),
						String.valueOf(item.getTextSizeIndex()),
						item.getCreateDate(), item.getCreateymd(),
						item.getCreateym(),
						String.valueOf(item.getIsModified()),
						String.valueOf(item.getImageId()) });
	}
	
	//����createdate�޸�����
	public void updateItemByCreateDate(BabyJiShiBen item) {
		db.execSQL("update baby_jishiben set title = ?, content = ?, textcolorindex = ?, textsizeindex = ? where createdate = ?", new String[]{item.getTitle(), item.getContent(), 
				String.valueOf(item.getTextColorIndex()), String.valueOf(item.getTextSizeIndex()), item.getCreateDate()});
	}

	// ��ʱ�併��������м��±��ռ�
	public ArrayList<BabyJiShiBen> listAll() {
		ArrayList<BabyJiShiBen> jishibenItems = new ArrayList<BabyJiShiBen>();
		BabyJiShiBen item = null;
		Cursor cursor = db.rawQuery(
				"select * from baby_jishiben order by createdate desc", null);
		while (cursor.moveToNext()) {
			item = new BabyJiShiBen();
			item.setIdx(cursor.getInt(cursor.getColumnIndex("idx")));
			item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			item.setContent(cursor.getString(cursor.getColumnIndex("content")));
			item.setTextColorIndex(cursor.getInt(cursor
					.getColumnIndex("textcolorindex")));
			item.setTextSizeIndex(cursor.getInt(cursor
					.getColumnIndex("textsizeindex")));
			item.setCreateDate(cursor.getString(cursor
					.getColumnIndex("createdate")));
			item.setImageId(cursor.getInt(cursor.getColumnIndex("imageid")));
			item.setIsModified(cursor.getInt(cursor
					.getColumnIndex("ismodyfied")));
			jishibenItems.add(item);
		}
		return jishibenItems;
	}

	// �޸ļ��±��ռ�
	/**
	 * int id; String title; String content; int textcolorindex; int
	 * textsizeindex; String date; int imageid;
	 * 
	 * @param item
	 */
	public void updateItem(BabyJiShiBen item) {
		db.execSQL(
				"update baby_jishiben set  content = ? , textcolorindex = ? , textsizeindex = ?, updatedate = ?, updateymd = ?, updateym = ?, ismodyfied = 1 where idx = ?",
				new String[] { item.getContent(),
						String.valueOf(item.getTextColorIndex()),
						String.valueOf(item.getTextSizeIndex()),
						item.getUpdateDate(), item.getUpdateymd(),
						item.getUpdateym(),
						String.valueOf(item.getIdx()) });
	}

	// ���Ҽ��±�
	public BabyJiShiBen findItemById(int id) {
		BabyJiShiBen item = new BabyJiShiBen();
		Cursor cursor = db.rawQuery(
				"select * from baby_jishiben where idx = ?",
				new String[] { String.valueOf(id) });
		cursor.moveToFirst();
		item.setIdx(cursor.getInt(cursor.getColumnIndex("idx")));
		item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
		item.setContent(cursor.getString(cursor.getColumnIndex("content")));
		item.setTextColorIndex(cursor.getInt(cursor
				.getColumnIndex("textcolorindex")));
		item.setTextSizeIndex(cursor.getInt(cursor
				.getColumnIndex("textsizeindex")));
		item.setCreateDate(cursor.getString(cursor.getColumnIndex("createdate")));
		item.setImageId(cursor.getInt(cursor.getColumnIndex("imageid")));
		return item;
	}

	// ����idxɾ�����±�
	public void deleteItemById(int idx) {
		db.execSQL("delete  from diary_jishiben where idx = ?",
				new String[] { String.valueOf(idx) });
	}

	// ����idx�޸ı���
	public void updateTitleById(int idx, String title, String updateDate,
			String updateYmd) {
		db.execSQL(
				"update baby_jishiben set title = ?, updatedate = ?, updateymd = ?, ismodyfied = 1 where idx = ?",
				new String[] { title, updateDate, updateYmd,
						String.valueOf(idx) });
	}



	// ���ҽ��촴�����ռǱ�
	public ArrayList<BabyJiShiBen> getTodayItems(String todayYmd) {
		ArrayList<BabyJiShiBen> jishibenItems = new ArrayList<BabyJiShiBen>();
		BabyJiShiBen item = null;
		Cursor cursor = db.rawQuery(
				"select * from baby_jishiben where  createymd = ? or updateymd = ? ",
				new String[] { todayYmd, todayYmd });
		while (cursor.moveToNext()) {
			item = new BabyJiShiBen();
			item.setIdx(cursor.getInt(cursor.getColumnIndex("idx")));
			item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			item.setContent(cursor.getString(cursor.getColumnIndex("content")));
			item.setTextColorIndex(cursor.getInt(cursor
					.getColumnIndex("textcolorindex")));
			item.setTextSizeIndex(cursor.getInt(cursor
					.getColumnIndex("textsizeindex")));
			item.setCreateDate(cursor.getString(cursor
					.getColumnIndex("createdate")));
			item.setImageId(cursor.getInt(cursor.getColumnIndex("imageid")));
			item.setIsModified(cursor.getInt(cursor
					.getColumnIndex("ismodyfied")));
			jishibenItems.add(item);
		}
		return jishibenItems;
	}
	
	//���������·�, ������������ӵ�����
	public List<String>  getMonths(String todayym) {
		List<String> months = new ArrayList<String>();
		String month = null;
		Cursor cursor = db.rawQuery("select createym from baby_jishiben group by createym", null);
		while(cursor.moveToNext()) {
			month = cursor.getString(cursor.getColumnIndex("createym"));
			months.add(month);
		}
		return months;
	}
	
	// ��ʱ�䴴��expandablelistview��Ҫ��ʾ��group
	// ����ģ����ܣ����£�ÿ��
	// yyyy��MM��dd
	public List<BabyJiShiBen> findItemsByYMD(String todayymd) {
		 List<BabyJiShiBen> jishibenItems = new ArrayList<BabyJiShiBen>();
		 BabyJiShiBen item = null;					
		 Cursor cursor = db.rawQuery("select * from baby_jishiben  where createymd = ? order by createdate desc", new
		 String[]{todayymd});
		 while(cursor.moveToNext()) {
				item = new BabyJiShiBen();
				item.setIdx(cursor.getInt(cursor.getColumnIndex("idx")));
				item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				item.setContent(cursor.getString(cursor.getColumnIndex("content")));
				item.setCreateDate(cursor.getString(cursor
						.getColumnIndex("createdate")));
				jishibenItems.add(item);
		 }
		return jishibenItems;
	}
	
	// ��ʱ�䴴��expandablelistview��Ҫ��ʾ��group
	// ����ģ����ܣ����£�ÿ��
	// yyyy��MM��dd
	public List<BabyJiShiBen> findItemsByYM(String todayym, String todayYMD) {
		 List<BabyJiShiBen> jishibenItems = new ArrayList<BabyJiShiBen>();
		 BabyJiShiBen item = null;
		 Cursor cursor = db.rawQuery("select * from baby_jishiben  where createym = ? and createymd != ? order by createdate desc", new
		 String[]{todayym, todayYMD});
		 while(cursor.moveToNext()) {
				item = new BabyJiShiBen();
				item.setIdx(cursor.getInt(cursor.getColumnIndex("idx")));
				item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				item.setContent(cursor.getString(cursor.getColumnIndex("content")));
				item.setCreateDate(cursor.getString(cursor
						.getColumnIndex("createdate")));
				jishibenItems.add(item);
		 }
		return jishibenItems;
	}

	//����idx����ɾ������
	public void deleteItemsByIdxs(List<Integer> idxs) {
		if(idxs.size() > 0) {
			StringBuffer sb = new StringBuffer("(");
			for(Integer id : idxs) {
				sb.append(id).append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
//			Log.i("a", sb.toString());
			String sqlStr = "delete from baby_jishiben where idx in " + sb.toString();
			db.execSQL(sqlStr);
			
		}
	}
	
	
	//ɾ�������ռ�
	public void deleteAll() {
		db.execSQL("delete from baby_jishiben");
	}
	
	public void closeDB() {
		db.close();
	}
}
