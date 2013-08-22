package com.example.lifememory.adapter;

import com.example.lifememory.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PregnancyJSBPopWindowGridAdapter extends BaseAdapter {
	private int[] imageIds;
	private String[] titles;
	private LayoutInflater inflater = null;
	public PregnancyJSBPopWindowGridAdapter(Context context, int[] imageIds, String[] titles) {
		this.imageIds = imageIds;
		this.titles = titles;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return imageIds.length;
	}

	@Override
	public Object getItem(int position) {
		return imageIds[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		TextView textView;
		ViewHolder vh;
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.pregnancy_jishiben_popwindow_griditem, null);
			imageView = (ImageView) convertView.findViewById(R.id.imageView);
			textView = (TextView) convertView.findViewById(R.id.title);
			vh = new ViewHolder();
			vh.textView = textView;
			vh.imageView = imageView;
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
			textView = vh.textView;
			imageView = vh.imageView;
		}
		
		textView.setText(titles[position]);
		imageView.setImageResource(imageIds[position]);
		
		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView textView;
	}
	
}







