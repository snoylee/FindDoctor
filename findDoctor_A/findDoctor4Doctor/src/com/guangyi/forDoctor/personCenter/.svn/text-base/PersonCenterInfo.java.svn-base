package com.guangyi.forDoctor.personCenter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.model.Doctor;

public class PersonCenterInfo extends BasicActivity {
	private TextView tv_name;
	private TextView tv_hospital;
	private TextView tv_sex;
	private TextView tv_positon;
	private TextView tv_department;
	private TextView tv_speci;
	private TextView tv_introduction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_center_info);
		SysApplication.getInstance().addActivity(this);
		initView();
	}
	
	private void initView(){
//		private String tv_name;
//		private String tv_phone;
//		private String tv_sex;
//		private String tv_age;
//		private String tv_positon;
//		private String tv_department;
//		private String tv_speci;
//		private String tv_introduction;
		tv_name=(TextView) findViewById(R.id.tv_name);
		tv_hospital=(TextView) findViewById(R.id.tv_hospital);
		tv_sex=(TextView) findViewById(R.id.tv_sex);
		tv_positon=(TextView) findViewById(R.id.tv_positon);
		tv_department=(TextView) findViewById(R.id.tv_department);
		tv_introduction=(TextView) findViewById(R.id.tv_introduction);
		tv_speci=(TextView) findViewById(R.id.tv_speci);
		
		tv_hospital.setText("医院:"+getCustomSharedPreference().getString(Doctor.DOCTHOSPNAME, ""));
		tv_name.setText(getCustomSharedPreference().getString(Doctor.DOCTNAME, ""));
		if(getCustomSharedPreference().getString(Doctor.DOCTSEX, "").equals("2"))
		{
			tv_sex.setText("性别:女");
		}
		if(getCustomSharedPreference().getString(Doctor.DOCTSEX, "").equals("1"))
		{
			tv_sex.setText("性别:男");
		}
		if(getCustomSharedPreference().getString(Doctor.DOCTSEX, "").equals("3"))
		{
			tv_sex.setText("性别:未知");
		}
		tv_positon.setText(getCustomSharedPreference().getString(Doctor.DOCTPOSI, ""));
		tv_department.setText(getCustomSharedPreference().getString(Doctor.DOCTDEPANAME, ""));
		tv_speci.setText(getCustomSharedPreference().getString(Doctor.DOCTSPECIALTY, ""));
		tv_introduction.setText("个人简介:\n"+getCustomSharedPreference().getString(Doctor.DOCTINTRODUCTION, ""));
		Button ib_back=(Button)findViewById(R.id.ib_back);
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
		
		//修改医生资料
		Button btn_Edit=(Button) findViewById(R.id.btn_Edit);
		//先判断医生有没有登录，有登录了才显示，如没有登录则隐藏；
		SharedPreferences sp = getSharedPreferences("doctor", MODE_PRIVATE);
		if(null == sp)
		{
			//用户未登录
			btn_Edit.setVisibility(View.INVISIBLE);
		}else
		{
			btn_Edit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					openActivity(EditDoctorInfo.class);
					finish();
				}
			});
		}
	}

}
