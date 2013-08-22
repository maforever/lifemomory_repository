package com.example.lifememory.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.example.lifememory.R;
import com.example.lifememory.adapter.GuideActivityPagerViewAdapter;
import com.example.lifememory.utils.AppSharedPreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
	

//欢迎界面，第一次使用应用会跳转到向导界面,第二次直接跳转到应用主界面
public class WelcomeActivity extends Activity{
	
	private ImageView imageView = null;
	private TranslateAnimation left, right;
	private AppSharedPreference appSp = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			if(appSp.isFirstUse()) {
				//不是第一次使用跳转到首界面
				Intent intent = new Intent(WelcomeActivity.this, IndexActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
				overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
			}else {
				//第一次使用应用跳转到指引界面
				appSp.setFirstUse();
				Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
				overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);

			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		appSp = new AppSharedPreference(WelcomeActivity.this);
		findViews();
		runAnimation();
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		};
		timer.schedule(task, 3000);
	}
	
	private void findViews() {
		imageView = (ImageView) this.findViewById(R.id.run_image);
	}
	
	private void runAnimation() {

		right = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
				Animation.RELATIVE_TO_PARENT, -1f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f);
		left = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f, Animation.RELATIVE_TO_PARENT, 0f);
		right.setDuration(10000);
//		left.setDuration(25000);
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
				imageView.startAnimation(left);
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
				imageView.startAnimation(right);
			}
		});
		imageView.startAnimation(right);
	}
}
