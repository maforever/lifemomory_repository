package com.example.lifememory.activity;

import java.io.File;

import com.example.lifememory.R;
import com.example.lifememory.activity.model.BabyLuYin;
import com.example.lifememory.activity.model.PregnancyLuYin;
import com.example.lifememory.db.service.BabyDiaryLuYinService;
import com.example.lifememory.db.service.PregnancyDiaryLuYinService;
import com.example.lifememory.dialog.DialogAlert;
import com.example.lifememory.dialog.DialogAlertListener;
import com.example.lifememory.dialog.DialogInputListener;
import com.example.lifememory.dialog.DialogPregnancyJiShiBenReNameDialog;
import com.example.lifememory.utils.CopyFileFromData;
import com.example.lifememory.utils.TimeSwitcher;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class BabyLuYinReadActivity extends Activity {
	private int itemId = 0;
	private BabyLuYin luYinItem = null;
	private BabyDiaryLuYinService dbService = null;
	private ImageView anim_bg_image = null;  //���¼����ť�󱳾��Զ��л�ͼƬ
	private AnimationDrawable animationDrawable = null;
	private TextView titleTv = null;
	private MediaPlayer mediaPlayer = null;
	private CheckBox play_stop_cb = null;
	private SeekBar seekBar = null;
	private TextView currentTime, totalTime = null;
	private final static int SEEKBAR_INTERVAL = 200;
	private boolean isPlaying = false;				//�Ƿ����ڲ��ű�־λ��������ڲ��ţ��Ͳ��ϵĸ���seekbar�Ľ���
	private boolean isPause = false;				//�Ƿ���ͣ��־λ�����ڿ��ư�ť״̬
	private SeekBarProgressTask task = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				//������
				Toast.makeText(getBaseContext(), "�޸�¼�����Ƴɹ�!", 0).show();
				break;
			case 1:
				//ɾ��
				BabyLuYinReadActivity.this.finish();
				overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
				Toast.makeText(getBaseContext(), "ɾ��¼���ļ��ɹ�!", 0).show();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.pregnancydiray_luyin_read);
		
		dbService = new BabyDiaryLuYinService(this);
		
		findViews();
		initDatas();
		initViews();
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			mediaPlayerClose();
			try {
				Thread.sleep(SEEKBAR_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BabyLuYinReadActivity.this.finish();
			overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
			break;
		case R.id.rename:
			//������
			new DialogPregnancyJiShiBenReNameDialog(BabyLuYinReadActivity.this, listener2, titleTv.getText().toString()).show();
			break;
		case R.id.del:
			new DialogAlert(BabyLuYinReadActivity.this, listener, "��ȷ��ɾ������¼����?").show();
			//ɾ��
			break;
		case R.id.share:
			//����
			break;
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
	
	private class DelThread extends Thread {
		@Override
		public void run() {
			super.run();
			dbService.deleteItemByIdx(itemId);
			handler.sendEmptyMessage(1);
			File file = new File(luYinItem.getAudioPath());
			CopyFileFromData.getInstance().copyDatabase(getBaseContext(), "pregnancy_diary.db");
			if(file.exists()) file.delete();
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
			if(text != null &&	!"".equals(text)) {
				luYinItem.setTitle(text);
				titleTv.setText(text);
				new UpdateTitleThread().start();
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
	
	
	private class UpdateTitleThread extends Thread {
		@Override
		public void run() {
			super.run();
			dbService.updateItemByIdx(luYinItem.getIdx(), luYinItem.getTitle());
			handler.sendEmptyMessage(0);
		}
	}
	
	private void mediaPlayerClose() {
		if(mediaPlayer != null) {
			isPlaying = false;
			//���ڵ�isPlayingΪtrueʱ�����߳��в��ϵ�ÿ��SEEKBAR_INTERVAL��ȡ��Ƶ��ǰ�Ĳ��ŵĺ�������Ȼ���ٴ�ͻȻstop��release�� = null�󣬶��߳�
			//�е�isplaying��Ȼ����Ϊtrue�����ڴ˴�������Ӧ��ͣ��SEEKBAR_INTERVAL����,ʹ�������task.cancel(true);����
//			try {
//				Thread.sleep(SEEKBAR_INTERVAL);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			mediaPlayer.stop();
			mediaPlayer.release();
			if(task != null) {
				task.cancel(true);    //�����ڸ���seekbar���߳�ȡ��,���򱨴�
			}
			mediaPlayer = null;
		}
	}
	
	private void initDatas() {
		itemId = this.getIntent().getIntExtra("itemId", 0);
		if(itemId > 0) {
			luYinItem = dbService.findItemById(itemId);
		}
	}
	
	private void findViews() {
		anim_bg_image = (ImageView) this.findViewById(R.id.anim_bg_image);
		titleTv = (TextView) this.findViewById(R.id.title);
		play_stop_cb = (CheckBox) this.findViewById(R.id.btn2);
		seekBar = (SeekBar) this.findViewById(R.id.seekBar);
		currentTime = (TextView) this.findViewById(R.id.currentTime);
		totalTime = (TextView) this.findViewById(R.id.totalTime);
	}
	
	private void initViews() {
		animationDrawable = (AnimationDrawable) anim_bg_image.getDrawable();
		if(luYinItem != null) {
			titleTv.setText(luYinItem.getTitle());
			mediaPlayer = new MediaPlayer();
			try {
				mediaPlayer.setDataSource(luYinItem.getAudioPath());
				mediaPlayer.prepare();
				seekBar.setMax(mediaPlayer.getDuration());
				totalTime.setText(TimeSwitcher.getInstance().formatLongToTimeStrEn((long)mediaPlayer.getDuration()));
				currentTime.setText("00:00:00");
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						play_stop_cb.setBackgroundResource(R.drawable.pregnancy_recoder_play);
						play_stop_cb.setChecked(false);
						currentTime.setText("00:00:00");
						isPlaying = false;
						seekBar.setProgress(0);
						Toast.makeText(getBaseContext(), "�������", 0).show();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		play_stop_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					//����
					if(isPause) {
						mediaPlayer.start();
						isPause = false;
					}
					if(!isPlaying) {
						mediaPlayer.start();
						isPlaying = true;
						task = new SeekBarProgressTask();
						task.execute(SEEKBAR_INTERVAL);
					}
					play_stop_cb.setBackgroundResource(R.drawable.pregnancy_recoder_stop);
					animationDrawable.start();
				}else {
					if(!isPause) {
						Toast.makeText(getBaseContext(), "������ͣ", 0).show();
						play_stop_cb.setBackgroundResource(R.drawable.pregnancy_recoder_play);
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
				currentTime.setText(TimeSwitcher.getInstance().formatLongToTimeStrEn((long)seekBar.getProgress()));
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
	
	
	private class SeekBarProgressTask extends AsyncTask<Integer, Integer, String> {

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
			currentTime.setText(TimeSwitcher.getInstance().formatLongToTimeStrEn((long)values[0]));
			if(!isPlaying) {
				seekBar.setProgress(0);
				currentTime.setText("00:00:00");
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.closeDB();
	}
}









