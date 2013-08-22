package com.example.lifememory.fragments;



import com.example.lifememory.R;
import com.example.lifememory.activity.IndexActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FFragment extends Fragment {
private IndexActivity indexActivity = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fr_f, null, false);
	}
}
