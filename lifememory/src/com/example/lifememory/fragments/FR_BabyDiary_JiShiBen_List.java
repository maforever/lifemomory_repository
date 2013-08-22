package com.example.lifememory.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.example.lifememory.R;
import com.example.lifememory.activity.BabyJiShiBenAddActivity;
import com.example.lifememory.activity.BabyJiShiBenReadActivity;
import com.example.lifememory.activity.PregnancyJiShiBenAddActivity;
import com.example.lifememory.activity.PregnancyJiShiBenReadActivity;
import com.example.lifememory.activity.model.BabyJiShiBen;
import com.example.lifememory.activity.model.BabyJiShiBenGridViewExpandableGroupItem;
import com.example.lifememory.activity.model.PregnancyJiShiBen;
import com.example.lifememory.activity.model.PrenancyJiShiBenGridViewExpandableGroupItem;
import com.example.lifememory.adapter.BabyMyExpandableListViewAdapterList;
import com.example.lifememory.adapter.MyExpandableListViewAdapterList;
import com.example.lifememory.db.service.BabyDiaryJiShiBenService;
import com.example.lifememory.db.service.PregnancyDiaryJiShiBenService;
import com.example.lifememory.utils.DateFormater;

public class FR_BabyDiary_JiShiBen_List extends Fragment {
	private List<BabyJiShiBen> jishibenItems = new ArrayList<BabyJiShiBen>();
	private static BabyDiaryJiShiBenService dbService = null;
	private List<BabyJiShiBenGridViewExpandableGroupItem> groupItems = null;
	private BabyJiShiBenGridViewExpandableGroupItem groupItem = null;
	private static ExpandableListView listView = null;
	public static BabyMyExpandableListViewAdapterList exAdapter = null;
	private static FragmentActivity fa = null;
	private List<String> groupTitles = new ArrayList<String>();
	public boolean isShowDelTag = false;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			exAdapter = new BabyMyExpandableListViewAdapterList(fa,
					fa, groupItems);
			listView.setAdapter(exAdapter);
			if(isShowDelTag) {
				exAdapter.showDeleteTag(isShowDelTag);
			}
			int groupCount = exAdapter.getGroupCount();
			for (int i = 0; i < groupCount; i++) {
				listView.expandGroup(i);
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbService = new BabyDiaryJiShiBenService(getActivity());
		fa = getActivity();
	}

	public void showDeleteTag(boolean isShow) {
		isShowDelTag = isShow;
		exAdapter.showDeleteTag(isShow);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fr_pregnancydiary_jishiben_grid,
				container, false);
		findViews(view);
		return view;
	}

	private void findViews(View view) {
		listView = (ExpandableListView) view
				.findViewById(R.id.expandableListView);
		listView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				if(groupPosition == 0 && childPosition == 0) {
					Intent intent = new Intent(getActivity(), BabyJiShiBenAddActivity.class);
					getActivity().startActivity(intent);
					getActivity().overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
				}else {
					Intent intent = new Intent(getActivity(), BabyJiShiBenReadActivity.class);
					intent.putExtra("itemId", groupItems.get(groupPosition).getJishibenItems().get(childPosition).getIdx());
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
		new InitDataThread().start();
	}

	private class InitDataThread extends Thread {
		@Override
		public void run() {
			FR_BabyDiary_JiShiBen_List.this.initDatas();
			handler.sendEmptyMessage(0);
		}
	}

	// 初始化数据库中的pregnancyjishibenitem
	private void initDatas() {
		BabyJiShiBen item = new BabyJiShiBen();
		jishibenItems = dbService.findItemsByYMD(DateFormater.getInstatnce().getYMD()); //获取今天写的日记
		item.setImageId(R.drawable.add_icon);
		jishibenItems.add(0, item);

		groupItems = new ArrayList<BabyJiShiBenGridViewExpandableGroupItem>();
		groupItem = new BabyJiShiBenGridViewExpandableGroupItem();
		groupItem.setTitle("今天");
		if (jishibenItems.size() == 1) {
			groupItem.setNum(0);
		} else {
			groupItem.setNum(jishibenItems.size() - 1);
		}
		groupItem.setImage(R.drawable.group_unexpand_icon);
		groupItem.setJishibenItems(jishibenItems);
		groupItems.add(groupItem);
		groupItem = null;
		groupTitles = dbService.getMonths(DateFormater.getInstatnce().getYM());
		if (groupTitles.size() > 0) {
			for (String groupTitle : groupTitles) {
				groupItem = new BabyJiShiBenGridViewExpandableGroupItem();
				groupItem.setTitle(groupTitle);
				groupItem.setJishibenItems(dbService.findItemsByYM(groupTitle, DateFormater.getInstatnce().getYMD()));
				if(groupItem.getJishibenItems().size() > 0) {
					groupItem.setNum(groupItem.getJishibenItems().size());
					groupItems.add(groupItem);
				}
				groupItem = null;
			}
		}
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		dbService.closeDB();
	}
}
