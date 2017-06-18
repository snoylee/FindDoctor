package com.guangyi.finddoctor.personCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.ViewGroup;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.CommonPatientAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.hospitalRegister.HospitalRegisterPost;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.CommonPatient;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.onlineAsk.OnLineAddConsultInfoForChart;
import com.guangyi.finddoctor.utils.Config;

/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:常用问诊人
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
public class CommonPatientActivity extends BasicActivity {
	private ListView mListView;
	private Button mBack;
	private ApiHttpUtil mApiHttpUtil;
	private Handler mHandler;
	private int userId = -1;

	private CommonPatientAdapter commonPatientAdapter;
	SharedPreferences mSharedPreferences;
	private List<CommonPatient> listCommonPatient;
	String reason;
	private String timeStr, feeStr, way;
	private int allDay,daySectionFlag;
	private Doctor doctor;
	private int tagType = 0;
	private Runnable mRunnable;
	private Thread mThread;
		
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
		setContentView(R.layout.common_patient);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		// bindData();
		initListener();
		getData();

	}

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

				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", String.valueOf(userId));

				msg.arg1 = 1;

				msg.obj = mApiHttpUtil.postMethod(
					Config.getProperty("PATIENT_STRING", ""), params);
//				msg.obj = mApiHttpUtil.postMethod(
//						Config.getProperty("REGION_STRING_ONE", ""), params);

				mHandler.sendMessage(msg);

			}
		};
		commonThreadStart();
	}

	private void initParams() {
		tagType = getIntent().getExtras().getInt("tagtype");
		if (this.getIntent().getExtras() != null) {
			feeStr = getIntent().getExtras().getString("fee");
			timeStr = getIntent().getExtras().getString("time");
			doctor = (Doctor) getIntent().getExtras().getSerializable("doctor");
			allDay=getIntent().getExtras().getInt("allDay", 0);
			daySectionFlag=getIntent().getExtras().getInt("daySectionFlag", 0);
		}
		initMySharedPreference();
		mApiHttpUtil = new ApiHttpUtil();
		mHandler = new MyHandler();
		listCommonPatient = new ArrayList<CommonPatient>();
		commonPatientAdapter = new CommonPatientAdapter(CommonPatientActivity.this, listCommonPatient);
	}

	private void initMySharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		userId = mSharedPreferences.getInt("userId", -1);

	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.lv_chose);
		mBack = (Button) findViewById(R.id.ib_back);
		
		View addButton = getLayoutInflater().inflate(R.layout.ui_add_button,
				null);
		mListView.addFooterView(addButton, null, false);
		Button btn_add = (Button) addButton.findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CommonPatientActivity.this,
						AddPatientActivity.class);
				intent.putExtra("tagtype", 0);
				// startActivityForResult(intent, 0);
				startActivityForResult(intent, 0);
			}
		});
		
		mListView.setAdapter(commonPatientAdapter);

	}

	private void initListener() {

		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CommonPatient item = (CommonPatient) arg0
						.getItemAtPosition(arg2);
				if (tagType == 1) {
					Intent it = new Intent();

					if (doctor != null && timeStr != null) {
						it.setClass(getApplicationContext(),
								HospitalRegisterPost.class);
						it.putExtra("time", timeStr);
						it.putExtra("fee", feeStr);
						it.putExtra("doctor", doctor);
						it.putExtra("way", String.valueOf(way));
						it.putExtra("commonPatient", item);
						it.putExtra("allDay", allDay);
						it.putExtra("daySectionFlag", daySectionFlag);
						startActivity(it);
					} else if (doctor == null) {
						it.setClass(getApplicationContext(),
								OnLineAddConsultInfoForChart.class);
						it.putExtra("commonPatient", item);
						startActivity(it);
					}
				}else{
					Intent intent = new Intent(CommonPatientActivity.this,AddPatientActivity.class);
					intent.putExtra("tagtype", 1);
					intent.putExtra("patient", item);
					startActivityForResult(intent, 0);
				}

			}
		});

		// 删除常用问诊人
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				CommonPatient item = (CommonPatient) arg0
						.getItemAtPosition(arg2);

				final int id = item.getId();
				String name = item.getName();
				new AlertDialog.Builder(CommonPatientActivity.this)
						.setTitle("删除常用问诊人")
						.setMessage("是否删除：" + name)

						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										delCommonPatient(id,arg2);
									}
								}).setNegativeButton("取消", null).show();
				return true;
			}
		});

	}

	private void delCommonPatient(final int id,final int position) {
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				msg.arg1 = 2;
				msg.arg2=position;
				msg.obj = mApiHttpUtil.getMethod(
						Config.getProperty("DEL_PATIENT", ""),
						String.valueOf(id));
				mHandler.sendMessage(msg);

			}
		};
		commonThreadStart();
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			
			canclePregressDialog();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			Log.i("jsonString", jsonString);
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
						try {
							if(listCommonPatient.size()>0)
							{
								listCommonPatient.clear();
							}
							JSONArray jsonArr = jsonObject
									.getJSONArray("PatientsList");
							CommonPatient commonPatient;
							for (int i = 0; i < jsonArr.length(); i++) {
								commonPatient = new CommonPatient();
								commonPatient.setId(jsonArr.getJSONObject(i)
										.getInt("id"));
								commonPatient.setName(jsonArr.getJSONObject(i)
										.getString("userName"));
								commonPatient.setAge(jsonArr.getJSONObject(i)
										.getInt("userAge"));
								commonPatient.setSex(jsonArr.getJSONObject(i)
										.getInt("userSex"));
								commonPatient
										.setUserCard(jsonArr.getJSONObject(i)
												.getString("userCard"));
								commonPatient.setUserMoble(jsonArr
										.getJSONObject(i)
										.getString("userMoble"));
								 commonPatient.setUserAddress(jsonArr.getJSONObject(i).getString("userAddress"));
								commonPatient.setDieaseName(jsonArr
										.getJSONObject(i).getString(
												"dieaseName"));

								listCommonPatient.add(commonPatient);

							}
							
							
//							View emptyView = LayoutInflater.from(CommonPatientActivity.this).inflate(R.layout.empty_view, null);
//							emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
//							emptyView.setVisibility(View.GONE);   
//							((ViewGroup)mListView.getParent()).addView(emptyView);   
//							mListView.setEmptyView(emptyView);
							
							commonPatientAdapter.notifyDataSetChanged();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					else if(msg.arg1==2)
					{
							Toast.makeText(CommonPatientActivity.this, reason,
									Toast.LENGTH_LONG).show();
							listCommonPatient.remove(msg.arg2);
							commonPatientAdapter.notifyDataSetChanged();
							
					}
						
				}
				else
				{
					showToast(reason);
				}
				
			}
			
			super.handleMessage(msg);
			
		
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == ResultCodeCategory.RESULT_CODE_ADD_PATIENT) {

			getData();

		}
		super.onActivityResult(requestCode, resultCode, data);
	}


}
