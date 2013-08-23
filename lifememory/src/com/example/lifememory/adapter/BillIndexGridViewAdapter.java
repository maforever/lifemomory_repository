package com.example.lifememory.adapter;

import com.example.lifememory.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BillIndexGridViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private String[] titles;
	private int[] imageIds;
	private boolean isFirstPage; // 用于标示是否是第一页的gridview数据，第一页有个特殊的显示界面

	public BillIndexGridViewAdapter(Context context, String[] titles,
			int[] imageIds, boolean isFirstPage) {
		inflater = LayoutInflater.from(context);
		this.titles = titles;
		this.imageIds = imageIds;
		this.isFirstPage = isFirstPage;
	}

	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public Object getItem(int position) {
		return titles[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		TextView title;
		ViewHolder vh;
		if (isFirstPage) {
			// 是第一页的
			if (position == 3) {
				View view = inflater.inflate(
						R.layout.gridview_item_bill_index_today, null);
				TextView todayAccount = (TextView) view
						.findViewById(R.id.benriAccount);
				TextView monthAccount = (TextView) view
						.findViewById(R.id.benyueAccount);
				TextView shengyuAccount = (TextView) view
						.findViewById(R.id.shengyuAccount);
				return view;
			}
		}

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gridview_item_bill_index,
					null);
			imageView = (ImageView) convertView.findViewById(R.id.imageView);
			title = (TextView) convertView.findViewById(R.id.title);
			vh = new ViewHolder();
			vh.imageView = imageView;
			vh.title = title;
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
			title = vh.title;
			imageView = vh.imageView;
		}
		title.setText(titles[position]);
		imageView.setImageResource(imageIds[position]);

		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView title;
	}
}
