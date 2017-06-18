package com.guangyi.finddoctor.selfService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.Department;
import com.guangyi.finddoctor.model.Hospital;
import com.guangyi.finddoctor.personCenter.UserLoginActivity;
import com.guangyi.finddoctor.spicalHosptalHome.AdapterForLinearLayout;
import com.guangyi.finddoctor.spicalHosptalHome.MyLinearLayoutForListAdapter;
import com.guangyi.finddoctor.utils.Config;

/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:医院主页
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:中国移动有限公司东莞分公司
 * </p>
 * 
 * @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
 * @version：1.0
 * @since：2013-9-23
 */
public class SelfServiceHospitalHome extends BasicActivity {
	private Hospital hospital;
	private TextView tv_homepage_hospname, tv_homepage_hospclass,
			tv_homepage_tel, tv_homepage_address, tv_hosp_introduce;
	private Button ib_hosp_collection;
	private Handler mHandler;
	private SharedPreferences mSharedPreferences;
	private int userId;
	private Runnable mRunnable;
	private Thread mThread;
	
	
	
private AdapterForLinearLayout adapter;
	
	private MyLinearLayoutForListAdapter mLinearLayout;
	
		
	private void commonThreadStart()
		{
			if(mRunnable!=null)
			{
			mThread=new Thread(mRunnable);
			mThread.start();
			}
		}

		protected void onStop() {
			mHandler.removeCallbacks(mRunnable);
			super.onStop();
		}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospital_homepage);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initData();
		if(hospital.getGuahaoHostId()!=null)
		{
		getData();
		}
	}
	
	
//	private void init(){
//		
//	}
	
//	private void setAdapterAndOnClickListener(){
//		initAdapter(list);
//		addLinearListener();
//		mLinearLayout.setAdapter(adapter);
//
//	}
	
//	private void initAdapter(List<Map<String, String>> list){
//		adapter = new AdapterForLinearLayout(this, R.layout.list_item, list, 
//				new String[]{"key1", "key2"}, new int[]{R.id.tv_list_item_1, R.id.tv_list_item_2});
//	}
	
