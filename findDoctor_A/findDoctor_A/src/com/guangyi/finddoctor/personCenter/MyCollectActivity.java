package com.guangyi.finddoctor.personCenter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.activity.R.color;
import com.guangyi.finddoctor.adapter.CollectDoctorAadpter;
import com.guangyi.finddoctor.adapter.CollectHospitalAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.hospitalRegister.DoctorHomeActivity;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.model.Hospital;
import com.guangyi.finddoctor.selfService.SelfServiceHospitalHome;
import com.guangyi.finddoctor.utils.Config;

/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:我的收藏
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
public class MyCollectActivity extends BasicActivity {

	private ViewPager cTabPager = null;
	private ImageView cTabImg = null;// 动画图片
	private TextView tv_cDoctor, tv_cHospital;
	private int zero = 0;// 动画图片偏移量
	private int currIdex = 0;// 当前页卡编号
	private int one;
	private Button mBack;
	private View view1, view2;
	private Handler mHandler;
	private int userID = -1;
	SharedPreferences mSharedPreferences;
	ListView lv_cDoctor;
	ListView lv_cHospital;
	private List<Doctor> listdoctor;
	private List<Hospital> listhospital;
	private Runnable mRunnable;
	private Thread mThread;
	
	private CollectDoctorAadpter doctAdapter;
	private CollectHospitalAdapter hosptAdapter;
		
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
		setContentView(R.layout.my_collect);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initListener();
		initTag();
//		if (listdoctor.size() > 0) {
//			listdoctor.clear();
//		}
		getData(2);

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void initParams() {
		initMySharedPreference();
		mHandler = new MyHandler();
		listdoctor = new ArrayList<Doctor>();
		listhospital = new ArrayList<Hospital>();
	}

