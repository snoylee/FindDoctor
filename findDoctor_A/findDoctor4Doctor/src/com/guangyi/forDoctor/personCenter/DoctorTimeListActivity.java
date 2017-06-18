package com.guangyi.forDoctor.personCenter;

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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;


import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.adapter.DoctorTimeGridAdapter;
import com.guangyi.forDoctor.config.Config;
import com.guangyi.forDoctor.dateview.JudgeDate;
import com.guangyi.forDoctor.dateview.ScreenInfo;
import com.guangyi.forDoctor.dateview.WheelMain;
import com.guangyi.forDoctor.http.ApiHttpUtil;
import com.guangyi.forDoctor.model.Doctor;
import com.guangyi.forDoctor.model.TimeList;
import com.guangyi.forDoctor.utils.DateTools;
import com.guangyi.forDoctor.utils.WeekTool;

public class DoctorTimeListActivity extends BasicActivity {
	private GridView gridView;
	private ApiHttpUtil mApiHttpUtil;
	private Handler mHandler;
	private Button mNext, mPrevious;
	private TextView mTime;
	private String todayStr;
	private WeekTool weekTool;
	private int  consType;//排版类型 4，在线预约 2 ，视频预约 3，电话预约
	private String expertId; //share 获取
	private Dialog dialog = null;
	private Calendar mCalendar = null;
	private TextView tv_topbar_center_text;
	private Runnable mRunnable;
	private Thread mThread;
	private WheelMain wheelMain;
		
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
		setContentView(R.layout.doctor_time_list);
		SysApplication.getInstance().addActivity(this);
		initBack();
		initParams();
		initView();
		initListener();
		mTime.setText(todayStr+"  周"+weekTool.getWeek(todayStr));
		getData(todayStr);
	}

	@Override
	protected void onStart() {

		super.onStart();
	}
	private void changeTextTime(String time)
	{
		mTime.setText(time+"  周"+weekTool.getWeek(todayStr));
		todayStr=time;
		getData(todayStr);
	}
	public void showTime()
	{
		LayoutInflater inflater=LayoutInflater.from(this);
		final View timepickerview=inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(this);
		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		Calendar calendar = Calendar.getInstance();
		if(JudgeDate.isDate(todayStr, "yyyy-MM-dd")){
			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				calendar.setTime(dateFormat.parse(todayStr));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
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
				todayStr= DateTools.getDateOffset(wheelMain.getTime(), 0);
				changeTextTime(todayStr);
				
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
	
	}

	private void initBack() {
		Button Back = (Button) findViewById(R.id.ib_back);

		Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.gridView1);
		mPrevious = (Button) findViewById(R.id.btn_previous);
		mNext = (Button) findViewById(R.id.btn_next);
		mTime=(TextView) findViewById(R.id.tv_time);
		tv_topbar_center_text=(TextView) findViewById(R.id.tv_topbar_center_text);
		if(consType==1)
		{
			tv_topbar_center_text.setText("预约挂号");
		}
		
		if(consType==3)
		{
			tv_topbar_center_text.setText("电话咨询");
		}
		
	}

	private void initListener() {
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				Intent intent = new Intent();
//				intent.putExtra("doctor", doctor);
//				intent.setClass(getApplicationContext(),
//						CommonPatientActivity.class);
//				startActivity(intent);
//
//			}
//		});
		mPrevious.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println(DateTools.getDateOffset(todayStr, -1));
				todayStr=DateTools.getDateOffset(todayStr, -1);
				mTime.setText(todayStr+"  周"+weekTool.getWeek(todayStr));
				getData(todayStr);
			}
		});
		mNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println(weekTool.getWeek(todayStr));
				System.out.println(DateTools.getDateOffset(todayStr, 1));
				todayStr=DateTools.getDateOffset(todayStr, 1);
				mTime.setText(todayStr+"  周"+weekTool.getWeek(todayStr));
				getData(todayStr);
			}
		});
		mTime.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {showTime();}
			});
		
		Button ib_post=(Button) findViewById(R.id.ib_post);
		ib_post.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {showTime();}
		});
	}
		private void initParams()
		{
			todayStr=DateTools.getCurrentDate();
			mHandler = new MyHandler();
			mApiHttpUtil = new ApiHttpUtil();
			weekTool = new WeekTool();
			consType=getIntent().getExtras().getInt("consType");
			
			//之前是预约挂号为1，电话咨询为3，现在该为预约挂号为4，没在入口处修改，在此次做处理
			if(consType==1)
			{
				consType=4;
			}else{
				consType=3;
			}
			expertId=getCustomSharedPreference().getString(Doctor.EXPERTID, "");
		}
	private void bindData(List<TimeList> list) {
		DoctorTimeGridAdapter	adapter = new DoctorTimeGridAdapter(this, list);
		gridView.setAdapter(adapter);
	}
	private void getData(final String dateStr) {
		initProgressDialog();
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				HashMap<String, String> parms = new HashMap<String, String>();
				parms.put("expertId", expertId);
				parms.put("shiftDate", dateStr);
				parms.put("consType", String.valueOf(consType));
				msg.arg1 = 1;
				msg.what = consType;

				String t = dateStr.substring(0,4)+dateStr.substring(5,7)+dateStr.substring(8);
				msg.arg2 = Integer.valueOf(t);
				
				msg.obj = mApiHttpUtil.postMethod(
						Config.getProperty("GET_DOCTOR_Time_STRING", ""), parms);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
	}
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			List<TimeList> mlist = new ArrayList<TimeList>();
			String jsonString=msg.obj.toString();
			canclePregressDialog();
			Log.i("result jsonString", jsonString);
			
			if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
				showToast(getResources().getString(R.string.soconntimeout));
			} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
				showToast(getResources().getString(R.string.conntimeout));
			}
			else 
			{
				JSONObject jsonObject;
				int code = -1;
                 try {
                	jsonObject=new JSONObject(jsonString);
                	code=jsonObject.getInt("code");
                	if(code==0)
                	{
                		JSONArray jsonArray = jsonObject
    							.getJSONArray("SchedulingListNew");
    					TimeList timeList;
    					if(msg.what == 4){
	    					for (int i = 0; i < jsonArray.length(); i++) {
								JSONArray jsonArray1=jsonArray.getJSONObject(i).getJSONArray("date");
								for (int j = 0; j < jsonArray1.length(); j++) {
									JSONArray jsonArray2 = jsonArray1
											.getJSONObject(j).getJSONArray(
													"times");
									System.out.println("arg2="+msg.arg2);
									String t = msg.arg2+"";
									String shift = t.substring(0,4)+"-"+t.substring(4,6)+"-"+t.substring(6);
									String shiftDate  = jsonArray1.getJSONObject(j).getString("shiftdate");
									System.out.println("shiftDate="+shiftDate);
									String shifts = jsonArray1.getJSONObject(j).toString();
									System.out.println("shifts="+shifts);
										for (int j2 = 0; j2 < jsonArray2
												.length(); j2++) {
											timeList = new TimeList();
											timeList.setBtnState(jsonArray2
													.getJSONObject(j2).getInt(
															TimeList.BTNSTATE));
											timeList.setTimeList(jsonArray2
													.getJSONObject(j2)
													.getString(
															TimeList.TIMELIST));
											if(shift.equals(shiftDate)){
												mlist.add(timeList);
											}
										}
									}
	    					}
    					}else if(msg.what == 3){
							for (int i = 0; i < jsonArray.length(); i++) {
								timeList = new TimeList();
								timeList.setId(jsonArray.getJSONObject(i)
										.getInt(timeList.ID));
								timeList.setBtnState(jsonArray.getJSONObject(i)
										.getInt(timeList.BTNSTATE));
								timeList.setTimeList(jsonArray.getJSONObject(i)
										.getString(timeList.TIMELIST));
								mlist.add(timeList);
								}
						}
					}
                	else
                	{
                		showToast(jsonObject.getString("reason"));
                	}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			bindData(mlist);
			super.handleMessage(msg);
		}
	}

}
