package com.hawkbrowser.shell;

import java.io.File;

import com.hawkbrowser.util.CommonUtil;

import android.app.Application;

public class HawkBrowserApplication extends Application {

	// Custom browser cache directory
	@Override
	public File getCacheDir() {
		return CommonUtil.getCacheDir();
	}
	
}
