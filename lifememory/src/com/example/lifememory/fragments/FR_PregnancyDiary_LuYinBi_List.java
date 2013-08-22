package com.example.lifememory.fragments;

import java.util.ArrayList;
import java.util.List;
import com.example.lifememory.R;
import com.example.lifememory.activity.IndexActivity;
import com.example.lifememory.activity.PregnancyJiShiBenAddActivity;
import com.example.lifememory.activity.PregnancyJiShiBenReadActivity;
import com.example.lifememory.activity.PregnancyLuYinAddActivity;
import com.example.lifememory.activity.PregnancyLuYinReadActivity;
import com.example.lifememory.activity.model.PregnancyLuYin;
import com.example.lifememory.activity.model.PrenancyJiShiBenListViewExpandableGroupItem;
import com.example.lifememory.adapter.MyExpandableListViewAdapterLuYinList;
import com.example.lifememory.db.service.PregnancyDiaryLuYinService;
import com.example.lifememory.utils.DateFormater;

import android.content.Intent;
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
import android.widget.ExpandableListView.OnChildClickListener;

public class FR_PregnancyDiary_LuYinBi_List extends Fragment {
	private static ExpandableListView listView = null;
	private List<PrenancyJiShiBenListViewExpandableGroupItem> groupItems = null;
	private PrenancyJiShiBenListViewExpandableGroupItem groupItem = null;
	public static MyExpandableListViewAdapterLuYinList exAdapter = null;
	private List<PregnancyLuYin> luyinItems = null;
	private PregnancyLuYin luyinItem = null;
	private static PregnancyDiaryLuYinService dbService = null;
	private List<String> groupTitles = null;
	public boolean isShowDelTag = false;
	private static FragmentActivity fa = null;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0) {
				//读取数据库
				setAdapter();
			}
		};
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbService = new PregnancyDiaryLuYinService(getActivity());
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
		exAdapter.showDeleteTag(isShow);
	}
	
	private void findViews(View view) {
		listView = (ExpandableListView) view.findViewById(R.id.expandableListView);
		
		listView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				if(groupPosition == 0 && childPosition == 0) {
					Intent intent = new Intent(getActivity(), PregnancyLuYinAddActivity.class);
					getActivity().startActivity(intent);
					getActivity().overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
				}else {
					Intent intent = new Intent(getActivity(), PregnancyLuYinReadActivity.class);
					intent.putExtra("itemId", groupItems.get(groupPosition).getLuyinItems().get(childPosition).getIdx());
					getActivity().startActivity(intent);
					getActivity().overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
				}
				
				return false;
			}
		});
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
		groupItems = new ArrayList<PrenancyJiShiBenListViewExpandableGroupItem>();
		groupItem = new PrenancyJiShiBenListViewExpandableGroupItem();
		groupItem.setTitle("今天");
		luyinItems = dbService.getTodayItems(DateFormater.getInstatnce().getYMD());
		luyinItem = new PregnancyLuYin();
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
				groupItem = new PrenancyJiShiBenListViewExpandableGroupItem();
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
		exAdapter = new MyExpandableListViewAdapterLuYinList(fa, fa, groupItems);
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