	private void initMySharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		userID = mSharedPreferences.getInt("userId", 0);

	}

	private void initView() {
		mBack = (Button) findViewById(R.id.ib_back);
	}

	private void initListener() {
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	private void initTag() {
		cTabPager = (ViewPager) findViewById(R.id.ctabpager);
		cTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
		tv_cDoctor = (TextView) findViewById(R.id.tv_cDoctor);
		tv_cHospital = (TextView) findViewById(R.id.tv_cHospital);


		tv_cDoctor.setOnClickListener(new MyOnClickListener(0));
		tv_cHospital.setOnClickListener(new MyOnClickListener(1));
		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		int displayHeight = currDisplay.getHeight();

		one = displayWidth / 2; 
		cTabImg = new ImageView(this);
		cTabImg.setImageResource(R.drawable.navi_top_line);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				one, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		RelativeLayout main_bottom = (RelativeLayout) findViewById(R.id.main_bottom);
		main_bottom.addView(cTabImg, params);
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.mycollect_list1, null);
		view2 = mLi.inflate(R.layout.mycollect_list2, null);
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
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

		cTabPager.setAdapter(mPagerAdapter);
		cTabPager.setCurrentItem(currIdex);

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
			cTabPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:

				if (listdoctor.size() <= 0) {
					getData(2);
				}
				tv_cDoctor.setTextColor(getResources().getColor(
						color.common_green));

				if (currIdex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					tv_cHospital.setTextColor(getResources().getColor(
							color.common_grey));
				}
				break;
			case 1:
				if (listhospital.size() <= 0) {
					getData(1);
				}

				tv_cHospital.setTextColor(getResources().getColor(
						color.common_green));

				if (currIdex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					tv_cDoctor.setTextColor(getResources().getColor(
							color.common_grey));
				}
				break;
			}
			currIdex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(150);
			cTabImg.startAnimation(animation);
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

	private void getData(final int type) {
		initProgressDialog();
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				new ApiHttpUtil();
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(type));
				list.add(String.valueOf(userID));
				msg.arg1 = type;

				msg.obj = ApiHttpUtil.getMethod(
						Config.getProperty("GET_COLLECT_STRING", ""), list);

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
					
					if(msg.arg1==1)
					{
						
						try {
							JSONArray jsonArr = jsonObject.getJSONArray("FavList");
							Hospital cHospital;
							for (int i = 0; i < jsonArr.length(); i++) {
								cHospital = new Hospital();
								cHospital.setHospId(jsonArr.getJSONObject(i)
										.getInt(Hospital.HOSPID));
								cHospital.setHospName(jsonArr.getJSONObject(i)
										.getString(Hospital.HOSPNAME));
								cHospital.setHospClass(jsonArr.getJSONObject(i)
										.getInt(Hospital.HOSPCLASS));
								cHospital.setHospTel(jsonArr.getJSONObject(i)
										.getString(Hospital.HOSPTEL));
								cHospital.setHospAddr(jsonArr.getJSONObject(i)
										.getString(Hospital.HOSPADDR));
								cHospital.setHospIntroduction(jsonArr
										.getJSONObject(i).getString(
												Hospital.HOSPINTRODUCTION));
								cHospital.setGuahaoHostId(jsonArr.getJSONObject(i).getString(Hospital.GUAHAOHOSPID));
								cHospital.setGuanghaoStatus(jsonArr.getJSONObject(i).getInt(Hospital.GUANGHAOSTATUS));
								listhospital.add(cHospital);
								
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
						
						finally
						{
							getHospView(view2, listhospital);
						}
					}
					
					else if(msg.arg1==2)
					{
						try {
							JSONArray jsonArray = jsonObject
									.getJSONArray("FavList");

							Doctor doctor;
							for (int i = 0; i < jsonArray.length(); i++) {
								doctor = new Doctor();
								doctor.setExpertId(jsonArray.getJSONObject(i)
										.getString(Doctor.EXPERTID));
								doctor.setDoctId(jsonArray.getJSONObject(i)
										.getInt(Doctor.DOCTID));
								doctor.setDoctName(jsonArray.getJSONObject(i)
										.getString(Doctor.DOCTNAME));
								
								doctor.setHospName(jsonArray.getJSONObject(i).getString(Doctor.HOSPNAME));
								doctor.setDepaName(jsonArray.getJSONObject(i).getString(Doctor.DEPANAME));
								doctor.setDoctorPosition(jsonArray.getJSONObject(i).getString(Doctor.DOCTORPOSITION));
								doctor.setDoctPosi(jsonArray.getJSONObject(i).getString(Doctor.DOCTPOSI));
								doctor.setDocIntroduction(jsonArray.getJSONObject(i).getString(Doctor.DOCINTRODUCTION));
								doctor.setDoctDepaid(jsonArray.getJSONObject(i).getString(Doctor.DOCTDEPAID));
								doctor.setDoctHospid(jsonArray.getJSONObject(i).getString(Doctor.DOCTHOSPID));
								doctor.setIsCanAppoiment(jsonArray
										.getJSONObject(i).getInt(
												Doctor.ISCANAPPOIMENT));
								
								doctor.setIsCanCons(jsonArray
										.getJSONObject(i).getInt(
												Doctor.ISCANCONS));
								doctor.setIsCanPhonePay(jsonArray
										.getJSONObject(i).getInt(
												Doctor.ISCANPHONEPAY));
								
								doctor.setDoctPosi(jsonArray.getJSONObject(i)
										.getString(Doctor.DOCTPOSI));
								doctor.setDoctorPosition(jsonArray
										.getJSONObject(i).getString(
												Doctor.DOCTORPOSITION));
								doctor.setDocIntroduction(jsonArray
										.getJSONObject(i).getString(
												Doctor.DOCINTRODUCTION));
								doctor.setDoctDepaid(jsonArray.getJSONObject(i)
										.getString(Doctor.DEPANAME));
								doctor.setHospName(jsonArray.getJSONObject(i)
										.getString(Doctor.HOSPNAME));
								doctor.setDepaName(jsonArray.getJSONObject(i)
										.getString(Doctor.DEPANAME));
								doctor.setScore(jsonArray.getJSONObject(i)
										.getInt(Doctor.SCORE));
								doctor.setDoctSchetype(jsonArray.getJSONObject(
										i).getInt(Doctor.DOCTSCHETYPE));
								doctor.setSumScore(jsonArray.getJSONObject(i)
										.getInt(Doctor.SCORENUM));
								doctor.setSumScore(jsonArray.getJSONObject(i)
										.getInt(Doctor.SCORENUM));
								doctor.setAttachFileByte(Config.getProperty("FILEURL", "")+jsonArray.getJSONObject(i).getString(
										"attachFileUrl"));
								
								doctor.setMoney(jsonArray.getJSONObject(i).getJSONObject("messageMap")
										.getString("money"));
								doctor.setRemainNum(jsonArray.getJSONObject(i).getJSONObject("messageMap")
										.getInt("remainNum"));
								doctor.setCostType(jsonArray.getJSONObject(i).getJSONObject("messageMap")
										.getInt("costType"));
								listdoctor.add(doctor);

							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						finally
						{
							getDoctView(view1, listdoctor);
						}
					}
					
					else if (msg.arg1 == 3) {
						Log.d("testColoct", jsonString+"a");
						showToast(reason);
						// listregister.clear();
						listdoctor.remove(msg.arg2);
						doctAdapter.notifyDataSetChanged();
						// getData(1);
					}

					else if (msg.arg1 == 4) {

						Log.d("testColoct", jsonString+"a");
						showToast(reason);
						// listconsult.clear();
						listhospital.remove(msg.arg2);
						hosptAdapter.notifyDataSetChanged();
						// getData(2);

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

	private void getDoctView(View view, List<Doctor> list) {

			lv_cDoctor = (ListView) view.findViewById(R.id.lv_cDoctor);
			
			View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
			emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
			emptyView.setVisibility(View.GONE);   
			((ViewGroup)lv_cDoctor.getParent()).addView(emptyView);   
			lv_cDoctor.setEmptyView(emptyView);
			
			
		    doctAdapter = new CollectDoctorAadpter(this,
					list);
			lv_cDoctor.setAdapter(doctAdapter);
			lv_cDoctor.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Doctor doctor = (Doctor) arg0.getItemAtPosition(arg2);
					Intent intent = new Intent(MyCollectActivity.this,
							DoctorHomeActivity.class);
					intent.putExtra("doctId", doctor.getDoctId());
					startActivity(intent);
					
					

				}
			});
			
			lv_cDoctor.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					delDialog(listdoctor.get(position).getDoctId(), 2, position);
					return true;
				}
			});
		

	}

	private void getHospView(View view, List<Hospital> list) {

			lv_cHospital = (ListView) view.findViewById(R.id.lv_cHospital);
			
			View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
			emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
			emptyView.setVisibility(View.GONE);   
			((ViewGroup)lv_cHospital.getParent()).addView(emptyView);   
			lv_cHospital.setEmptyView(emptyView);
			
			 hosptAdapter = new CollectHospitalAdapter(this,
					list);
			lv_cHospital.setAdapter(hosptAdapter);
			
			lv_cHospital.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Hospital hospital = (Hospital) arg0.getItemAtPosition(arg2);
					
					Intent intent = new Intent(MyCollectActivity.this,
							SelfServiceHospitalHome.class);
					intent.putExtra("hospital", hospital);
					startActivity(intent);
				}
			});
			lv_cHospital.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					delDialog(listhospital.get(position).getHospId(), 1, position);
					return true;
				}
			});
		}

	
	
	private void delDialog(final int id, final int type, final int position) {
		AlertDialog dialog=new AlertDialog.Builder(MyCollectActivity.this).setTitle("删除收藏")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						delMyRegister(id, type, position);

					}
				}).setNegativeButton("取消", null).create();
		if(type==1)
		{
			dialog.setMessage("确定删除医院:"+listhospital.get(position).getHospName()+"?");
		}
		else
			if(type==2)
			{
				dialog.setMessage("确定删除医生:"+listdoctor.get(position).getDoctName()+"?");
			}
		dialog.show();
		
	}
	
	private void delMyRegister(final int id, final int type, final int position) {
		initProgressDialog();
		mRunnable = new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				if (type == 2) {
					msg.arg1 = 3;
				} else if (type == 1) {
					msg.arg1 = 4;
				}
				msg.arg2 = position;
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("type", type+"");
				params.put("favId", id+"");
				params.put("userId", getDataSf().getInt("userId",-1)+"");
				msg.obj = new ApiHttpUtil().postMethod(
						Config.getProperty("CANCELFAV", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
	}
	
	

}
