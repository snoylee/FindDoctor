package com.guangyi.forDoctor.phoneAsk;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.model.PhoneOrHospital;

public class PhoneAskDetail extends BasicActivity{
	private TextView tv_time,tv_sex,tv_age,tv_time_str,tv_disease,tv_symptom;
	private PhoneOrHospital phoneAskItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_ask_detail);
		SysApplication.getInstance().addActivity(this);
		initView();
		initParams();
	}
	private void initView()
	{
		tv_time=(TextView) findViewById(R.id.tv_time);
		tv_age=(TextView) findViewById(R.id.tv_age);
		tv_time_str=(TextView) findViewById(R.id.tv_time_str);
		tv_sex=(TextView) findViewById(R.id.tv_sex);
		//tv_disease=(TextView) findViewById(R.id.tv_disease);
		tv_symptom=(TextView) findViewById(R.id.tv_symptom);
		Button ib_back=(Button) findViewById(R.id.ib_back);
		ib_back.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
			
		}
	});
	}
	
	private void initParams()
	{
		phoneAskItem=(PhoneOrHospital) getIntent().getSerializableExtra("phoneAskItem");
		if(phoneAskItem!=null)
		{
			tv_time.setText(phoneAskItem.getTimesection().substring(0, phoneAskItem.getTimesection().length()-2));
			//tv_age.setText(phoneAskItem.getAge()+"岁");
			tv_time_str.setText(20*(phoneAskItem.getTimesection().split(", ").length)+"分钟");
			
			tv_sex=(TextView) findViewById(R.id.tv_sex);
			if(phoneAskItem.getSex().equals("1"))
			{
				tv_sex.setText("男");
			}
			if(phoneAskItem.getSex().equals("2"))
			{
				tv_sex.setText("女");
			}
			if(phoneAskItem.getSex().equals("3"))
			{
				tv_sex.setText("未知");
			}
			
			//此处数据的带出 不需要
			//tv_disease.setText(phoneAskItem.getDisease());
			tv_symptom.setText(phoneAskItem.getDisease());
		}
	}

}
