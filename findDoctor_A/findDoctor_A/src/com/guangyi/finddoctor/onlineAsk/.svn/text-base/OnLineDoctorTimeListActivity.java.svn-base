package com.guangyi.finddoctor.onlineAsk;
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
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.OnLineDoctorTimeGridAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.dateview.JudgeDate;
import com.guangyi.finddoctor.dateview.ScreenInfo;
import com.guangyi.finddoctor.dateview.WheelMain;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.model.TimeList;
import com.guangyi.finddoctor.personCenter.CommonPatientActivity;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.DateTools;
import com.guangyi.finddoctor.utils.WeekTool;

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

public class OnLineDoctorTimeListActivity extends BasicActivity {
	private GridView gridView;
	private ApiHttpUtil mApiHttpUtil;
	private Handler mHandler;
	private Button mNext, mPrevious;
	private TextView mTime;
	private String todayStr;
	private WeekTool weekTool;
	private String HospitalId, DepartmentID, expertId;
	private Doctor mDoctor;
	private String timeStrAll;
	private int childItemCount = 0;
	private Dialog dialog = null;
	private Calendar mCalendar = null;
	private Runnable mRunnable;
	private Thread mThread;
	private Spinner mSpinner;
	private float TimeMin;
	private float feePrice;
	private Float money;
	private WheelMain wheelMain;
	private List<TimeList> mlist = new ArrayList<TimeList>();

	private void commonThreadStart() {
		if (mRunnable != null) {
			mThread = new Thread(mRunnable);
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
		setContentView(R.layout.online_doctor_time_list);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initBack();
		initView();
		initParams();
		initListener();
		mTime.setText(todayStr + "  周" + weekTool.getWeek(todayStr));
//		reflashData();
		getData(todayStr);

	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	private void changeTextTime(String time) {
		mTime.setText(time + "  周" + weekTool.getWeek(todayStr));
		todayStr = time;
//		reflashData();
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
		View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
		emptyView.setVisibility(View.GONE);  
		((ViewGroup)gridView.getParent()).addView(emptyView);   
		gridView.setEmptyView(emptyView);
		
		mPrevious = (Button) findViewById(R.id.btn_previous);
		mNext = (Button) findViewById(R.id.btn_next);
		mTime = (TextView) findViewById(R.id.tv_time);
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		mSpinner.setClickable(false);
	}

	private void initSpin(final String[] _stringItem) {
		// 设置下拉列表的风格
		TimeMin = Integer.valueOf(_stringItem[0].split("/")[1].replace("分钟", ""));
		 money = Float.valueOf(_stringItem[0].split("/")[0].replace("元",
				""));
		feePrice = (float) (money / TimeMin);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
				OnLineDoctorTimeListActivity.this, R.layout.spinner_layout,
				_stringItem);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setClickable(true);
		
		mSpinner.setAdapter(spinnerAdapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				TimeMin = Integer.valueOf(_stringItem[arg2].split("/")[1].replace("分钟", ""));
				 money = Float.valueOf(_stringItem[arg2].split("/")[0]
						.replace("元", ""));
				feePrice = (float) (money / TimeMin);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				TimeMin = Integer.valueOf(_stringItem[0].split("/")[1].replace("分钟", ""));
				 money = Float.valueOf(_stringItem[0].split("/")[0].replace("元",
						""));
				feePrice = (float) (money / TimeMin);

			}
		});
	}

	private void initListener() {
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						CommonPatientActivity.class);
				startActivity(intent);

			}
		});
		mPrevious.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// System.out.println(DateTools.getDateOffset(todayStr, -1));
				todayStr = DateTools.getDateOffset(todayStr, -1);
				mTime.setText(todayStr + "  周" + weekTool.getWeek(todayStr));
				getData(todayStr);

			}
		});
		mNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// System.out.println(weekTool.getWeek(todayStr));
				// System.out.println(DateTools.getDateOffset(todayStr, 1));
				todayStr = DateTools.getDateOffset(todayStr, 1);
				mTime.setText(todayStr + "  周" + weekTool.getWeek(todayStr));
