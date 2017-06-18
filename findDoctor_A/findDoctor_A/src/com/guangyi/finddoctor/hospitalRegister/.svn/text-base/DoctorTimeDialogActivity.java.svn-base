package com.guangyi.finddoctor.hospitalRegister;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.DoctorTimeGridAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.custview.MyGridView;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.model.TimeList;
import com.guangyi.finddoctor.utils.DateTools;

public class DoctorTimeDialogActivity extends BasicActivity {
	private Doctor doctor;
	private List<TimeList> timeListAM;
	private List<TimeList> timeListPM,timeListNight,timeListAll;
	private List<TimeList> timeList;
	private LinearLayout linear1, linear2,linear3,linear4;
	private TextView tv_date_str,tv_tips;
	private String timeStr;
	private int tag,isCanRegister;
	private int regtotalAM, regtotalPM,regtotalNight,regtotalAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getNotitle();
		setContentView(R.layout.doctor_time_dialog_activity);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
	}

	@SuppressWarnings("unchecked")
	private void initParams() {
		isCanRegister=getIntent().getIntExtra("tag", -1);
		timeList = (List<TimeList>) getIntent()
				.getSerializableExtra("timeList");
		tag = getIntent().getIntExtra("tag", -1);
		isCanRegister = getIntent().getIntExtra("isCanRegister", -1);
		doctor = (Doctor) getIntent().getExtras().get("doctor");
		timeStr= getIntent().getStringExtra("time");
		timeListAM = new ArrayList<TimeList>();
		timeListPM = new ArrayList<TimeList>();
		timeListNight=new ArrayList<TimeList>();
		timeListAll=new ArrayList<TimeList>();
		
		if (timeList != null) {
			for (int i = 0; i < timeList.size(); i++) {
				if (timeList.get(i).getDaysection() == 1) {
					timeListAM.add(timeList.get(i));
					regtotalAM=timeList.get(i).getRegtotalAM();
				}
                if (timeList.get(i).getDaysection() == 2) {
					timeListPM.add(timeList.get(i));
					regtotalPM=timeList.get(i).getRegtotalPM();
				}
                if (timeList.get(i).getDaysection() == 3) {
                	
                	timeListNight.add(timeList.get(i));
					regtotalNight=timeList.get(i).getRegtotalNight();
                	
				}
                
                if (timeList.get(i).getDaysection() == 4) {
                	
                	timeListAll.add(timeList.get(i));
				}
                
			}
		}
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		
		if (tag == 1) {
			linear1.setVisibility(View.VISIBLE);
		}

		if (tag == 2) {
			linear2.setVisibility(View.VISIBLE);
		}

		if (tag == 3) {
			
			linear3.setVisibility(View.VISIBLE);
		}
         if (tag == 4) {
			
			linear4.setVisibility(View.VISIBLE);
		}
		
		

        
        if(tag==5)
        {
        	if(regtotalAM>0)
        	{
        		linear1.setVisibility(View.VISIBLE);
        	}
        	if(regtotalPM>0)
        	{
        		linear2.setVisibility(View.VISIBLE);
        	}
        	if(regtotalNight>0)
        	{
        		linear3.setVisibility(View.VISIBLE);
        	}
        	if(regtotalAll>0)
        	{
        		linear4.setVisibility(View.VISIBLE);
        	}
        	
        }
		
		tv_date_str=(TextView) findViewById(R.id.tv_date_str);
		tv_tips=(TextView) findViewById(R.id.tv_tips);
		
		if(timeStr!=null&&timeStr.length()>0)
		{
		tv_date_str.setText(timeStr.split("-")[1]+"月"+timeStr.split("-")[2]+"日  "+DateTools.getChinaDayOfWeek(timeStr));
		}
		if(timeList!=null&&timeList.size()>0)
		{
//			String timeStr=timeList.get(0).getShiftdate();
			
		}
		else
		{
			tv_tips.setText("当前日期无排班");
			tv_tips.setVisibility(View.VISIBLE);
			linear1.setVisibility(View.GONE);
			linear2.setVisibility(View.GONE);
			linear3.setVisibility(View.GONE);
			linear4.setVisibility(View.GONE);
			
		}
		
		 timeList=null;
		
	}

	private void initView() {
		MyGridView gridViewAM, gridViewPM,gridViewNight,gridViewAll;
		gridViewAM = (MyGridView) findViewById(R.id.gridViewAM);
		gridViewAM.setHaveScrollbar(false);
		DoctorTimeGridAdapter adapter1 = new DoctorTimeGridAdapter(this, timeListAM, doctor,regtotalAM,1,isCanRegister);
		gridViewAM.setAdapter(adapter1);
		gridViewPM = (MyGridView) findViewById(R.id.gridViewPM);
		gridViewPM.setHaveScrollbar(false);
		DoctorTimeGridAdapter adapter2 = new DoctorTimeGridAdapter(this, timeListPM,doctor,regtotalPM,2,isCanRegister);
		gridViewPM.setAdapter(adapter2);
		
		gridViewNight = (MyGridView) findViewById(R.id.gridViewNight);
		gridViewNight.setHaveScrollbar(false);
		DoctorTimeGridAdapter adapter3 = new DoctorTimeGridAdapter(this, timeListNight,doctor,regtotalNight,3,isCanRegister);
		gridViewNight.setAdapter(adapter3);
		
		gridViewAll = (MyGridView) findViewById(R.id.gridViewALL);
		gridViewAll.setHaveScrollbar(false);
		DoctorTimeGridAdapter adapter4 = new DoctorTimeGridAdapter(this, timeListAll,doctor,regtotalAll,4,isCanRegister);
		gridViewAll.setAdapter(adapter4);
	}
	
	
	//点击屏幕区域外关闭activity
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this, event)) {
			finish();
			return false;
		}
		return super.onTouchEvent(event);
	}

	private boolean isOutOfBounds(Activity context, MotionEvent event) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
		final View decorView = context.getWindow().getDecorView();
		return (x < -slop) || (y < -slop)|| (x > (decorView.getWidth() + slop))|| (y > (decorView.getHeight() + slop));
	}

}
