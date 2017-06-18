package com.guangyi.finddoctor.hospitalRegister;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.DoctorTimeAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.dateview.JudgeDate;
import com.guangyi.finddoctor.dateview.ScreenInfo;
import com.guangyi.finddoctor.dateview.WheelMain;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.model.NewTimeItem;
import com.guangyi.finddoctor.model.TimeList;
import com.guangyi.finddoctor.reflashView.PullToRefreshView;
import com.guangyi.finddoctor.reflashView.PullToRefreshView.OnFooterRefreshListener;
import com.guangyi.finddoctor.reflashView.PullToRefreshView.OnHeaderRefreshListener;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.DateTools;

/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:医生排班列表
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
public class DoctorTimeListActivity extends BasicActivity {
	private Handler mHandler;
	private String todayStr;
	private Doctor doctor;
	private int consType;// 排版类型
	private Runnable mRunnable;
	private Thread mThread;
	private WheelMain wheelMain;
	
	
	
    private List<NewTimeItem> mList;
	private ListView listView;
	private DoctorTimeAdapter mAdapter;
	private PullToRefreshView mPullToRefreshView;
	private Boolean isFirstReflash=true;
	private String dialogTime;
	private int isCanRegister;
	private Boolean isVisible;
	private int listFsize=0;

	
	
	private void commonThreadStart() {
		if (mRunnable != null) {
			mThread = new Thread(mRunnable);
			mThread.start();
		}
	}

	protected void onStop() {
		mHandler.removeCallbacks(mRunnable);
		isVisible=false;
		super.onStop();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_time_list);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initBack();
		initParams();
		initListView();
		getData(todayStr,0,0,1);
	}

	@Override
	protected void onStart() {
		isVisible=true;
		super.onStart();
	}

	private void changeTextTime(String time) {
//		mTime.setText(time + "  周" + weekTool.getWeek(todayStr));
//		todayStr = time;
		time= DateTools.getDateOffset(time, 0);
		dialogTime=time;
		getData(time, 1,0,2);
	}


	private void initBack() {
		Button Back = (Button) findViewById(R.id.ib_back);
		Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Button ib_post = (Button) findViewById(R.id.ib_post);
		ib_post.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTime();
			}
		});

	}
	private void initListView() {
		mPullToRefreshView = (PullToRefreshView)findViewById(R.id.main_pull_refresh_view);
		listView= (ListView) findViewById(R.id.mlist);
		listView.setAdapter(mAdapter);
		//刷新的组件设置的监听
		mPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefresh());
		mPullToRefreshView.setOnFooterRefreshListener(new OnFooterRefresh());

	}
	//刷新需要的时间
	private void onLoaded() {
		mPullToRefreshView.onHeaderRefreshComplete(getResources().getString(
				R.string.pull_to_refresh_pull_last_time)
				+ DateTools.getCurrentDateString("MM-dd HH:mm:ss"));
		mPullToRefreshView.onFooterRefreshComplete();
	}
