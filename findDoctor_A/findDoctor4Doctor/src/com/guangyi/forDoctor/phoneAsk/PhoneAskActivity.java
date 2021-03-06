package com.guangyi.forDoctor.phoneAsk;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.adapter.PhoneAskListAdapter;
import com.guangyi.forDoctor.config.Config;
import com.guangyi.forDoctor.dateview.JudgeDate;
import com.guangyi.forDoctor.dateview.ScreenInfo;
import com.guangyi.forDoctor.dateview.WheelMain;
import com.guangyi.forDoctor.hospitalRegister.HospitalRegisterActivity;
import com.guangyi.forDoctor.http.ApiHttpUtil;
import com.guangyi.forDoctor.model.PhoneOrHospital;
import com.guangyi.forDoctor.reflashView.PullToRefreshView;
import com.guangyi.forDoctor.reflashView.PullToRefreshView.OnFooterRefreshListener;
import com.guangyi.forDoctor.reflashView.PullToRefreshView.OnHeaderRefreshListener;
import com.guangyi.forDoctor.utils.DateTools;
import com.guangyi.forDoctor.utils.WeekTool;

public class PhoneAskActivity extends BasicActivity implements
		android.view.GestureDetector.OnGestureListener {
	private ListView listView1, listView2;
	private PullToRefreshView mPullToRefreshView1,mPullToRefreshView2;
	private View view1, view2;
	private List<PhoneOrHospital> list;
	private PhoneAskListAdapter adapter;
	private Calendar mCalendar = null;
	private TextView view1_tv_first, view1_tv_second, view1_tv_third,
			view2_tv_first, view2_tv_second, view2_tv_third;
	private String todayStr;
	private WeekTool weekTool;
	private Dialog dialog = null;
	private ApiHttpUtil mApiHttpUtil;
	private Handler mHandler;
	private String doctId;
	private int pageNo = 1, pageSize = 6;
	// 声明手势识别
	private GestureDetector gd;
	private WheelMain wheelMain;
	// 声明ViewFlipper控件
	ViewFlipper vf;
	private static final int SWIPE_MIN_DISTANCE = 120;
	  private static final int SWIPE_MAX_OFF_PATH = 250;
	  private static final int SWIPE_THRESHOLD_VELOCITY = 20;
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
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_ask_main);
		SysApplication.getInstance().addActivity(this);
		vf = (ViewFlipper) findViewById(R.id.myViewFlipper);
		LayoutInflater mLi = getLayoutInflater();
		view1 = mLi.inflate(R.layout.t_online_main_list, null);
		view2 = mLi.inflate(R.layout.t_online_main_list, null);
		vf.addView(view1);
		vf.addView(view2);
		gd = new GestureDetector(this);
		initView();
		initParams();
		initView1(view1);
		initView2(view2);
		initTag();
	}

	private void initView() {

		Button mBack = (Button) findViewById(R.id.ib_back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});
		Button ib_post=(Button) findViewById(R.id.ib_post);
		ib_post.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {showTime();}
		});
		view1_tv_first = (TextView) view1.findViewById(R.id.tv_first);
		view1_tv_second = (TextView) view1.findViewById(R.id.tv_second);
		view1_tv_third = (TextView) view1.findViewById(R.id.tv_third);
		view2_tv_first = (TextView) view2.findViewById(R.id.tv_first);
		view2_tv_second = (TextView) view2.findViewById(R.id.tv_second);
		view2_tv_third = (TextView) view2.findViewById(R.id.tv_third);
		
