package com.example.lifememory.activity.views;

import android.widget.GridView;
/**
 * 功能:用于expandablelistview中嵌套的gridview
 * @author machao
 *
 */
public class MyGridView extends GridView
{
	public MyGridView(android.content.Context context,
			android.util.AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}

}