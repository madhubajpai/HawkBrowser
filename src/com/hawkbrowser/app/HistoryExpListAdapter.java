package com.hawkbrowser.app;

import java.util.List;

import com.hawkbrowser.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryExpListAdapter extends BaseExpandableListAdapter {

	private List< List<History.Item> > mGroupItems;
	private Context mContext;
	
	private static final int[] mGroupNames = {
		R.string.history_today,
		R.string.history_yestory,
		R.string.history_recentweek,
		R.string.history_recentmonth
	};
	
	public HistoryExpListAdapter(Context context, 
			List< List<History.Item> > items) {
		
		mContext = context;
		mGroupItems = items;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return (groupPosition + 1) << 8 + childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, 
		boolean isLastChild, View convertView, ViewGroup parent) {

		ViewGroup vg = null;
		
		if(null != convertView) {
			vg = (ViewGroup) convertView;
		}
		
		if(null == vg) {
			vg = (ViewGroup) LayoutInflater.from(mContext).inflate(
					R.layout.history_list_item, null);
		}
				
		TextView tv = (TextView)vg.findViewById(R.id.history_list_item_title);
		tv.setText(mGroupItems.get(groupPosition).get(childPosition).title());
		
		return tv;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if(null != mGroupItems) {
			if(groupPosition >= 0 && groupPosition < mGroupItems.size()) {
				return mGroupItems.get(groupPosition).size();
			}
		}
		
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		if(null != mGroupItems) {
			return mGroupItems.size();
		} else {
			return 0;
		}
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, 
		View convertView, ViewGroup parent) {
				
		ViewGroup vg = null;
		
		if(null != convertView) {
			vg = (ViewGroup) convertView;
		}
		
		if(null == vg) {
			vg = (ViewGroup) LayoutInflater.from(mContext).inflate(
					R.layout.history_group_item, null);
		}
		
		TextView titleView = (TextView) 
			vg.findViewById(R.id.history_group_item_date);
		titleView.setText(mGroupNames[groupPosition]);

		ImageView arrowView = (ImageView)
			vg.findViewById(R.id.history_group_item_arrow);
		
		arrowView.setImageResource(
			isExpanded ? R.drawable.arrow_up : R.drawable.arrow_down);
		
		return vg;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
