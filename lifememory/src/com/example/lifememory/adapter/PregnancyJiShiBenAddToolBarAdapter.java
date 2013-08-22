package com.example.lifememory.adapter;

import com.example.lifememory.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PregnancyJiShiBenAddToolBarAdapter extends BaseAdapter {
	private int[] imageIds;
	private String[] titles;
	private LayoutInflater inflater;
	private int currentSelected = -1;
	public PregnancyJiShiBenAddToolBarAdapter(Context context, int[] imageIds, String[] titles) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageIds = imageIds;
		this.titles = titles;
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

	public void setSelected(int selectId) {
		currentSelected = selectId;
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		ImageView imageView;
		TextView title;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.pregnancy_jishiben_add_toolbar_gridviewitem, null);
			imageView = (ImageView) convertView.findViewById(R.id.image);
			title = (TextView) convertView.findViewById(R.id.text);
			vh = new ViewHolder();
			vh.imageView = imageView;
			vh.title = title;
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
			title = vh.title;
			imageView = vh.imageView;
		}
		
		if(currentSelected == position) {
			convertView.setBackgroundResource(R.drawable.toolbar_color_bg);
		}else {
			convertView.setBackgroundResource(0);
		}
		
		imageView.setImageResource(imageIds[position]);
		title.setText(titles[position]);
		return convertView;
	}
	
	
	static class ViewHolder {
		ImageView imageView;
		TextView title;
	}
}







