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
	private ImageView imageView = null;      //��ƵƵ�ʱ仯ͼƬ
	private ImageView anim_bg_image = null;  //���¼����ť�󱳾��Զ��л�ͼƬ
	private AnimationDrawable animationDrawable = null;
	private CheckBox start_pause_cb = null;		  //¼������İ�ť
	private CheckBox play_stor_cb = null;		  //���Ž���İ�ť
	private LinearLayout listLayout = null;       //��������¼���б�
	private LinearLayout stopLayout = null;       //ֹͣ
	private LinearLayout dropLayout = null;       //����
	private LinearLayout saveLayout = null;		  //����
	private LinearLayout seekBarLayout = null;
	private PregnancyLuYin luyinItem = null;
	private boolean isStop = false;    //�Ƿ�ֹͣ¼��
	private SoundMeter soundMeter = null;
	private List<File> recodeFiles = new ArrayList<File>();     //���ڴ�����е�¼���ļ������ں��ڵĺϲ�
	private String fileName = null;					//¼������
	private String title = null;					//¼������
	private TextView titleTv = null;
	private String  recodeSavePath;                 //¼����ŵ�ַ
	private PregnancyDiaryLuYinService dbService = null;
	private static final int POLL_INTERVAL = 200;   //ÿ0.3���ػ���ƵƵ��ͼƬ
	private Long totalMs = 0l;     //�ܹ������������ڼ�¼¼��ʱ��
	private Long startMs, endMs;   //���¼����ֹͣʱ�ĺ�����
	private TextView currentTimeTv, totalTimeTv;    //��ǰ���ŵ�ʱ�䣬 ��ʱ����
	private boolean isRecodePage = true;            //�Ƿ���¼������  falseΪԤ������ 
    private Handler mHandler = new Handler() {
    	public void handleMessage(android.os.Message msg) {
    		//����
    		if(msg.what == 0) {
    			mediaStop(true);
    			PregnancyLuYinAddActivity.this.finish();
    			overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
    			Toast.makeText(getBaseContext(), "¼���ļ�����ɹ�!", 0).show();
    		}
    		//��ֹͣ��ť��
    		if(msg.what == 1) {
    			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						play_stor_cb.setBackgroundResource(R.drawable.pregnancy_recoder_play);
						play_stor_cb.setChecked(false);
						currentTimeTv.setText("00:00:00");
						isPlaying = false;
						seekBar.setProgress(0);
						Toast.makeText(getBaseContext(), "�������", 0).show();
					}
				});
    			currentTimeTv.setText("00:00:00");
				totalTimeTv.setText(TimeSwitcher.getInstance().formatLongToTimeStrEn((long)mediaPlayer.getDuration()));
				seekBar.setMax(mediaPlayer.getDuration()); //seekbar�����ֵ��Ϊ��Ƶ��ʱ�䳤��
    		}
    	};
    };
    private TextView time = null;                   //��ʾ¼������
    private SeekBar seekBar = null;                 //Ԥ�������¼���϶���
    private MediaPlayer mediaPlayer = null;
	private boolean isPlaying = false;				//�Ƿ����ڲ��ű�־λ��������ڲ��ţ��Ͳ��ϵĸ���seekbar�Ľ���
	private boolean isPause = false;				//�Ƿ���ͣ��־λ�����ڿ��ư�ť״̬
	private SeekBarTask barTask;					//�߳���ÿ0.3��Ϊ�������һ��seekbar����
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
					//��ʼ¼��
					if(!isStop) {
						fileName = DateFormater.getInstatnce().getCurrentDate();
					}
					start_pause_cb.setBackgroundResource(R.drawable.pregnancy_recoder_pause);
					animationDrawable.start();
					
					startMs = System.currentTimeMillis();  //��¼ÿ�ο�ʼ¼������ʵ������
					
					listLayout.setVisibility(ViewGroup.GONE);    //�б���ͼ����
					stopLayout.setVisibility(ViewGroup.VISIBLE); //ֹͣ��ͼ��ʾ
					String recodeName = UUID.randomUUID().toString();
					String recodeFilePath = recodeSavePath + File.separator + recodeName + ".amr";
					soundMeter.start(recodeSavePath, recodeName + ".amr");
					mHandler.postDelayed(mPollTask, POLL_INTERVAL);  //ÿ200�������һ���߳�mPollTask
					recodeFiles.add(new File(recodeFilePath));
				}else {
					//��ͣ
					soundMeter.stop();
					totalMs += (endMs - startMs);    //ÿ����ͣ����ô�¼�������¼��������֮ͣ�䣩�ܺ����������ڼ�������¼���ĺ�����
					mHandler.removeCallbacks(mPollTask);
					start_pause_cb.setBackgroundResource(R.drawable.pregnancy_recoder_start);
					animationDrawable.selectDrawable(0);  //ѡ�е�һ��ͼƬ
					animationDrawable.stop();
				}
			}
		});
		
		play_stor_cb.setBackgroundResource(R.drawable.pregnancy_recoder_play);
		play_stor_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//����
				if(isChecked) {
					if(isPause) {
						mediaPlayer.start();
						isPause = false;
					}
					
					if(!isPlaying) {
						Toast.makeText(getBaseContext(), "���ſ�ʼ", 0).show();
						mediaPlayer.start();
						isPlaying = true;
						barTask = new SeekBarTask();
						barTask.execute(POLL_INTERVAL);
					}
					play_stor_cb.setBackgroundResource(R.drawable.pregnancy_recoder_stop);
					animationDrawable.start();
					
				}else {
					//��ͣ
					if(!isPause) {
						Toast.makeText(getBaseContext(), "������ͣ", 0).show();
						play_stor_cb.setBackgroundResource(R.drawable.pregnancy_recoder_play);
						mediaPlayer.pause();
						isPause = true;
						animationDrawable.selectDrawable(0);  //ѡ�е�һ��ͼƬ
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
			updateDisplay(amp);   //��������ͼ�仯
			updateTime();		  //ʱ��仯
			//��ʱ���񣬱���0.3���Ժ����µ���mPollTask
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);
		}
	};
	
	private void updateTime() {
		endMs = System.currentTimeMillis();  //��ǰ������
		time.setText(TimeSwitcher.getInstance().formatLongToTimeStr(endMs - startMs + totalMs));   //��ǰ������ȥ��ʼ¼��ʱ�����������ϴ���ͣǰ������
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
//			barTask.cancel(true);    //�����ڸ���seekbar���߳�ȡ��,���򱨴�
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
			isRecodePage = false;  //����Ԥ������
			
			if(title == null || "".equals(title)) {
				titleTv.setText(fileName);
			}else {
				titleTv.setText(title);
			}
			
			
			//ֹͣ��ť
			soundMeter.stop();
			animationDrawable.selectDrawable(0);  //ѡ�е�һ��ͼƬ
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
			
			time.setVisibility(ViewGroup.GONE);   //����ʱ����ͼ
			seekBarLayout.setVisibility(ViewGroup.VISIBLE);   //��ʾ�϶���
			stopLayout.setVisibility(ViewGroup.GONE);   //����ֹͣ��ͼ
			start_pause_cb.setVisibility(ViewGroup.GONE); //����¼������İ�ť
			play_stor_cb.setVisibility(ViewGroup.VISIBLE); //��ʾ���Ž���İ�ť
			dropLayout.setVisibility(ViewGroup.VISIBLE);
			saveLayout.setVisibility(ViewGroup.VISIBLE);
			break;
		case R.id.rename:
			//������
//			Toast.makeText(getApplicationContext(), "rename", 0).show();
			if(isRecodePage) {
				//¼������
				new DialogPregnancyJiShiBenReNameDialog(PregnancyLuYinAddActivity.this, listener2, "").show();
			}else {
				new DialogPregnancyJiShiBenReNameDialog(PregnancyLuYinAddActivity.this, listener2, titleTv.getText().toString()).show();
			}
			break;
		case R.id.btn2:
			//������ͣ��ť
			break;
		case R.id.drop:
			//������ť
			mediaStop(false);
			time.setVisibility(ViewGroup.VISIBLE);   //����ʱ����ͼ
			seekBarLayout.setVisibility(ViewGroup.GONE);   //��ʾ�϶���
			stopLayout.setVisibility(ViewGroup.VISIBLE);   //����ֹͣ��ͼ
			start_pause_cb.setVisibility(ViewGroup.VISIBLE); //����¼������İ�ť
			play_stor_cb.setVisibility(ViewGroup.GONE); //��ʾ���Ž���İ�ť
			dropLayout.setVisibility(ViewGroup.GONE);
			saveLayout.setVisibility(ViewGroup.GONE);
			time.setText("00ʱ00��00��");   //¼��ʱ������
			totalMs = 0l;				  //¼��ʱ������
			isStop = false;
			imageView.setImageResource(R.drawable.record_tone_highlight_1);
			break;
		case R.id.save:
			//���水ť
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
					titleTv.setText("¼���ļ�");
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
	
	//����¼���ļ���Ϣ���߳�
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
				barTask.cancel(true);    //�����ڸ���seekbar���߳�ȡ��,���򱨴�
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
