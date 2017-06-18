package com.guangyi.finddoctor.hospitalRegister;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
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
import com.guangyi.finddoctor.adapter.DoctorHomeConsultAdapter;
import com.guangyi.finddoctor.adapter.DoctorHomeDiscussAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.imageManager.ImageManager2;
import com.guangyi.finddoctor.model.Doctor;
import com.guangyi.finddoctor.model.DoctorConsult;
import com.guangyi.finddoctor.model.DoctorDiscuss;
import com.guangyi.finddoctor.onlineAsk.OnLineAddConsultInfoForChart;
import com.guangyi.finddoctor.onlineAsk.OnLineDoctorTimeListActivity;
import com.guangyi.finddoctor.onlineAsk.OnlineChatActivity;
import com.guangyi.finddoctor.personCenter.UserLoginActivity;
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
 * Description:医生主页
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
public class DoctorHomeActivity extends BasicActivity implements OnScrollListener{
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
	private Doctor doctor;
	private int doctId;
	private SharedPreferences mSharedPreferences;
	private Editor mEditor;
	private ApiHttpUtil mApiHttpUtil;
	private Handler mHandler;
    private List<DoctorDiscuss> listDiscuss;
    private List<DoctorConsult> listConsult;
    private int pageNo1 = 1,pageNo2 = 1, pageSize = 10;
    private Runnable mRunnable;
    private Thread mThread;
    private TextView tv_doctName, tv_doctor_score,tv_doctor_score_people,tv_doctPosi,tv_doctor_hospital,tv_doct_dep,tv_doctor_skill, tv_doctor_introduce;
    private ImageView iv_doctor_pic;
    private Button ib_hospital_register, ib_online_ask , ib_phone_ask;
    private boolean isLogin=false;
    private DoctorHomeConsultAdapter consultAdapter; 
    private DoctorHomeDiscussAdapter discussAdapter;
    private ListView discussView,consultView;
    private PullToRefreshView discussRefreshView,consultRefreshView;
    private int listDiscussSize=0;
    private int listConsultSize=0;
    private int mvisibleItemCount=0;
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_home);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		iniListener();
		initTag();
		initView1(view1);
		getDoctorInfo();
		
		getDiscussView(view2);
		getConsultView(view3);
	}

	@Override
	protected void onStart() {
//		getView1(view1);
		// getView2(view2);
		// getView3(view3);
		super.onStart();
	}

	private void initParams() {
		mHandler = new MyHandler();
		mApiHttpUtil = new ApiHttpUtil();
		listDiscuss = new ArrayList<DoctorDiscuss>();
	    listConsult = new ArrayList<DoctorConsult>();
	    consultAdapter=new DoctorHomeConsultAdapter(this, listConsult); 
	    discussAdapter = new DoctorHomeDiscussAdapter(this, listDiscuss);
		// listConsult=(List<DoctorConsult>)
		// getIntent().getSerializableExtra("listConsult");
		// listDiscuss=(List<DoctorDiscuss>)
		// getIntent().getSerializableExtra("listDicuss");
//		doctor = (Doctor) getIntent().getSerializableExtra("doctor");
		doctId=getIntent().getIntExtra("doctId", -1);
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			canclePregressDialog();
			onLoaded();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			Log.d("doctor", jsonString);
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
						showToast(reason);
					}
					
					else if(msg.arg1==2)
					{
						try {
							listDiscussSize=listDiscuss.size();
							JSONArray jsonArr = jsonObject.getJSONArray("DoctorAllEval");
							DoctorDiscuss model;
							for(int i = 0; i < jsonArr.length(); i++){
								model = new DoctorDiscuss();
								model.setContent(jsonArr.getJSONObject(i).getString(DoctorDiscuss.CONTENT));
								model.setCreateTime(jsonArr.getJSONObject(i).getString(DoctorDiscuss.CREATETIME));
								model.setDoctorId(jsonArr.getJSONObject(i).getInt(DoctorDiscuss.DOCTORID));
								model.setEvalDocTitle(jsonArr.getJSONObject(i).getString(DoctorDiscuss.EVALDOCTITLE));
								model.setScore(jsonArr.getJSONObject(i).getInt(DoctorDiscuss.SCORE));
								model.setType(jsonArr.getJSONObject(i).getInt(DoctorDiscuss.TYPE));
								listDiscuss.add(model);
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						finally
						{
							if(listDiscuss.size()>0)
							{
								discussRefreshView.openFootView();
							}
							else
							{
								discussRefreshView.closeFootView();
							}
							View emptyView = LayoutInflater.from(DoctorHomeActivity.this).inflate(R.layout.empty_view, null);
							emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
							emptyView.setVisibility(View.GONE);   
							((ViewGroup)discussView.getParent()).addView(emptyView);   
							discussView.setEmptyView(emptyView);
							discussAdapter.notifyDataSetChanged();
							
							
							if(pageNo1==1)
							{
							
								if (listDiscuss.size() > listDiscussSize) {
									pageNo1++;
									
								}
							}
							
							else
							{
								if (listDiscuss.size() > listDiscussSize) {
									pageNo1++;
//									discussView.setSelection(listDiscuss.size()-1);
									discussView.setSelection(listDiscussSize-mvisibleItemCount+1);
								}
								
								else
								{
									discussRefreshView.stopCanFlash();
								}
							}
						}
					}
					
					
					else if(msg.arg1==3)
					{
                          try {
                        	  listConsultSize=listConsult.size();
							JSONArray jsonArr = jsonObject.getJSONArray("DoctAllConsultation");
							DoctorConsult model;
							for(int i = 0; i < jsonArr.length(); i++ ){
								model = new DoctorConsult();
								model.setConsId(jsonArr.getJSONObject(i).getInt(DoctorConsult.CONSID));
								model.setConsProblem(jsonArr.getJSONObject(i).getString(DoctorConsult.CONSPROBLEM));
								model.setConsReplyProblem(jsonArr.getJSONObject(i).getString(DoctorConsult.CONSREPLYPROBLEM));
								model.setConsReplyTitle(jsonArr.getJSONObject(i).getString(DoctorConsult.CONSREPLYTITLE));
								model.setConsTime(jsonArr.getJSONObject(i).getString(DoctorConsult.CONSTIME));
								model.setConsTitle(jsonArr.getJSONObject(i).getString(DoctorConsult.CONSTITLE));
								model.setExpertName(jsonArr.getJSONObject(i).getString(DoctorConsult.EXPERTNAME));
								model.setScore(jsonArr.getJSONObject(i).getString(DoctorConsult.SCORE));
								model.setConsDoctorId(jsonArr.getJSONObject(i).getInt(DoctorConsult.CONSDOCTORID));
								model.setConsUserReply(jsonArr
										.getJSONObject(i).getString(
												DoctorConsult.CONSUSERREPLY));
								model.setAttachFileByte(Config.getProperty("FILEURL", "")+jsonArr
										.getJSONObject(i).getString(
												"attachFileUrl"));
								model.setDoctName(doctor.getDoctName());
								listConsult.add(model);
								}
							
						
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                          
                          finally
                          {
                        	  if(listConsult.size()>0)
  							{
//  								pageNo2++;
  								consultRefreshView.openFootView();
  								
  							}
  							
  							else
  							{
  								consultRefreshView.closeFootView();
  							}
                        	  
                        		View emptyView = LayoutInflater.from(DoctorHomeActivity.this).inflate(R.layout.empty_view, null);
    							emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
    							emptyView.setVisibility(View.GONE);   
    							((ViewGroup)consultView.getParent()).addView(emptyView);   
    							consultView.setEmptyView(emptyView);
                        	    consultAdapter.notifyDataSetChanged();
                        	    
                        	    
                        	    if(pageNo2==1)
    							{
    							
    								if (listConsult.size() > listConsultSize) {
    									pageNo2++;
    									
    								}
    							}
    							
    							else
    							{
    								if (listConsult.size() > listConsultSize) {
        								pageNo2++;
//        								consultView.setSelection(listConsult.size()-1);
        								

        								consultView.setSelection(listConsultSize-mvisibleItemCount+1);
        							}
        							
        							else
        							{
        								consultRefreshView.stopCanFlash();
        							}
    								
    							}
                        	    
                        	    
                          }
						
					}
					
					else if(msg.arg1==4)
					{
						doctor=new Doctor();
						try {
							Log.d("jsonDoctor", jsonString);
							doctor.setExpertId(jsonObject.getJSONObject("DoctorInfo").getString(Doctor.EXPERTID));
							doctor.setDoctId(jsonObject.getJSONObject("DoctorInfo").getInt(Doctor.DOCTID));
							doctor.setDoctName(jsonObject.getJSONObject("DoctorInfo").getString(Doctor.DOCTNAME));
							doctor.setScore(jsonObject.getJSONObject("DoctorInfo").getInt(Doctor.SCORE));
							doctor.setSumScore(jsonObject.getJSONObject("DoctorInfo").getInt(Doctor.SCORENUM));
							doctor.setHospName(jsonObject.getJSONObject("DoctorInfo").getString(Doctor.HOSPNAME));
							doctor.setDepaName(jsonObject.getJSONObject("DoctorInfo").getString(Doctor.DEPANAME));
							doctor.setDoctorPosition(jsonObject.getJSONObject("DoctorInfo").getString(Doctor.DOCTORPOSITION));
							doctor.setDoctPosi(jsonObject.getJSONObject("DoctorInfo").getString(Doctor.DOCTPOSI));
							doctor.setDocIntroduction(jsonObject.getJSONObject("DoctorInfo").getString(Doctor.DOCINTRODUCTION));
							doctor.setDoctDepaid(jsonObject.getJSONObject("DoctorInfo").getString(Doctor.DOCTDEPAID));
							doctor.setDoctHospid(jsonObject.getJSONObject("DoctorInfo").getString(Doctor.DOCTHOSPID));
							doctor.setIsCanAppoiment(jsonObject.getJSONObject("DoctorInfo").getInt(Doctor.ISCANAPPOIMENT));
							doctor.setIsCanCons(jsonObject.getJSONObject("DoctorInfo").getInt(
											Doctor.ISCANCONS));
							doctor.setIsCanPhonePay(jsonObject.getJSONObject("DoctorInfo").getInt(
											Doctor.ISCANPHONEPAY));
							doctor.setAttachFileByte(Config.getProperty("FILEURL", "")+jsonObject.getJSONObject("DoctorInfo").getString(
											"attachFileUrl"));
							doctor.setMoney(jsonObject.getJSONObject("DoctorInfo").getJSONObject("messageMap")
									.getString("money"));
							doctor.setRemainNum(jsonObject.getJSONObject("DoctorInfo").getJSONObject("messageMap")
									.getInt("remainNum"));
							doctor.setCostType(jsonObject.getJSONObject("DoctorInfo").getJSONObject("messageMap")
									.getInt("costType"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(doctor.getDoctId()>0)
						{
						tv_doctName.setText(doctor.getDoctName() + "");
						tv_doctor_score.setText(doctor.getScore()*2 + "分");
						tv_doctor_score_people.setText(doctor.getSumScore() + "人评");
						tv_doctor_hospital.setText(doctor.getHospName() + "");  ///
						tv_doct_dep.setText(doctor.getDepaName() + "");//
						tv_doctor_skill.setText(doctor.getDoctorPosition() + "");
						tv_doctPosi.setText(doctor.getDoctPosi() + "");
						tv_doctor_introduce.setText(doctor.getDocIntroduction() + "");
						ImageManager2.from(DoctorHomeActivity.this).displayImage(iv_doctor_pic, doctor.getAttachFileByte(), R.drawable.touxiang);
						if (doctor.getIsCanAppoiment() == 0) {
							ib_hospital_register.setText("预约挂号");
							ib_hospital_register.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									
								}
							});
//							ib_hospital_register.setText("查看排班");
//							ib_hospital_register.setOnClickListener(new View.OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									getSharedPreference();
//									if (isLogin) {
//										Intent it = new Intent();
//										it.setClass(DoctorHomeActivity.this,
//												DoctorTimeListActivity.class);
//										it.putExtra("doctor", doctor);
//										it.putExtra("isCanRegister", 0);
//										it.putExtra("consType", 4);
//										startActivity(it);
//										
//									} else {
//										Toast.makeText(DoctorHomeActivity.this, "您还没有登录，请登录！",
//												Toast.LENGTH_LONG).show();
//										openActivityforResult(UserLoginActivity.class);
//									}
//								}
//							});
						} else if(doctor.getIsCanAppoiment() == 1){
							ib_hospital_register
									.setText("预约挂号");
							ib_hospital_register.setEnabled(true);
							ib_hospital_register.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									getSharedPreference();
									if (isLogin) {
										Intent it = new Intent();
										it.setClass(DoctorHomeActivity.this,
												DoctorTimeListActivity.class);
										it.putExtra("doctor", doctor);
										it.putExtra("consType", 4);
										startActivity(it);
									} else {
										Toast.makeText(DoctorHomeActivity.this, "您还没有登录，请登录！",
												Toast.LENGTH_LONG).show();
										openActivityforResult(UserLoginActivity.class);
									}

								}
							});
						}
							
						

						ib_online_ask.setText("图文咨询");
						ib_online_ask.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								getSharedPreference();
								if (isLogin) {
									showPayDialog(doctor.getMoney()+"", doctor.getRemainNum(), doctor.getCostType());
								} else {
									Toast.makeText(DoctorHomeActivity.this, "您还没有登录，请登录！",
											Toast.LENGTH_LONG).show();
									openActivityforResult(UserLoginActivity.class);
								}

							}
						});
						ib_phone_ask.setText("电话咨询");
						ib_phone_ask.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								getSharedPreference();
								if (isLogin) {
									Intent it = new Intent();
									it.setClass(DoctorHomeActivity.this,
											OnLineDoctorTimeListActivity.class);
									it.putExtra("doctor", doctor);
									startActivity(it);
								} else {
									Toast.makeText(DoctorHomeActivity.this, "您还没有登录，请登录！",
											Toast.LENGTH_LONG).show();
									openActivityforResult(UserLoginActivity.class);
								}

							}
						});
						
						if(doctor.getIsCanAppoiment() ==0)
						{
							ib_hospital_register.setEnabled(false);
							ib_hospital_register.setTextColor(getResources().getColor(R.color.common_grey));
						}
						if(doctor.getIsCanCons() ==0)
						{
							ib_online_ask.setEnabled(false);
							ib_online_ask.setTextColor(getResources().getColor(R.color.common_grey));
						}
						if(doctor.getIsCanPhonePay() ==0)
						{
							ib_phone_ask.setEnabled(false);
							ib_phone_ask.setTextColor(getResources().getColor(R.color.common_grey));
						}
						
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

	private void initView() {
		mBack = (Button) findViewById(R.id.ib_back);
	}


	
	private void iniListener() {
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		Button ib_post = (Button) findViewById(R.id.ib_post);
		ib_post.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String userId = getSharedPreferences("personCenter",
						Context.MODE_PRIVATE).getInt("userId", -1)
						+ "";
				if (getSharedPreferences("personCenter", Context.MODE_PRIVATE)
						.getInt("userId", -1) > 0) {
					initProgressDialog();
					mRunnable=new Runnable() {
						@Override
						public void run() {
							HashMap<String, String> params = new HashMap<String, String>();
							params.put("favType", "2");
							params.put("favId", doctId + "");
							params.put("transactionId", new Date().getTime()
									+ "");
							params.put("userId", userId);
							Message msg = new Message();
							msg.arg1 = 1;
							msg.obj = mApiHttpUtil.postMethod(
									Config.getProperty("SAVEFAVOURITE", ""),
									params);
							mHandler.sendMessage(msg);
						}
					};
					commonThreadStart();
				}
				else {
					showToast("请登陆后重试");
					openActivity(UserLoginActivity.class);
				}
			}
		});
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
		// mTabImg.setLayoutParams(new
		// RelativeLayout.LayoutParams(one,RelativeLayout.LayoutParams.WRAP_CONTENT)
		// );
		// 滑动条自动匹配屏幕宽度
		mTabImg = new ImageView(this);
		mTabImg.setImageResource(R.drawable.navi_top_line);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				one, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		RelativeLayout main_bottom = (RelativeLayout) findViewById(R.id.main_bottom);
		main_bottom.addView(mTabImg, params);
		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.doctor_home_introduce, null);
		view2 = mLi.inflate(R.layout.doctor_home_discuss, null);
		view3 = mLi.inflate(R.layout.doctor_home_consult, null);

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
				if(doctor==null||String.valueOf(doctor.getDoctId()).length()<=0)
				{
					getDoctorInfo();
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
				if(listDiscuss.size() <= 0){
					getDoctDiscuss(0);
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
				if(listConsult.size() <= 0){
					getDoctConsult(0);
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

	private void getSharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		isLogin=mSharedPreferences.getBoolean("isLogin", false);
		
		
	}

	public void getDoctDiscuss(int from ) {
		if(from==0)
		{
		initProgressDialog();
		}
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(doctId));
				list.add(pageNo1+"");
				list.add(pageSize+"");
				msg.arg1 = 2;
				msg.obj = mApiHttpUtil.getMethod(
						Config.getProperty("GET_DOCTOR_DISCUSS", ""), list);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();

	}

	public void getDoctConsult(int from) {
		if(from==0)
		{
		initProgressDialog();
		}
		mRunnable=new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(doctId));
				list.add(pageNo2+"");
				list.add(pageSize+"");
				msg.arg1 = 3;
				msg.obj = mApiHttpUtil.getMethod(
						Config.getProperty("GET_DOCTOR_CONSULT", ""), list);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();

	}

	private void initView1(View view) {
		 tv_doctName = (TextView) view.findViewById(R.id.tv_doctName);
		 tv_doctor_score = (TextView) view
				.findViewById(R.id.tv_doctor_score);
		 tv_doctor_score_people = (TextView) view
				.findViewById(R.id.tv_doctor_score_people);
		 tv_doctPosi = (TextView) view.findViewById(R.id.tv_doctPosi);
		 tv_doctor_hospital = (TextView) view
				.findViewById(R.id.tv_doctor_hospital);
		 tv_doct_dep = (TextView) view.findViewById(R.id.tv_doct_dep);
		 tv_doctor_skill = (TextView) view
				.findViewById(R.id.tv_doctor_skill);
		 tv_doctor_introduce = (TextView) view
				.findViewById(R.id.tv_doctor_introduce);
		 
	      ib_hospital_register = (Button) view
					.findViewById(R.id.ib_hospital_register);
			 ib_online_ask = (Button) view
					.findViewById(R.id.ib_online_ask);
			 ib_phone_ask = (Button) view
					.findViewById(R.id.ib_phone_ask);
			 iv_doctor_pic=(ImageView) view.findViewById(R.id.iv_doctor_pic);
	}
	private void getDoctorInfo()
	{
		initProgressDialog();
		mRunnable=new Runnable() {

			@Override
			public void run() {
//				HashMap<String, String> params = new HashMap<String, String>();
//				params.put("doctId", doctId + "");
////				params.put("transactionId", new Date().getTime()
////						+ "");
				Message msg = new Message();
				msg.arg1 = 4;
				Log.v("doctor_info_Before-----", doctId+"");
				System.out.println("doctor_info_Before-----"+ doctId);
				
				msg.obj = mApiHttpUtil.getMethod(
						Config.getProperty("GET_DOCTOR_INFO", ""),
						doctId+"");
				Log.v("doctor_info", msg.obj.toString());
				System.out.println("doctor_info-----"+msg.obj.toString());
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
	}
	

	private void getConsultView(View view) {
		
		consultRefreshView = (PullToRefreshView)view.findViewById(R.id.main_pull_refresh_view);
		consultView= (ListView) view.findViewById(R.id.mlist);
		consultView.setOnScrollListener(this);
		if(listConsult.size()<=0)
		{
			consultRefreshView.closeFootView();
		}
		
		consultView.setAdapter(consultAdapter);
		consultView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent it=new Intent();
					it.setClass(DoctorHomeActivity.this, OnlineChatActivity.class);
					it.putExtra("consId", listConsult.get(arg2).getConsId()+"");
					it.putExtra("is_bottom_visible", 0);
					startActivity(it);
				}
			});

			consultRefreshView.setOnHeaderRefreshListener(new ConsultHeaderOnRefresh());
			consultRefreshView.setOnFooterRefreshListener(new ConsultFooterOnRefresh());
