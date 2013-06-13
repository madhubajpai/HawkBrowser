package com.example.hawkbrowser.shell;

import com.example.hawkbrowser.R;
import com.example.hawkbrowser.core.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public final class HawkBrowser extends Activity {

	private HawkWebView wv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_frame);
		
		ViewGroup layout = (ViewGroup) findViewById(R.id.main_frame);
		wv = new HawkWebView(this);
		wv.setLayoutParams(
			new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		layout.addView(wv);
		
		final EditText et = (EditText) findViewById(R.id.addressbar);
		final Button goBtn = (Button) findViewById(R.id.go);
		goBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url = et.getText().toString();
				
				if(!url.startsWith("http")) {
					wv.loadUrl("http://" + url);
				}
			}
		});
		
		wv.loadUrl("http://www.baidu.com");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        wv.destroy();
    }

}
