package com.example.lifememory.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.lifememory.R;
import com.example.lifememory.activity.model.PregnancyJiShiBen;
import com.example.lifememory.activity.views.KeyboardListenRelativeLayout;
import com.example.lifememory.activity.views.KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener;
import com.example.lifememory.adapter.ColorGridViewAdapter;
import com.example.lifememory.adapter.PregnancyJiShiBenAddToolBarAdapter;
import com.example.lifememory.db.service.PregnancyDiaryJiShiBenService;
import com.example.lifememory.dialog.DialogInputListener;
import com.example.lifememory.dialog.DialogPregnancyJiShiBenReNameDialog;
import com.example.lifememory.utils.CopyFileFromData;
import com.example.lifememory.utils.DateFormater;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class PregnancyJiShiBenEditActivity extends Activity{
	private EditText contentEt = null;  //����
	private TextView titleTv = null;    //����
//	private ScrollView scrollView = null;
//	private LinearLayout layout = null;
	private GridView toolBarGridView = null; //toolbar�ϵ�gridview
	private LinearLayout gridViewLayout = null;
	private PregnancyJiShiBenAddToolBarAdapter adapter = null; //toolbar��Gridview��������
	private int[] imageIds = {R.drawable.toolbar_list_icon, R.drawable.toolbar_font_icon, R.drawable.toolbar_color_icon,
			R.drawable.toolbar_timer_icon, R.drawable.toolbar_save_icon, R.drawable.toolbar_share_icon, R.drawable.toolbar_chgname_icon, R.drawable.toolbar_delall_icon
			};//toolbarͼ��
	private int[] colorImageIds = {R.drawable.color_black_icon, R.drawable.color_blue_icon, R.drawable.color_green_icon,
			   R.drawable.color_pink_icon, R.drawable.color_red_icon, R.drawable.color_yellow_icon};//��ɫͼ��
	private String[] titles = {"�ļ�","����", "��ɫ", "ʱ��", "����", "����", "����", "���Ϊ"}; //toolbar����
	private int[] textSize = {20, 25, 30}; //��������
	private int textSizeIndex = 0; //�����±�
	private PopupWindow colorPopupWindow = null;  //��ɫѡ��popwindow
	private ColorGridViewAdapter colorAdapter; //��ɫ������
	private GridView colorGridView; //��ɫgridview
	private LinearLayout bottonLayout = null; //����������ʾpopupwindow
	private int screenHeight; //��Ļ�ĸ߶�
	private int[] locations = new int[2];
	private KeyboardListenRelativeLayout relativeLayout;
	private int colorSelectedId = 0;
	private  int[] colors = new int[6];
	private boolean isShowKeyBoard = false;  //������Ƿ񵯳�
	private PregnancyDiaryJiShiBenService dbService = null; //�������±����ݿ����
	private PregnancyJiShiBen jishibenItem; //���ڼ��±�
	private int itemId;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			//��ȡ
			case 0:
				initViews();
				initColorPopupWindow();
				break;
			//�޸�
			case 1:
				PregnancyJiShiBenEditActivity.this.finish();
				overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
				Toast.makeText(PregnancyJiShiBenEditActivity.this, "�ռ��޸ĳɹ�!", 0).show();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pregnancydiray_jishiben_edit);
		
		//��Ļ�߶�
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		screenHeight = dm.heightPixels;
		//��ʼ����ɫ
		initColors();
		//��ʼ�����ݿ����
		dbService = new PregnancyDiaryJiShiBenService(this);
		
		findViews();
		new InitDatasThread().start();
		//��ʼ����ɫpopupWindow

		adapter = new PregnancyJiShiBenAddToolBarAdapter(this, imageIds, titles);
		toolBarGridView.setAdapter(adapter);
		toolBarGridView.setOnItemClickListener(new ToolBarGridViewItemClick());
		
