package com.guangyi.finddoctor.hospitalRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.imageManager.ImageManager2;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.personCenter.CommonPatientActivity;
/**
* <p>Title: 网络医院运营支撑平台-APP个人版</p>
* <p>Description:确认挂号信息 </p>
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company:中国移动有限公司东莞分公司 </p>
* @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
* @version：1.0
* @since：2013-9-23
*/
public class HospitalRegisterConfirmOrder extends BasicActivity{
	private Button ok,cancle;
	private TextView time,regfee,way,tv_doctor_name,tv_doctor_position,tv_doctor_skill,depCategory,dep;
	private String timeStr,feeStr;
	private Doctor mDoctor;
	private String depCategoryAStr;
	private ImageView iv_doctor_pic;
	private int allDay;
	private int daySectionFlag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospital_register_confirm_order);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initView();
		initParams();
	}
	
	private void initParams()
	{
		mDoctor=(Doctor) getIntent().getExtras().getSerializable("doctor");
		timeStr=getIntent().getExtras().getString("time");
		feeStr=getIntent().getExtras().getString("regfee");
		depCategoryAStr=getIntent().getExtras().getString("depCategory");
		iv_doctor_pic=(ImageView) findViewById(R.id.iv_doctor_pic);
		 allDay=getIntent().getExtras().getInt("allDay");
		 daySectionFlag=getIntent().getExtras().getInt("daySectionFlag");
		if(mDoctor!=null)
		{
			regfee.setText(feeStr+"元(现场支付)");
			depCategory.setText(depCategoryAStr);
			tv_doctor_name.setText(mDoctor.getDoctName());
			tv_doctor_position.setText(mDoctor.getDoctPosi());
			
			System.out.print("ddfffffffffffffffffff医院名称------------"+mDoctor.getHospName());
			System.out.print(mDoctor+"ddffffffffffffffffff-----------------");
			tv_doctor_skill.setText("医院:"+mDoctor.getHospName());
			dep.setText(mDoctor.getDepaName());
			
			if(allDay==1)
			{
				timeStr=timeStr.split(",")[0]+",上午";
			}
			else if(allDay==2)
			{
				timeStr=timeStr.split(",")[0]+",下午";
			}
			
			else if(allDay==3)
			{
				timeStr=timeStr.split(",")[0]+",晚上";
				
			}
			
			else if(allDay==4)
			{
				timeStr=timeStr.split(",")[0]+",全天";
				
			}
			time.setText(timeStr);
			
			ImageManager2.from(this).displayImage(iv_doctor_pic, mDoctor.getAttachFileByte(), R.drawable.touxiang);
			
		}
		
	}
	
	private void initView()
	{
		ok=(Button) findViewById(R.id.ok);
		cancle=(Button) findViewById(R.id.cancle);
		time=(TextView) findViewById(R.id.time);
		regfee=(TextView) findViewById(R.id.regfee);
		way=(TextView) findViewById(R.id.way);
		depCategory=(TextView) findViewById(R.id.depCategory);
		dep=(TextView) findViewById(R.id.dep);
		tv_doctor_name=(TextView) findViewById(R.id.tv_doctor_name);
		tv_doctor_position=(TextView) findViewById(R.id.tv_doctor_position);
		tv_doctor_skill=(TextView) findViewById(R.id.tv_doctor_skill);
		Button Back = (Button) findViewById(R.id.ib_back);

		Back.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});
		
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				it.putExtra("tagtype", 1);
				it.putExtra("time", timeStr);
			    it.putExtra("fee","");
				it.putExtra("doctor", mDoctor);
				it.putExtra("way","预约挂号");
				it.putExtra("allDay", allDay);
				it.putExtra("daySectionFlag", daySectionFlag);
				it.setClass(getApplicationContext(), CommonPatientActivity.class);
				startActivity(it);
			}
		});
		cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
				
		
	}

}
