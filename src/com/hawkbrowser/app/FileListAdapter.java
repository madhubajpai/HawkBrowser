package com.hawkbrowser.app;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hawkbrowser.R;
import com.hawkbrowser.util.CommonUtil;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileListAdapter extends BaseAdapter
	implements AdapterView.OnItemClickListener {
	
	private List<File> mFiles;
	private Context mContext;
	private Listener mListener;
	
	public static interface Listener {
		void onFileItemClick(File file);
	}
	
	public FileListAdapter(Context context, File currentFolder) {
		mContext = context;
		setData(currentFolder);
	}
	
	public void setData(File currentFolder) {
		
		File[] files = currentFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return !pathname.isHidden();
			}
		});
		
		if((null == files) || (files.length <= 0)) {
			mFiles = new ArrayList<File>();
		} else {
			mFiles = new ArrayList<File>(files.length);
			
			for(File file : files) {
				mFiles.add(file);
			}
			
			Collections.sort(mFiles, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
				    if(f1.isDirectory()) {
				    	if(f2.isDirectory()) {
				    		return f1.getName().compareTo(f2.getName());
				    	} else {
				    		return -1;
				    	}
				    } else {
				    	if(f2.isDirectory()) {
				    		return 1;
				    	} else {
				    		return f1.getName().compareTo(f2.getName());
				    	}
				    }
				}
			});
		}
		
		File rootFile = Environment.getExternalStorageDirectory();
		if(!rootFile.equals(currentFolder)) {
			mFiles.add(0, null);
		}
	}
		
	public void setListener(Listener listener) {
		mListener = listener;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, 
			View view, int position, long id) {
		
		if(null != mListener) {
			mListener.onFileItemClick(mFiles.get(position));
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub		
		return mFiles.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		File file = mFiles.get(position);
		return (null != file) ? file.hashCode() : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewGroup itemView = (ViewGroup) convertView;
		
		if(null == itemView) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			itemView = (ViewGroup) 
				inflater.inflate(R.layout.file_list_item, null);
		}
		
		ImageView itemIcon = (ImageView) 
			itemView.findViewById(R.id.file_item_icon);
		TextView itemName = (TextView)
			itemView.findViewById(R.id.file_item_name);
		ImageView arrowIcon = (ImageView)
			itemView.findViewById(R.id.file_item_arrow);
		
		File file = mFiles.get(position);
		
		if(null == file) {
			itemIcon.setImageResource(R.drawable.icon_up_dir);
			itemName.setText(R.string.back_to_parent);
			arrowIcon.setVisibility(View.GONE);
		} else {
			itemName.setText(file.getName());
			
			if(file.isDirectory()) {
				itemIcon.setImageResource(R.drawable.icon_folder);
				arrowIcon.setVisibility(View.VISIBLE);
			} else {
				itemIcon.setImageResource(R.drawable.icon_file);
				arrowIcon.setVisibility(View.GONE);
			}
		}
				
		return itemView;
	}
}
