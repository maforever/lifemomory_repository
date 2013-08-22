package com.example.lifememory.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.lifememory.R;
import com.example.lifememory.activity.slidingmenu.menulib.SlidingMenu;
import com.example.lifememory.fragments.PregnancyDiaryFragment;
import com.example.lifememory.fragments.FR_PregnancyDiary_JiShiBen_Grid;
import com.example.lifememory.fragments.FR_PregnancyDiary_LuYinBi_Grid;
import com.example.lifememory.fragments.EFragment;
import com.example.lifememory.fragments.FFragment;
import com.example.lifememory.fragments.LeftMenuFragment;
import com.example.lifememory.slidingmenu.menulib.app.SlidingFragmentActivity;
import com.example.lifememory.utils.CopyFileFromData;

public class IndexActivity extends SlidingFragmentActivity {
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private final static String LEFT_MENU_TAG = "LeftMenu";
	public FragmentManager fm = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setBehindContentView(R.layout.fr_menu_behind);
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fl_menu_behind,
						new LeftMenuFragment(getSupportFragmentManager(), this),
						LEFT_MENU_TAG).commit();

		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		getSlidingMenu().setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		setContentView(R.layout.fr_index_content);
		fm = getSupportFragmentManager();
		Fragment fragment = new PregnancyDiaryFragment();
		fragments.add(fragment);
		fragment = new FR_PregnancyDiary_JiShiBen_Grid();
		fragments.add(fragment);
		fragment = new FR_PregnancyDiary_LuYinBi_Grid();
		fragments.add(fragment);
		fragment = new EFragment();
		fragments.add(fragment);
		fragment = new FFragment();
		fragments.add(fragment);
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fl_index_content, fragments.get(0)).commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			if (getSlidingMenu().isMenuShowing()) {
				Toast.makeText(this, "显示", 0).show();
			} else {
				Toast.makeText(this, "不显示", 0).show();
				CopyFileFromData.getInstance().copyDatabase(getBaseContext(), "pregnancy_diary");
				IndexActivity.this.finish();
			}
			
			break;
		}
		return true;
	}
	/**
	 * 自定义拖动事件，分发给fragment
	 */
	private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
			10);

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		for (MyOnTouchListener listener : onTouchListeners) {
			listener.onTouch(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
		onTouchListeners.add(myOnTouchListener);
	}

	public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
		onTouchListeners.remove(myOnTouchListener);
	}

	public interface MyOnTouchListener {
		public boolean onTouch(MotionEvent ev);
	}
	
	
}