//				reflashData();
				getData(todayStr);

			}
		});

		mTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {showTime();}
		});

		Button ib_post = (Button) findViewById(R.id.ib_post);
		ib_post.setOnClickListener(new View.OnClickListener() {

			// 获取button的时间 并提交

			@Override
			public void onClick(View v) {

				// int feeCount = 0;
				// timeStrAll = "";
				// childItemCount = gridView.getChildCount();
				//
				// for (int i = 0; i <= childItemCount - 1; i++) {
				// Button btn = (Button) gridView.getChildAt(i);
				// if (btn.isLongClickable()) {
				// timeStrAll += btn.getText().toString() + ", ";
				// feeCount += 1;
				// }
				//
				// }
				// Log.i("feeCount", feeCount + "");
				//
				// if (timeStrAll.equals("")) {
				// showToast("请先选择排版时间!");
				// } else {
				// Intent it = new Intent();
				// it.putExtra("time", timeStrAll);
				// it.putExtra("feeCount", feeCount);
				// it.putExtra("doctor", mDoctor);
				// it.putExtra("way", "电话咨询");
				// it.putExtra("todayStr", todayStr);
				// it.setClass(getApplicationContext(),
				// OnlineConfirmOrder.class);
				// startActivity(it);
				// Log.i("timeStrAll", timeStrAll);
				// }

				if (mlist.size() > 0) {

					Long feeCount = (long) 0;
					int feeMin = 0;
					Long b = (long) TimeMin;
					Long a = mlist.get(0).getTimeMin();

					int Count = 1;
					timeStrAll = "";
					childItemCount = gridView.getChildCount();

					for (int i = 0; i <= childItemCount - 1; i++) {
						Button button = (Button) gridView.getChildAt(i);
						if (button.getHint().equals("1")) {
							timeStrAll += button.getText().toString() + ", ";
							feeCount += 1;
							feeMin += mlist.get(i).getTimeMin();
						}

					}

					if (a >= b) {
						Count = 1;
					} else {
						while (a < b) {
							Count++;
							a = a * Count;

						}
					}
//					Log.i("a", a + "");
//					Log.i("b", b + "");
					Log.i("feeCount", feeCount + "");
//					Log.i("Count", Count + "");

					if (feeCount == Count) {
						Intent it = new Intent();
						it.putExtra("time", timeStrAll);
//						it.putExtra("feeCount", feeCount);
						
						
//						Float feeStr=Float.parseFloat(feeMin * feePrice+"");
//						DecimalFormat fnum = new DecimalFormat("##0.00"); 
//						String feeStr1=fnum.format(feeStr)+ "元";
						it.putExtra("feeStr", money+"元");

						
						
						it.putExtra("doctor", mDoctor);
						it.putExtra("way", "电话咨询");
						it.putExtra("todayStr", todayStr);
						it.setClass(OnLineDoctorTimeListActivity.this,
								OnlineConfirmOrder.class);
						startActivity(it);
//						Log.i("timeStrAll", timeStrAll);
					}

					else {
						showToast("请选择" + Count + "个排班时间段!");
					}

				}

			}
		});

	}

	private void initParams() {

//		todayStr = DateTools.getCurrentDate();
		todayStr= DateTools.getDateOffset(DateTools.getCurrentDate(), 1);
		mHandler = new MyHandler();
		mApiHttpUtil = new ApiHttpUtil();
		weekTool = new WeekTool();
		mDoctor = (Doctor) getIntent().getSerializableExtra("doctor");
		HospitalId = mDoctor.getDoctHospid();
		DepartmentID = mDoctor.getDoctDepaid();
		expertId = mDoctor.getExpertId();

	}

	private void bindData(List<TimeList> list) {
		
		OnLineDoctorTimeGridAdapter adapter = new OnLineDoctorTimeGridAdapter(
				this, list);
		gridView.setAdapter(adapter);

	}
	
//	private void reflashData()
//	{
//		mlist=new ArrayList<TimeList>();
//		bindData(mlist);
//	}
//	

	private void getData(final String dateStr) {
		mlist=new ArrayList<TimeList>();
//		bindData(mlist);
		initProgressDialog();
		mRunnable = new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				HashMap<String, String> parms = new HashMap<String, String>();
				parms.put("HospitalId", HospitalId);
				parms.put("DepartmentID", DepartmentID);
				parms.put("expertId", expertId);
				parms.put("shiftDate", dateStr);
				// parms.put("weekNo", "1");
				parms.put("consType ", "3");

				// parms.put("HospitalId",
				// "05ba2f6c-ec92-4a58-a6d0-31befb5474ed");
				// parms.put("DepartmentID",
				// "2b7ac3ed-6cad-4307-93a4-1b111c5e182e");
				// parms.put("expertId",
				// "dd94d76e-0529-414c-b3b7-f0885aba7014");

				parms.put("shiftDate", dateStr);
				msg.arg1 = 1;
				msg.obj = mApiHttpUtil
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (code == 0) {

					if (msg.arg1 == 1) {

						mlist = new ArrayList<TimeList>();
						try {

							JSONArray jsonArray = jsonObject
									.getJSONArray("SchedulingListNew");
							TimeList timeList;
							for (int i = 0; i < jsonArray.length(); i++) {
								timeList = new TimeList();
								timeList.setId(jsonArray.getJSONObject(i)
										.getInt(TimeList.ID));
								timeList.setBtnState(jsonArray.getJSONObject(i)
										.getInt(TimeList.BTNSTATE));
								timeList.setTimeList(jsonArray.getJSONObject(i)
										.getString(TimeList.TIMELIST));
								Long time = DateTools.getDiffCustom(
										jsonArray.getJSONObject(i)
												.getString(TimeList.TIMELIST)
												.split("-")[1],
										jsonArray.getJSONObject(i)
												.getString(TimeList.TIMELIST)
												.split("-")[0]);
								timeList.setTimeMin(time);
								timeList.setHint(0);
								mlist.add(timeList);
							}
							JSONArray jsonArray2 = jsonObject
									.getJSONObject("results")
									.getJSONObject("SchedulingDayTe")
									.getJSONArray("rules");
							String[] spinnerItem = new String[jsonArray2
									.length()];
							for (int i = 0; i < jsonArray2.length(); i++) {
								spinnerItem[i] = jsonArray2.getJSONObject(i)
										.getString("consMoney")
										+ "元"
										+ "/"
										+ jsonArray2.getJSONObject(i).getInt(
												"conSpace") + "分钟";

							}

							if (spinnerItem != null && spinnerItem.length > 0) {
								initSpin(spinnerItem);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						finally
						{
							bindData(mlist);
						}
						
					}

				}

				else {
					showToast(reason);
				}

			}

			super.handleMessage(msg);
		}
	}

}
