package com.example.lifememory.activity;

import com.example.lifememory.R;
import com.example.lifememory.activity.model.Bill;
import com.example.lifememory.utils.AppAplication;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class BillInputActivity extends Activity {
	private TextView zhichuBtn, shouruBtn, zhuanzhangBtn;
	private CheckBox baoxiaoCb = null;
	private int currentIndex = 0;
	private ViewFlipper viewFlipper = null;
	private LayoutInflater inflater;
	private View childView1, childView2, childView3;
	private PopupWindow calculator = null;    //计算器
	private View popWinParentView = null;
	private TextView zhichuJine, shouruJine, zhuanzhangJine, jineTv;
	private AppAplication myApplication;
	private String jie_txt;
	private Bill bill = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.bill_input_layout);
		
		inflater = LayoutInflater.from(this);
		bill = new Bill();
		findViews();
		initViews();
		initCalculator();
	}
	
	private void findViews() {
		zhichuBtn = (TextView) this.findViewById(R.id.zhichu);
		shouruBtn = (TextView) this.findViewById(R.id.shouru);
		zhuanzhangBtn = (TextView) this.findViewById(R.id.zhuanzhang);
		baoxiaoCb = (CheckBox) this.findViewById(R.id.baoxiaocb);
		viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
		childView1 = viewFlipper.getChildAt(0);
		childView2 = viewFlipper.getChildAt(1);
		childView3 = viewFlipper.getChildAt(2);
		zhichuJine = (TextView) childView1.findViewById(R.id.jine);
		shouruJine = (TextView) childView2.findViewById(R.id.jine);
		zhuanzhangJine = (TextView) childView3.findViewById(R.id.jine);
		
		
		popWinParentView = this.findViewById(R.id.popWinParent);
	}
	
	private void initViews() {
		zhichuBtn.setBackgroundResource(R.drawable.exit_demo_mode_btn_pressed);
		zhichuBtn.setTextColor(Color.WHITE);
		
	}
	
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.zhichu:
			//支出按钮
			changeBtnBack(0);
			break;
		case R.id.shouru:
			//收入按钮
			changeBtnBack(1);
			break;
		case R.id.zhuanzhang:
			//转账按钮
			changeBtnBack(2);
			break;
		case R.id.jinelayout:
			//金额
			showCalculator();
			break;
		case R.id.leixinglayout:
			break;
		case R.id.zhanghulayout:
			break;
		case R.id.riqilayout:
			break;
		case R.id.chengyuanlayout:
			break;
		case R.id.beizhulayout:
			break;
		case R.id.baoxiaolayout:
			if(baoxiaoCb.isChecked()) {
				baoxiaoCb.setChecked(false);
			}else {
				baoxiaoCb.setChecked(true);
			}
			break;
		case R.id.back:
			BillInputActivity.this.finish();
			overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
			break;
		case R.id.save:
			break;
		}
	}
	
	private void changeBtnBack(int index) {
		currentIndex = index;
		switch (index) {
		case 0:
			//支出
			zhichuBtn.setBackgroundResource(R.drawable.exit_demo_mode_btn_pressed);
			zhichuBtn.setTextColor(Color.WHITE);
			shouruBtn.setBackgroundResource(R.drawable.exit_demo_mode_btn_normal);
			shouruBtn.setTextColor(Color.BLACK);
			zhuanzhangBtn.setBackgroundResource(R.drawable.exit_demo_mode_btn_normal);
			zhuanzhangBtn.setTextColor(Color.BLACK);
			
			if(viewFlipper.getDisplayedChild() != 0) {
				if(viewFlipper.getDisplayedChild() == 1) {
					viewFlipper.showPrevious();
				}else if(viewFlipper.getDisplayedChild() == 2) {
					viewFlipper.showPrevious();
					viewFlipper.showPrevious();
				}
			}
			
			
			
			
			break;
		case 1:
			//收入
			shouruBtn.setBackgroundResource(R.drawable.exit_demo_mode_btn_pressed);
			shouruBtn.setTextColor(Color.WHITE);
			zhuanzhangBtn.setBackgroundResource(R.drawable.exit_demo_mode_btn_normal);
			zhuanzhangBtn.setTextColor(Color.BLACK);
			zhichuBtn.setBackgroundResource(R.drawable.exit_demo_mode_btn_normal);
			zhichuBtn.setTextColor(Color.BLACK);
			
			if(viewFlipper.getDisplayedChild() != 1) {
				if(viewFlipper.getDisplayedChild() == 0) {
					viewFlipper.showNext();
				}else if(viewFlipper.getDisplayedChild() == 2) {
					viewFlipper.showPrevious();
				}
			}
			
			break;
		case 2:
			//转账
			zhuanzhangBtn.setBackgroundResource(R.drawable.exit_demo_mode_btn_pressed);
			zhuanzhangBtn.setTextColor(Color.WHITE);
			zhichuBtn.setBackgroundResource(R.drawable.exit_demo_mode_btn_normal);
			zhichuBtn.setTextColor(Color.BLACK);
			shouruBtn.setBackgroundResource(R.drawable.exit_demo_mode_btn_normal);
			shouruBtn.setTextColor(Color.BLACK);
			
			if(viewFlipper.getDisplayedChild() != 2) {
				if(viewFlipper.getDisplayedChild() == 0) {
					viewFlipper.showNext();
					viewFlipper.showNext();
				}else if(viewFlipper.getDisplayedChild() == 1) {
					viewFlipper.showNext();
				}
			}
			break;
		}
		refreshJinETextView();
	}
	
	
	
	//初始化计算器 popwindow
	private void initCalculator() {
		View contentView = inflater.inflate(R.layout.popwindow_bill_counter, null);
		calculator = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		TextView cal_add = (TextView) contentView.findViewById(R.id.cal_add);
		TextView cal_minus = (TextView) contentView.findViewById(R.id.cal_minus);
		TextView cal_multiply = (TextView) contentView.findViewById(R.id.cal_multiply);
		TextView cal_divide = (TextView) contentView.findViewById(R.id.cal_divide);
		TextView cal_one = (TextView) contentView.findViewById(R.id.cal_one);
		TextView cal_two = (TextView) contentView.findViewById(R.id.cal_two);
		TextView cal_three = (TextView) contentView.findViewById(R.id.cal_three);
		TextView cal_four = (TextView) contentView.findViewById(R.id.cal_four);
		TextView cal_five = (TextView) contentView.findViewById(R.id.cal_five);
		TextView cal_six = (TextView) contentView.findViewById(R.id.cal_six);
		TextView cal_seven = (TextView) contentView.findViewById(R.id.cal_seven);
		TextView cal_eight = (TextView) contentView.findViewById(R.id.cal_eight);
		TextView cal_nine = (TextView) contentView.findViewById(R.id.cal_nine);
		TextView cal_zero = (TextView) contentView.findViewById(R.id.cal_zero);
		TextView cal_dot = (TextView) contentView.findViewById(R.id.cal_dot);
		LinearLayout cal_equal = (LinearLayout) contentView.findViewById(R.id.cal_equal);
		LinearLayout cal_del =  (LinearLayout) contentView.findViewById(R.id.cal_del);
		ImageView cal_sure = (ImageView) contentView.findViewById(R.id.cal_sure);
		cal_add.setOnClickListener(new CalculatorBtnClickListener());
		cal_minus.setOnClickListener(new CalculatorBtnClickListener());
		cal_multiply.setOnClickListener(new CalculatorBtnClickListener());
		cal_divide.setOnClickListener(new CalculatorBtnClickListener());
		cal_one.setOnClickListener(new CalculatorBtnClickListener());
		cal_two.setOnClickListener(new CalculatorBtnClickListener());
		cal_three.setOnClickListener(new CalculatorBtnClickListener());
		cal_four.setOnClickListener(new CalculatorBtnClickListener());
		cal_five.setOnClickListener(new CalculatorBtnClickListener());
		cal_six.setOnClickListener(new CalculatorBtnClickListener());
		cal_seven.setOnClickListener(new CalculatorBtnClickListener());
		cal_eight.setOnClickListener(new CalculatorBtnClickListener());
		cal_nine.setOnClickListener(new CalculatorBtnClickListener());
		cal_zero.setOnClickListener(new CalculatorBtnClickListener());
		cal_dot.setOnClickListener(new CalculatorBtnClickListener());
		cal_del.setOnClickListener(new CalculatorBtnClickListener());
		cal_sure.setOnClickListener(new CalculatorBtnClickListener());
		cal_equal.setOnClickListener(new CalculatorBtnClickListener());
		calculator.setFocusable(true);
	}
	
	private void showCalculator() {
		calculator.showAtLocation(popWinParentView, Gravity.BOTTOM, 0, 0);
	}
	
	private class CalculatorBtnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cal_add:
				Toast.makeText(getBaseContext(), "cal_add", 0).show();
				break;
			case R.id.cal_minus:
				Toast.makeText(getBaseContext(), "cal_minus", 0).show();
				break;
			case R.id.cal_multiply:
				Toast.makeText(getBaseContext(), "cal_multiply", 0).show();
				break;
			case R.id.cal_divide:
				Toast.makeText(getBaseContext(), "cal_divide", 0).show();
				break;
			case R.id.cal_sure:
				if(calculator.isShowing()) {
					calculator.dismiss();
				}
				break;
			case R.id.cal_equal:
				break;
			case R.id.cal_one:
				onClickNum(1);
				break;
			case R.id.cal_two:
				onClickNum(2);
				break;
			case R.id.cal_three:
				onClickNum(3);
				break;
			case R.id.cal_four:
				onClickNum(4);
				break;
			case R.id.cal_five:
				onClickNum(5);
				break;
			case R.id.cal_six:
				onClickNum(6);
				break;
			case R.id.cal_seven:
				onClickNum(7);
				break;
			case R.id.cal_eight:
				onClickNum(8);
				break;
			case R.id.cal_nine:
				onClickNum(9);
				break;
			case R.id.cal_dot:
				break;
			case R.id.cal_zero:
				onClickNum(0);
				break;
			case R.id.cal_del:
				break;
			}
		}
	}
	
	private void refreshJinETextView() {
		if(bill.getJine() == null || "".equals(bill.getJine())) {
			jie_txt = "0";
		}else {
			jie_txt = bill.getJine();
		}
		zhichuJine.setText(jie_txt);
		shouruJine.setText(jie_txt);
		zhuanzhangJine.setText(jie_txt);
	}
	
	private void onClickNum(int numStr) {
		jineTv = (TextView) viewFlipper.getCurrentView().findViewById(R.id.jine);
		Long num = Long.parseLong(jineTv.getText().toString());
		if(num == 0) {
			jie_txt = "" + numStr;
		}else {
			jie_txt = jineTv.getText().toString() + numStr;
		}
		jineTv.setText(jie_txt);
		bill.setJine(jie_txt);
	}
	
}










