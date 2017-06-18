package com.guangyi.finddoctor.onlineAsk;

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
/**
* <p>Title: 网络医院运营支撑平台-APP个人版</p>
* <p>Description:确认预约信息 </p>
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company:中国移动有限公司东莞分公司 </p>
* @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
* @version：1.0
* @since：2013-9-23
*/
public class OnlineConfirmOrder extends BasicActivity{
	private Button ok,cancle;
	private TextView time,fee,way,tv_doctor_name,tv_doctor_position,tv_doctor_skill;
	private String timeStr,feeStr,wayStr,todayStr;
	private Doctor mDoctor;
	private ImageView iv_doctor_pic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_confirm_order);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initView();
		initParams();
	}
	
	private void initParams()
	{
//		int feeCount=getIntent().getIntExtra("feeCount", 0);
//		FindDoctorApplication application=(FindDoctorApplication) getApplication();
		iv_doctor_pic=(ImageView) findViewById(R.id.iv_doctor_pic);
		mDoctor=(Doctor) getIntent().getExtras().getSerializable("doctor");
		timeStr=getIntent().getExtras().getString("time");
//	    feeStr=feeCount*mDoctor.getMoney()+"";
		feeStr=getIntent().getExtras().getString("feeStr");
		wayStr=getIntent().getExtras().getString("way");
		todayStr=getIntent().getExtras().getString("todayStr");
		time.setText(timeStr.substring(0, timeStr.length()-2));
		fee.setText(feeStr);
		way.setText(wayStr);
		
		if(mDoctor!=null)
		{
			tv_doctor_name.setText(mDoctor.getDoctName());
			tv_doctor_position.setText(mDoctor.getDoctPosi());
			tv_doctor_skill.setText(mDoctor.getHospName());
//			if(mDoctor.getAttachFileByte()!=null&&mDoctor.getAttachFileByte().length()>0)
//			{
//				 byte[] tempb = Base64.decode(mDoctor.getAttachFileByte(), Base64.DEFAULT);
//				 iv_doctor_pic.setImageBitmap(BitmapFactory.decodeByteArray(tempb,0 ,tempb.length));
//			}
			ImageManager2.from(this).displayImage(iv_doctor_pic, mDoctor.getAttachFileByte(), R.drawable.touxiang);
			
		}
		
	}
	
	private void initView()
	{
		ok=(Button) findViewById(R.id.ok);
		cancle=(Button) findViewById(R.id.cancle);
		time=(TextView) findViewById(R.id.time);
		fee=(TextView) findViewById(R.id.fee);
		way=(TextView) findViewById(R.id.way);
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
				it.putExtra("time", timeStr);
			    it.putExtra("fee",feeStr.replaceAll("元", ""));
				it.putExtra("doctor", mDoctor);
				it.putExtra("todayStr", todayStr);
				it.putExtra("way","电话咨询");
				it.setClass(OnlineConfirmOrder.this, OnLineAddConsultInfo.class);
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