//复写的两个刷新主要的方法
	class OnHeaderRefresh implements OnHeaderRefreshListener {

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {

			if(isFirstReflash)
			{
			todayStr= DateTools.getDateOffset(todayStr, 15);
			}
			isFirstReflash=false;
			getData(todayStr,0,1,1);
		}

	}

	class OnFooterRefresh implements OnFooterRefreshListener {
		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			if(isFirstReflash)
			{
			todayStr= DateTools.getDateOffset(todayStr, 15);
			}
			isFirstReflash=false;
			getData(todayStr,0,1,1);
		}

	}
	public void showTime()
	{
		LayoutInflater inflater=LayoutInflater.from(this);
		final View timepickerview=inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(this);
		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		Calendar calendar = Calendar.getInstance();
		if(JudgeDate.isDate(DateTools.getCurrentDate(), "yyyy-MM-dd")){
			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(dateFormat.parse(DateTools.getCurrentDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year,month,day);
		new AlertDialog.Builder(this)
		.setTitle("选择日期")
		.setView(timepickerview)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String time= DateTools.getDateOffset(wheelMain.getTime(), 0);
				changeTextTime(time);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
	}
	private void initParams() {
		doctor = (Doctor) getIntent().getSerializableExtra("doctor");
		consType = getIntent().getExtras().getInt("consType");
		isCanRegister=getIntent().getIntExtra("isCanRegister", -1);
		todayStr = DateTools.getCurrentDate();
		todayStr= DateTools.getDateOffset(todayStr, 1);
		mHandler = new MyHandler();
		mList=new ArrayList<NewTimeItem>();
		mAdapter=new DoctorTimeAdapter(this, mList,doctor,isCanRegister);
	}

	

	// 获取医生排班信息
	private void getData(final String dateStr,final int searchType,final int tag,final int arg1) {
		Log.i("dateStr", dateStr);
		if(tag==0)
		{
			initProgressDialog();
		}
		
		mRunnable = new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				HashMap<String, String> parms = new HashMap<String, String>();
				parms.put("HospitalId", doctor.getDoctHospid());
				parms.put("DepartmentID", doctor.getDoctDepaid());
				parms.put("expertId", doctor.getExpertId());
				parms.put("shiftDate", dateStr);
				parms.put("consType", String.valueOf(consType));
				parms.put("searchType", String.valueOf(searchType));
				msg.arg1 = arg1;
				msg.arg2=tag;
				msg.obj = new ApiHttpUtil()
						.postMethod(Config.getProperty(
								"GET_DOCTOR_Time_STRING", ""), parms);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
	}
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			canclePregressDialog();
			onLoaded();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
				showToast(getResources().getString(R.string.soconntimeout));
			} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
				showToast(getResources().getString(R.string.conntimeout));
			} else {

				int code = -1;
				String reason = getResources().getString(
						R.string.network_preoblem);
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					
					code = jsonObject.getInt("code");
					reason = jsonObject.getString("reason");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (code == 0) {

					if (msg.arg1 == 1) {
						if(msg.arg2==0)
						{
							mList.clear();
						}
						try {
							listFsize=mList.size();
							NewTimeItem newTimeItem;
							List<TimeList> listTimemeList;
							String rule=jsonObject.getJSONObject("results").getString("rule");
							FindDoctorApplication application=(FindDoctorApplication) getApplication();
							application.setRegisterRule(rule);
							JSONArray jsonArray = jsonObject
									.getJSONArray("SchedulingListNew");
							for (int i = 0; i < jsonArray.length(); i++) {
								listTimemeList=new ArrayList<TimeList>();
								newTimeItem=new NewTimeItem();
								JSONArray jsonArray1=jsonArray.getJSONObject(i).getJSONArray("date");
								for (int j = 0; j < jsonArray1.length(); j++) {
									JSONArray mJsonArray=jsonArray1.getJSONObject(j).getJSONArray("groupList");
									FindDoctorApplication app=(FindDoctorApplication) getApplication();
									app.setmJsonArray(mJsonArray);
									String clinicidname=jsonArray1.getJSONObject(j).getString(NewTimeItem.CLINICIDNAME);
									int daysection=jsonArray1.getJSONObject(j).getInt(TimeList.DAYSECTION);
									String shiftdate =jsonArray1.getJSONObject(j).getString(NewTimeItem.SHIFTDATE);
									String scid=jsonArray1.getJSONObject(j).getString(NewTimeItem.SCID);
									int regtotal=jsonArray1.getJSONObject(j).getInt(NewTimeItem.REGTOTALAM);
									String regfee=jsonArray1.getJSONObject(j).getString(NewTimeItem.REGFEE);
									newTimeItem.setScid(scid);
									newTimeItem.setClinicidname(clinicidname);
									newTimeItem.setShiftdate(shiftdate);
									newTimeItem.setRegfee(regfee);
									if(daysection==1)
									{
									newTimeItem.setRegtotalAM(regtotal);
									newTimeItem.setAM(true);
									}
									if(daysection==2)
									{
										newTimeItem.setRegtotalPM(regtotal);
										newTimeItem.setPM(true);
									}
									if(daysection==3)
									{
										newTimeItem.setRegtotalNight(regtotal);
										newTimeItem.setNight(true);
									}
									
									if(daysection==4)
									{
										newTimeItem.setRegtotalAll(regtotal);
										newTimeItem.setAll(true);
									}
									JSONArray jsonArray2=jsonArray1.getJSONObject(j).getJSONArray("times");
									
									for (int j2 = 0; j2 < jsonArray2.length(); j2++) {
										TimeList timeList=new TimeList();
										timeList.setBtnState(jsonArray2.getJSONObject(j2).getInt(TimeList.BTNSTATE));
										timeList.setTimeList(jsonArray2.getJSONObject(j2).getString(TimeList.TIMELIST));
										timeList.setDaysection(daysection);
										timeList.setClinicidname(clinicidname);
										timeList.setShiftdate(shiftdate);
										timeList.setScid(scid);
										timeList.setRegfee(regfee);
										if(daysection==1)
										{
											timeList.setRegtotalAM(regtotal);
										}
										 if(daysection==2)
										{
											timeList.setRegtotalPM(regtotal);
										}
										 
										 if(daysection==3)
											{
												timeList.setRegtotalNight(regtotal);
											}
										 
										 if(daysection==4)
											{
												timeList.setRegtotalAll(regtotal);	
											}
										listTimemeList.add(timeList);
									}
								}
								newTimeItem.setListTimemeList(listTimemeList);
								mList.add(newTimeItem);
								Log.d("mListLength", mList.size()+"");
//								if(mList.size()<=0&&msg.arg2==0)
//								{
//									showToast("当前日期无排班");
//								}
//								Log.i("mlistsize--->",mList.size()+"");
//								if(mList.size()<=0)
//								{
//									showToast("当前日期无排班");
//								}
							}
							if(msg.arg2==1)
							{
								todayStr= DateTools.getDateOffset(todayStr, 8);
								isFirstReflash=false;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						finally
						{
							Log.i("mlistsize--->",mList.size()+"");
							if(code==0&&mList.size()<=0)
							{
//								showToast("当前日期无排班");
								
								View emptyView = LayoutInflater.from(DoctorTimeListActivity.this).inflate(R.layout.empty_view, null);
								emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
								emptyView.setVisibility(View.GONE);   
								((ViewGroup)listView.getParent()).addView(emptyView);   
								listView.setEmptyView(emptyView);
								mPullToRefreshView.closeFootView();
							}
							mAdapter.notifyDataSetChanged();
							
//							if (mList.size() > listFsize) {
//								listView.setSelection(mList.size()-1);
//							}
//							
//							else
//							{
//								mPullToRefreshView.stopCanFlash();
//							}

						}
					}
					else if (msg.arg1 == 2) {
						try {
							List<TimeList> listTimemeList1 = null;
							JSONArray jsonArray = jsonObject
									.getJSONArray("SchedulingListNew");
							for (int i = 0; i < jsonArray.length(); i++) {
								listTimemeList1=new ArrayList<TimeList>();
								JSONArray jsonArray1=jsonArray.getJSONObject(i).getJSONArray("date");
								for (int j = 0; j < jsonArray1.length(); j++) {
									JSONArray mJsonArray=jsonArray1.getJSONObject(i).getJSONArray("groupList");
									FindDoctorApplication app=(FindDoctorApplication) getApplication();
									app.setmJsonArray(mJsonArray);
									String clinicidname=jsonArray1.getJSONObject(j).getString(NewTimeItem.CLINICIDNAME);
									int daysection=jsonArray1.getJSONObject(j).getInt(TimeList.DAYSECTION);
									String shiftdate =jsonArray1.getJSONObject(j).getString(NewTimeItem.SHIFTDATE);
									String scid=jsonArray1.getJSONObject(j).getString(NewTimeItem.SCID);
									int regtotal=jsonArray1.getJSONObject(j).getInt(NewTimeItem.REGTOTALAM);
									String regfee=jsonArray1.getJSONObject(j).getString(NewTimeItem.REGFEE);
									
									JSONArray jsonArray2=jsonArray1.getJSONObject(j).getJSONArray("times");
									for (int j2 = 0; j2 < jsonArray2.length(); j2++) {
										TimeList timeList=new TimeList();
										timeList.setBtnState(jsonArray2.getJSONObject(j2).getInt(TimeList.BTNSTATE));
										timeList.setTimeList(jsonArray2.getJSONObject(j2).getString(TimeList.TIMELIST));
										timeList.setDaysection(daysection);
										timeList.setClinicidname(clinicidname);
										timeList.setShiftdate(shiftdate);
										timeList.setScid(scid);
										timeList.setRegfee(regfee);
										if(daysection==1)
										{
											timeList.setRegtotalAM(regtotal);
											timeList.setAM(true);
										}
										if(daysection==2)
										{
											timeList.setRegtotalPM(regtotal);
											timeList.setPM(true);
										}
										
										 if(daysection==3)
											{
												timeList.setRegtotalNight(regtotal);
												timeList.setNight(true);
											}
										 
										 if(daysection==4)
											{
												timeList.setRegtotalAll(regtotal);	
												timeList.setAll(true);
											}
										listTimemeList1.add(timeList);
									}
								}
								
							}
							if(isVisible)
							{
							Intent it = new Intent();
							it.setClass(DoctorTimeListActivity.this, DoctorTimeDialogActivity.class);
							it.putExtra("doctor", doctor);
							it.putExtra("time", dialogTime);
							it.putExtra("timeList", (Serializable)listTimemeList1);
							it.putExtra("tag", 5);
							it.putExtra("isCanRegister", isCanRegister);
							startActivity(it);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
						finally
						{
							mAdapter.notifyDataSetChanged();
						}
					}


				} else {
					showToast(reason);
				}

			}
			super.handleMessage(msg);

		}
	}

}
