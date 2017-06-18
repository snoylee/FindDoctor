package com.guangyi.finddoctor.personCenter;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
/**
* <p>Title: ����ҽԺ��Ӫ֧��ƽ̨-APP���˰�</p>
* <p>Description:���� </p>
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company:�й��ƶ����޹�˾��ݸ�ֹ�˾ </p>
* @author��<a href=��mailto:jomin5120@sina.com��>jomin5120@sina.com</a>
* @version��1.0
* @since��2013-9-23
*/
public class DeclareActivity extends BasicActivity {
	private Button ib_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.declare_activity);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
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