//		}
	
	}
	
	private void getDiscussView(View view) {
		
		discussRefreshView = (PullToRefreshView)view.findViewById(R.id.main_pull_refresh_view);
		discussView= (ListView) view.findViewById(R.id.mlist);
		discussView.setOnScrollListener(this);

		if(listDiscuss.size()<=0)
		{
			discussRefreshView.closeFootView();
		}
		discussView.setAdapter(discussAdapter);
		discussRefreshView.setOnHeaderRefreshListener(new DiscussHeaderOnRefresh());
		discussRefreshView.setOnFooterRefreshListener(new DiscussFooterOnRefresh());
		
	}
	
	private void onLoaded() {
		discussRefreshView.onHeaderRefreshComplete(getResources().getString(R.string.pull_to_refresh_pull_last_time)+DateTools.getCurrentDateString("MM-dd HH:mm:ss"));
		discussRefreshView.onFooterRefreshComplete();
		
		consultRefreshView.onHeaderRefreshComplete(getResources().getString(R.string.pull_to_refresh_pull_last_time)+DateTools.getCurrentDateString("MM-dd HH:mm:ss"));
		consultRefreshView.onFooterRefreshComplete();
	}
	
	class ConsultHeaderOnRefresh implements OnHeaderRefreshListener {
		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			getDoctConsult(1);
		}
	}
	
	
	class ConsultFooterOnRefresh implements OnFooterRefreshListener {
		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			getDoctConsult(1);
		}
	}
	
	
	
	class DiscussHeaderOnRefresh implements OnHeaderRefreshListener {
		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			getDoctDiscuss(1);
		}
	}
	
	
	class DiscussFooterOnRefresh implements OnFooterRefreshListener {

		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			getDoctDiscuss(1);
		}
	}
	
	private void showPayDialog(final String money,final int remainNum,int costType)
	{
			View view=LayoutInflater.from(this).inflate(R.layout.progress_dialog_pay, null);
	    	final Dialog dialog=new Dialog(this, R.style.Translucent_NoTitle);
	    	dialog.setContentView(view);
	    	TextView tv_progress=(TextView) view.findViewById(R.id.tv_progress);
	    	Log.d("costType", costType+"t");
	    	Log.d("remainNum", remainNum+"t");
	    	if(costType==0)
	    	{
	    		if(remainNum<=0)
	    		{
	    			SpannableStringBuilder builder = new SpannableStringBuilder("向医生提问需要支付"
	    					+ money + "元/条");
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 9, money.length() + 9,
	    					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			tv_progress.setText(builder);
	    		}
	    		else
	    		{
	    			SpannableStringBuilder builder = new SpannableStringBuilder("向医生提问前"
	    					+remainNum+ "条免费,继续追问需要"+money+"元/条");
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 6, String.valueOf(remainNum).length() + 6,
	    					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 16+(remainNum+"").length(), money.length() + 16+(remainNum+"").length(),
	    					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
	    			tv_progress.setText(builder);
	    		}
	    	}
	    	else if(costType==1)
	    	{
	    		if(remainNum<=0)
	    		{
	    			SpannableStringBuilder builder = new SpannableStringBuilder("向医生提问需要支付"
	    					+ money + "元");
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 9, money.length() + 9,
	    					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			tv_progress.setText(builder);
	    		}
	    		else
	    		{
	    			SpannableStringBuilder builder = new SpannableStringBuilder("向医生提问前"
	    					+ remainNum+ "条免费,继续追问需要"+money+"元");
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 6, String.valueOf(remainNum).length() + 6,
	    					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 16+(remainNum+"").length(), money.length() + 16+(remainNum+"").length(),
	    					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			tv_progress.setText(builder);
	    		}
	    	}
	    	else 
	    	{
	    			SpannableStringBuilder builder = new SpannableStringBuilder("向医生提问免费");
	    			builder.setSpan(new ForegroundColorSpan(Color.RED), 5, 7,
	    					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			tv_progress.setText(builder);
	    	}
//			SpannableStringBuilder builder = new SpannableStringBuilder("已经有"
//					+ count + "人成功预约!");
//			builder.setSpan(redSpan, 3, count.length() + 3,
//					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//	    	tv_progress.setText("您的免费咨询条数已经用完，继续咨询需要付费:"+money+"元");
	    	Button btn_ok=(Button) view.findViewById(R.id.btn_ok);
	    	btn_ok.setText("去咨询");
	    	Button btn_cancle=(Button) view.findViewById(R.id.btn_cancle);
	    	btn_cancle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
	    	btn_ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent it = new Intent();
					it.setClass(DoctorHomeActivity.this,
							OnLineAddConsultInfoForChart.class);
					it.putExtra("doctor", doctor);
					startActivity(it);
					dialog.cancel();
				}
			});
	    	dialog.setCanceledOnTouchOutside(false);
	    	dialog.show();
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

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mvisibleItemCount=visibleItemCount;
	}

}