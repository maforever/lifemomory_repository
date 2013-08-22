package com.example.lifememory.adapter;

import java.util.List;
import com.example.lifememory.R;
import com.example.lifememory.activity.PregnancyJiShiBenReadActivity;
import com.example.lifememory.activity.PregnancyLuYinAddActivity;
import com.example.lifememory.activity.PregnancyLuYinReadActivity;
import com.example.lifememory.activity.model.PrenancyJiShiBenListViewExpandableGroupItem;
import com.example.lifememory.activity.views.MyGridView;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyExpandableListViewLuYinAdapter  extends BaseExpandableListAdapter{
	private Context context;
	private LayoutInflater inflater;
	private MyGridView toolbarGrid;
	public PregnancyDiaryLuYinBenListAdapter adapter = null;
	private List<PrenancyJiShiBenListViewExpandableGroupItem> groupItems;
	private FragmentActivity fa;
	private boolean isFirstGroup; //是否是第一组的标志位，因为如果是第一组，会有一个添加的按钮
	private boolean isShowDeleteTag = false;   //是否显示删除按钮
	public MyExpandableListViewLuYinAdapter(Context context, FragmentActivity fa,  List<PrenancyJiShiBenListViewExpandableGroupItem> groupItems) {
		this.context = context;
		this.groupItems = groupItems;
		this.fa = fa;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getGroupCount() {
		return groupItems.size();
	}

	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	public Object getGroup(int groupPosition) {
		return groupItems.get(groupPosition);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return 1;
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
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.expandable_gridview_child, null);
		}
		toolbarGrid = (MyGridView) convertView
				.findViewById(R.id.GridView_toolbar);
		toolbarGrid.setNumColumns(3);// 设置每行列数
		toolbarGrid.setGravity(Gravity.CENTER);// 位置居中
		if(groupPosition == 0) {
			isFirstGroup = true;
		}else {
			isFirstGroup = false;
		}
		adapter = new PregnancyDiaryLuYinBenListAdapter(context, groupItems.get(groupPosition).getLuyinItems(), isFirstGroup);
		if(isShowDeleteTag) {
			adapter.showDeleteTag(isShowDeleteTag);
		}
		toolbarGrid.setAdapter(adapter);
		toolbarGrid.setOnItemClickListener(new MyOnItemClick(groupPosition, childPosition));
		return convertView;
	}

	private class MyOnItemClick implements OnItemClickListener {
		int groupPosition;
		int childPosition;
		public MyOnItemClick(int groupPosition, int  childPosition) {
			this.groupPosition = groupPosition;
			this.childPosition = childPosition;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//点击添加按钮
			if(groupPosition == 0 && position == 0) {
				Intent intent = new Intent(context, PregnancyLuYinAddActivity.class);
				context.startActivity(intent);
				fa.overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
			}else {
				Intent intent = new Intent(context, PregnancyLuYinReadActivity.class);
				intent.putExtra("itemId", groupItems.get(groupPosition).getLuyinItems().get(position).getIdx());
				context.startActivity(intent);
				fa.overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
			}
		}
	}
	
	
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		
		return true;
	}

}
