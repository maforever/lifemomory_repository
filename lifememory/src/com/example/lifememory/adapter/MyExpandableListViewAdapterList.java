package com.example.lifememory.adapter;

import java.util.ArrayList;
import java.util.List;
import com.example.lifememory.R;
import com.example.lifememory.activity.IndexActivity;
import com.example.lifememory.activity.PregnancyJiShiBenAddActivity;
import com.example.lifememory.activity.PregnancyJiShiBenReadActivity;
import com.example.lifememory.activity.model.PrenancyJiShiBenGridViewExpandableGroupItem;
import com.example.lifememory.activity.views.MyGridView;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MyExpandableListViewAdapterList  extends BaseExpandableListAdapter{
	private Context context;
	private LayoutInflater inflater;
	private List<PrenancyJiShiBenGridViewExpandableGroupItem> groupItems;
	private FragmentActivity fa;
	private boolean isFirstGroup; //是否是第一组的标志位，因为如果是第一组，会有一个添加的按钮
	private boolean isShowDeleteTag = false;   //是否显示删除按钮
	public static List<Integer> idxs = new ArrayList<Integer>();   //存放id的集合
	public MyExpandableListViewAdapterList(Context context, FragmentActivity fa,  List<PrenancyJiShiBenGridViewExpandableGroupItem> groupItems) {
		this.context = context;
		this.groupItems = groupItems;
		this.fa = fa;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getGroupCount() {
		return groupItems.size();
	}

	public int getChildrenCount(int groupPosition) {
		return groupItems.get(groupPosition).getJishibenItems().size();
	}

	public Object getGroup(int groupPosition) {
		return groupItems.get(groupPosition);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return groupItems.get(groupPosition).getJishibenItems().get(childPosition);
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public boolean hasStableIds() {
		return false;
	}

	public void showDeleteTag(boolean isShow) {
		this.isShowDeleteTag = isShow;
		notifyDataSetChanged();
//		Log.i("a", "isShowDeleteTag ==== " + isShowDeleteTag);
	}
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView title;
		TextView num;
		ImageView imageView;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.expandable_list_group, null);
		}
		title = (TextView) convertView.findViewById(R.id.title);
		num = (TextView) convertView.findViewById(R.id.num);
		imageView = (ImageView) convertView.findViewById(R.id.imageView);
		title.setText(groupItems.get(groupPosition).getTitle());
		num.setText("" + groupItems.get(groupPosition).getNum());
		imageView.setImageResource(R.drawable.group_expand_icon);
		convertView.setBackgroundResource(R.drawable.group_bg);
		if(!isExpanded) {
			imageView.setImageResource(R.drawable.group_unexpand_icon);
			convertView.setBackgroundResource(R.drawable.group_bg_h);
		}
		return convertView;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TextView title;
		TextView createDate;
		TextView updateDate;
		ImageView image;
		CheckBox cb;
		View view1, view2 = null;
//		Log.i("a", "groupPosition = " + groupPosition + " childPosition = " + childPosition);
		
		if(groupPosition == 0 && childPosition == 0) {
			view1 = inflater.inflate(R.layout.fr_pregnancy_jishiben_addbtn_list, null);
			image = (ImageView) view1.findViewById(R.id.imageView);
			image.setImageResource(R.drawable.list_add_icon);
			return view1;
		}else {
			if(view2 == null){
				view2 = inflater.inflate(R.layout.expandable_list_child, null);
			}
			
			title = (TextView) view2.findViewById(R.id.title);
			createDate = (TextView) view2.findViewById(R.id.createDate);
			updateDate = (TextView) view2.findViewById(R.id.updateDate);
			cb = (CheckBox) view2.findViewById(R.id.delete_tag);
			
			final Integer idx = groupItems.get(groupPosition).getJishibenItems().get(childPosition).getIdx();
			
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
			
			
			title.setText(groupItems.get(groupPosition).getJishibenItems().get(childPosition).getTitle());
			String createDateStr = groupItems.get(groupPosition).getJishibenItems().get(childPosition).getCreateDate();
			String updateDateStr = groupItems.get(groupPosition).getJishibenItems().get(childPosition).getUpdateDate();
			if(updateDateStr == null || "".equals(updateDateStr.trim())) {
				updateDateStr = createDateStr;
			}
			createDate.setText(createDateStr);
			updateDate.setText(updateDateStr);
		}
		return view2;
		
//		//如果是第一组
//		if(groupPosition == 0) {
//			//如果是第一组的第一个
//			if(childPosition == 0) {
//				convertView = inflater.inflate(R.layout.fr_pregnancy_jishiben_addbtn_list, null);
//				image = (ImageView) convertView.findViewById(R.id.imageView);
//				image.setImageResource(R.drawable.list_add_icon);
//				return convertView;
//			}else {
//				convertView = inflater.inflate(R.layout.expandable_list_child, null);
//			}
//		}else {
//			convertView = inflater.inflate(R.layout.expandable_list_child, null);
//		}
//		title = (TextView) convertView.findViewById(R.id.title);
//		createDate = (TextView) convertView.findViewById(R.id.createDate);
//		updateDate = (TextView) convertView.findViewById(R.id.updateDate);
//		cb = (CheckBox) convertView.findViewById(R.id.delete_tag);
//		
//		
//		title.setText(groupItems.get(groupPosition).getJishibenItems().get(childPosition).getTitle());
//		String createDateStr = groupItems.get(groupPosition).getJishibenItems().get(childPosition).getCreateDate();
//		String updateDateStr = groupItems.get(groupPosition).getJishibenItems().get(childPosition).getUpdateDate();
//		if(updateDateStr == null || "".equals(updateDateStr.trim())) {
//			updateDateStr = createDateStr;
//		}
//		createDate.setText(createDateStr);
//		updateDate.setText(updateDateStr);
//		
//		return convertView;
	}

	
	
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		
		return true;
	}

}
