package com.example.lifememory.fragments;


import java.util.ArrayList;
import java.util.List;

import com.example.lifememory.R;
import com.example.lifememory.activity.IndexActivity;
import com.example.lifememory.activity.model.LeftMenuItem;
import com.example.lifememory.adapter.LeftMenuListViewAdapter;

import android.app.FragmentManager.BackStackEntry;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class LeftMenuFragment extends Fragment {
	private ListView listView = null;
	private FragmentManager fm = null;//得到fragmentTransaction
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();//存放左右的fragment
	private IndexActivity indexActivity = null;//使用toggle方法
	private LeftMenuItem menuItem = null;  //左侧菜单项封装
	private List<LeftMenuItem> menuItems = null;  //存放所有菜单项
	private LeftMenuListViewAdapter adapter = null; //左侧菜单listview适配器
	
	public LeftMenuFragment() {
		
	}
	public LeftMenuFragment(FragmentManager fm, IndexActivity indexActivity) {
		this.fm = fm;
		this.indexActivity = indexActivity;
		initDatas();
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fr_left_menu, container, false);
		listView = (ListView) view.findViewById(R.id.listView);
		
		
		adapter = new LeftMenuListViewAdapter(getActivity(), menuItems);
		listView.setAdapter(adapter);
		adapter.setSelected(0);
		Fragment fragment = new PregnancyDiaryFragment(indexActivity);
		fragments.add(fragment);
		fragment = new FR_PregnancyDiary_JiShiBen_Grid();
		fragments.add(fragment);
		fragment = new BabyDiaryFragment(indexActivity);
		fragments.add(fragment);
		fragment = new EFragment();
		fragments.add(fragment);
		fragment = new FFragment();
		fragments.add(fragment);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					adapter.setSelected(0);
					changeFragment(0);
					break;
				case 1:
					adapter.setSelected(1);
					changeFragment(1);
					break;
				case 2:
					adapter.setSelected(2);
					changeFragment(2);
					break;
				case 3:
					adapter.setSelected(3);
					changeFragment(3);
					break;
				}
			}
		});
		return view;
	}
	
    private void initDatas() {
    	menuItems = new ArrayList<LeftMenuItem>();
    	menuItem = new LeftMenuItem();
    	menuItem.setTitle("孕期日志");
    	menuItem.setSubTitle("Pregnancy Diary");
    	menuItem.setImageId(R.drawable.leftmenu_icon);
    	menuItems.add(menuItem);
    	
    	menuItem = new LeftMenuItem();
    	menuItem.setTitle("孕期指南");
    	menuItem.setSubTitle("Pregnancy guide");
    	menuItem.setImageId(R.drawable.leftmenu_icon);
    	menuItems.add(menuItem);
    	
    	menuItem = new LeftMenuItem();
    	menuItem.setTitle("宝贝日志");
    	menuItem.setSubTitle("Baby Diary");
    	menuItem.setImageId(R.drawable.leftmenu_icon);
    	menuItems.add(menuItem);
    	
    	menuItem = new LeftMenuItem();
    	menuItem.setTitle("宝贝指南");
    	menuItem.setSubTitle("Baby Diary");
    	menuItem.setImageId(R.drawable.leftmenu_icon);
    	menuItems.add(menuItem);
    	
    	menuItem = new LeftMenuItem();
    	menuItem.setTitle("我的记账");
    	menuItem.setSubTitle("consume Diary");
    	menuItem.setImageId(R.drawable.leftmenu_icon);
    	menuItems.add(menuItem);
    	
    	menuItem = new LeftMenuItem();
    	menuItem.setTitle("我的店铺");
    	menuItem.setSubTitle("my Shop");
    	menuItem.setImageId(R.drawable.leftmenu_icon);
    	menuItems.add(menuItem);
    }

	private void changeFragment(int fragmentId) {
		Fragment fr = fragments.get(fragmentId);
		getFragmentManager().beginTransaction().replace(R.id.fl_index_content, fr).addToBackStack(null).commit(); 
		
//		if(lastFragmentId != fragmentId) {
//			
//			FragmentTransaction ft = fm.beginTransaction();
//			fm.popBackStackImmediate();
//			
//			
//			Fragment fr = fragments.get(fragmentId);
//			Log.i("a", "before getBackStackEntryCount = " + fm.getBackStackEntryCount());
//			if(fm.getBackStackEntryCount() == 0) {
//				ft.replace(R.id.fl_index_content, fr).addToBackStack(null);
//				fm.popBackStackImmediate();
//			}
//			Log.i("a", "after getBackStackEntryCount = " + fm.getBackStackEntryCount());
//			ft.commit();
//			lastFragmentId = fragmentId;
//			
//			Log.i("a", "after after getBackStackEntryCount = " + fm.getBackStackEntryCount());
//		}
		indexActivity.toggle();
	}
}
