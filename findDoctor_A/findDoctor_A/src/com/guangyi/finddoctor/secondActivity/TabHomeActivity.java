package com.guangyi.finddoctor.secondActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
public class TabHomeActivity extends TabActivity
{
	RadioGroup mRadioGroup = null;
	RadioButton mRadioButtonHome = null;
	RadioButton mRadioButtonMessage = null;
	RadioButton mRadioButtonUser = null;
	RadioButton mRadioButtonSearch = null;
    Intent intent;
    private  int checkId;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lay_hostmain);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		intent=getIntent();
		checkId=intent.getIntExtra("ckeckId", 0);
		mRadioGroup = (RadioGroup) findViewById(R.id.radio_group_main);
		mRadioButtonHome = (RadioButton) findViewById(R.id.radio_home);
		mRadioButtonMessage = (RadioButton) findViewById(R.id.radio_message);
		mRadioButtonUser = (RadioButton) findViewById(R.id.radio_user);
		mRadioButtonSearch = (RadioButton) findViewById(R.id.radio_search);
		
		ActivityController.empty();
		ActivityController.mInstance = this;
		ActivityController.mTabHost = getTabHost();
		ActivityController.init(this);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		switch (checkId) {
		case 0:
			mRadioButtonHome.setChecked(true);
			ActivityController.startView(new Intent(
					ActivityController.hospitalRegister));
			break;
		case 1:
			mRadioButtonMessage.setChecked(true);
			ActivityController.startView(new Intent(
					ActivityController.onlineAsk));
			break;
		case 2:
			mRadioButtonUser.setChecked(true);
			ActivityController.startView(new Intent(
					ActivityController.selfService));
			break;
		case 3:
			mRadioButtonSearch.setChecked(true);
			ActivityController.startView(new Intent(
					ActivityController.personCenter));
			break;
		default:
			break;
		}
		mRadioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
				{
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId)
					{
						if (checkedId == R.id.radio_home) {
							ActivityController.startView(new Intent(
									ActivityController.hospitalRegister));
						} else if (checkedId == R.id.radio_message) {
							ActivityController.startView(new Intent(
									ActivityController.onlineAsk));
						} else if (checkedId == R.id.radio_user) {
							ActivityController.startView(new Intent(
									ActivityController.selfService));
						} else if (checkedId == R.id.radio_search) {
							ActivityController.startView(new Intent(
									ActivityController.personCenter));
						} else {
						}
					}
				});
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
}