//		view1_tv_second.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//
//				if (dialog != null) {
//					dialog.show();
//				} else {
//					createDialog().show();
//				}
//
//				
//			}
//		});
//view2_tv_second.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//
//				if (dialog != null) {
//					dialog.show();
//				} else {
//					createDialog().show();
//				}
//
//				
//			}
//		});

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@SuppressWarnings("deprecation")
	private void initParams() {
		list = new ArrayList<PhoneOrHospital>();
		adapter = new PhoneAskListAdapter(this, list);
		doctId = getCustomSharedPreference().getString("doctId", "-1");
		todayStr = DateTools.getCurrentDate();
		weekTool = new WeekTool();
		changeTextTime(todayStr);
		mApiHttpUtil = new ApiHttpUtil();
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				onLoaded();
				Log.i("content", msg.obj.toString());
				canclePregressDialog();
				JSONObject jsonObject = null;
				String jsonString = msg.obj.toString();
				if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
					showToast(getResources().getString(R.string.soconntimeout));
				} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
					showToast(getResources().getString(R.string.conntimeout));
				} else {
					try {
						jsonObject = new JSONObject(msg.obj.toString());
						if (jsonObject.getInt("code") == 0) {
							JSONArray jsonArray = jsonObject
									.getJSONArray("appomentOrder");
							PhoneOrHospital phoneAskItem;
							for (int i = 0; i < jsonArray.length(); i++) {
								phoneAskItem = new PhoneOrHospital();
								phoneAskItem.setAge(jsonArray.getJSONObject(i)
										.getString(PhoneOrHospital.AGE));
								phoneAskItem.setAppoTime(jsonArray
										.getJSONObject(i).getString(
												PhoneOrHospital.APPOTIME));
								phoneAskItem.setDisease(jsonArray
										.getJSONObject(i).getString(
												PhoneOrHospital.DISEASE));
								phoneAskItem.setIsFrist(jsonArray
										.getJSONObject(i).getString(
												PhoneOrHospital.ISFRIST));
								phoneAskItem.setReturnflag(jsonArray
										.getJSONObject(i).getString(
												PhoneOrHospital.RETURNFLAG));
								phoneAskItem.setSex(jsonArray.getJSONObject(i)
										.getString(PhoneOrHospital.SEX));
								phoneAskItem.setSymptom(jsonArray
										.getJSONObject(i).getString(
												PhoneOrHospital.SYMPTOM));
								phoneAskItem.setTimesection(jsonArray
										.getJSONObject(i).getString(
												PhoneOrHospital.TIMESECTION));
								Log.i("timesection", jsonArray.getJSONObject(i)
										.getString(PhoneOrHospital.TIMESECTION));
								list.add(phoneAskItem);
							}
							if (list.size() > 0) {
								pageNo++;
								mPullToRefreshView1.openFootView();
								mPullToRefreshView2.openFootView();
								
							}
							else
							{
								mPullToRefreshView1.closeFootView();
								mPullToRefreshView2.closeFootView();
								View emptyView = LayoutInflater.from(PhoneAskActivity.this).inflate(R.layout.empty_view, null);
								emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
								emptyView.setVisibility(View.GONE);   
								((ViewGroup)listView1.getParent()).addView(emptyView);   
								listView1.setEmptyView(emptyView);
								
								
								View emptyView2 = LayoutInflater.from(PhoneAskActivity.this).inflate(R.layout.empty_view, null);
								emptyView2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
								emptyView2.setVisibility(View.GONE); 
								((ViewGroup)listView2.getParent()).addView(emptyView2);   
								listView2.setEmptyView(emptyView2);
								
							}
							adapter.notifyDataSetChanged();
						} else {
							showToast(jsonObject.getString("reason"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				super.handleMessage(msg);
			}
		};
	}

	private void changeTextTime(String time) {

		view1_tv_first.setText("周"
				+ weekTool.getWeek(DateTools.getDateOffset(time, -1)) + "("
				+ DateTools.getDateOffsetMM(time, -1) + ")");
		view1_tv_second.setText("周"
				+ weekTool.getWeek(DateTools.getDateOffset(time, 0)) + "("
				+ DateTools.getDateOffsetMM(time, 0) + ")");
		view1_tv_third.setText("周"
				+ weekTool.getWeek(DateTools.getDateOffset(time, +1)) + "("
				+ DateTools.getDateOffsetMM(time, +1) + ")");

		view2_tv_first.setText("周"
				+ weekTool.getWeek(DateTools.getDateOffset(time, -1)) + "("
				+ DateTools.getDateOffsetMM(time, -1) + ")");
		view2_tv_second.setText("周"
				+ weekTool.getWeek(DateTools.getDateOffset(time, 0)) + "("
				+ DateTools.getDateOffsetMM(time, 0) + ")");
		view2_tv_third.setText("周"
				+ weekTool.getWeek(DateTools.getDateOffset(time, +1)) + "("
				+ DateTools.getDateOffsetMM(time, +1) + ")");
		getData(pageNo, pageSize, doctId,1);
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
				pageNo=1;
				list.clear();
				adapter.notifyDataSetChanged();
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

	private void initTag() {
		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		int displayHeight = currDisplay.getHeight();
		int one = displayWidth / 3; // 设置水平动画平移大小
		ImageView mTabImg1 = new ImageView(view1.getContext());
		ImageView mTabImg2 = new ImageView(view2.getContext());
		mTabImg1.setImageResource(R.drawable.navi_top_line);
		mTabImg2.setImageResource(R.drawable.navi_top_line);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				one, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		RelativeLayout view1_main_bottom = (RelativeLayout) view1
				.findViewById(R.id.main_bottom);
		view1_main_bottom.addView(mTabImg1, params);
		RelativeLayout view2_main_bottom = (RelativeLayout) view2
				.findViewById(R.id.main_bottom);
		view2_main_bottom.addView(mTabImg2, params);
	}

	private void getData(final int _pageNo, final int _pageSize,
			final String _doctId,int isProgress) {
		if(isProgress==1)
		{
			initProgressDialog();
		}
		mRunnable=new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("nowTime", todayStr);
				params.put("doctId", _doctId);
				params.put("pageNo", _pageNo + "");
				params.put("pageSize", _pageSize + "");
				params.put("appType", "2");
				msg.obj = mApiHttpUtil.postMethod(
						Config.getProperty("PHONEASK_MAIN", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
	}
	
	private void initView1(View view) {
		mPullToRefreshView1 = (PullToRefreshView)view.findViewById(R.id.main_pull_refresh_view);
mPullToRefreshView1.setOnHeaderRefreshListener(new OnHeaderRefresh());
mPullToRefreshView1.setOnFooterRefreshListener(new OnFooterRefresh());
		listView1= (ListView) view.findViewById(R.id.mlist);
		listView1.setAdapter(adapter);
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 Intent it = new Intent();
				 it.putExtra("phoneAskItem", list.get(arg2));
				 it.setClass(PhoneAskActivity.this,
				 PhoneAskDetail.class);
				 startActivity(it);

			}
		});
		
		
		

	}

	private void initView2(View view) {
		mPullToRefreshView2 = (PullToRefreshView)view.findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView2.setOnHeaderRefreshListener(new OnHeaderRefresh());
		mPullToRefreshView2.setOnFooterRefreshListener(new OnFooterRefresh());
				listView2= (ListView) view.findViewById(R.id.mlist);
		listView2.setAdapter(adapter);
		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent();
				 it.putExtra("phoneAskItem", list.get(arg2));
				 it.setClass(PhoneAskActivity.this,
				 PhoneAskDetail.class);
				 startActivity(it);

			}
		});
	}



	
	

