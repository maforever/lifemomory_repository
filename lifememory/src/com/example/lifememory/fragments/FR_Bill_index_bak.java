package com.example.lifememory.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.lifememory.R;
import com.example.lifememory.activity.IndexActivity;
import com.example.lifememory.activity.slidingmenu.menulib.SlidingMenu.OnClosedListener;
import com.example.lifememory.activity.slidingmenu.menulib.SlidingMenu.OnOpenListener;
import com.example.lifememory.activity.views.DragGrid;
import com.example.lifememory.activity.views.ScrollLayout;
import com.example.lifememory.adapter.DateAdapter;
import com.example.lifememory.utils.Configure;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 闪电记， 记一笔，预算平衡表，信用卡，翻日历，查账户，
 * @author Administrator
 *
 */
public class FR_Bill_index_bak extends Fragment {
	private TranslateAnimation left, right;
	private ImageView runImage;
	private ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();// 全部数据的集合集lists.size()==countpage;
	private ArrayList<String> lstDate = new ArrayList<String>();// 每一页的数据
	private ScrollLayout lst_views = null;
	private TextView tv_page = null;
	private LinearLayout.LayoutParams param;
	private DragGrid gridView = null;
	public static final int PAGE_SIZE = 4;
	private LinearLayout linear;
	private ArrayList<DragGrid> gridviews = new ArrayList<DragGrid>();
	private String[] firstPageTitle = new String[]{"闪电记", "记一笔", "预算平衡表", "本月"};
	private String[] secondPageTitle = new String[]{"信用卡", "翻日历", "查账户"};
	private IndexActivity indexActivity = null;
	private IndexActivity.MyOnTouchListener myOnTouchListener = null; // 添加滑动事件
	private int currentIndex = 0; // 用于记录当前滑动到的界面，根据界面适时的设置是否可滑动

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	public FR_Bill_index_bak(IndexActivity indexActivity) {
		this.indexActivity = indexActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fr_mybill_index, container, false);
		findViews(view);
		runAnimation();

		for (int i = 0; i < 12; i++) {
			lstDate.add("" + i);
		}
		init();
		initData();

		for (int i = 0; i < Configure.countPages; i++) {
			lst_views.addView(addGridView(i));
		}

		lst_views.setPageListener(new ScrollLayout.PageListener() {
			@Override
			public void page(int page) {
				setCurPage(page);
			}
		});

