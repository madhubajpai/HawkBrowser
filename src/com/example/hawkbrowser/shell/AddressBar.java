package com.example.hawkbrowser.shell;

import com.example.hawkbrowser.R;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddressBar {

	private	ViewGroup		mAddressBarView;
	private EventListener	mListener;
	private Context			mContext;
	
	public interface EventListener {
		void onGo(String url);
	}
	
	public AddressBar(Context ctx, ViewGroup view) {
		mAddressBarView = view;
		mContext = ctx;
		
		init();
	}
	
	public AddressBar(Context ctx, ViewGroup view, EventListener listener) {
		this(ctx, view);
		mListener = listener;
	}
	
	public void setEventListener(EventListener listener) {
		mListener = listener;
	}
	
	private void init() {
		final EditText et = (EditText) 
				mAddressBarView.findViewById(R.id.addressbar_edit);
		
		final View goBtn = mAddressBarView.findViewById(R.id.addressbar_gobtn);
		goBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url = et.getText().toString();
				
				if(url.isEmpty()) {
					Resources rs = mContext.getResources();
					url = rs.getString(R.string.homepageurl);
				}
				
				if(!url.startsWith("http")) {
					url = "http://" + url;
				}
				
				if(null != mListener) {
					mListener.onGo(url);
				}
			}
		});
		
		final View clearBtn = 
				mAddressBarView.findViewById(R.id.addressbar_clearbtn);
		clearBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				et.setText("");				
			}
		});
	}
}
