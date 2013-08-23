package com.example.lifememory.fragments;

import java.util.ArrayList;
import java.util.List;

import com.example.lifememory.R;
import com.example.lifememory.activity.IndexActivity;
import com.example.lifememory.activity.slidingmenu.menulib.SlidingMenu.OnCloseListener;
import com.example.lifememory.activity.slidingmenu.menulib.SlidingMenu.OnClosedListener;
import com.example.lifememory.activity.slidingmenu.menulib.SlidingMenu.OnOpenListener;
import com.example.lifememory.activity.slidingmenu.menulib.SlidingMenu.OnOpenedListener;
import com.example.lifememory.adapter.MyFragmentViewPagerAdapter;
import com.example.lifememory.adapter.PregnancyJSBPopWindowGridAdapter;
import com.example.lifememory.db.service.BabyDiaryJiShiBenService;
import com.example.lifememory.db.service.BabyDiaryLuYinService;
import com.example.lifememory.db.service.PregnancyDiaryJiShiBenService;
import com.example.lifememory.db.service.PregnancyDiaryLuYinService;
import com.example.lifememory.dialog.DialogAlert;
import com.example.lifememory.dialog.DialogAlertListener;

import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class BabyDiaryFragment extends Fragment {
	private static IndexActivity indexActivity = null;
	private ImageView leftmenuHandler = null;
	private LinearLayout layout_jishiben, layout_luyinbi;
	private FragmentManager fm = null;
	private ViewPager pager = null;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private static int offset;
	private static int currIndex;
	private ImageView iv_bottom_line;
	private TextView title; // 导航栏标题
	// private float x, y; //iv_bottom_line的坐标
	private CheckBox listGridSwitcer, listGridSwitcherLuyin = null; // list与grid视图切换按钮
	private IndexActivity.MyOnTouchListener myOnTouchListener = null;
	private Fragment jishibenFragment, luyinFragment = null;
	private boolean isJiShiBenFragment = true; // 是否是记事本的fragment
												// false是录音的fragment
	private boolean isJSBGridStyle = true; // 记事本是否是Grid显示方式
	private boolean isLYGridStyle = true; // 录音是否是Grid显示方式
	private PopupWindow jishibenPopWindow = null;
	private LinearLayout parentView = null; // 显示popwindow
	private CheckBox settingCb = null;
	private LinearLayout tool_main, tool_delete; // 界面下方的主tool与删除tool
	private TextView deleteBtn, deleteCancleBtn = null;
	private FR_BabyDiary_JiShiBen_Grid fr_jsb_grid;
	private FR_BabyDiary_JiShiBen_List fr_jsb_list;
	private FR_BabyDiary_LuYinBi_Grid fr_ly_grid;
	private FR_BabyDiary_LuYinBi_List fr_ly_list;
	private boolean isShowDelTag = false;
	private BabyDiaryJiShiBenService jsbDBService = null;
	private BabyDiaryLuYinService lyDBService = null;
	private List<Integer> idxs = new ArrayList<Integer>();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				//记事本grid批量删除成功
				fr_jsb_grid.onResume();
				fr_jsb_grid.exAdapter.adapter.idxs.clear();
				Toast.makeText(getActivity(), "批量删除日记成功!", 0).show();
				break;
			case 1:
				//记事本grid全部删除成功
				fr_jsb_grid.onResume();
				Toast.makeText(getActivity(), "所有日记已被清空!", 0).show();
				break;
			case 2:
				//记事本list批量删除成功
				fr_jsb_list.onResume();
				fr_jsb_list.exAdapter.idxs.clear();
				Toast.makeText(getActivity(), "批量删除日记成功!", 0).show();
				break;
			case 3:
				//记事本list全部删除成功
				fr_jsb_list.onResume();
				Toast.makeText(getActivity(), "所有日记已被清空!", 0).show();
				break;
			case 4:
				//录音grid批量删除成功
				fr_ly_grid.onResume();
				fr_ly_grid.exAdapter.adapter.idxs.clear();
				Toast.makeText(getActivity(), "批量删除录音成功!", 0).show();
				break;
			case 5:
				//录音grid全部删除成功
				fr_ly_grid.onResume();
				Toast.makeText(getActivity(), "批量删除录音成功!", 0).show();
				break;
			case 6:
				//录音list批量删除成功
				fr_ly_list.onResume();
				fr_ly_list.exAdapter.idxs.clear();
				Toast.makeText(getActivity(), "批量删除录音成功!", 0).show();
				break;
			case 7:
				//录音list全部删除成功
				fr_ly_list.onResume();
				Toast.makeText(getActivity(), "批量删除录音成功!", 0).show();
				break;
			}
		};
	};
	public BabyDiaryFragment(IndexActivity indexActivity) {
		this.indexActivity = indexActivity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jsbDBService = new BabyDiaryJiShiBenService(getActivity());
		lyDBService = new BabyDiaryLuYinService(getActivity());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		jsbDBService.closeDB();
		lyDBService.closeDB();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pregnancydiary, null,
				false);
		findViews(view);
		initFragments();
		initViewPager();
		initWidth();
		initPopWindow(inflater);
		leftmenuHandler.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				indexActivity.getSlidingMenu().setSlidingEnabled(true);
				if (!indexActivity.getSlidingMenu().isMenuShowing()) {
					indexActivity
							.unregisterMyOnTouchListener(myOnTouchListener);
				}
				((IndexActivity) getActivity()).toggle();
			}
		});
		return view;
	}

	private void initPopWindow(LayoutInflater inflater) {
		View contentView = inflater.inflate(
				R.layout.pregnancy_jishiben_popwindow, null);
		GridView gridView = (GridView) contentView.findViewById(R.id.gridView);
		int[] imageIds = { R.drawable.toolbar_del_icon,
				R.drawable.toolbar_delall_icon, R.drawable.toolbar_timer_icon,
				R.drawable.toolbar_list_icon };
		String[] titles = { "批量删除", "全部删除", "按时查找", "文件备份" };
		PregnancyJSBPopWindowGridAdapter adapter = new PregnancyJSBPopWindowGridAdapter(
				getActivity(), imageIds, titles);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					//批量删除
					if (jishibenPopWindow.isShowing()) {
						jishibenPopWindow.dismiss();
					}

					if (isJiShiBenFragment) {
						// 是记事本界面
						if (isJSBGridStyle) {
							// 是grid显示方式
							fr_jsb_grid = (FR_BabyDiary_JiShiBen_Grid) fragments
									.get(0);
							fr_jsb_grid.showDeleteTag(true);
							isShowDelTag = true;
//							Toast.makeText(getActivity(), "记事本界面 --- grid显示方式", 0).show();
						} else {
							// 是list显示方式
							fr_jsb_list = (FR_BabyDiary_JiShiBen_List) fragments
									.get(0);
							fr_jsb_list.showDeleteTag(true);
							isShowDelTag = true;
//							Toast.makeText(getActivity(), "记事本界面 --- list显示方式", 0).show();
						}

					} else {
						// 是录音界面
						if (isLYGridStyle) {
							// 是grid显示方式
							fr_ly_grid = (FR_BabyDiary_LuYinBi_Grid) fragments
									.get(1);
							fr_ly_grid.showDeleteTag(true);
							isShowDelTag = true;
//							Toast.makeText(getActivity(), "录音界面 --- grid显示方式", 0).show();
						} else {
							// 是list显示方式
							fr_ly_list = (FR_BabyDiary_LuYinBi_List) fragments
									.get(1);
							fr_ly_list.showDeleteTag(true);
							isShowDelTag = true;
//							fr_ly_list = (FR_PregnancyDiary_LuYinBi_List) fragments
//									.get(1);
//							Toast.makeText(getActivity(), "录音界面 --- list显示方式", 0).show();
						}
					}

					tool_main.setVisibility(ViewGroup.GONE);
					tool_delete.setVisibility(ViewGroup.VISIBLE);
					break;

				case 1:
					//全部删除
					if (jishibenPopWindow.isShowing()) {
						jishibenPopWindow.dismiss();
					}
					
					loadCurrentFragment();
					
					
					if (isJiShiBenFragment) {
						// 是记事本界面
						if (isJSBGridStyle) {
							// 是grid显示方式
							new DialogAlert(getActivity(), listener, "确定要删除所有日记吗？无法恢复！").show();
						} else {
							// 是list显示方式
							new DialogAlert(getActivity(), listener2, "确定要删除所有日记吗？无法恢复！").show();
//							Toast.makeText(getActivity(), "记事本界面 --- list显示方式", 0).show();
						}

					} else {
						// 是录音界面
						if (isLYGridStyle) {
							// 是grid显示方式
							new DialogAlert(getActivity(), listener3, "确定要删除所有录音吗？无法恢复！").show();
							
						} else {
							// 是list显示方式
							new DialogAlert(getActivity(), listener4, "确定要删除所有录音吗？无法恢复！").show();
						}
					}
					
					
					
					break;
				case 2:
					//按时查找
					break;
				case 3:
					//文件备份
					break;
				}
			}
		});
		jishibenPopWindow = new PopupWindow(contentView,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//		jishibenPopWindow.setBackgroundDrawable(new BitmapDrawable());
		jishibenPopWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pregnancy_jishiben_popwindow_bg));
		jishibenPopWindow.setFocusable(true);
		jishibenPopWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				settingCb.setChecked(false);
			}
		});
	}
	
	private void loadCurrentFragment() {
		if (isJiShiBenFragment) {
			// 是记事本界面
			if (isJSBGridStyle) {
				// 是grid显示方式
				fr_jsb_grid = (FR_BabyDiary_JiShiBen_Grid) fragments
						.get(0);
			} else {
				// 是list显示方式
				fr_jsb_list = (FR_BabyDiary_JiShiBen_List) fragments
						.get(0);
			}

		} else {
			// 是录音界面
			if (isLYGridStyle) {
				// 是grid显示方式
				fr_ly_grid = (FR_BabyDiary_LuYinBi_Grid) fragments
						.get(1);
			} else {
				// 是list显示方式
				fr_ly_list = (FR_BabyDiary_LuYinBi_List) fragments
						.get(1);
			}
		}
	}
	
	private DialogAlertListener listener = new DialogAlertListener() {

		@Override
		public void onDialogUnSave(Dialog dlg, Object param) {

		}
		@Override
		public void onDialogSave(Dialog dlg, Object param) {

		}
		@Override
		public void onDialogOk(Dialog dlg, Object param) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					jsbDBService.deleteAll();
					handler.sendEmptyMessage(1);
				}
			}).start();
		}
		@Override
		public void onDialogCreate(Dialog dlg, Object param) {

		}
		@Override
		public void onDialogCancel(Dialog dlg, Object param) {

		}
	};

	private DialogAlertListener listener2 = new DialogAlertListener() {
		@Override
		public void onDialogUnSave(Dialog dlg, Object param) {

		}
		@Override
		public void onDialogSave(Dialog dlg, Object param) {

		}
		@Override
		public void onDialogOk(Dialog dlg, Object param) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					jsbDBService.deleteAll();
					handler.sendEmptyMessage(3);
				}
			}).start();
		}
		@Override
		public void onDialogCreate(Dialog dlg, Object param) {

		}
		@Override
		public void onDialogCancel(Dialog dlg, Object param) {

		}
	};

	private DialogAlertListener listener3 = new DialogAlertListener() {
		@Override
		public void onDialogUnSave(Dialog dlg, Object param) {

		}
		@Override
		public void onDialogSave(Dialog dlg, Object param) {

		}
		@Override
		public void onDialogOk(Dialog dlg, Object param) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					lyDBService.deleteAll();
					handler.sendEmptyMessage(5);
				}
			}).start();
		}

		@Override
		public void onDialogCreate(Dialog dlg, Object param) {

		}

		@Override
		public void onDialogCancel(Dialog dlg, Object param) {

		}
	};

	private DialogAlertListener listener4 = new DialogAlertListener() {
		
		
		@Override
		public void onDialogUnSave(Dialog dlg, Object param) {
			
		}
		@Override
		public void onDialogSave(Dialog dlg, Object param) {
			
		}
		@Override
		public void onDialogOk(Dialog dlg, Object param) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					lyDBService.deleteAll();
					handler.sendEmptyMessage(7);
				}
			}).start();
		}
		
		@Override
		public void onDialogCreate(Dialog dlg, Object param) {
			
		}
		
		@Override
		public void onDialogCancel(Dialog dlg, Object param) {
			
		}
	};
	
	public BabyDiaryFragment() {
	}
	

	private void findViews(View view) {
		pager = (ViewPager) view.findViewById(R.id.viewPager);
		listGridSwitcer = (CheckBox) view.findViewById(R.id.checkbox);
		listGridSwitcherLuyin = (CheckBox) view
				.findViewById(R.id.checkbox_luyin);
		settingCb = (CheckBox) view.findViewById(R.id.setting_checkBox);
		tool_main = (LinearLayout) view.findViewById(R.id.tool_main);
		tool_delete = (LinearLayout) view.findViewById(R.id.tool_delete);
		deleteBtn = (TextView) view.findViewById(R.id.delete);
		deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tool_main.setVisibility(ViewGroup.VISIBLE);
				tool_delete.setVisibility(ViewGroup.GONE);
				
				if (isJiShiBenFragment) {
					// 是记事本界面
					if (isJSBGridStyle) {
						// 是grid显示方式
						fr_jsb_grid.showDeleteTag(false);
						isShowDelTag = false;
						idxs = fr_jsb_grid.exAdapter.adapter.idxs;
//						Log.i("a", "idxs = " + fr_jsb_grid.exAdapter.adapter.idxs);
						if(idxs.size() > 0) {
							
							new Thread(new Runnable() {
								@Override
								public void run() {
									jsbDBService.deleteItemsByIdxs(idxs);
									handler.sendEmptyMessage(0);
								}
							}).start();
						}
					} else {
						fr_jsb_list.showDeleteTag(false);
						isShowDelTag = false;
						idxs = fr_jsb_list.exAdapter.idxs;
//						Log.i("a", "fr_jsb_list idxs = " + idxs);
						
						if(idxs.size() > 0) {
							
							new Thread(new Runnable() {
								@Override
								public void run() {
									jsbDBService.deleteItemsByIdxs(idxs);
									handler.sendEmptyMessage(2);
								}
							}).start();
						}
						// 是list显示方式
						// fr_jsb_list =
						// (FR_PregnancyDiary_JiShiBen_List)fragments.get(0);
					}

				} else {
					// 是录音界面
					if (isLYGridStyle) {
						// 是grid显示方式
						fr_ly_grid.showDeleteTag(false);
						isShowDelTag = false;
						idxs = fr_ly_grid.exAdapter.adapter.idxs;
//						Log.i("a", "idxs = " + fr_jsb_grid.exAdapter.adapter.idxs);
						if(idxs.size() > 0) {
							
							new Thread(new Runnable() {
								@Override
								public void run() {
									lyDBService.deleteItemsByIdxs(idxs);
									handler.sendEmptyMessage(4);
								}
							}).start();
						}
					} else {
						fr_ly_list.showDeleteTag(false);
						isShowDelTag = false;
						idxs = fr_ly_list.exAdapter.idxs;
//						Log.i("a", "fr_ly_list idxs = " + idxs);
						
						if(idxs.size() > 0) {
							
							new Thread(new Runnable() {
								@Override
								public void run() {
									lyDBService.deleteItemsByIdxs(idxs);
									handler.sendEmptyMessage(6);
								}
							}).start();
						}
						// 是list显示方式
						// fr_ly_list =
						// (FR_PregnancyDiary_LuYinBi_List)fragments.get(1);
					}
				}
			}
		});
		deleteCancleBtn = (TextView) view.findViewById(R.id.delete_cancle);
		deleteCancleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isJiShiBenFragment) {
					// 是记事本界面
					if (isJSBGridStyle) {
						// 是grid显示方式
						fr_jsb_grid.showDeleteTag(false);
						fr_jsb_grid.exAdapter.adapter.idxs.clear();
					} else {
						fr_jsb_list.showDeleteTag(false);
						fr_jsb_list.exAdapter.idxs.clear();
						// 是list显示方式
						// fr_jsb_list =
						// (FR_PregnancyDiary_JiShiBen_List)fragments.get(0);
					}

				} else {
					// 是录音界面
					if (isJSBGridStyle) {
						fr_ly_grid.showDeleteTag(false);
						fr_ly_grid.exAdapter.adapter.idxs.clear();
						// 是grid显示方式
						// fr_ly_grid =
						// (FR_PregnancyDiary_LuYinBi_Grid)fragments.get(1);
					} else {
						fr_ly_list.showDeleteTag(false);
						fr_ly_list.exAdapter.idxs.clear();
						// 是list显示方式
						// fr_ly_list =
						// (FR_PregnancyDiary_LuYinBi_List)fragments.get(1);
					}
				}

				tool_main.setVisibility(ViewGroup.VISIBLE);
				tool_delete.setVisibility(ViewGroup.GONE);
			}
		});
		title = (TextView) view.findViewById(R.id.title);
		title.setText("宝贝成长日记");
		parentView = (LinearLayout) view.findViewById(R.id.RelativeLayout1);
		listGridSwitcer
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 选中
						if (isChecked) {
							listGridSwitcer
									.setBackgroundResource(R.drawable.pregnancy_list_btn_selector);
							isJSBGridStyle = false; // 是list显示方式
						} else {
							listGridSwitcer
									.setBackgroundResource(R.drawable.pregnancy_grid_btn_selector);
							isJSBGridStyle = true; // 是grid显示方式
						}
						initFragments();
						initViewPager();
					}
				});

		listGridSwitcherLuyin
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 选中
						if (isChecked) {
							listGridSwitcherLuyin
									.setBackgroundResource(R.drawable.pregnancy_list_btn_selector);
							isLYGridStyle = false; // 是list显示方式
						} else {
							listGridSwitcherLuyin
									.setBackgroundResource(R.drawable.pregnancy_grid_btn_selector);
							isLYGridStyle = true; // 是grid显示方式
						}
						initFragments();
						initViewPager();
					}
				});

		settingCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					jishibenPopWindow.showAtLocation(parentView,
							Gravity.BOTTOM, 0, 0);
				} else {
					jishibenPopWindow.dismiss();
				}
			}
		});

		leftmenuHandler = (ImageView) view.findViewById(R.id.leftmenu_handler);
		layout_jishiben = (LinearLayout) view
				.findViewById(R.id.layout_jishiben);
		layout_luyinbi = (LinearLayout) view.findViewById(R.id.layout_luyinbi);
		iv_bottom_line = (ImageView) view.findViewById(R.id.iv_bottom_line);
		layout_jishiben.setOnClickListener(new OnClickListenerImpl(0));
		layout_luyinbi.setOnClickListener(new OnClickListenerImpl(1));

		// 滑动事件
		myOnTouchListener = new IndexActivity.MyOnTouchListener() {
			public boolean onTouch(MotionEvent event) {
				// 手指按下并且没有显示leftmenufragment
				if (event.getAction() == MotionEvent.ACTION_DOWN
						&& !indexActivity.getSlidingMenu().isMenuShowing()) {
					if (pager.getCurrentItem() != 0) {
						indexActivity.getSlidingMenu().setSlidingEnabled(false);
					} else {
						indexActivity.getSlidingMenu().setSlidingEnabled(true);
					}
				}
				return false;
			}
		};
		this.indexActivity.registerMyOnTouchListener(myOnTouchListener);

		// 显示leftmenufragment时注销掉PregnancyDiaryFragment的ontouch事件
		indexActivity.getSlidingMenu().setOnOpenListener(new OnOpenListener() {

			@Override
			public void onOpen() {
//				Log.i("a", "onOpen");
				indexActivity.getSlidingMenu().setSlidingEnabled(true);
				indexActivity.unregisterMyOnTouchListener(myOnTouchListener);
			}
		});
		
		// 当隐藏左侧的leftmenuframent的时候注册ontouch时间
		indexActivity.getSlidingMenu().setOnClosedListener(new OnClosedListener() {
			
			@Override
			public void onClosed() {
				indexActivity.getSlidingMenu().setSlidingEnabled(true);
				indexActivity.registerMyOnTouchListener(myOnTouchListener);
			}
		});
	}

	private void initWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

		int screenW = dm.widthPixels;
		LayoutParams params = new LayoutParams(screenW / 2,
				LinearLayout.LayoutParams.MATCH_PARENT);
		iv_bottom_line.setLayoutParams(params);
		offset = screenW / 2;

		// int[] locations = new int[2];
		// iv_bottom_line.getLocationInWindow(locations);
		// x = iv_bottom_line.getX();
		// y = iv_bottom_line.getY();
		// offset = 16;

	}

	private void initViewPager() {
		pager.setAdapter(new MyFragmentViewPagerAdapter(this
				.getChildFragmentManager(), fragments));
		pager.setOnPageChangeListener(new OnPageChangerListenerImpl());
		pager.setCurrentItem(currIndex);
	}

	private void initFragments() {
		fragments = new ArrayList<Fragment>();
		// 选中，list视图
		if (listGridSwitcer.isChecked()) {
			jishibenFragment = new FR_BabyDiary_JiShiBen_List();
		} else {
			jishibenFragment = new FR_BabyDiary_JiShiBen_Grid();
		}
		if (listGridSwitcherLuyin.isChecked()) {
			luyinFragment = new FR_BabyDiary_LuYinBi_List();
		} else {
			luyinFragment = new FR_BabyDiary_LuYinBi_Grid();
		}
		fragments.add(jishibenFragment);
		fragments.add(luyinFragment);
		
		
		
		
		
	}

	private class OnClickListenerImpl implements OnClickListener {
		private int index = 0;

		public OnClickListenerImpl(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			pager.setCurrentItem(index);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private class OnPageChangerListenerImpl implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				animation = new TranslateAnimation(offset, 0, 0, 0);
				animation.setFillAfter(true);
				animation.setDuration(300);
				iv_bottom_line.startAnimation(animation);
				listGridSwitcer.setVisibility(ViewGroup.VISIBLE);
				listGridSwitcherLuyin.setVisibility(ViewGroup.GONE);
				title.setText("宝贝成长日记");
				isJiShiBenFragment = true;
				// iv_bottom_line.setX(0f);
				// iv_bottom_line.setY(y);
				break;
			case 1:
				animation = new TranslateAnimation(0, offset, 0, 0);
				animation.setFillAfter(true);
				animation.setDuration(300);
				iv_bottom_line.startAnimation(animation);
				listGridSwitcer.setVisibility(ViewGroup.GONE);
				listGridSwitcherLuyin.setVisibility(ViewGroup.VISIBLE);
				title.setText("宝贝成长录音");
				isJiShiBenFragment = false;
				// iv_bottom_line.setX(0f + 320);
				// iv_bottom_line.setY(y);
				break;
			}
			currIndex = arg0;
		}
	}

}
