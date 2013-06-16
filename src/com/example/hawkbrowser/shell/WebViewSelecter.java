package com.example.hawkbrowser.shell;

import java.util.ArrayList;
import com.example.hawkbrowser.R;
import com.example.hawkbrowser.util.CommonUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class WebViewSelecter {
	
	public interface EventListener {
		void onItemSelect(int i);
		void onItemClose(int i);
	}
	
	private static final int ITEM_COUNT_PER_SCREEN = 3;
	private	EventListener mListener;
	private Context mContext;
	private ArrayList<String> mTitles;
	private ArrayList<Bitmap> mBitmaps;
	private ViewGroup mView;
	private	int	mCurrentScreen;
	private PopupWindow mPopup;
	
	public WebViewSelecter(Context ctx, ArrayList<String> titles, 
			ArrayList<Bitmap> bitmaps) {
		
		mContext = ctx;
		mTitles = titles;
		mBitmaps = bitmaps;
		mCurrentScreen = 0;
		
		mView = createView();
		updateItems();
	}
	
	public void setEventListener(EventListener listener) {
		mListener = listener;
	}
	
	public View getView() {
		return mView;
	}
	
    private ViewGroup createView() {
    	
    	LayoutInflater inflater = (LayoutInflater)
				mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
		ViewGroup webThumbContainer = (ViewGroup) 
				inflater.inflate(R.layout.select_webview, null);
		
		for(int i = 0; i < webThumbContainer.getChildCount(); ++i) {

			ViewGroup item = (ViewGroup)
				webThumbContainer.getChildAt(i);
								
			ImageView webImage = (ImageView)
					item.findViewById(R.id.selectwebviewitem_image);
						
			webImage.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(null != mListener) {
						mListener.onItemSelect((Integer)v.getTag());
					}
				}
			});
			
			View closeView = item.findViewById(R.id.selectwebviewitem_title);
			closeView.setTag(i);
			closeView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(null != mListener) {
						int i = (Integer)v.getTag();
						ViewGroup item = (ViewGroup) mView.getChildAt(i);
						item.setVisibility(View.INVISIBLE);
						mTitles.remove(i);
						mBitmaps.remove(i);
						mListener.onItemClose(i);
					}
				}
			});
		}
		
		return webThumbContainer;
    }
    
    private void setItemsVisibility(int visibility) {
    
		for(int i = 0; i < mView.getChildCount(); ++i) {

			ViewGroup item = (ViewGroup) mView.getChildAt(i);
			item.setVisibility(visibility);
		}
    }
    
    public void updateItems() {
   	
    	setItemsVisibility(View.INVISIBLE);
    	
		int startPos = mCurrentScreen * ITEM_COUNT_PER_SCREEN;
    	int endPos = Math.min(mTitles.size(), startPos + 3);
    	
		for(int i = startPos; i < endPos; ++i) {

			ViewGroup item = (ViewGroup)
				mView.getChildAt(i - startPos);
			
			item.setVisibility(View.VISIBLE);
			
			TextView titleView = (TextView) 
					item.findViewById(R.id.selectwebviewitem_title);
			titleView.setText(mTitles.get(i));
			
			ImageView webImage = (ImageView)
					item.findViewById(R.id.selectwebviewitem_image);
			webImage.setImageBitmap(mBitmaps.get(i));
			webImage.setTag(i);
		}
    }
    
    public void show(View anchor) {
    	if(null != mPopup) {
    		Point screenSize = CommonUtil.screenSize(mContext);
    		mPopup = new PopupWindow(mView, screenSize.x, 200);
    		mPopup.showAsDropDown(anchor, 0, 0);
    	}
    }
    
    public void dismiss() {
    	if(null != mPopup) {
    		mPopup.dismiss();
    		mPopup = null;
    	}
    }
}
