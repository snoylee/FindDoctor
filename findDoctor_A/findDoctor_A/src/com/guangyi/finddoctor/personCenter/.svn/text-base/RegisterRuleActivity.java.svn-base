package com.guangyi.finddoctor.personCenter;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;

/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:服务规则
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:中国移动有限公司东莞分公司
 * </p>
 * 
 * @author：<a 
 *            href=”mailto:chunhuizhao@gysoftown.com”>chunhuizhao@gysoftown.com</
 *            a>
 * @version：1.0
 * @since：2013-9-23
 */
public class RegisterRuleActivity extends BasicActivity {
	private Button ib_back;
	private TextView regist_rule_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_rule_activity);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initView();
		initListener();
	}

	private void initView() {
		ib_back = (Button) findViewById(R.id.ib_back);
		WebView webView=(WebView) findViewById(R.id.webView);
		webView.loadUrl("file:///android_asset/Register.html");    
		
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
