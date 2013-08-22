package com.example.lifememory.dialog;

import android.app.Dialog;

public interface DialogInputListener extends DialogAlertListener
{
	public void onDialogInput(Dialog dlg, String text);
}
