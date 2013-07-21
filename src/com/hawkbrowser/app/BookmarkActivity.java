package com.hawkbrowser.app;

import java.util.List;

import com.hawkbrowser.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public class BookmarkActivity extends Activity {
	
	private final String BOOKMARK_TAB_ID = "bookmark";
	private final String HISTORY_TAB_ID = "history";
	
	private TabHost	mTabHost;
	private TabWidget mTabWidget;
	private Bookmark mBookmark;
	
	class BookmarkArrayAdapter extends BaseAdapter {
		
		private List<Bookmark.Item> mItems;
		private LayoutInflater mInflater;
		
		public BookmarkArrayAdapter(Context context, 
				List<Bookmark.Item> items) {
					
			mItems = items;
			mInflater = (LayoutInflater) 
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return mItems.size();
		}
		
		@Override
		public Object getItem(int position) {
			if(position > 0 && position < mItems.size()) {
				return mItems.get(position);
			}
			
			return null;
		}
		
		@Override
		public long getItemId(int position) {
			if(position > 0 && position < mItems.size()) {
				return mItems.get(position).id();
			}
			
			return 0;
		}
		
		@Override
		public View getView(int position, 
				View convertView, ViewGroup parent) {
			
			Log.d("Bookmark", String.format("getView: %d", position));
			
			if(null != convertView) {
				return convertView;
			}
					
			ViewGroup itemView = (ViewGroup) 
				mInflater.inflate(R.layout.bookmark_list_item, null);
			
			Bookmark.Item bi = mItems.get(position);
			String name = null;
			
			if(bi.type() == Bookmark.Type.Folder) {
				ImageView icon = (ImageView)
					itemView.findViewById(R.id.bookmark_list_item_icon);
				icon.setImageResource(R.drawable.icon_folder);
				name = bi.title();
			} else {
				name = bi.title() + "\n" + bi.url();
			}
			
			Log.d("Bookmark", 
				String.format("getView, item name: %s", name));
			
			TextView titleView = (TextView) 
					itemView.findViewById(R.id.bookmark_list_item_title);
			titleView.setText(name);
			
			return itemView;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.bookmark_history);
		
		initLayout();
		loadBookmark();
	}
	
	private void initLayout() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabWidget = mTabHost.getTabWidget();
		
		Resources rc = getResources();
		
		TabSpec tabBookmark = mTabHost.newTabSpec(BOOKMARK_TAB_ID);
		tabBookmark.setContent(R.id.bookmarkhistory_bookmark);
		tabBookmark.setIndicator(rc.getString(R.string.bookmark));
		mTabHost.addTab(tabBookmark);

		TabSpec tabHistory = mTabHost.newTabSpec(HISTORY_TAB_ID);
		tabHistory.setContent(R.id.bookmarkhistory_history);
		tabHistory.setIndicator(rc.getString(R.string.history));
		mTabHost.addTab(tabHistory);
	}
	
	private void loadBookmark() {
		mBookmark = new Bookmark(this);
		List<Bookmark.Item> items = mBookmark.getChildren(null);
		
		Log.d("Bookmark", String.format("bookmarks count: %d", items.size()));
		
		BookmarkArrayAdapter adapter = new BookmarkArrayAdapter(this, items);
				
		ListView listView = (ListView)
			mTabHost.findViewById(R.id.bookmarkhistory_bookmark);
		listView.setAdapter(adapter);
	}
}