//	private void addLinearListener(){
//				mLinearLayout.setOnClickListener(new OnClickListener() {
//	            @Override
//	            public void onClick(View v) {
//	                showToast(((TextView)v.findViewById(R.id.tv_list_item_1)).getText().toString());
////	            	showToast("-_-");
//	            }
//	        });
//	}
	
	
	@Override
	protected void onStart() {
		
		super.onStart();
	}

	private void getData() {
		initProgressDialog();
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(hospital.getGuahaoHostId()));
				msg.arg1 = 1;
				msg.obj = new ApiHttpUtil().getMethod(
						Config.getProperty("GET_DEPARTBASEINFO", ""), list);

				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();

	}

	private void initParams() {
		getSharedPreference();
		hospital = (Hospital) getIntent().getSerializableExtra("hospital");
		mHandler = new MyHandler();
	}

	private void getSharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		
	}

	private void initView() {
		mLinearLayout = (MyLinearLayoutForListAdapter)this.findViewById(R.id.mylinear_way1);
		ib_hosp_collection = (Button) findViewById(R.id.ib_hosp_collection);
		tv_homepage_hospname = (TextView) findViewById(R.id.tv_homepage_hospname);
		tv_homepage_hospclass = (TextView) findViewById(R.id.tv_homepage_hospclass);
		tv_homepage_tel = (TextView) findViewById(R.id.tv_homepage_tel);
		tv_homepage_address = (TextView) findViewById(R.id.tv_homepage_address);
		tv_hosp_introduce = (TextView) findViewById(R.id.tv_hosp_introduce);
		Button Back = (Button) findViewById(R.id.ib_back);

		Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		ib_hosp_collection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userId = mSharedPreferences.getInt("userId", -1);
				if (userId>-1) {
					initProgressDialog();
					mRunnable=new Runnable() {
						@Override
						public void run() {
							HashMap<String, String> parms = new HashMap<String, String>();
							parms.put("transactionId  ",
									String.valueOf(new Date().getTime()));
							parms.put("favId",
									String.valueOf(hospital.getHospId()));
							parms.put("favType", "1");
							parms.put("userId", String.valueOf(userId));
							Message msg = new Message();
							msg.arg1 = 2;
							msg.obj = new ApiHttpUtil().postMethod(
									Config.getProperty("SAVEFAVOURITE", ""),
									parms);
							mHandler.sendMessage(msg);

						}
					};
					commonThreadStart();
				} else {
					showToast("请先登录后重试!");
					Intent it = new Intent(SelfServiceHospitalHome.this,
							UserLoginActivity.class);
//					startActivityForResult(it, 0);
					startActivity(it);
				}

			}
		});
	}

	private void initData() {
		if (hospital != null) {
			tv_homepage_hospname.setText(Html.fromHtml(hospital.getHospName()));
			if (hospital.getHospClass() == 11) {
				tv_homepage_hospclass.setText("一级丙等");
			}
			if (hospital.getHospClass() == 12) {
				tv_homepage_hospclass.setText("一级乙等");
			}
			if (hospital.getHospClass() == 13) {
				tv_homepage_hospclass.setText("一级甲等");
			}
			
			if (hospital.getHospClass() == 21) {
				tv_homepage_hospclass.setText("二级丙等");
			}
			if (hospital.getHospClass() == 22) {
				tv_homepage_hospclass.setText("二级乙等");
			}
			if (hospital.getHospClass() == 23) {
				tv_homepage_hospclass.setText("二级甲等");
			}
			
			if (hospital.getHospClass() == 31) {
				tv_homepage_hospclass.setText("三级丙等");
			}
			if (hospital.getHospClass() == 32) {
				tv_homepage_hospclass.setText("三级乙等");
			}
			if (hospital.getHospClass() == 33) {
				tv_homepage_hospclass.setText("三级甲等");
			}
			tv_homepage_tel.setText(Html.fromHtml(hospital.getHospTel()));
			tv_homepage_address.setText(Html.fromHtml(hospital.getHospAddr()));
			tv_hosp_introduce.setText(Html.fromHtml(hospital.getHospIntroduction()));
		}

	}
	

	private void getDeptView(final List<Department> list) {
		adapter=new AdapterForLinearLayout(this, list);
		mLinearLayout.setAdapter(adapter,this,list);
		
	}

	

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			canclePregressDialog();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
				showToast(getResources().getString(R.string.soconntimeout));
			} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
				showToast(getResources().getString(R.string.conntimeout));
			}
			else
			{
				int code = -1;
				String reason=getResources().getString(R.string.network_preoblem);
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					code = jsonObject.getInt("code");
					reason=jsonObject.getString("reason");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(code==0)
				{
					if(msg.arg1==1)
					{
						try
						{
							JSONArray jsonArr = jsonObject
									.getJSONArray("DepartBaseList");
							List<Department> list = new ArrayList<Department>();
							Department department;
							for (int i = 0; i < jsonArr.length(); i++) {
								department = new Department();
								department.setHdeptId(jsonArr.getJSONObject(i)
										.getString(Department.HDEPTID));
								department.setDepaName(jsonArr.getJSONObject(i)
										.getString(Department.DEPANAME));
								department.setDoctNum(jsonArr.getJSONObject(i)
										.getInt(Department.DOCTNUM));
								department.setGuanghaoStatus(jsonArr.getJSONObject(i)
										.getInt(Department.GUANGHAOSTATUS));
								list.add(department);
							}
							getDeptView(list);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					else if(msg.arg1==2)
					{
						showToast("收藏成功！");
						
					}

						
				}
				else
				{
					showToast(reason);
				}
				
			}
			
			super.handleMessage(msg);
			
			
			
			
			
			
			
			
//			
//			if (msg.obj != null) {
//				switch (msg.arg1) {
//				case 1:
//					String jsonStr = (String) msg.obj;
//					Log.i("deptbaseinfo", jsonStr);
//					if (jsonStr.equals(ApiHttpUtil.CONNTIMEOUT)) {
//						canclePregressDialog();
//						showToast(getResources().getString(R.string.conntimeout));
//					} else {
//						try {
//							canclePregressDialog();
//							JSONObject jsonObj = new JSONObject(jsonStr);
//							JSONArray jsonArr = jsonObj
//									.getJSONArray("DepartBaseList");
//							List<Department> list = new ArrayList<Department>();
//							Department department;
//							for (int i = 0; i < jsonArr.length(); i++) {
//								department = new Department();
//								department.setHdeptId(jsonArr.getJSONObject(i)
//										.getString(Department.HDEPTID));
//								department.setDepaName(jsonArr.getJSONObject(i)
//										.getString(Department.DEPANAME));
//								department.setDoctNum(jsonArr.getJSONObject(i)
//										.getInt(Department.DOCTNUM));
//								list.add(department);
//							}
//							getDeptView(list);
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					break;
//				case 2:
//					String jsonStr1 = (String) msg.obj;
//					String reason = "";
//					if (jsonStr1.equals(ApiHttpUtil.CONNTIMEOUT)) {
//						canclePregressDialog();
//						showToast(getResources().getString(R.string.conntimeout));
//					} else {
//						try {
//							canclePregressDialog();
//							JSONObject jsonObject = new JSONObject(jsonStr1);
//							reason = jsonObject.getString("reason");
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						showToast(reason);
//					}
//					break;
//
//				default:
//					break;
//				}
//
//			} else {
//				showToast("请检查网络连接状态");
//			}
//
//			super.handleMessage(msg);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == ResultCodeCategory.RESULT_CODE_LOGIN) {
//			mSharedPreferences = getSharedPreferences("personCenter",
//					Context.MODE_PRIVATE);
//			mEditor = mSharedPreferences.edit();
//			mEditor.putInt("userId", data.getExtras().getInt("userId"));
//			mEditor.putBoolean("isLogin", data.getExtras()
//					.getBoolean("isLogin"));
//			mEditor.putBoolean("isShow", true);
//			mEditor.putString("userMobile",
//					data.getExtras().getString("userMobile"));
//			mEditor.commit();
//			getSharedPreference();
//
//		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
