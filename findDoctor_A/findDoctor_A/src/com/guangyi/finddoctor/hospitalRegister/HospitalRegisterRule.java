package com.guangyi.finddoctor.hospitalRegister;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;

public class HospitalRegisterRule extends BasicActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getNotitle();
		setContentView(R.layout.hospital_resister_rules);
		TextView tv_rule_content=(TextView) findViewById(R.id.tv_rule_content);
		FindDoctorApplication app=(FindDoctorApplication) getApplication();
		String rule_content=app.getRegisterRule();
		if(rule_content!=null)
		{
			tv_rule_content.setText(rule_content);
		}
	}
	
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
