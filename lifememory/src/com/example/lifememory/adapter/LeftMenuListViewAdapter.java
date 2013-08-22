package com.example.lifememory.adapter;

import java.util.List;

import com.example.lifememory.R;
import com.example.lifememory.activity.model.LeftMenuItem;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeftMenuListViewAdapter extends BaseAdapter {
	private LayoutInflater inflater = null;
	private List<LeftMenuItem> menuItems = null;
	private int currentSelected = -1;
	
	public LeftMenuListViewAdapter(Context context, List<LeftMenuItem> menuItems) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.menuItems = menuItems;
	}
	
	@Override
	public int getCount() {
		return menuItems.size();
	}

	@Override
	public Object getItem(int position) {
		return menuItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setSelected(int selectedId) {
		this.currentSelected = selectedId;
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		TextView title;
		TextView subTitle;
		ImageView image;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.lefmenu_listview_item, null);
			title = (TextView) convertView.findViewById(R.id.title);
			subTitle = (TextView) convertView.findViewById(R.id.subTitle);
			image = (ImageView) convertView.findViewById(R.id.imageView);
			vh = new ViewHolder();
			vh.title = title;
			vh.subTitle = subTitle;
			vh.image = image;
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
			title = vh.title;
			subTitle = vh.subTitle;
			image = vh.image;
		}
		
		title.setText(menuItems.get(position).getTitle());
		subTitle.setText(menuItems.get(position).getSubTitle());
		image.setImageResource(menuItems.get(position).getImageId());
		
		if(currentSelected == position) {
			convertView.setBackgroundResource(R.drawable.leftmenuselected);
		}else {
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView subTitle;
		ImageView image;
	}
	
}
