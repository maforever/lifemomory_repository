package com.example.lifememory.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.lifememory.R;
import com.example.lifememory.activity.model.PregnancyJiShiBen;
import com.example.lifememory.db.service.PregnancyDiaryJiShiBenService;
import com.example.lifememory.dialog.DialogAlert;
import com.example.lifememory.dialog.DialogAlertListener;
import com.example.lifememory.dialog.DialogInputListener;
import com.example.lifememory.dialog.DialogPregnancyJiShiBenReNameDialog;
import com.example.lifememory.utils.MyAnimations;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PregnancyJiShiBenReadActivity extends Activity{
	private TextView contentEt = null;
	private int[] textSize = {20, 25, 30}; //字体数据
	private  int[] colors = new int[6];
	private PregnancyJiShiBen jishibenItem; //孕期记事本
	private int itemId = 0;
	private TextView titleTv = null;
	private PregnancyDiaryJiShiBenService dbService = null;
	//仿Path
	private boolean areButtonsShowing;
	private RelativeLayout composerButtonsWrapper;
	private ImageView composerButtonsShowHideButtonIcon;
	private RelativeLayout composerButtonsShowHideButton;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			//读取
			case 0:
				initViews();
				break;
			//删除
			case 1:
				PregnancyJiShiBenReadActivity.this.finish();
				overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
				Toast.makeText(PregnancyJiShiBenReadActivity.this, "删除日记成功!", 0).show();
				break;
			//重命名
			case 2:
				Toast.makeText(PregnancyJiShiBenReadActivity.this, "日记重命名成功!", 0).show();
				break;
		
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pregnancydiray_jishiben_read);
		
		dbService = new PregnancyDiaryJiShiBenService(this);
		
		MyAnimations.initOffset(PregnancyJiShiBenReadActivity.this);
		
		initColors();   //初始化颜色数组
		findViews();    //实例化视图
		
		
		new InitDatasThread().start(); //在线程中初始化数据，在主线程中渲染视图
//		initDatas();    //初始化数据
//		initViews();	//填充视图内容
		
		
		composerButtonsShowHideButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!areButtonsShowing) {
					MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
					composerButtonsShowHideButtonIcon
							.startAnimation(MyAnimations.getRotateAnimation(0,
									-270, 300));
				} else {
					MyAnimations
							.startAnimationsOut(composerButtonsWrapper, 300);
					composerButtonsShowHideButtonIcon
							.startAnimation(MyAnimations.getRotateAnimation(
									-270, 0, 300));
				}
				areButtonsShowing = !areButtonsShowing;
			}
		});
		for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
			composerButtonsWrapper.getChildAt(i).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							switch (view.getId()) {
							//修改
							case R.id.composer_button_edit:
								Intent intent = new Intent(PregnancyJiShiBenReadActivity.this, PregnancyJiShiBenEditActivity.class);
								intent.putExtra("itemId", itemId);
								PregnancyJiShiBenReadActivity.this.startActivity(intent);
								overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
								break;
							//分享
							case R.id.composer_button_share:
								break;
							//重命名
							case R.id.composer_button_rename:
								new DialogPregnancyJiShiBenReNameDialog(PregnancyJiShiBenReadActivity.this, listener2, titleTv.getText().toString()).show();
								break;
							//删除
							case R.id.composer_button_del:
								new DialogAlert(PregnancyJiShiBenReadActivity.this, listener, "您确定删除此条日记吗?").show();
								break;
								
							}
						}
					});
		}
		
		composerButtonsShowHideButton.startAnimation(MyAnimations
				.getRotateAnimation(0, 360, 200));
	}
	
	private class RenameThread extends Thread {
		String title;
		public RenameThread(String title) {
			this.title = title;
		}
		@Override
		public void run() {
			dbService.updateTitleById(itemId, title, getDate(), getYMD());
			handler.sendEmptyMessage(2);
		}
	}
	
	private DialogInputListener listener2 = new DialogInputListener() {
		
		@Override
		public void onDialogOk(Dialog dlg, Object param) {
			
		}
		
		@Override
		public void onDialogCreate(Dialog dlg, Object param) {
			
		}
		
		@Override
		public void onDialogCancel(Dialog dlg, Object param) {
			
		}
		
		@Override
		public void onDialogInput(Dialog dlg, String text) {
			titleTv.setText(text);
			new RenameThread(text).start();
		}

		@Override
		public void onDialogSave(Dialog dlg, Object param) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDialogUnSave(Dialog dlg, Object param) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private class DelThread extends Thread {
		@Override
		public void run() {
			dbService.deleteItemById(jishibenItem.getIdx());
			handler.sendEmptyMessage(1);
		}
	}
	
    private DialogAlertListener listener = new DialogAlertListener() {
		
		@Override
		public void onDialogOk(Dialog dlg, Object param) {
			new DelThread().start();
		}
		
		@Override
		public void onDialogCreate(Dialog dlg, Object param) {
			
		}
		
		@Override
		public void onDialogCancel(Dialog dlg, Object param) {
		}

		@Override
		public void onDialogSave(Dialog dlg, Object param) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDialogUnSave(Dialog dlg, Object param) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void initDatas() {
		itemId = this.getIntent().getIntExtra("itemId", 0);
		if(itemId > 0) {
			jishibenItem = dbService.findItemById(itemId);
		}
	}
	
	private class InitDatasThread extends Thread {
		@Override
		public void run() {
			initDatas();
			handler.sendEmptyMessage(0);
		}
	}
	
	private void initColors() {
		colors[0] = Color.BLACK;
		colors[1] = getResources().getColor(R.color.colorBlue);
		colors[2] = Color.GREEN;
		colors[3] = getResources().getColor(R.color.colorPink);
		colors[4] = Color.RED;
		colors[5] = getResources().getColor(R.color.colorPurple);
	}
	
	private void findViews() {
		contentEt = (TextView) this.findViewById(R.id.content);
		titleTv = (TextView) this.findViewById(R.id.title);
		composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
		composerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
		composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			PregnancyJiShiBenReadActivity.this.finish();
			overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
			break;
		}
	}
	//填充视图
	private void initViews() {
		contentEt.setTextColor(colors[jishibenItem.getTextColorIndex()]);
		contentEt.setTextSize(textSize[jishibenItem.getTextSizeIndex()]);
		contentEt.setText(jishibenItem.getContent());
		titleTv.setText(jishibenItem.getTitle());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(itemId > 0) {
			new InitDatasThread().start();
		}
	}
	
	//获得时间
	private String getDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		String dateStr = sdf.format(date); 
		return dateStr;
	}
	//获取年月日
	private String getYMD() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String dateStr = sdf.format(date); 
		return dateStr;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.closeDB();
	}
}





