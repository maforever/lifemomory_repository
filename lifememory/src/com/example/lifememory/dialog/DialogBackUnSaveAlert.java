package com.example.lifememory.dialog;


import com.example.lifememory.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class DialogBackUnSaveAlert extends Dialog implements OnClickListener
{
	private TextView btnSave;
	private TextView btnUnSave;
	private TextView btnCancel;
	private DialogAlertListener listener;
	private String title;
	private Object param;

	public DialogBackUnSaveAlert(Context context, DialogAlertListener listener, String title)
	{
		super(context, R.style.dialog);
		this.listener = listener;
		this.title = title;
	}

	public DialogBackUnSaveAlert(Context context, DialogAlertListener listener, String title, Object param)
	{
		super(context, R.style.dialog);
		this.listener = listener;
		this.title = title;
		this.param = param;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_backunsave_alert);
		setCancelable(false);
		setCanceledOnTouchOutside(false);

		btnSave = (TextView) findViewById(R.id.save);
		btnSave.setOnClickListener(this);
		btnUnSave = (TextView) findViewById(R.id.unsave);
		btnUnSave.setOnClickListener(this);
		btnCancel = (TextView) findViewById(R.id.cancel);
		btnCancel.setOnClickListener(this);

		TextView txtTitle = (TextView) findViewById(R.id.title);
		txtTitle.setText(title);

		if (listener != null)
		{
			listener.onDialogCreate(this, param);
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v == btnSave)
			onBtnSave();
		else if (v == btnCancel)
			onBtnCancel();
		else if	(v ==btnUnSave)
			onBtnUnSave();
	}

	private void onBtnSave()
	{
		cancel();
		if (listener != null)
		{
			listener.onDialogSave(this, param);
		}
	}

	private void onBtnUnSave()
	{
		cancel();
		if (listener != null)
		{
			listener.onDialogUnSave(this, param);
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
}
