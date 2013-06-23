package com.example.hawkbrowser.shell;

import com.example.hawkbrowser.R;
import com.example.hawkbrowser.util.CommonUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class PopMenuBar {

	private Context mContext;
	private ViewGroup mView;
	private PopupWindow mPopup;
	
	public PopMenuBar(Context context) {
		mContext = context;
		
		init();
	}
	
	private void init() {
		
		LayoutInflater li = (LayoutInflater) 
			mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mView = (ViewGroup) li.inflate(R.layout.pop_menu, null);
	}
	
    public void show(View anchor) {
    	if(null == mPopup) {
    		Point screenSize = CommonUtil.screenSize(mContext);
    		Resources rs = mContext.getResources();
    		int height = rs.getDimensionPixelSize(R.dimen.popmenu_height);
    		mPopup = new PopupWindow(mView, screenSize.x, height);
    		mPopup.showAsDropDown(anchor, 0, 0);
    	}
    }
    
    public boolean isShow() {
    	return mPopup.isShowing();
    }
    
    public void dismiss() {
    	if(null != mPopup) {
    		mPopup.dismiss();
    		mPopup = null;
    	}
    }
}
