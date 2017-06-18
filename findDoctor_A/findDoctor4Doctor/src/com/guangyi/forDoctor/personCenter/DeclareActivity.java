package com.guangyi.forDoctor.personCenter;

import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class DeclareActivity extends BasicActivity {
	private Button ib_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.declare_activity);
		SysApplication.getInstance().addActivity(this);
		initView();
		initListener();
	}
	private void initView() {
		ib_back = (Button) findViewById(R.id.ib_back);
		WebView webView=(WebView) findViewById(R.id.webView);
		webView.loadUrl("file:///android_asset/Statement.html");    
		
	}

	private void initListener() {
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}

}
