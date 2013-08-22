package com.example.lifememory.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.lifememory.R;
import com.example.lifememory.activity.IndexActivity;
import com.example.lifememory.activity.model.PregnancyJiShiBen;
import com.example.lifememory.activity.model.PrenancyJiShiBenGridViewExpandableGroupItem;
import com.example.lifememory.activity.slidingmenu.menulib.SlidingMenu.OnOpenListener;
import com.example.lifememory.adapter.MyExpandableListViewAdapter;
import com.example.lifememory.db.service.PregnancyDiaryJiShiBenService;
import com.example.lifememory.utils.DateFormater;

public class FR_PregnancyDiary_JiShiBen_Grid extends Fragment {
	private List<PregnancyJiShiBen> jishibenItems = new ArrayList<PregnancyJiShiBen>();
	private static PregnancyDiaryJiShiBenService dbService = null;
	private List<PrenancyJiShiBenGridViewExpandableGroupItem> groupItems = null;
	private PrenancyJiShiBenGridViewExpandableGroupItem groupItem = null;
	private static ExpandableListView listView = null;
	public static MyExpandableListViewAdapter exAdapter = null;
	private List<String> groupTitles = new ArrayList<String>();
	private IndexActivity indexActivity = null;
	public boolean isShowDelTag = false;
	private static FragmentActivity fa = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0) {
				exAdapter = new MyExpandableListViewAdapter(fa,
						fa, groupItems);
				listView.setAdapter(exAdapter);
				if(isShowDelTag) {
					exAdapter.showDeleteTag(isShowDelTag);
				}
				int groupCount = exAdapter.getGroupCount();
				for (int i = 0; i < groupCount; i++) {
					listView.expandGroup(i);
				}
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		dbService = new PregnancyDiaryJiShiBenService(getActivity());
		fa = getActivity();
		indexActivity = (IndexActivity) getActivity();
		Log.i("a", "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fr_pregnancydiary_jishiben_grid,
				container, false);
		findViews(view);
		Log.i("a", "onCreateView");
		return view;
	}

	private void findViews(View view) {
		listView = (ExpandableListView) view
				.findViewById(R.id.expandableListView);
	}
	
	public void showDeleteTag(boolean isShow) {
		isShowDelTag = isShow;
		exAdapter.showDeleteTag(isShowDelTag);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("a", "onResume");
		new InitDataThread().start();
	}
	

	private class InitDataThread extends Thread {

		@Override
		public void run() {
			FR_PregnancyDiary_JiShiBen_Grid.this.initDatas();
			handler.sendEmptyMessage(0);
		}
	}

	// 初始化数据库中的pregnancyjishibenitem
	private void initDatas() {
		PregnancyJiShiBen item = new PregnancyJiShiBen();
		jishibenItems = dbService.findItemsByYMD(DateFormater.getInstatnce().getYMD()); //获取今天写的日记
		item.setImageId(R.drawable.add_icon);
		jishibenItems.add(0, item);

		groupItems = new ArrayList<PrenancyJiShiBenGridViewExpandableGroupItem>();
		groupItem = new PrenancyJiShiBenGridViewExpandableGroupItem();
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
				groupItem = new PrenancyJiShiBenGridViewExpandableGroupItem();
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbService.closeDB();
	}
}
