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
import com.example.lifememory.dialog.DialogAlertListener;
import com.example.lifememory.dialog.DialogBackUnSaveAlert;
import com.example.lifememory.dialog.DialogInputListener;
import com.example.lifememory.dialog.DialogPregnancyJiShiBenReNameDialog;
import com.example.lifememory.utils.CopyFileFromData;
import com.example.lifememory.utils.DateFormater;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

public class PregnancyJiShiBenAddActivity extends Activity{
	private EditText contentEt = null;
	private TextView titleTv = null;
//	private ScrollView scrollView = null;
//	private LinearLayout layout = null;
	private GridView toolBarGridView = null; //toolbar�ϵ�gridview
	private LinearLayout gridViewLayout = null;
	private PregnancyJiShiBenAddToolBarAdapter adapter = null; //toolbar��Gridview��������
	private int[] imageIds = {R.drawable.toolbar_list_icon, R.drawable.toolbar_font_icon, R.drawable.toolbar_color_icon,
			R.drawable.toolbar_timer_icon, R.drawable.toolbar_save_icon, R.drawable.toolbar_share_icon, R.drawable.toolbar_chgname_icon, R.drawable.toolbar_delall_icon
			};//toolbarͼ��
	private String[] titles = {"�ļ�","����", "��ɫ", "ʱ��", "����", "����", "����", "���Ϊ"}; //toolbar����
	private int[] textSize = {20, 25, 30}; //��������
	private int textSizeIndex = 0; //�����±�
	private PopupWindow colorPopupWindow = null;  //��ɫѡ��popwindow
	private int[] colorImageIds = {R.drawable.color_black_icon, R.drawable.color_blue_icon, R.drawable.color_green_icon,
								   R.drawable.color_pink_icon, R.drawable.color_red_icon, R.drawable.color_yellow_icon};//��ɫͼ��
	private ColorGridViewAdapter colorAdapter; //��ɫ������
	private GridView colorGridView; //��ɫgridview
	private LinearLayout bottonLayout = null; //����������ʾpopupwindow
	private int screenHeight; //��Ļ�ĸ߶�
	private int[] locations = new int[2];
	private KeyboardListenRelativeLayout relativeLayout;
	private int colorSelectedId = 0;
	private  int[] colors = new int[6];
	private boolean isSave = true;           //�Ƿ��������水ť���б���
	private boolean isShowKeyBoard = false;  //������Ƿ񵯳�
	private PregnancyDiaryJiShiBenService dbService = null; //�������±����ݿ����
	private PregnancyJiShiBen jishibenItem; //���ڼ��±�
	private String title;    //�ʼǱ���
	private boolean update = false;      //�Ƿ�����޸ĵķ�ʽ��������
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
//			PregnancyJiShiBenAddActivity.this.finish();
//			overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
			Toast.makeText(PregnancyJiShiBenAddActivity.this, "�����ռǳɹ�!", 0).show();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pregnancydiray_jishiben_add);
		
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		screenHeight = dm.heightPixels;
		
//		colors = {Color.BLACK, getResources().getColor(R.color.colorBlue), Color.GREEN, getResources().getColor(R.color.colorPink), Color.RED, getResources().getColor(R.color.colorPurple)};
		//��ʼ����ɫ
		colors[0] = Color.BLACK;
		colors[1] = getResources().getColor(R.color.colorBlue);
		colors[2] = Color.GREEN;
		colors[3] = getResources().getColor(R.color.colorPink);
		colors[4] = Color.RED;
		colors[5] = getResources().getColor(R.color.colorPurple);
		
		dbService = new PregnancyDiaryJiShiBenService(this);
		
		findViews();
		initColorPopupWindow();
		adapter = new PregnancyJiShiBenAddToolBarAdapter(this, imageIds, titles);
		toolBarGridView.setAdapter(adapter);
		toolBarGridView.setOnItemClickListener(new ToolBarGridViewItemClick());
