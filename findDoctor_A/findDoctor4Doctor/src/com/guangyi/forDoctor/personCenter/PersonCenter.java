package com.guangyi.forDoctor.personCenter;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.model.Doctor;

public class PersonCenter extends BasicActivity {
	
	   // 个人中心
		private Button btn_logout;
		private RelativeLayout realivelay_phone_register;
		private RelativeLayout relative_person_data;
		private RelativeLayout relative_see_evaluate;
		private RelativeLayout relative_hospital_register;
		private RelativeLayout relative_suggest;//意见反馈
		private RelativeLayout relative_introduce;//精品推荐
		private RelativeLayout relative_edition;//版本信息
		private RelativeLayout relative_declare;//声明
		private RatingBar ratingBar_score;
		private TextView tv_score,tv_user_name;
		private CheckBox check_box;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
            setContentView(R.layout.person_center);
            SysApplication.getInstance().addActivity(this);
			super.onCreate(savedInstanceState);
			initView();
			initListener();
		}
		
		@Override
		protected void onStart() {
			initParams();
		super.onStart();
		}

		
		private void initParams()
		{
			tv_score.setText(getCustomSharedPreference().getInt(Doctor.DOCSCORE, 0)*2+"分");
			ratingBar_score.setRating(getCustomSharedPreference().getInt(Doctor.DOCSCORE, 0));
			tv_user_name.setText(getCustomSharedPreference().getString(Doctor.USERMOBLE, ""));
			check_box.setChecked(getDataSf().getBoolean("isBannerCan", true));
		}
		
		
		
		// 个人中心部分初始化
		private void initView() {
			btn_logout=(Button) findViewById(R.id.btn_logout);
			ratingBar_score=(RatingBar) findViewById(R.id.ratingBar_score);
			tv_score=(TextView) findViewById(R.id.tv_score);
			tv_user_name=(TextView) findViewById(R.id.tv_user_name);
			realivelay_phone_register = (RelativeLayout) findViewById(R.id.realivelay_phone_register);
			relative_person_data = (RelativeLayout) findViewById(R.id.relative_person_data);
			relative_see_evaluate = (RelativeLayout) findViewById(R.id.relative_see_evaluate);
			relative_hospital_register = (RelativeLayout) findViewById(R.id.relative_hospital_register);
			relative_suggest = (RelativeLayout)findViewById(R.id.relative_suggest);
			relative_introduce = (RelativeLayout) findViewById(R.id.relative_introduce);
			relative_edition = (RelativeLayout) findViewById(R.id.relative_edition);
			relative_declare = (RelativeLayout) findViewById(R.id.relative_declare);
			btn_logout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				Editor editor=getSharedPreferences("doctor", MODE_PRIVATE).edit();
				editor.remove("isLogin");
				editor.commit();
					openActivity(UserLoginActivity.class);
					SysApplication.getInstance().exit();
					finish();
				}
			});
			Button ib_back=(Button) findViewById(R.id.ib_back);
			ib_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
				}
			});

		}
		
		private void initListener() {

			relative_see_evaluate.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openActivity(EvaluationListActivity.class);
				}
			});
			
			relative_hospital_register.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent it=new Intent();
					it.putExtra("consType", 1);
					it.setClass(PersonCenter.this, DoctorTimeListActivity.class);
					startActivity(it);
					
				}
			});
			
			realivelay_phone_register.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent it=new Intent();
					it.putExtra("consType", 3);
					it.setClass(PersonCenter.this, DoctorTimeListActivity.class);
					startActivity(it);
					
				}
			});


			relative_person_data.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openActivity(PersonCenterInfo.class);
					
				}
			});
			

			relative_suggest.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openActivity(SuggestActivity.class);
					
				}
			});
			
			relative_introduce.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openActivity(IntroduceActivity.class);
					
				}
			});
			
			relative_edition.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openActivity(EditionActivity.class);
					
				}
			});
			
			relative_declare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openActivity(DeclareActivity.class);
				}
			});
			
			 check_box=(CheckBox) findViewById(R.id.check_box);
			check_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					Editor editor=getDataSfEditor();
					editor.putBoolean("isBannerCan", isChecked);
					editor.commit();
				}
			});
			
			
		}

}
