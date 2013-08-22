package com.example.lifememory.dialog;



import com.example.lifememory.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class DialogPregnancyJiShiBenReNameDialog extends Dialog implements OnClickListener
{
	private TextView btnOk;
	private TextView btnCancel;
	private DialogInputListener listener;
	private String text;
	private Object param;
	private boolean ifNum=false;

	public DialogPregnancyJiShiBenReNameDialog(Context context, DialogInputListener listener, String text)
	{
		super(context, R.style.dialog);
		this.listener = listener;
		this.text = text;
		
	}
	public DialogPregnancyJiShiBenReNameDialog(Context context, DialogInputListener listener, String title, String text,boolean ifSetNum)
	{
		super(context, R.style.dialog);
		this.listener = listener;
		this.text = text;
		this.ifNum=ifSetNum;
	}

	public DialogPregnancyJiShiBenReNameDialog(Context context, DialogInputListener listener, String title, String text, Object param)
	{
		super(context, R.style.dialog);
		this.listener = listener;
		this.text = text;
		this.param = param;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_rename);
		setCancelable(false);
		setCanceledOnTouchOutside(false);

		btnOk = (TextView) findViewById(R.id.sure);
		btnOk.setOnClickListener(this);
		btnCancel = (TextView) findViewById(R.id.cancel);
		btnCancel.setOnClickListener(this);

		
		EditText edtText = (EditText) findViewById(R.id.name);
		edtText.setText(text);
		CharSequence text = edtText.getText();
		if(text instanceof Spannable) {
			Spannable spannableText = (Spannable) text;
			Selection.setSelection(spannableText, edtText.length());
		}
		
		
		if(ifNum)
		{
			setNumberInput();
		}
		
		if (listener != null)
		{
			listener.onDialogCreate(this, param);
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v == btnOk)
			onBtnOk();
		else if (v == btnCancel)
			onBtnCancel();
	}

	private void onBtnOk()
	{
		cancel();
		if (listener != null)
		{
			EditText edtText = (EditText) findViewById(R.id.name);
			listener.onDialogOk(this, param);
			listener.onDialogInput(this, edtText.getText().toString());
		}
	}

	private void onBtnCancel()
	{
		cancel();
		if (listener != null)
		{
			listener.onDialogCancel(this, param);
		}
	}
	
	public void setNumberInput()
	{ 
		EditText edtText = (EditText) findViewById(R.id.name);
		edtText.setText(text);		
		edtText.setInputType(InputType.TYPE_CLASS_PHONE);
		edtText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
	}
}
