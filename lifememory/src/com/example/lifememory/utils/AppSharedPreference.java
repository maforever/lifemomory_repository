package com.example.lifememory.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;

public class AppSharedPreference {
	private Context context = null;
	public AppSharedPreference(Context context) {
		this.context = context;
	}
	
	public void setFirstUse() {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isFirstUse", true);
		editor.commit();
	}
	
	public boolean isFirstUse() {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		return sp.getBoolean("isFirstUse", false);
		
	}
}
