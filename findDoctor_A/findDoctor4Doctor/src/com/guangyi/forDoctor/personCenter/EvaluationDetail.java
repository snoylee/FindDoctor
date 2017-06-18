package com.guangyi.forDoctor.personCenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.model.Evaluate;
import com.guangyi.forDoctor.onlineConsult.OnlineChatActivity;

public class EvaluationDetail extends BasicActivity {
	private TextView tv_evaluation_content,tv_service,tv_skill,tv_efficiency;
	private RatingBar RatingBar_service,RatingBar_skill,RatingBar_efficiency;
	private Evaluate evaluate; 
	private RelativeLayout relative_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evaluate_detail);
		SysApplication.getInstance().addActivity(this);
		initParams();
		initView();
		initData();
		
	}
	
	private void initParams()
	{
		evaluate=(Evaluate) getIntent().getSerializableExtra("evaluate");
	}
	
	private void initView()
	{
		relative_button=(RelativeLayout) findViewById(R.id.relative_button);
		tv_evaluation_content=(TextView) findViewById(R.id.tv_evaluation_content);
		tv_service=(TextView) findViewById(R.id.tv_service);
		tv_skill=(TextView) findViewById(R.id.tv_skill);
		tv_efficiency=(TextView) findViewById(R.id.tv_efficiency);
		RatingBar_service=(RatingBar) findViewById(R.id.RatingBar_service);
		RatingBar_skill=(RatingBar) findViewById(R.id.RatingBar_skill);
		RatingBar_efficiency=(RatingBar) findViewById(R.id.RatingBar_efficiency);
		
		Button btn_see_problem=(Button) findViewById(R.id.btn_see_problem);
		if(evaluate.getConsId()>0)
		{
			relative_button.setVisibility(View.VISIBLE);
			 btn_see_problem.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent it=new Intent();
						it.putExtra("type", 1);
						it.putExtra("is_bottom_visible", 0);
						it.putExtra("consId", evaluate.getConsId()+"");
						it.setClass(EvaluationDetail.this, OnlineChatActivity.class);
						startActivity(it);
					}
				});
		}
		Button ib_back=(Button) findViewById(R.id.ib_back);
		
		ib_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}
	
	private void initData()
	{
		
		
		if(evaluate!=null)
		{
		tv_evaluation_content.setText(evaluate.getEvaluateContext());
		tv_service.setText(evaluate.getService()*2+"·Ö");
		tv_skill.setText(evaluate.getSkill()*2+"·Ö");
		tv_efficiency.setText(evaluate.getEfficiency()*2+"·Ö");
		RatingBar_service.setRating(evaluate.getService());
		RatingBar_skill.setRating(evaluate.getSkill());
		RatingBar_efficiency.setRating(evaluate.getEfficiency());
		}
	}
	
	
	
	
	}



