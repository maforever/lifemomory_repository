package com.example.lifememory.adapter;

import com.example.lifememory.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ColorGridViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private int[] colorImageIds;
	private int currentSelectedId = -1;
	public ColorGridViewAdapter(Context context, int[] colorImageIds) {
		this.colorImageIds = colorImageIds;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return colorImageIds.length;
	}

	@Override
	public Object getItem(int position) {
		return colorImageIds[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setSelected(int selectedId) {
		this.currentSelectedId = selectedId;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView colorImage;
		ImageView seletedImage;
		convertView = inflater.inflate(R.layout.color_gridview_item, null);
		
		colorImage = (ImageView) convertView.findViewById(R.id.image);
		seletedImage = (ImageView) convertView.findViewById(R.id.selected);
		
		colorImage.setImageResource(colorImageIds[position]);
		if(currentSelectedId == position) {
			seletedImage.setVisibility(ViewGroup.VISIBLE);
		}else {
			seletedImage.setVisibility(ViewGroup.GONE);
		}
		
		return convertView;
	}

}
