		return view;
	}

	private void findViews(View view) {
		runImage = (ImageView) view.findViewById(R.id.run_image);
		lst_views = (ScrollLayout) view.findViewById(R.id.views);
		tv_page = (TextView) view.findViewById(R.id.tv_page);
		// 滑动事件
		myOnTouchListener = new IndexActivity.MyOnTouchListener() {
			public boolean onTouch(MotionEvent event) {
				// 手指按下并且没有显示leftmenufragment
				if (event.getAction() == MotionEvent.ACTION_DOWN
						&& !indexActivity.getSlidingMenu().isMenuShowing()) {
					if (currentIndex != 0) {
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
				indexActivity.getSlidingMenu().setSlidingEnabled(true);
				indexActivity.unregisterMyOnTouchListener(myOnTouchListener);
			}
		});
		// 当隐藏左侧的leftmenuframent的时候注册ontouch时间
		indexActivity.getSlidingMenu().setOnClosedListener(
				new OnClosedListener() {

					@Override
					public void onClosed() {
						indexActivity.getSlidingMenu().setSlidingEnabled(true);
						indexActivity
								.registerMyOnTouchListener(myOnTouchListener);
					}
				});
	}

	public void runAnimation() {
		right = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
				Animation.RELATIVE_TO_PARENT, -1f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f);
		left = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f, Animation.RELATIVE_TO_PARENT, 0f);
		right.setDuration(25000);
		left.setDuration(25000);
		right.setFillAfter(true);
		left.setFillAfter(true);

		right.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				runImage.startAnimation(left);
			}
		});
		left.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				runImage.startAnimation(right);
			}
		});
		runImage.startAnimation(right);
	}

	public void init() {

		tv_page.setText("1");
		Configure.init(getActivity());
		param = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		param.rightMargin = 100;
		param.leftMargin = 20;
		if (gridView != null) {
			lst_views.removeAllViews();
		}
	}

	public void initData() {
		Configure.countPages = (int) Math.ceil(lstDate.size()
				/ (float) PAGE_SIZE);

		lists = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < Configure.countPages; i++) {
			lists.add(new ArrayList<String>());
			for (int j = PAGE_SIZE * i; j < (PAGE_SIZE * (i + 1) > lstDate
					.size() ? lstDate.size() : PAGE_SIZE * (i + 1)); j++)
				lists.get(i).add(lstDate.get(j));
		}
		boolean isLast = true;
		for (int i = lists.get(Configure.countPages - 1).size(); i < PAGE_SIZE; i++) {
			if (isLast) {
				lists.get(Configure.countPages - 1).add(null);
				isLast = false;
			} else
				lists.get(Configure.countPages - 1).add("none");
		}
	}

	public int getFristNonePosition(ArrayList<String> array) {
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i) != null && array.get(i).toString().equals("none")) {
				return i;
			}
		}
		return -1;
	}

	public int getFristNullPosition(ArrayList<String> array) {
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i) == null) {
				return i;
			}
		}
		return -1;
	}

	public void setCurPage(final int page) {
		Animation a = AnimationUtils.loadAnimation(getActivity(),
				R.anim.scale_in);
		a.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				tv_page.setText((page + 1) + "");
				currentIndex = page;
				tv_page.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), R.anim.scale_out));
			}
		});
		tv_page.startAnimation(a);

	}

	public LinearLayout addGridView(int i) {
//		if (lists.get(i).size() < PAGE_SIZE)
//			lists.get(i).add(null);

		linear = new LinearLayout(getActivity());
		gridView = new DragGrid(getActivity());
		gridView.setAdapter(new DateAdapter(getActivity(), lists.get(i)));
		gridView.setNumColumns(2);
		gridView.setHorizontalSpacing(0);
		gridView.setVerticalSpacing(0);
		final int ii = i;
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getActivity(), "onItemClick", 0).show();
			}
		});
//		gridView.setSelector(R.anim.grid_light);
		gridView.setPageListener(new DragGrid.G_PageListener() {
			@Override
			public void page(int cases, int page) {
				switch (cases) {
				case 0:// 滑动页面
					lst_views.snapToScreen(page);
					setCurPage(page);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Configure.isChangingPage = false;
						}
					}, 800);
					break;
				}
			}
		});
//		gridView.setOnItemChangeListener(new DragGrid.G_ItemChangeListener() {
//			@Override
//			public void change(int from, int to, int count) {
//				String toString = (String) lists.get(
//						Configure.curentPage - count).get(from);
//
//				lists.get(Configure.curentPage - count).add(from,
//						(String) lists.get(Configure.curentPage).get(to));
//				lists.get(Configure.curentPage - count).remove(from + 1);
//				lists.get(Configure.curentPage).add(to, toString);
//				lists.get(Configure.curentPage).remove(to + 1);
//
//				((DateAdapter) ((gridviews.get(Configure.curentPage - count))
//						.getAdapter())).notifyDataSetChanged();
//				((DateAdapter) ((gridviews.get(Configure.curentPage))
//						.getAdapter())).notifyDataSetChanged();
//			}
//		});
		gridviews.add(gridView);
		linear.addView(gridviews.get(i), param);
		return linear;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		gridviews = new ArrayList<DragGrid>();  //避免java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
		lists = new ArrayList<ArrayList<String>>();   //下面两项可以避免每次打开数据累加的
		lstDate = new ArrayList<String>();// 每一页的数据
	}
	
//	@Override
//	public void onPause() {
//		Log.i("a", "onPause 1111111");
//		gridviews = new ArrayList<DragGrid>();
//		lists = new ArrayList<ArrayList<String>>();
//		lstDate = new ArrayList<String>();// 每一页的数据
//		super.onPause();
//		
//	}
}
