package com.guangyi.finddoctor.secondActivity;

import java.net.SocketException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.MainActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.selfService.MainSymtomSearchActivity;
import com.guangyi.finddoctor.selfService.SelfExaminationActivity;
import com.guangyi.finddoctor.selfService.SelfServiceLibraryAssayLibrary;
import com.guangyi.finddoctor.selfService.SelfServiceLibraryDieseaseLibrary;
import com.guangyi.finddoctor.selfService.SelfServiceMapDrugstoreActivity;
import com.guangyi.finddoctor.selfService.SelfServiceMapHospitalActivity;
import com.guangyi.finddoctor.utils.StateInfos;

public class SselfService extends BasicActivity{

	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_service);
		

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initView();
		initSelfView();
		
	}
	
	
	@Override
	protected void onStart() {
		try {
			new StateInfos(SselfService.this,"自助服务",1).postStateInfo();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onStart();
	}
	
	
	private void initView() {
	  
		Button mBack;mBack = (Button) findViewById(R.id.ib_back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity(MainActivity.class);
				finish();


			}
		});

	}
	
	
	
	
	
	private void initSelfView() {
		Button self_service_item01, self_service_item02, self_service_item03, self_service_item04, self_service_item05;
		self_service_item01 = (Button) findViewById(R.id.self_service_item01);
		self_service_item02 = (Button) findViewById(R.id.self_service_item02);
		self_service_item03 = (Button) findViewById(R.id.self_service_item03);
		self_service_item04 = (Button) findViewById(R.id.self_service_item04);
		self_service_item05 = (Button) findViewById(R.id.self_service_item05);
		self_service_item01.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openActivity(SelfExaminationActivity.class);

			}
		});

		self_service_item02.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openActivity(SelfServiceLibraryDieseaseLibrary.class);

			}
		});

		self_service_item03.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openActivity(SelfServiceMapHospitalActivity.class);
//				Intent it=new Intent();
//				it.setClass(SselfService.this,SelfServiceMapHospitalActivity.class);
//				it.putExtra("tag", 1);
//				startActivity(it);

			}
		});

		self_service_item04.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openActivity(SelfServiceMapDrugstoreActivity.class);
//				Intent it=new Intent();
//				it.setClass(SselfService.this,SelfServiceMapDrugstoreActivity.class);
//				it.putExtra("tag", 1);
//				startActivity(it);


			}
		});

		self_service_item05.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openActivity(SelfServiceLibraryAssayLibrary.class);

			}
		});
		// mOnLineGridView = (GridView) view.findViewById(R.id.gv_main);
		final EditText editText1 = (EditText)findViewById(R.id.editText1);
		ImageView iv_search_btn = (ImageView)findViewById(R.id.iv_search_btn);
//		editText1.setOnKeyListener(new OnKeyListener() {
//
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_ENTER) {
//					Intent it = new Intent();
//					it.putExtra("word", editText1.getText().toString().trim());
//					it.setClass(getApplicationContext(),
//							MainSearchActivity.class);
//					startActivity(it);
//				}
//				return false;
//			}
//
//		});

		iv_search_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
				//得到InputMethodManager的实例 
				if (imm.isActive()) { 
				//如果开启 
				imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS); 
				//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的 
				} 
				
				if(editText1.getText().toString().trim().length()>0)
				{
					if(isNetworkConnected())
					{
					Intent it = new Intent();
					it.putExtra("word", editText1.getText().toString().trim());
					it.setClass(SselfService.this, MainSymtomSearchActivity.class);
					startActivity(it);
					}
				}
				else
				{
					showToast("搜索内容不能为空!");
				}

			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	    
			openActivity(MainActivity.class);
			finish();
			
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
