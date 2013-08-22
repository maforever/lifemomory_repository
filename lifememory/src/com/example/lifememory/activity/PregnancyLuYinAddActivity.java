package com.example.lifememory.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.UUID;
import com.example.lifememory.R;
import com.example.lifememory.activity.model.PregnancyLuYin;
import com.example.lifememory.db.service.PregnancyDiaryLuYinService;
import com.example.lifememory.dialog.DialogInputListener;
import com.example.lifememory.dialog.DialogPregnancyJiShiBenReNameDialog;
import com.example.lifememory.utils.CopyFileFromData;
import com.example.lifememory.utils.DateFormater;
import com.example.lifememory.utils.MutilRecodeManager;
import com.example.lifememory.utils.SDCardManager;
import com.example.lifememory.utils.SoundMeter;
import com.example.lifememory.utils.TimeSwitcher;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData.Item;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PregnancyLuYinAddActivity extends Activity {
	private ImageView imageView = null;      //音频频率变化图片
	private ImageView anim_bg_image = null;  //点击录音按钮后背景自动切换图片
	private AnimationDrawable animationDrawable = null;
	private CheckBox start_pause_cb = null;		  //录音界面的按钮
	private CheckBox play_stor_cb = null;		  //播放界面的按钮
	private LinearLayout listLayout = null;       //返回所以录音列表
	private LinearLayout stopLayout = null;       //停止
	private LinearLayout dropLayout = null;       //丢弃
	private LinearLayout saveLayout = null;		  //保存
	private LinearLayout seekBarLayout = null;
	private PregnancyLuYin luyinItem = null;
	private boolean isStop = false;    //是否停止录音
	private SoundMeter soundMeter = null;
	private List<File> recodeFiles = new ArrayList<File>();     //用于存放所有的录音文件，用于后期的合并
	private String fileName = null;					//录音名称
	private String title = null;					//录音标题
	private TextView titleTv = null;
	private String  recodeSavePath;                 //录音存放地址
	private PregnancyDiaryLuYinService dbService = null;
	private static final int POLL_INTERVAL = 200;   //每0.3秒重回音频频率图片
	private Long totalMs = 0l;     //总共毫秒数，用于记录录音时间
	private Long startMs, endMs;   //点击录音与停止时的毫秒数
	private TextView currentTimeTv, totalTimeTv;    //当前播放的时间， 总时间数
	private boolean isRecodePage = true;            //是否是录音界面  false为预览界面 
    private Handler mHandler = new Handler() {
    	public void handleMessage(android.os.Message msg) {
    		//保存
    		if(msg.what == 0) {
    			mediaStop(true);
    			PregnancyLuYinAddActivity.this.finish();
    			overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
    			Toast.makeText(getBaseContext(), "录音文件保存成功!", 0).show();
    		}
    		//按停止按钮后
    		if(msg.what == 1) {
    			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						play_stor_cb.setBackgroundResource(R.drawable.pregnancy_recoder_play);
						play_stor_cb.setChecked(false);
						currentTimeTv.setText("00:00:00");
						isPlaying = false;
						seekBar.setProgress(0);
						Toast.makeText(getBaseContext(), "播放完毕", 0).show();
					}
				});
    			currentTimeTv.setText("00:00:00");
				totalTimeTv.setText(TimeSwitcher.getInstance().formatLongToTimeStrEn((long)mediaPlayer.getDuration()));
				seekBar.setMax(mediaPlayer.getDuration()); //seekbar的最大值设为音频的时间长度
    		}
    	};
    };
    private TextView time = null;                   //显示录音秒数
    private SeekBar seekBar = null;                 //预览界面的录音拖动条
    private MediaPlayer mediaPlayer = null;
	private boolean isPlaying = false;				//是否正在播放标志位，如果是在播放，就不断的更新seekbar的进度
	private boolean isPause = false;				//是否暂停标志位，用于控制按钮状态
	private SeekBarTask barTask;					//线程中每0.3秒为间隔更新一次seekbar进度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.pregnancydiray_luyin_add);
		
		dbService = new PregnancyDiaryLuYinService(this);
		soundMeter = new SoundMeter();
		recodeSavePath = SDCardManager.getInstance().getPregnancyRecodePath();
		findViews();
		initViews();
	}
	
	private void findViews() {
		time = (TextView) this.findViewById(R.id.time);
		seekBar = (SeekBar) this.findViewById(R.id.seekBar);
		start_pause_cb = (CheckBox) this.findViewById(R.id.btn);
		play_stor_cb = (CheckBox) this.findViewById(R.id.btn2);
		anim_bg_image = (ImageView) this.findViewById(R.id.anim_bg_image);
		imageView = (ImageView) this.findViewById(R.id.imageView);
		listLayout = (LinearLayout) this.findViewById(R.id.list);
		stopLayout = (LinearLayout) this.findViewById(R.id.stop);
		saveLayout = (LinearLayout) this.findViewById(R.id.save);
		dropLayout = (LinearLayout) this.findViewById(R.id.drop);
		seekBarLayout = (LinearLayout) this.findViewById(R.id.seekBarLayout);
		currentTimeTv = (TextView) this.findViewById(R.id.currentTime);
		totalTimeTv = (TextView) this.findViewById(R.id.totalTime);
		titleTv = (TextView) this.findViewById(R.id.titleTv);
	}
	
	
	private void initViews() {
		animationDrawable = (AnimationDrawable) anim_bg_image.getDrawable();
		start_pause_cb.setBackgroundResource(R.drawable.pregnancy_recoder_start);
		start_pause_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					//开始录音
					if(!isStop) {
						fileName = DateFormater.getInstatnce().getCurrentDate();
					}
					start_pause_cb.setBackgroundResource(R.drawable.pregnancy_recoder_pause);
					animationDrawable.start();
					
					startMs = System.currentTimeMillis();  //记录每次开始录音的其实毫秒数
					
					listLayout.setVisibility(ViewGroup.GONE);    //列表视图隐藏
					stopLayout.setVisibility(ViewGroup.VISIBLE); //停止视图显示
					String recodeName = UUID.randomUUID().toString();
					String recodeFilePath = recodeSavePath + File.separator + recodeName + ".amr";
					soundMeter.start(recodeSavePath, recodeName + ".amr");
					mHandler.postDelayed(mPollTask, POLL_INTERVAL);  //每200毫秒调用一次线程mPollTask
					recodeFiles.add(new File(recodeFilePath));
				}else {
					//暂停
					soundMeter.stop();
					totalMs += (endMs - startMs);    //每次暂停保存该次录音（点击录音与点击暂停之间）总毫秒数，用于计算所有录音的毫秒数
					mHandler.removeCallbacks(mPollTask);
					start_pause_cb.setBackgroundResource(R.drawable.pregnancy_recoder_start);
					animationDrawable.selectDrawable(0);  //选中第一个图片
					animationDrawable.stop();
				}
			}
		});
		
		play_stor_cb.setBackgroundResource(R.drawable.pregnancy_recoder_play);
		play_stor_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//播放
				if(isChecked) {
					if(isPause) {
						mediaPlayer.start();
						isPause = false;
					}
					
					if(!isPlaying) {
						Toast.makeText(getBaseContext(), "播放开始", 0).show();
						mediaPlayer.start();
						isPlaying = true;
						barTask = new SeekBarTask();
						barTask.execute(POLL_INTERVAL);
					}
					play_stor_cb.setBackgroundResource(R.drawable.pregnancy_recoder_stop);
					animationDrawable.start();
					
				}else {
					//暂停
					if(!isPause) {
						Toast.makeText(getBaseContext(), "播放暂停", 0).show();
						play_stor_cb.setBackgroundResource(R.drawable.pregnancy_recoder_play);
						mediaPlayer.pause();
						isPause = true;
						animationDrawable.selectDrawable(0);  //选中第一个图片
						animationDrawable.stop();
					}
				}
			}
		});
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mediaPlayer.seekTo(seekBar.getProgress());
				currentTimeTv.setText(TimeSwitcher.getInstance().formatLongToTimeStrEn((long)seekBar.getProgress()));
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});
	}
	
	
	private  class SeekBarTask extends AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			while(isPlaying) {
				try {
					Thread.sleep(params[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.publishProgress(mediaPlayer.getCurrentPosition());
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			seekBar.setProgress(values[0]);
			currentTimeTv.setText(TimeSwitcher.getInstance().formatLongToTimeStrEn((long)values[0]));
			if(!isPlaying) {
				seekBar.setProgress(0);
				currentTimeTv.setText("00:00:00");
			}
		}
		
	}
	
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = soundMeter.getAmplitude();
			updateDisplay(amp);   //音量背景图变化
			updateTime();		  //时间变化
			//定时任务，比如0.3秒以后重新调用mPollTask
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);
		}
	};
	
	private void updateTime() {
		endMs = System.currentTimeMillis();  //当前的秒数
		time.setText(TimeSwitcher.getInstance().formatLongToTimeStr(endMs - startMs + totalMs));   //当前秒数减去开始录音时的秒数加上上次暂停前的秒数
	}
	
	private void updateDisplay(double signalEMA) {
		
		
		switch ((int) signalEMA) {
		case 0:
		case 1:
			imageView.setImageResource(R.drawable.record_tone_highlight_1);
			break;
		case 2:
		case 3:
			imageView.setImageResource(R.drawable.record_tone_highlight_2);
			break;
		case 4:
		case 5:
			imageView.setImageResource(R.drawable.record_tone_highlight_3);
			break;
		case 6:
		case 7:
			imageView.setImageResource(R.drawable.record_tone_highlight_4);
			break;
		case 8:
			imageView.setImageResource(R.drawable.record_tone_highlight_5);
		case 9:
			imageView.setImageResource(R.drawable.record_tone_highlight_6);
			break;
		case 10:
			imageView.setImageResource(R.drawable.record_tone_highlight_7);
		case 11:
			imageView.setImageResource(R.drawable.record_tone_highlight_8);
			break;
		default:
			imageView.setImageResource(R.drawable.record_tone_highlight_9);
			break;
		}
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			mediaStop(false);
//			barTask.cancel(true);    //将用于更新seekbar的线程取消,否则报错
//			if(mediaPlayer != null) {
//				isPlaying = false;
//				mediaPlayer.stop();
//				mediaPlayer.release();
//				Toast.makeText(getBaseContext(), "back", 0).show();
//				
////				mediaPlayer = null;
//			}
			PregnancyLuYinAddActivity.this.finish();
			overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
			break;
		case R.id.list:
			PregnancyLuYinAddActivity.this.finish();
			overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
//			Toast.makeText(getApplicationContext(), "list", 0).show();
			break;
		case R.id.stop:
			isStop = true;
			isRecodePage = false;  //进入预览界面
			
			if(title == null || "".equals(title)) {
				titleTv.setText(fileName);
			}else {
				titleTv.setText(title);
			}
			
			
			//停止按钮
			soundMeter.stop();
			animationDrawable.selectDrawable(0);  //选中第一个图片
			animationDrawable.stop();
			
			final String filePath = recodeSavePath + File.separator + fileName + ".amr";
			
			luyinItem = new PregnancyLuYin();
			if(title == null || "".equals(title.trim())) {
				luyinItem.setTitle(fileName);
			}else {
				luyinItem.setTitle(title);
			}
			luyinItem.setCreateDate(fileName);
			luyinItem.setCreateYMD(DateFormater.getInstatnce().getYMD());
			luyinItem.setCreateYM(DateFormater.getInstatnce().getYM());
			luyinItem.setAudioPath(filePath);
			
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					MutilRecodeManager.getInstance().mergeAllAmrFiles(recodeFiles, filePath);
					recodeFiles = new ArrayList<File>();
					mediaPlayer = new MediaPlayer();
					try {
						mediaPlayer.setDataSource(filePath);
//						Log.i("a", "filepath = " + filePath);
						mediaPlayer.prepare();
						mHandler.sendEmptyMessage(1);
						
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			
			time.setVisibility(ViewGroup.GONE);   //隐藏时间视图
			seekBarLayout.setVisibility(ViewGroup.VISIBLE);   //显示拖动条
			stopLayout.setVisibility(ViewGroup.GONE);   //隐藏停止视图
			start_pause_cb.setVisibility(ViewGroup.GONE); //隐藏录音界面的按钮
			play_stor_cb.setVisibility(ViewGroup.VISIBLE); //显示播放界面的按钮
			dropLayout.setVisibility(ViewGroup.VISIBLE);
			saveLayout.setVisibility(ViewGroup.VISIBLE);
			break;
		case R.id.rename:
			//重命名
//			Toast.makeText(getApplicationContext(), "rename", 0).show();
			if(isRecodePage) {
				//录音界面
				new DialogPregnancyJiShiBenReNameDialog(PregnancyLuYinAddActivity.this, listener2, "").show();
			}else {
				new DialogPregnancyJiShiBenReNameDialog(PregnancyLuYinAddActivity.this, listener2, titleTv.getText().toString()).show();
			}
			break;
		case R.id.btn2:
			//播放暂停按钮
			break;
		case R.id.drop:
			//抛弃按钮
			mediaStop(false);
			time.setVisibility(ViewGroup.VISIBLE);   //隐藏时间视图
			seekBarLayout.setVisibility(ViewGroup.GONE);   //显示拖动条
			stopLayout.setVisibility(ViewGroup.VISIBLE);   //隐藏停止视图
			start_pause_cb.setVisibility(ViewGroup.VISIBLE); //隐藏录音界面的按钮
			play_stor_cb.setVisibility(ViewGroup.GONE); //显示播放界面的按钮
			dropLayout.setVisibility(ViewGroup.GONE);
			saveLayout.setVisibility(ViewGroup.GONE);
			time.setText("00时00分00秒");   //录音时间清零
			totalMs = 0l;				  //录音时间清零
			isStop = false;
			imageView.setImageResource(R.drawable.record_tone_highlight_1);
			break;
		case R.id.save:
			//保存按钮
			new SaveLuYinThread().start();
			break;
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
			title = text;
			
			if(isRecodePage) {
				if(title == null || "".equals(title)) {
					titleTv.setText("录音文件");
				}else {
					titleTv.setText(title);
				}
			}else {
				if(title == null || "".equals(title)) {
					titleTv.setText(fileName);
				}else {
					titleTv.setText(title);
				}
				luyinItem.setTitle(titleTv.getText().toString());
			}
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
	
	//保存录音文件信息的线程
	private class SaveLuYinThread extends Thread {
		@Override
		public void run() {
			super.run();
			dbService.addLuYinItem(luyinItem);
			CopyFileFromData.getInstance().copyDatabase(PregnancyLuYinAddActivity.this, "pregnancy_diary.db");
			mHandler.sendEmptyMessage(0);
		}
	}
	
	private void mediaStop(boolean isSave) {
		if(mediaPlayer != null) {
			if(barTask != null) {
				barTask.cancel(true);    //将用于更新seekbar的线程取消,否则报错
			}
			isPlaying = false;
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		if(!isSave) {
			File file = new File(recodeSavePath + File.separator + fileName + ".amr");
			if(file.exists()) {
				file.delete();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.closeDB();
	}
}
