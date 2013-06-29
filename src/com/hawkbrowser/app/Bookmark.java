package com.hawkbrowser.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.hawkbrowser.base.Tree;


public class Bookmark {

	final private String BOOKMARK_FILE_NAME = "bookmark.dat";
	
	private Tree<Item> mTree;
	private int mCurrentId;
	
	public static enum Type {
		Folder, Link
	}
	
	
	public static class Item implements Serializable  {
		transient private int mId;
		private String mName;
		private String mFolderName;
		private String mUrl;
		private Type mType;
		
		public Item(String name, String url, Type type) {
			mName = name;
			mUrl = url;
			mType = type;
			mId = 0;
		}
		
		public boolean equals(Object obj) {
			if(!(obj instanceof Item)) {
				return false;
			}
			
			Item r = (Item) obj;
			return mId == r.mId;
		}
		
		public int hashCode() {
			return mId;
		}
	
	}
	
	public Bookmark() {
		mCurrentId = 0;
	
		Load();
	}
	
	public void Add(Item item) {
		Tree.Node<Item> parent = mTree.find(item, 
				new Tree.FindMatcher() {
					@Override
					public boolean isMatch(Object l, Object r) {
						Item li = (Item)l;
						Item ri = (Item)r;
						return (Type.Folder == li.mType) 
								&& (li.mName == ri.mFolderName);
					}
				});
		
		mTree.add(parent, item);
	}
	
	private void Load() {
		
		mTree = new Tree<Item>(null);
		
		ObjectInputStream inputStream = null;
		
		try {
			inputStream = new ObjectInputStream(
					new FileInputStream(BOOKMARK_FILE_NAME));
			Item item = (Item) inputStream.readObject();
			item.mId = ++mCurrentId;
			Add(item);
		} catch(Exception e) {
			
		} finally {
			if(null != inputStream) {
				try {
					inputStream.close();
					inputStream = null;
				} catch(Exception e) {
					
				}
			}
		}
	}
	
	class BookmarkTreeSerializer implements Tree.Iterator {
		
		private ObjectOutputStream mOutStream = null;
		
		public BookmarkTreeSerializer() {
			try {
				mOutStream = new ObjectOutputStream(
						new FileOutputStream(BOOKMARK_FILE_NAME));
			} catch(Exception e) {
				
			} finally {
				if(null != mOutStream) {
					try {
						mOutStream.close();
						mOutStream = null;
					} catch(Exception e) {
						
					}
				}
			}
		}
		
		public ObjectOutputStream getStream() {
			return mOutStream;
		}
		
		@Override
		public void iterate(Object l, Object r) {
			
			ObjectOutputStream outStream = (ObjectOutputStream) r;
			
			try {
				Item item = (Item)l;
				outStream.writeObject(item);
			} catch(Exception e) {
				
			} finally {
				if(null != outStream) {
					try {
						outStream.close();
						outStream = null;
					} catch(Exception e) {
						
					}
				}
			}
		}
	}
	
	private void Flush() {
		BookmarkTreeSerializer ts = new BookmarkTreeSerializer();
		ObjectOutputStream outStream = ts.getStream();
		
		if(null != outStream) {
			mTree.iterate(outStream, ts);
		}
	}
}
