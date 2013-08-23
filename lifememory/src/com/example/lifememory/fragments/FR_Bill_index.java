package com.example.lifememory.fragments;

import java.util.ArrayList;
import com.example.lifememory.R;
import com.example.lifememory.activity.BillInputActivity;
import com.example.lifememory.activity.IndexActivity;
import com.example.lifememory.activity.slidingmenu.menulib.SlidingMenu.OnClosedListener;
import com.example.lifememory.activity.slidingmenu.menulib.SlidingMenu.OnOpenListener;
import com.example.lifememory.adapter.BillIndexGridViewAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
/**
 * 闪电记， 记一笔，预算平衡表，信用卡，翻日历，查账户，
 * @author Administrator
 *
 */
public class FR_Bill_index extends Fragment {
	private TranslateAnimation left, right;
	private ImageView runImage;
	private ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();// 全部数据的集合集lists.size()==countpage;
	private TextView tv_page = null;
	private String[] firstPageTitle = new String[]{"闪电记", "记一笔", "预算平衡表", "今日"};
	private String[] secondPageTitle = new String[]{"信用卡", "翻日历", "查账户"};
	private int[] firstPageImageIds = new int[]{R.drawable.metro_drawable_flashbook, R.drawable.metro_drawable_book, R.drawable.metro_drawable_coupon, 1};
	private int[] secondPageImageIds = new int[]{R.drawable.metro_drawable_card, R.drawable.metro_drawable_calendar, R.drawable.metro_drawable_account};
	private BillIndexGridViewAdapter firstAdapter, secondAdapter;
	private IndexActivity indexActivity = null;
	private IndexActivity.MyOnTouchListener myOnTouchListener = null; // 添加滑动事件
	private int currentIndex = 0; // 用于记录当前滑动到的界面，根据界面适时的设置是否可滑动
	private ViewFlipper viewFlipper = null;
	private GridView firstGridView, secondGridView = null;
	private Animation flipperLeftInAnim, flipperLeftOutAnim, flipperRightInAnim, flipperRightOutAnim = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	public FR_Bill_index(IndexActivity indexActivity) {
		this.indexActivity = indexActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fr_mybill_index, container, false);
		findViews(view);
		GridViewAdapter();
		runAnimation();


		return view;
	}
	
	private void findViews(View view) {
		runImage = (ImageView) view.findViewById(R.id.run_image);
		tv_page = (TextView) view.findViewById(R.id.tv_page);
		viewFlipper = (ViewFlipper) view.findViewById(R.id.views);
		viewFlipper.setFocusable(true);
		firstGridView = (GridView) view.findViewById(R.id.firstGridView);
		secondGridView = (GridView) view.findViewById(R.id.secondGridView);
		
		firstGridView.setOnTouchListener(new FlipperOnTouchListener());
		secondGridView.setOnTouchListener(new FlipperOnTouchListener());
		viewFlipper.setOnTouchListener(new FlipperOnTouchListener());
		
		firstGridView.setOnItemClickListener(new MyOnItemClickListener1());
		secondGridView.setOnItemClickListener(new MyOnItemClickListener2());
		
		
		// 滑动事件
		myOnTouchListener = new IndexActivity.MyOnTouchListener() {
			public boolean onTouch(MotionEvent event) {
				// 手指按下并且没有显示leftmenufragment
				if (event.getAction() == MotionEvent.ACTION_DOWN
						&& !indexActivity.getSlidingMenu().isMenuShowing()) {
					if (viewFlipper.getDisplayedChild() != 0) {
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
	
	
	private void GridViewAdapter() {
		firstAdapter = new BillIndexGridViewAdapter(getActivity(), firstPageTitle, firstPageImageIds, true);
		secondAdapter = new BillIndexGridViewAdapter(getActivity(), secondPageTitle, secondPageImageIds, false);
		firstGridView.setAdapter(firstAdapter);
		secondGridView.setAdapter(secondAdapter);
	}

	
	public void runAnimation() {
		
		flipperLeftInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.filpper_left_in);
		flipperLeftOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.flipper_left_out);
		flipperRightInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.flipper_right_in);
		flipperRightOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.flipper_right_out);
		
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

	private class FlipperOnTouchListener implements OnTouchListener {
		float x1 = 0f, x2 = 0f;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				x1 = event.getX();
				break;
			case MotionEvent.ACTION_UP:
				x2 = event.getX();
				if(x1 - x2 < -50) {
					if(viewFlipper.getDisplayedChild() != 0) {
						viewFlipper.setInAnimation(flipperLeftInAnim);
						viewFlipper.setOutAnimation(flipperLeftOutAnim);
						viewFlipper.showPrevious();
						tv_page.setText(1 + "");
						tv_page.startAnimation(AnimationUtils.loadAnimation(
								getActivity(), R.anim.scale_out));
					}
//					Log.i("a", "viewFlipper childId = " + viewFlipper.getDisplayedChild());
				}else if(x1 - x2 > 50) {
					if(viewFlipper.getDisplayedChild() != 1) {
						viewFlipper.setInAnimation(flipperRightInAnim);
						viewFlipper.setOutAnimation(flipperRightOutAnim);
						viewFlipper.showNext();
						tv_page.setText(2 + "");
						tv_page.startAnimation(AnimationUtils.loadAnimation(
								getActivity(), R.anim.scale_out));
					}
//					Log.i("a", "viewFlipper childId = " + viewFlipper.getDisplayedChild());
				}
				break;
			}
			return false;
		}
	}
	
	private class MyOnItemClickListener1 implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				Toast.makeText(getActivity(), firstPageTitle[position], 0).show();
				break;
			case 1:
				Intent intent = new Intent(getActivity(), BillInputActivity.class);
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
				break;
			case 2:
				Toast.makeText(getActivity(), firstPageTitle[position], 0).show();
				break;
			case 3:
				Toast.makeText(getActivity(), "统计", 0).show();
				break;
			}
		}
	}
	
	private class MyOnItemClickListener2 implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
				Toast.makeText(getActivity(), secondPageTitle[position], 0).show();
				break;
			case 1:
				Toast.makeText(getActivity(), secondPageTitle[position], 0).show();
				break;
			case 2:
				Toast.makeText(getActivity(), secondPageTitle[position], 0).show();
				break;
			}
		}
	}
	
}