private void onLoaded() {
		mPullToRefreshView1.onHeaderRefreshComplete(getResources().getString(R.string.pull_to_refresh_pull_last_time)+DateTools.getCurrentDateString("MM-dd HH:mm:ss"));
		mPullToRefreshView1.onFooterRefreshComplete();
		mPullToRefreshView2.onHeaderRefreshComplete(getResources().getString(R.string.pull_to_refresh_pull_last_time)+DateTools.getCurrentDateString("MM-dd HH:mm:ss"));
		mPullToRefreshView2.onFooterRefreshComplete();
	}


	class OnHeaderRefresh implements OnHeaderRefreshListener
	{
	

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			getData(pageNo, pageSize, doctId,0);
			
		}
		
	}
	
	class OnFooterRefresh implements OnFooterRefreshListener
	{

		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			getData(pageNo, pageSize, doctId,0);
			
		}
		
	}




	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		// 向左滑动
		if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
			      && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			pageNo=1;
			list.clear();
			adapter.notifyDataSetChanged();
			todayStr = DateTools.getDateOffset(todayStr, 1);
			changeTextTime(todayStr);
			vf.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			vf.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			this.vf.showNext();
			

		}
		// 向右滑向
		else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
			      && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			pageNo=1;
			list.clear();
			adapter.notifyDataSetChanged();
			todayStr = DateTools.getDateOffset(todayStr, -1);
			changeTextTime(todayStr);
			vf.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			vf.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			vf.showPrevious();
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return this.gd.onTouchEvent(event);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		gd.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

}
