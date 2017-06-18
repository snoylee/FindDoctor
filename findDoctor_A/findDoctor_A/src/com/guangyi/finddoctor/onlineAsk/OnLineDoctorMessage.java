package com.guangyi.finddoctor.onlineAsk;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.activity.R.color;
import com.guangyi.finddoctor.adapter.OnLineDoctorMessageAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.DoctorConsult;
import com.guangyi.finddoctor.model.Hospital;
import com.guangyi.finddoctor.personCenter.UserLoginActivity;
import com.guangyi.finddoctor.utils.Config;

/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:在线医生信息
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

public class OnLineDoctorMessage extends BasicActivity {
	private ViewPager mTabPager;
	private ImageView mTabImg;// 动画图片
	private TextView mTab1, mTab2, mTab3, mTab4;
	private int zero = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int one;// 单个水平动画位移
	private int two;
	private int three;
	private Button mBack;
	private View view1, view2, view3;
	private Handler mHandler;
	private ApiHttpUtil mApiHttpUtil;
	private String depId;
	private TextView titleText;
	private String title;
	private List<DoctorConsult> list1, list2, list3;
	private Button btn_media_consult, btn_phone_consult;
	private SharedPreferences mSharedPreferences;
	private boolean isLogin;
	private Editor mEditor;
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
		setContentView(R.layout.online_doctor_message);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		iniListener();
		initTag();
		getData(1, 2);
	}
	@Override
	protected void onStart() {
//		 getData(1, 2);
//		 getData(2, 2);
		super.onStart();
	}
	private void initParams() {
		title = getIntent().getStringExtra("title");
		// depId = getIntent().getStringExtra(OnLineGridItem.DEPID);
		depId = title;
		mApiHttpUtil = new ApiHttpUtil();
		new ArrayList<Hospital>();
		mHandler = new MyHandler();
		list1 = new ArrayList<DoctorConsult>();
		list2 = new ArrayList<DoctorConsult>();
		list3 = new ArrayList<DoctorConsult>();
	}

	private void getData(final int type, final int status) {
		initProgressDialog();
		mRunnable=new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				Map<String, String> params = new HashMap<String, String>();
				params.put("transactionId",
						String.valueOf(new Date().getTime()));
				params.put("Type", String.valueOf(type));
				params.put("Status", String.valueOf(status));
				params.put("depId", depId);
				params.put("pageNo", "1");
				params.put("pageSize", "10");
				msg.arg1 = type;
				msg.obj = mApiHttpUtil.postMethod(
						Config.getProperty("ON_LINE_DOCTOR_MESSAGE_STRING", ""),
						params);
				mHandler.sendMessage(msg);

			}
		};
		commonThreadStart();
	}

	private void initView() {
		mBack = (Button) findViewById(R.id.ib_back);
		btn_media_consult = (Button) findViewById(R.id.btn_media_consult);
		btn_phone_consult = (Button) findViewById(R.id.btn_phone_consult);
		titleText = (TextView) findViewById(R.id.tv_topbar_center_text);
		titleText.setText(title);

	}

	private void iniListener() {
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		btn_phone_consult.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(getApplicationContext(),
						OnLineDoctorList.class);
				it.putExtra("depId", depId);
				startActivity(it);

			}
		});
		btn_media_consult.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getSharedPreference();
				if (isLogin) {
					Intent it = new Intent();
					it.setClass(OnLineDoctorMessage.this,
							OnLineAddConsultInfoForChart.class);
					it.putExtra("dispilayDictor", 0);
					startActivity(it);
				} else {
					Toast.makeText(getApplicationContext(), "您还没有登录，请登录！",
							Toast.LENGTH_LONG).show();
					openActivityforResult(UserLoginActivity.class);
				}
			}
		});
	}

	private void getSharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		isLogin = mSharedPreferences.getBoolean("isLogin", false);
	}
	private void initTag() {
		mTabPager = (ViewPager) findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

		mTab1 = (TextView) findViewById(R.id.tv_third);
		mTab2 = (TextView) findViewById(R.id.tv_second);
		mTab3 = (TextView) findViewById(R.id.tv_first);
		// mTabImg = (ImageView) findViewById(R.id.img_tab_now);
		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		mTab3.setOnClickListener(new MyOnClickListener(2));
		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		int displayHeight = currDisplay.getHeight();
		one = displayWidth / 3; // 设置水平动画平移大小
		two = one * 2;
		three = one * 3;

		mTabImg = new ImageView(this);
		mTabImg.setImageResource(R.drawable.navi_top_line);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				one, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		RelativeLayout main_bottom = (RelativeLayout) findViewById(R.id.main_bottom);
		main_bottom.addView(mTabImg, params);
		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.online_doctor_message_son, null);
		view2 = mLi.inflate(R.layout.online_doctor_message_son, null);
		view3 = mLi.inflate(R.layout.online_doctor_message_son, null);

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};
		mTabPager.setAdapter(mPagerAdapter);
		mTabPager.setCurrentItem(currIndex);
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (list1.size() <= 0) {
					getData(1, 2);
				}
				mTab1.setTextColor(getResources().getColor(color.common_green));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setTextColor(getResources().getColor(
							color.common_grey));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setTextColor(getResources().getColor(
							color.common_grey));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					mTab4.setTextColor(getResources().getColor(
							color.common_grey));
				}
				break;
			case 1:
				if (list2.size() <= 0) {
					getData(0, 2);
				}
				mTab2.setTextColor(getResources().getColor(color.common_green));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setTextColor(getResources().getColor(
							color.common_grey));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setTextColor(getResources().getColor(
							color.common_grey));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					mTab4.setTextColor(getResources().getColor(
							color.common_grey));
				}
				break;
			case 2:
				if (list3.size() <= 0) {
					getData(2, 2);
				}
				mTab3.setTextColor(getResources().getColor(color.common_green));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setTextColor(getResources().getColor(
							color.common_grey));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setTextColor(getResources().getColor(
							color.common_grey));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					mTab4.setTextColor(getResources().getColor(
							color.common_grey));
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

	}

	private void getView1(View view, final List<DoctorConsult> list) {
		ListView listView1 = (ListView) view.findViewById(R.id.lv_chose);
		
		View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
		emptyView.setVisibility(View.GONE);   
		((ViewGroup)listView1.getParent()).addView(emptyView);   
		listView1.setEmptyView(emptyView);
		
		listView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it=new Intent(OnLineDoctorMessage.this,OnlineChatActivity.class);
				it.putExtra("consId", list.get(arg2).getConsId()+"");
				it.putExtra("is_bottom_visible", 0);
				startActivity(it);
			}
		});
		OnLineDoctorMessageAdapter adapter = new OnLineDoctorMessageAdapter(
				getApplicationContext(), list);
		listView1.setAdapter(adapter);
	}

	private void getView2(View view, final List<DoctorConsult> list) {

		ListView listView1 = (ListView) view.findViewById(R.id.lv_chose);
		View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
		emptyView.setVisibility(View.GONE);   
		((ViewGroup)listView1.getParent()).addView(emptyView);   
		listView1.setEmptyView(emptyView);
		
		listView1.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					Intent it=new Intent(OnLineDoctorMessage.this,OnlineChatActivity.class);
					it.putExtra("consId", list.get(arg2).getConsId()+"");
					it.putExtra("is_bottom_visible", 0);
					startActivity(it);
			}
		});
		OnLineDoctorMessageAdapter adapter = new OnLineDoctorMessageAdapter(
				getApplicationContext(), list);
		
		listView1.setAdapter(adapter);
	}

	private void getView3(View view, final List<DoctorConsult> list) {
		ListView listView1 = (ListView) view.findViewById(R.id.lv_chose);
		
		View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
		emptyView.setVisibility(View.GONE);   
		((ViewGroup)listView1.getParent()).addView(emptyView);   
		listView1.setEmptyView(emptyView);
		
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it=new Intent(OnLineDoctorMessage.this,OnlineChatActivity.class);
				it.putExtra("consId", list.get(arg2).getConsId()+"");
				it.putExtra("is_bottom_visible", 0);
				startActivity(it);
			}
		});
		OnLineDoctorMessageAdapter adapter = new OnLineDoctorMessageAdapter(
				getApplicationContext(), list);
		listView1.setAdapter(adapter);
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			canclePregressDialog();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			Log.i("doctormessage", jsonString);
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
					e1.printStackTrace();
				}
				if(code==0)
				{
					

					List<DoctorConsult> listConsult = new ArrayList<DoctorConsult>();
						try {
							JSONArray jsonArray = jsonObject
									.getJSONArray("consultingList");
							DoctorConsult doctorConsult;
							
							for (int i = 0; i < jsonArray.length(); i++) {
								doctorConsult = new DoctorConsult();
								doctorConsult.setExpertName(jsonArray
										.getJSONObject(i).getString(
												DoctorConsult.EXPERTNAME));
								doctorConsult.setConsProblem(jsonArray
										.getJSONObject(i).getString(
												DoctorConsult.CONSPROBLEM));
								doctorConsult.setConsReplyProblem(jsonArray
										.getJSONObject(i).getString(
												DoctorConsult.CONSREPLYPROBLEM));
								doctorConsult.setConsId(jsonArray
										.getJSONObject(i).getInt(
												DoctorConsult.CONSID));
								doctorConsult.setConsDoctorId(jsonArray
										.getJSONObject(i).getInt(
												DoctorConsult.CONSDOCTORID));
								doctorConsult.setConsUserReply(jsonArray
										.getJSONObject(i).getString(
												DoctorConsult.CONSUSERREPLY));
								doctorConsult.setAttachFileByte(Config.getProperty("FILEURL", "")+jsonArray
										.getJSONObject(i).getString(
												"attachFileUrl"));
								
								Log.i("test", Config.getProperty("FILEURL", "")+jsonArray
										.getJSONObject(i).getString(
												"attachFileUrl"));
								listConsult.add(doctorConsult);
							}
							
//							if (listConsult.size() > 0) {

							
//							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
						finally
						{
							switch (msg.arg1) {
							case 0:
								list2 = listConsult;
								getView2(view2, list2);
								break;
							case 1:
								list1 = listConsult;
								getView1(view1, list1);
								break;
							case 2:
								list3 = listConsult;
								getView3(view3, list3);
								break;
							default:
								break;
							}
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
		if (resultCode == ResultCodeCategory.RESULT_CODE_LOGIN) {
			mSharedPreferences = getSharedPreferences("personCenter",
					Context.MODE_PRIVATE);
			mEditor = mSharedPreferences.edit();
			mEditor.putInt("userId", data.getExtras().getInt("userId"));
			mEditor.putBoolean("isLogin", data.getExtras()
					.getBoolean("isLogin"));
			mEditor.putBoolean("isShow", true);
			mEditor.putString("userMobile",
					data.getExtras().getString("userMobile"));
			mEditor.commit();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