//		ViewTreeObserver vto2 = scrollView.getViewTreeObserver();   
//	    vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
//	        @Override   
//	        public void onGlobalLayout() {  
//	        	scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
////	        	contentEt.setHeight(scrollView.getHeight());
//	        	android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) layout.getLayoutParams();
//	        	params.height = scrollView.getHeight();
//	        	layout.setLayoutParams(params);
//	        }   
//	    });   
	}
	
	private class ToolBarGridViewItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			adapter.setSelected(position);
			
			switch (position) {
			//�ļ�
			case 0:
				PregnancyJiShiBenAddActivity.this.finish();
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
				isSave = false;
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
				if(!update) {
					jishibenItem = new PregnancyJiShiBen();
					jishibenItem.setContent(contentEt.getText().toString());
					jishibenItem.setCreateDate(DateFormater.getInstatnce().getCurrentDate());
					jishibenItem.setCreateymd(DateFormater.getInstatnce().getYMD());
					jishibenItem.setCreateym(DateFormater.getInstatnce().getYM());
					jishibenItem.setTextColorIndex(colorSelectedId);
					jishibenItem.setTextSizeIndex(textSizeIndex);
					Log.i("a", "save state title = " + title);
					if(title != null && !"".equals(title)) {
						jishibenItem.setTitle(title);
					}else {
						jishibenItem.setTitle(jishibenItem.getCreateDate());
					}
					update = true;
					new SaveJiShiBenThread(jishibenItem).start();
				}else {
					jishibenItem.setContent(contentEt.getText().toString());
					jishibenItem.setTextColorIndex(colorSelectedId);
					jishibenItem.setTextSizeIndex(textSizeIndex);
					if(title != null && !"".equals(title)) {
						jishibenItem.setTitle(title);
					}else {
						jishibenItem.setTitle(jishibenItem.getCreateDate());
					}
					dbService.updateItemByCreateDate(jishibenItem);
					Toast.makeText(PregnancyJiShiBenAddActivity.this, "�����ռǳɹ�!", 0).show();
				}
				isSave = true;
				CopyFileFromData.getInstance().copyDatabase(PregnancyJiShiBenAddActivity.this, "pregnancy_diary.db");
				break;
			//����
			case 5:
				break;
			//����
			case 6:
				new DialogPregnancyJiShiBenReNameDialog(PregnancyJiShiBenAddActivity.this, pj_renameDilaoglistener,title == null ? "" : title).show();
				break;
			//���Ϊ
			case 7:
				
				break;
			}
		}
	}
	
	private class SaveJiShiBenThread extends Thread {
		PregnancyJiShiBen jishiben = null;
		public SaveJiShiBenThread(PregnancyJiShiBen jishiben) {
			this.jishiben = jishiben;
		}
		@Override
		public void run() {
			dbService.addPregnancyDiaryJiShiBen(jishiben);
			handler.sendEmptyMessage(0);
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
			if(text != null && !"".equals(text)) {
				title = text;
				isSave = false;
			}
		}

		@Override
		public void onDialogSave(Dialog dlg, Object param) {
			
		}

		@Override
		public void onDialogUnSave(Dialog dlg, Object param) {
			
		}
	};
	
	private void findViews() {
		contentEt = (EditText) this.findViewById(R.id.content);
		titleTv = (TextView) this.findViewById(R.id.titleTv);
		contentEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				isSave = false;  //�����ı������仯���ͱ���û�б���
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
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
			
			if(isSave) {
				//�����
				PregnancyJiShiBenAddActivity.this.finish();
				overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
			}else if(!isSave) {
				//û����
				new DialogBackUnSaveAlert(PregnancyJiShiBenAddActivity.this, new listener(), "�Ƿ񱣴浱ǰ����?").show();
			}
			break;
		}
	}
	
	private class listener implements DialogAlertListener {
		@Override
		public void onDialogCreate(Dialog dlg, Object param) {
			
		}

		@Override
		public void onDialogOk(Dialog dlg, Object param) {
			
		}

		@Override
		public void onDialogCancel(Dialog dlg, Object param) {
			
		}

		@Override
		public void onDialogSave(Dialog dlg, Object param) {
			if(!update) {
				jishibenItem = new PregnancyJiShiBen();
				jishibenItem.setContent(contentEt.getText().toString());
				jishibenItem.setCreateDate(DateFormater.getInstatnce().getCurrentDate());
				jishibenItem.setCreateymd(DateFormater.getInstatnce().getYMD());
				jishibenItem.setCreateym(DateFormater.getInstatnce().getYM());
				jishibenItem.setTextColorIndex(colorSelectedId);
				jishibenItem.setTextSizeIndex(textSizeIndex);
				Log.i("a", "save state title = " + title);
				if(title != null && !"".equals(title)) {
					jishibenItem.setTitle(title);
				}else {
					jishibenItem.setTitle(jishibenItem.getCreateDate());
				}
				isSave = true;
				new SaveJiShiBenThread(jishibenItem).start();
			}else {
				jishibenItem.setContent(contentEt.getText().toString());
				jishibenItem.setTextColorIndex(colorSelectedId);
				jishibenItem.setTextSizeIndex(textSizeIndex);
				Log.i("a", "update state title = " + title);
				if(title != null && !"".equals(title)) {
					jishibenItem.setTitle(title);
				}else {
					jishibenItem.setTitle(jishibenItem.getCreateDate());
				}
				dbService.updateItemByCreateDate(jishibenItem);
			}
			PregnancyJiShiBenAddActivity.this.finish();
			overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
		}

		@Override
		public void onDialogUnSave(Dialog dlg, Object param) {
			PregnancyJiShiBenAddActivity.this.finish();
			overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
		}
	}
	
	//��ʼ����ɫѡ��popupwindow
	private void initColorPopupWindow() {
		View contentView = this.getLayoutInflater().inflate(R.layout.colorselect_popupwindow, null);
		colorGridView = (GridView) contentView.findViewById(R.id.gridView);
		colorAdapter = new ColorGridViewAdapter(this, colorImageIds);
		colorGridView.setAdapter(colorAdapter);
		colorGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				colorAdapter.setSelected(position);
				colorSelectedId = position;
				contentEt.setTextColor(colors[colorSelectedId]);
				isSave = false;
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
	//�������
	private String getYM() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��");
		String dateStr = sdf.format(date); 
		return dateStr;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbService.closeDB();
	}
}





