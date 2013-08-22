package com.example.lifememory.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.lifememory.R;
import com.example.lifememory.activity.model.PregnancyJiShiBen;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class PregnancyDiaryJiShiListAdapter extends BaseAdapter {
	private Context context;
	private List<PregnancyJiShiBen> items = null;
	private LayoutInflater inflater = null;
	private boolean isFirstGroup; // 是否是第一组的标志位，因为如果是第一组，会有一个添加的按钮
	private boolean isShowDeleteTag = false; // 是否显示删除标记
	public static List<Integer> idxs = new ArrayList<Integer>();   //存放id的集合

	public PregnancyDiaryJiShiListAdapter(Context context,
			List<PregnancyJiShiBen> jishibenItems, boolean isFirstGroup) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.items = jishibenItems;
		this.isFirstGroup = isFirstGroup;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void refreshView() {
		this.notifyDataSetChanged();
	}

	public void showDeleteTag(boolean isShow) {
		this.isShowDeleteTag = isShow;
//		Log.i("a", "showDeleteTag = " + isShowDeleteTag);
		// notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView title;
		TextView content;
		TextView date;
		ImageView image;
		CheckBox cb = null;
		ViewHolder vh;
		View view1, view2 = null;
		// 第一个按钮位置已被占用

		if (isFirstGroup && position == 0) {
			view1 = inflater.inflate(R.layout.fr_pregnancy_jishiben_addbtn,
					null);
			image = (ImageView) view1.findViewById(R.id.addImage);
			image.setImageResource(items.get(position).getImageId());
			return view1;
		} else {
			if (view2 == null) {
				view2 = inflater.inflate(R.layout.fr_pregnancy_jishiben_item,
						null);
			}
			title = (TextView) view2.findViewById(R.id.diaryTitle);
			content = (TextView) view2.findViewById(R.id.diaryContent);
			date = (TextView) view2.findViewById(R.id.diaryDate);
			cb = (CheckBox) view2.findViewById(R.id.delete_tag);
			
			final Integer idx = items.get(position).getIdx();
			
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						if (!idxs.contains(idx)) {
//							Log.i("a", "not contains");
							idxs.add(idx);
						}
					} else {
						if (idxs.contains(idx)) {
							idxs.remove(idx);
//							Log.i("a", "contains");
						}
					}
//					Log.i("a", "1 adapter idxs = " + idxs);
				}
			});
			if (isShowDeleteTag) {
				cb.setVisibility(ViewGroup.VISIBLE);
			} else {
				cb.setVisibility(ViewGroup.GONE);
			}
			
			if(idxs.contains(idx)) {
				cb.setChecked(true);
			}
			
//			Log.i("a", "2 adapter idxs = " + idxs);
			
			title.setText(items.get(position).getTitle());
			content.setText(items.get(position).getContent());
			date.setText(items.get(position).getCreateDate());
		}
		return view2;

		// if(isFirstGroup) {
		// if(position == 0) {
		// convertView = inflater.inflate(R.layout.fr_pregnancy_jishiben_addbtn,
		// null);
		// image = (ImageView) convertView.findViewById(R.id.addImage);
		// image.setImageResource(items.get(position).getImageId());
		// return convertView;
		// }else {
		// convertView = inflater.inflate(R.layout.fr_pregnancy_jishiben_item,
		// null);
		// title = (TextView) convertView.findViewById(R.id.diaryTitle);
		// content = (TextView) convertView.findViewById(R.id.diaryContent);
		// date = (TextView) convertView.findViewById(R.id.diaryDate);
		// cb = (CheckBox) convertView.findViewById(R.id.delete_tag);
		// final int location = position;
		// cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// Integer idx = items.get(location).getIdx();
		// if(isChecked) {
		// if(!idxs.contains(idx)) {
		// idxs.add(idx);
		// }
		// }else {
		// if(idxs.contains(idx)) {
		// idxs.remove(idx);
		// }
		// }
		// }
		// });
		// if(isShowDeleteTag) {
		// cb.setVisibility(ViewGroup.VISIBLE);
		// }else {
		// cb.setVisibility(ViewGroup.GONE);
		// }
		// title.setText(items.get(position).getTitle());
		// content.setText(items.get(position).getContent());
		// date.setText(items.get(position).getCreateDate());
		// }
		// }else {
		// if(convertView == null) {
		// convertView = inflater.inflate(R.layout.fr_pregnancy_jishiben_item,
		// null);
		// }
		// title = (TextView) convertView.findViewById(R.id.diaryTitle);
		// content = (TextView) convertView.findViewById(R.id.diaryContent);
		// date = (TextView) convertView.findViewById(R.id.diaryDate);
		// cb = (CheckBox) convertView.findViewById(R.id.delete_tag);
		// final int location = position;
		// cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// Integer idx = items.get(location).getIdx();
		// if(isChecked) {
		// if(!idxs.contains(idx)) {
		// idxs.add(idx);
		// }
		// }else {
		// if(idxs.contains(idx)) {
		// idxs.remove(idx);
		// }
		// }
		// }
		// });
		// if(isShowDeleteTag) {
		// cb.setVisibility(ViewGroup.VISIBLE);
		// }else {
		// cb.setVisibility(ViewGroup.GONE);
		// }
		// title.setText(items.get(position).getTitle());
		// content.setText(items.get(position).getContent());
		// date.setText(items.get(position).getCreateDate());
		// }
		// return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView content;
		TextView date;
		ImageView image;
	}

}
