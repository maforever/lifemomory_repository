package com.example.lifememory.fragments;



import java.util.ArrayList;
import java.util.List;

import com.example.lifememory.R;
import com.example.lifememory.activity.IndexActivity;
import com.example.lifememory.activity.model.BabyJiShiBen;
import com.example.lifememory.activity.model.BabyJiShiBenListViewExpandableGroupItem;
import com.example.lifememory.activity.model.BabyLuYin;
import com.example.lifememory.activity.model.PregnancyLuYin;
import com.example.lifememory.activity.model.PrenancyJiShiBenGridViewExpandableGroupItem;
import com.example.lifememory.activity.model.PrenancyJiShiBenListViewExpandableGroupItem;
import com.example.lifememory.adapter.BabyMyExpandableListViewLuYinAdapter;
import com.example.lifememory.adapter.MyExpandableListViewAdapter;
import com.example.lifememory.adapter.MyExpandableListViewLuYinAdapter;
import com.example.lifememory.db.service.BabyDiaryLuYinService;
import com.example.lifememory.db.service.PregnancyDiaryLuYinService;
import com.example.lifememory.utils.DateFormater;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class FR_BabyDiary_LuYinBi_Grid extends Fragment {
	private static ExpandableListView listView = null;
	private List<BabyJiShiBenListViewExpandableGroupItem> groupItems = null;
	private BabyJiShiBenListViewExpandableGroupItem groupItem = null;
	public static BabyMyExpandableListViewLuYinAdapter exAdapter = null;
	private List<BabyLuYin> luyinItems = null;
	private BabyLuYin luyinItem = null;
	private static BabyDiaryLuYinService dbService = null;
	private static FragmentActivity fa = null;
	private List<String> groupTitles = null;
	public boolean isShowDelTag = false;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0) {
				//��ȡ���ݿ�
				setAdapter();
			}
		};
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbService = new BabyDiaryLuYinService(getActivity());
		fa = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fr_pregnancydiary_luyin_grid, null, false);
		findViews(view);
//		initDatas();
//		setAdapter();
		return view;
	}
	
	public void showDeleteTag(boolean isShow) {
		isShowDelTag = isShow;
		exAdapter.showDeleteTag(isShowDelTag);
	}
	
	private void findViews(View view) {
		listView = (ExpandableListView) view.findViewById(R.id.expandableListView);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		new InitDatasThread().start();
	}
	
	private class InitDatasThread extends Thread{
		@Override
		public void run() {
			super.run();
			initDatas();
			mHandler.sendEmptyMessage(0);
		}
	}
	
	private void initDatas() {
		groupItems = new ArrayList<BabyJiShiBenListViewExpandableGroupItem>();
		groupItem = new BabyJiShiBenListViewExpandableGroupItem();
		groupItem.setTitle("����");
		luyinItems = dbService.getTodayItems(DateFormater.getInstatnce().getYMD());
		luyinItem = new BabyLuYin();
		luyinItem.setImageId(R.drawable.add_icon);
		luyinItems.add(0, luyinItem);
		if (luyinItems.size() == 1) {
			groupItem.setNum(0);
		} else {
			groupItem.setNum(luyinItems.size() - 1);
		}
		groupItem.setImage(R.drawable.group_unexpand_icon);
		groupItem.setLuyinItems(luyinItems);
		groupItems.add(groupItem);
		

		groupTitles = dbService.getMonths(DateFormater.getInstatnce().getYM());
		if (groupTitles.size() > 0) {
			for (String groupTitle : groupTitles) {
				groupItem = new BabyJiShiBenListViewExpandableGroupItem();
				groupItem.setTitle(groupTitle);
				groupItem.setLuyinItems(dbService.findItemsByYM(groupTitle, DateFormater.getInstatnce().getYMD()));
				if(groupItem.getLuyinItems().size() > 0) {
					groupItem.setNum(groupItem.getLuyinItems().size());
					groupItems.add(groupItem);
				}
				groupItem = null;
			}
		}
		
		
	}
	
	private void setAdapter() {
		exAdapter = new BabyMyExpandableListViewLuYinAdapter(fa, fa, groupItems);
		listView.setAdapter(exAdapter);
		if(isShowDelTag) {
			exAdapter.showDeleteTag(isShowDelTag);
		}
		int groupCount = exAdapter.getGroupCount();
		for (int i = 0; i < groupCount; i++) {
			listView.expandGroup(i);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbService.closeDB();
	}
}














