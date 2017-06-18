package com.guangyi.finddoctor.personCenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
/**
* <p>Title: 网络医院运营支撑平台-APP个人版</p>
* <p>Description:选择满意度 </p>
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company:中国移动有限公司东莞分公司 </p>
* @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
* @version：1.0
* @since：2013-9-23
*/
public class SuggestFraction extends BasicActivity {
	private Button ib_back;
	private LinearLayout ll_verywell,ll_well,ll_justso,ll_notwell,ll_verynotwell;
	private TextView tv_verywell,tv_well,tv_justso,tv_notwell,tv_verynotwell;
	
	private void initListener(){
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(ResultCodeCategory.RESULT_CODE_FRCTION_BACK, intent);
				finish();
				
			}
		});
		
		ll_verywell.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.putExtra("fraction", tv_verywell.getText().toString());
			setResult(ResultCodeCategory.RESULT_CODE_FRCTION, intent);
			finish();
				
			}
		});
		
		ll_well.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("fraction", tv_well.getText().toString());
				setResult(ResultCodeCategory.RESULT_CODE_FRCTION, intent);
				finish();
				
			}
		});
		
		ll_justso.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("fraction", tv_justso.getText().toString());
				setResult(ResultCodeCategory.RESULT_CODE_FRCTION, intent);
				finish();
				
			}
		});
		
		ll_notwell.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("fraction", tv_notwell.getText().toString());
				setResult(ResultCodeCategory.RESULT_CODE_FRCTION, intent);
				finish();
			}
		});
		
		ll_verynotwell.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("fraction", tv_verynotwell.getText().toString());
				setResult(ResultCodeCategory.RESULT_CODE_FRCTION, intent);
				finish();
			}
		});
		
	}

	private void initView() {
		ib_back = (Button) findViewById(R.id.ib_back);
		ll_verywell = (LinearLayout) findViewById(R.id.ll_verywell);
		ll_well = (LinearLayout) findViewById(R.id.ll_well);
		ll_justso = (LinearLayout) findViewById(R.id.ll_justso);
		ll_notwell = (LinearLayout) findViewById(R.id.ll_notwell);
		ll_verynotwell = (LinearLayout) findViewById(R.id.ll_verynotwell);
		tv_verywell = (TextView) findViewById(R.id.tv_verywell);
		tv_well = (TextView) findViewById(R.id.tv_well);
		tv_justso = (TextView) findViewById(R.id.tv_justso);
		tv_notwell = (TextView) findViewById(R.id.tv_notwell);
		tv_verynotwell = (TextView) findViewById(R.id.tv_verynotwell);
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggest_fraction);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initView();
		initListener();
	}

}