//		initDatas();
//		initViews();
	}
	
	private class InitDatasThread extends Thread {
		@Override
		public void run() {
			initDatas();
			handler.sendEmptyMessage(0);
		}
	}
	
	//��ʼ��PregnancyJiShiBen
	private void initDatas() {
		itemId = this.getIntent().getIntExtra("itemId", 0);
		if(itemId > 0) {
			jishibenItem = dbService.findItemById(itemId);
			textSizeIndex = jishibenItem.getTextSizeIndex();
			colorSelectedId = jishibenItem.getTextColorIndex();
		}
	}
	
	//�����ͼ
	private void initViews() {
		contentEt.setTextColor(colors[jishibenItem.getTextColorIndex()]);
		contentEt.setTextSize(textSize[jishibenItem.getTextSizeIndex()]);
		contentEt.setText(jishibenItem.getContent());
		titleTv.setText(jishibenItem.getTitle());
	}
	
	//��ʼ����ɫ
	private void initColors() {
		colors[0] = Color.BLACK;
		colors[1] = getResources().getColor(R.color.colorBlue);
		colors[2] = Color.GREEN;
		colors[3] = getResources().getColor(R.color.colorPink);
		colors[4] = Color.RED;
		colors[5] = getResources().getColor(R.color.colorPurple);
	}
	
	private class ToolBarGridViewItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			adapter.setSelected(position);
			
			switch (position) {
			//�ļ�
			case 0:
				PregnancyJiShiBenEditActivity.this.finish();
				overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
				break;
			//����
			case 1:
				textSizeIndex++;
				if(textSizeIndex >= textSize.length) {
					textSizeIndex = 0;
				}
				if(colorPopupWindow.isShowing()) {
					colorPopupWindow.dismiss();
				}
				contentEt.setTextSize(textSize[textSizeIndex]);
				break;
			//��ɫ
			case 2:
				if(colorPopupWindow.isShowing()) {
					colorPopupWindow.dismiss();
				}else {
					bottonLayout.getLocationInWindow(locations);
					colorPopupWindow.showAtLocation(bottonLayout, Gravity.BOTTOM, 0, screenHeight - locations[1]);
				}
				break;
			//ʱ��
			case 3:
				contentEt.append(getDate());
				break;
			//����
			case 4:
				jishibenItem.setContent(contentEt.getText().toString());
				jishibenItem.setTextColorIndex(colorSelectedId);
				jishibenItem.setTextSizeIndex(textSizeIndex);
				jishibenItem.setUpdateDate(DateFormater.getInstatnce().getCurrentDate());
				jishibenItem.setUpdateymd(DateFormater.getInstatnce().getYMD());
				jishibenItem.setUpdateym(DateFormater.getInstatnce().getYM());
				new UpdateThread(jishibenItem).start();
//				dbService.updateItem(jishibenItem);
				CopyFileFromData.getInstance().copyDatabase(PregnancyJiShiBenEditActivity.this, "pregnancy_diary.db");
				break;
			//����
			case 5:
				break;
			//����
			case 6:
				new DialogPregnancyJiShiBenReNameDialog(PregnancyJiShiBenEditActivity.this, pj_renameDilaoglistener,"name").show();
				break;
			//���Ϊ
			case 7:
				
				break;
			}
		}
	}
	
	//�޸ļ��±��ռǵ��߳�
	private class UpdateThread extends Thread {
		PregnancyJiShiBen jishiben = null;
		public UpdateThread(PregnancyJiShiBen jishiben) {
			this.jishiben = jishiben;
		}
		@Override
		public void run() {
			dbService.updateItem(jishiben);
			handler.sendEmptyMessage(1);
		}
	}
	
	private DialogInputListener pj_renameDilaoglistener = new DialogInputListener() {
		
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
			Toast.makeText(getApplicationContext(), text, 0).show();
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
	
	private void findViews() {
		contentEt = (EditText) this.findViewById(R.id.content);
		titleTv = (TextView) this.findViewById(R.id.title);
//		scrollView = (ScrollView) this.findViewById(R.id.scrollView);
//		layout = (LinearLayout) this.findViewById(R.id.layout);
		bottonLayout = (LinearLayout) this.findViewById(R.id.bottom);
		//ˮƽ�϶�gridview
		relativeLayout = (KeyboardListenRelativeLayout) findViewById(R.id.keyboardRelativeLayout);
        relativeLayout.setOnKeyboardStateChangedListener(new IOnKeyboardStateChangedListener() {
			
			public void onKeyboardStateChanged(int state) {
				switch (state) {
				case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE://���������
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					isShowKeyBoard = false;
					if(colorPopupWindow.isShowing()) {
						colorPopupWindow.dismiss();
						bottonLayout.getLocationInWindow(locations);
						colorPopupWindow.showAtLocation(bottonLayout, Gravity.BOTTOM, 0, screenHeight - locations[1]);
					}
					break;
				case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW://�������ʾ
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
					isShowKeyBoard = true;
					if(colorPopupWindow.isShowing()) {
						colorPopupWindow.dismiss();
						bottonLayout.getLocationInWindow(locations);
						colorPopupWindow.showAtLocation(bottonLayout, Gravity.BOTTOM, 0, screenHeight - locations[1]);
					}
					break;
				default:
					break;
				}
			}
		});
		toolBarGridView = new GridView(this);
		
		LayoutParams params = new LayoutParams(8 * 80,
				LayoutParams.WRAP_CONTENT);
		toolBarGridView.setLayoutParams(params);
		toolBarGridView.setSelector(R.drawable.pregnancyjishiben_toolbarselector);
		toolBarGridView.setNumColumns(8);
		gridViewLayout = (LinearLayout) this.findViewById(R.id.gridViewLayout);
		gridViewLayout.addView(toolBarGridView);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			PregnancyJiShiBenEditActivity.this.finish();
			overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
			break;
		}
	}
	
	
	//��ʼ����ɫѡ��popupwindow
	private void initColorPopupWindow() {
		View contentView = this.getLayoutInflater().inflate(R.layout.colorselect_popupwindow, null);
		colorGridView = (GridView) contentView.findViewById(R.id.gridView);
		colorAdapter = new ColorGridViewAdapter(this, colorImageIds);
		colorAdapter.setSelected(colorSelectedId);
		colorGridView.setAdapter(colorAdapter);
		colorGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				colorAdapter.setSelected(position);
				colorSelectedId = position;
				contentEt.setTextColor(colors[colorSelectedId]);
				if(colorPopupWindow.isShowing()) colorPopupWindow.dismiss(); 

			}
		});
		colorPopupWindow = new PopupWindow(contentView, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		colorPopupWindow.setFocusable(true);
	}
	
	//���ʱ��
	private String getDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��");
		String dateStr = sdf.format(date); 
		return dateStr;
	}
	
	//��ȡ������
	private String getYMD() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
		String dateStr = sdf.format(date); 
		return dateStr;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.closeDB();
	}
}





