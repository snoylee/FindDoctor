package com.guangyi.forDoctor.hospitalRegister;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.model.PhoneOrHospital;

public class HospitalRegisterDetail extends BasicActivity{
	private TextView tv_time,tv_sex,tv_age,tv_disease,tv_symptom,tv_return_flag,tv_isFrist,tv_appostate;
	private PhoneOrHospital phoneAskItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospital_register_detail);
		SysApplication.getInstance().addActivity(this);
		initView();
		initParams();
		
	}
	
	private void initView()
	{
		tv_appostate=(TextView) findViewById(R.id.tv_appostate);
		tv_time=(TextView) findViewById(R.id.tv_time);
		tv_age=(TextView) findViewById(R.id.tv_age);
		tv_return_flag=(TextView) findViewById(R.id.tv_return_flag);
		tv_isFrist=(TextView) findViewById(R.id.tv_isFrist);
		tv_sex=(TextView) findViewById(R.id.tv_sex);
		tv_disease=(TextView) findViewById(R.id.tv_disease);
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
			if(phoneAskItem.getAppoState()==1)
			{
				tv_appostate.setText("��ʼԤԼ");
			}
			else if(phoneAskItem.getAppoState()==2)
			{
				tv_appostate.setText("ԤԼ�ɹ�");
			}
			else if(phoneAskItem.getAppoState()==3)
			{
				tv_appostate.setText("ȡ��ԤԼ");
			}
			else if(phoneAskItem.getAppoState()==4)
			{
				tv_appostate.setText("�����");
			}
		
			tv_time.setText(phoneAskItem.getTimesection());
			tv_age.setText(phoneAskItem.getTimesection());
			tv_sex=(TextView) findViewById(R.id.tv_sex);
			if(phoneAskItem.getSex().equals("1"))
			{
				tv_sex.setText("��");
			}
			if(phoneAskItem.getSex().equals("2"))
			{
				tv_sex.setText("Ů");
			}
			if(phoneAskItem.getSex().equals("3"))
			{
				tv_sex.setText("δ֪");
			}
			
			
			if(phoneAskItem.getIsFrist().equals("0"))
			{
				tv_isFrist.setText("�״�ԤԼ");
			}
			if(phoneAskItem.getIsFrist().equals("1"))
			{
				tv_isFrist.setText("���״�ԤԼ");
			}
			
			
			if(phoneAskItem.getReturnflag().equals("0"))
			{
				tv_return_flag.setText("����");
			}
			if(phoneAskItem.getReturnflag().equals("1"))
			{
				tv_return_flag.setText("����");
			}
			if(phoneAskItem.getReturnflag().equals("2"))
			{
				tv_return_flag.setText("��Ժ����");
			}
			
			tv_disease.setText(phoneAskItem.getSymptom());
			tv_symptom.setText(phoneAskItem.getDisease());
		}

		
		
	}

}
