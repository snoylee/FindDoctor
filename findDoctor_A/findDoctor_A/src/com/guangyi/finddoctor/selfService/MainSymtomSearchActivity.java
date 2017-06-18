package com.guangyi.finddoctor.selfService;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.activity.R.color;
import com.guangyi.finddoctor.adapter.BodyDiseaseAdapter;
import com.guangyi.finddoctor.adapter.SearchAssayAdapter;
import com.guangyi.finddoctor.adapter.SearchDiseaseAdapter;
import com.guangyi.finddoctor.adapter.SearchHospitalAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.Assay;
import com.guangyi.finddoctor.model.BodyDisease;
import com.guangyi.finddoctor.model.Disease;
import com.guangyi.finddoctor.model.Hospital;
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
 * Description:搜索展示
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
public class MainSymtomSearchActivity extends BasicActivity implements
		android.view.GestureDetector.OnGestureListener ,OnScrollListener{
	private ImageView mTabImg;// 动画图片
	private TextView mTab1, mTab2, mTab3, mTab4;
	private int zero = 0;// 动画片偏移量
	private int currIndex = 0;// 前页卡编号
	private int one;// 单个水平动画位移
	private int two;
	private int three;
	private int pageNo = 1; // 当前页
	private int pageSize = 10; // 页显示数
	private List<String> mList;
	private Handler mHandler;
	private String mWord;
	private SearchDiseaseAdapter diseaseAdp;
	private SearchHospitalAdapter hospitalAdp;
	private SearchAssayAdapter assayAda;
	private BodyDiseaseAdapter bodyDiseaseAda;

	private List<Disease> diseases;
	private List<Hospital> hospitals;
	private List<Assay> assays;
	private List<BodyDisease> bodyDiseases;

	private Runnable mRunnable;
	private Thread mThread;
	private GestureDetector gd;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 20;
	private static int count;
	private List<TextView> listTextView;
	private TextView tv_tips;
	private ListView listView;
	private PullToRefreshView mPullToRefreshView;
	 private  LinearLayout linear;
	 private List<TextView> tvs;
	 private int listFsize=0;
	 private int mvisibleItemCount=0;
	 private boolean isVisival=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_search_home);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		// 获取查询编号
		mRunnable = new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> parms = new HashMap<String, String>();
				String a = URLEncoder.encode(mWord);
				parms.put("word", a);
				parms.put("type", "1");
				Message msg = new Message();
				msg.obj = new ApiHttpUtil().postMethod(
						Config.getProperty("SEARCHTYPE", ""), parms);
				msg.arg1 = 0;
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
		initProgressDialog();

	}

	@Override
	protected void onStart() {
		isVisival=true;
		super.onStart();
	}

	protected void onStop() {
		isVisival=false;
		mHandler.removeCallbacks(mRunnable);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		count = 0;
		super.onDestroy();
	}

	private void commonThreadStart() {
		if (mRunnable != null) {
			mThread = new Thread(mRunnable);
			mThread.start();
		}
	}

	private void initParams() {
		count = 0;
		gd = new GestureDetector(this);
		mWord = getIntent().getStringExtra("word");
		diseases = new ArrayList<Disease>();
		hospitals = new ArrayList<Hospital>();
		tvs=new ArrayList<TextView>();
		assays = new ArrayList<Assay>();
		bodyDiseases = new ArrayList<BodyDisease>();
		hospitalAdp = new SearchHospitalAdapter(this, hospitals);
		diseaseAdp = new SearchDiseaseAdapter(this, diseases);
		assayAda = new SearchAssayAdapter(this, assays);
		bodyDiseaseAda = new BodyDiseaseAdapter(this, bodyDiseases);
		mList = new ArrayList<String>();
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				onLoaded();
				canclePregressDialog();
				JSONObject jsonObject = null;
				String jsonString = msg.obj.toString();
				Log.i("jsoninfo", jsonString);
				if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
					showToast(getResources().getString(R.string.soconntimeout));
				} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
					showToast(getResources().getString(R.string.conntimeout));
				} else {
					try {
						jsonObject = new JSONObject(msg.obj.toString());
						if (jsonObject.getInt("code") == 0) {
							switch (msg.arg1) {
							case 0:
								try {
									String a = jsonObject
											.getString("searchType");
									JSONArray array = new JSONArray(a);
									 LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1);
									for (int i = 0; i < array.length(); i++) {
										mList.add(array.getString(i));
										TextView tv=new TextView(MainSymtomSearchActivity.this);
										
										 if(array.getString(i).equals("1"))
											{
											 tv.setText("医院");
											 
											}
										 else if(array.getString(i).equals("4"))
											{
											 tv.setText("疾病库");
											}
										 else if(array.getString(i).equals("6"))
											{
											 tv.setText("化验检查");
											}
										 else if(array.getString(i).equals("7"))
											{
											 tv.setText("症状");
											}
								
										 tv.setGravity(Gravity.CENTER);
										linear.addView(tv,params);
										tvs.add(tv);
									}
									if (mList.size() > 0) {
										mPullToRefreshView.setVisibility(View.VISIBLE);
										tv_tips.setVisibility(View.GONE);
										if (Integer.valueOf(mList.get(0)) == 1) {
											if(isVisival)
											{
											initProgressDialog();
											getData(mWord, pageNo, pageSize,
													Integer.valueOf(mList
															.get(0)),
													Integer.valueOf(mList
															.get(0)));
											}
											
										} else if (Integer
												.valueOf(mList.get(0)) == 4) {
											if(isVisival)
											{
											initProgressDialog();
											getData(mWord, pageNo, pageSize,
													Integer.valueOf(mList
															.get(0)),
													Integer.valueOf(mList
															.get(0)));
											}
											
										} else if (Integer
												.valueOf(mList.get(0)) == 6) {
											if(isVisival)
											{
											initProgressDialog();
											getData(mWord, pageNo, pageSize,
													Integer.valueOf(mList
															.get(0)),
													Integer.valueOf(mList
															.get(0)));
											}
											
										} else if (Integer
												.valueOf(mList.get(0)) == 7) {
											if(isVisival)
											{
											initProgressDialog();
											getData(mWord, pageNo, pageSize,
													Integer.valueOf(mList
															.get(0)),
													Integer.valueOf(mList
															.get(0)));
											}
											
										}
										Display currDisplay = getWindowManager()
												.getDefaultDisplay();// 获取屏幕当前分辨率
										int displayWidth = currDisplay
												.getWidth();
										int displayHeight = currDisplay
												.getHeight();
										one = displayWidth / mList.size(); // 设置水平动画平移大小
										two = one * 2;
										three = one * 3;
										mTabImg = new ImageView(
												MainSymtomSearchActivity.this);
										mTabImg.setImageResource(R.drawable.navi_top_line);
										RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
												one,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
										RelativeLayout main_bottom = (RelativeLayout) findViewById(R.id.main_bottom);
										main_bottom.addView(mTabImg, params1);
										for (int j = 0; j < tvs.size(); j++) {
											if(tvs.get(j).getText().toString().trim().equals("医院"))
											{
												tvs.get(j).setOnClickListener(new TextViewClick(1,j));
											}
											if(tvs.get(j).getText().toString().trim().equals("疾病库"))
											{
												tvs.get(j).setOnClickListener(new TextViewClick(4,j));
											}
											if(tvs.get(j).getText().toString().trim().equals("化验检查"))
											{
												tvs.get(j).setOnClickListener(new TextViewClick(6,j));
											}
											if(tvs.get(j).getText().toString().trim().equals("症状"))
											{
												tvs.get(j).setOnClickListener(new TextViewClick(7,j));
											}
											
										
											
										}

									}
									
									
									else 
									{
										mPullToRefreshView.setVisibility(View.GONE);
										tv_tips.setVisibility(View.VISIBLE);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
								break;

							case 4: 
								pageNo += 1;
								listFsize=diseases.size();
								Log.i("Disease", listFsize+"");
								JSONArray jsonArray1 = jsonObject
										.getJSONObject("pageSearch")
										.getJSONArray("disease");
								for (int i = 0; i < jsonArray1.length(); i++) {
									Disease disease = new Disease();
									disease.setName(jsonArray1.getJSONObject(i)
											.getString(Disease.NAME));
									disease.setAliases(jsonArray1
											.getJSONObject(i).getString(
													Disease.ALIASES));
									disease.setDescript(jsonArray1
											.getJSONObject(i).getString(
													Disease.DESCRIPT));
									disease.setDietary(jsonArray1
											.getJSONObject(i).getString(
													Disease.DIETARY));
									disease.setExercise(jsonArray1
											.getJSONObject(i).getString(
													Disease.EXERCISE));
									disease.setFamilyHistory(jsonArray1
											.getJSONObject(i).getString(
													Disease.FAMILYHISTORY));
									disease.setHdeptid(jsonArray1
											.getJSONObject(i).getString(
													Disease.HDEPTID));
									disease.setHdeptName(jsonArray1
											.getJSONObject(i).getString(
													Disease.HDEPTNAME));
									disease.setId(jsonArray1.getJSONObject(i)
											.getInt(Disease.ID));
									disease.setLaboratory(jsonArray1
											.getJSONObject(i).getString(
													Disease.LABORATORY));
									disease.setLocation(jsonArray1
											.getJSONObject(i).getString(
													Disease.LOCATION));
									disease.setMedication(jsonArray1
											.getJSONObject(i).getString(
													Disease.MEDICATION));
									disease.setName(jsonArray1.getJSONObject(i)
											.getString(Disease.NAME).trim());
									disease.setOutline(jsonArray1
											.getJSONObject(i).getString(
													Disease.OUTLINE));
									disease.setPathogen(jsonArray1
											.getJSONObject(i).getString(
													Disease.PATHOGEN));
									disease.setPerson(jsonArray1.getJSONObject(
											i).getString(Disease.PERSON));
									disease.setRehabilitation(jsonArray1
											.getJSONObject(i).getString(
													Disease.REHABILITATION));
									disease.setSymptoms(jsonArray1
											.getJSONObject(i).getString(
													Disease.SYMPTOMS));
									disease.setSymptomsLabel(jsonArray1
											.getJSONObject(i).getString(
													Disease.SYMPTOMSLABEL));
									diseases.add(disease);
								}
								getView(msg.arg1);
								break;
							case 1: 
								pageNo += 1;
								listFsize=hospitals.size();
								Log.i("Hospital", listFsize+"");
								JSONArray jsonArray2 = jsonObject
										.getJSONObject("pageSearch")
										.getJSONArray("hostial");
								for (int i = 0; i < jsonArray2.length(); i++) {
									Hospital hospital = new Hospital();
									hospital.setHospName(jsonArray2
											.getJSONObject(i).getString(
													Hospital.HOSPNAME));
									hospital.setHospAddr(jsonArray2
											.getJSONObject(i).getString(
													Hospital.HOSPADDR));
									hospital.setHospClass(jsonArray2
											.getJSONObject(i).getInt(
													Hospital.HOSPCLASS));
									hospital.setHospTel(jsonArray2
											.getJSONObject(i).getString(
													Hospital.HOSPTEL));
									hospital.setHospId(jsonArray2
											.getJSONObject(i).getInt(
													Hospital.HOSPID));
									hospital.setHospIntroduction(jsonArray2
											.getJSONObject(i).getString(
													Hospital.HOSPINTRODUCTION));
									hospitals.add(hospital);
								}
								getView(msg.arg1);
								break;
							case 6:
								pageNo += 1;
								listFsize=assays.size();
								Log.i("Assay", listFsize+"");
								Assay assay;
								JSONArray jsonArray3 = jsonObject
										.getJSONObject("pageSearch")
										.getJSONArray("assay");
								for (int i = 0; i < jsonArray3.length(); i++) {
									assay = new Assay();
									assay.setIntroduce(jsonArray3
											.getJSONObject(i).getString(
													Assay.INTRODUCE));
									assay.setName(jsonArray3.getJSONObject(i)
											.getString(Assay.NAME));
									assay.setNormalValue(jsonArray3
											.getJSONObject(i).getString(
													Assay.NORMALVALUE));
									assay.setNotes(jsonArray3.getJSONObject(i)
											.getString(Assay.NOTES));
									assay.setOtherassay(jsonArray3
											.getJSONObject(i).getString(
													Assay.OTHERASSAY));
									assay.setSense(jsonArray3.getJSONObject(i)
											.getString(Assay.SENSE));
									assay.setId(jsonArray3.getJSONObject(i)
											.getInt(Assay.ID));
									assay.setIndex(jsonArray3.getJSONObject(i)
											.getString("pinYin")
											.substring(0, 1).toUpperCase());
									assays.add(assay);
								}
								getView(msg.arg1);
								break;
							case 7: 
								pageNo += 1;
								listFsize=bodyDiseases.size();
								Log.i("BodyDisease", listFsize+"");
								JSONArray jsonArray4 = jsonObject
										.getJSONObject("pageSearch")
										.getJSONArray("labelNames");
								BodyDisease model;
								for (int i = 0; i < jsonArray4.length(); i++) {
									model = new BodyDisease();
									model.setId(jsonArray4.getJSONObject(i)
											.getInt("id"));
									model.setDisId(jsonArray4.getJSONObject(i)
											.getInt("disId"));
									model.setBody(jsonArray4.getJSONObject(i)
											.getString("body"));
									model.setLableName(jsonArray4
											.getJSONObject(i).getString(
													"lableName"));
									bodyDiseases.add(model);
								}
								getView(msg.arg1);
								break;
							default:
								break;
							}
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

	private void initView() {
		linear=(LinearLayout) findViewById(R.id.linear_top_navi);
		tv_tips=(TextView) findViewById(R.id.tv_tips);
		mPullToRefreshView = (PullToRefreshView)findViewById(R.id.main_pull_refresh_view);
		listView= (ListView) findViewById(R.id.mlist);

listView.setOnScrollListener(this);
		listTextView = new ArrayList<TextView>();
//		mTab1 = (TextView) findViewById(R.id.tv_name1);
//		mTab2 = (TextView) findViewById(R.id.tv_name2);
//		mTab3 = (TextView) findViewById(R.id.tv_name3);
//		mTab4 = (TextView) findViewById(R.id.tv_name4);
		listTextView.add(mTab1);
		listTextView.add(mTab2);
		listTextView.add(mTab3);
		listTextView.add(mTab4);
		Button mBack = (Button) findViewById(R.id.ib_back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// 疾病
	private void getView(int tag) {
		mPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefresh(tag));
		mPullToRefreshView.setOnFooterRefreshListener(new OnFooterRefresh(tag));
		// 1 医院 4 疾病库 6 化验检查，7 症状
		switch (tag) {
		case 1:
			if(listView.getAdapter() instanceof SearchHospitalAdapter)
			{
				hospitalAdp.notifyDataSetChanged();
				if (hospitals.size() > 0) {
					listView.setSelection(listFsize-mvisibleItemCount+1);
					if(hospitals.size()<=listFsize)
					{
						mPullToRefreshView.stopCanFlash();
					}
				}
			}
			
			else
			{
				mPullToRefreshView.startCanFlash();
				listView.setAdapter(hospitalAdp);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent it = new Intent();
						it.putExtra("hospital", hospitals.get(arg2));
						it.setClass(MainSymtomSearchActivity.this,
								SelfServiceHospitalHome.class);
						startActivity(it);
					}
				});
			}
			

			break;
		case 4:
			// 1 医院 4 疾病库 6 化验检查，7 症状
			if(listView.getAdapter() instanceof SearchDiseaseAdapter)
			{
				diseaseAdp.notifyDataSetChanged();
				if(diseases.size()>0)
				{
					listView.setSelection(listFsize-mvisibleItemCount+1);
				if (diseases.size() <=listFsize) {
					mPullToRefreshView.stopCanFlash();
					
				}
				}
			}
			
			else
			{
				mPullToRefreshView.startCanFlash();
				listView.setAdapter(diseaseAdp);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent it = new Intent();
						it.putExtra("disease", diseases.get(arg2));
						it.setClass(MainSymtomSearchActivity.this,
								SelfServiceLibraryDieseaseHome.class);
						startActivity(it);
					}
				});
			}
		

			break;

		case 6:
			if(listView.getAdapter() instanceof SearchAssayAdapter)
			{
				assayAda.notifyDataSetChanged();
				if(assays.size()>0)
				{
					listView.setSelection(listFsize-mvisibleItemCount+1);
				if (assays.size() <=listFsize) {
					mPullToRefreshView.stopCanFlash();
					
				}
				}
			}
			
			else
			{
				mPullToRefreshView.startCanFlash();
				listView.setAdapter(assayAda);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent it = new Intent();
						it.putExtra("assay", assays.get(arg2));
						it.setClass(MainSymtomSearchActivity.this,
								SelfServiceLibraryAssayHome.class);
						startActivity(it);
					}
				});

			}
			
		
			break;

		case 7:
			if(listView.getAdapter() instanceof BodyDiseaseAdapter)
			{
				bodyDiseaseAda.notifyDataSetChanged();
				if(bodyDiseases.size()>0)
				{
					listView.setSelection(listFsize-mvisibleItemCount+1);
				if (bodyDiseases.size() <=listFsize) {
					mPullToRefreshView.stopCanFlash();
					
				}
				}
			}
			else
			{
				mPullToRefreshView.startCanFlash();
				listView.setAdapter(bodyDiseaseAda);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(MainSymtomSearchActivity.this,
								BodyDiseaseActivity.class);
						intent.putExtra("lableName", bodyDiseases.get(arg2)
								.getLableName());
						startActivity(intent);
					}
				});
				
			}
			
			

			break;
		default:
			break;
		}
	}

	
	
	
	private void onLoaded() {
		mPullToRefreshView.onHeaderRefreshComplete(getResources().getString(R.string.pull_to_refresh_pull_last_time)+DateTools.getCurrentDateString("MM-dd HH:mm:ss"));
		mPullToRefreshView.onFooterRefreshComplete();
	}


	class OnHeaderRefresh implements OnHeaderRefreshListener
	{
		private int from;
		public OnHeaderRefresh(int from)
		{
			this.from=from;
		}

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			switch (from) {
			case 1:
				getData(mWord, pageNo, pageSize, 1, 1);
				break;
			case 4:
				getData(mWord, pageNo, pageSize, 4, 4);
				break;

			case 6:
				getData(mWord, pageNo, pageSize, 6, 6);
				break;

			case 7:
				getData(mWord, pageNo, pageSize, 7, 7);
				break;
			default:
				break;
			}
			
		}
		
	}
	
	class OnFooterRefresh implements OnFooterRefreshListener
	{
		private int from;
		public OnFooterRefresh(int from)
		{
			this.from=from;
		}

		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			switch (from) {
			case 1:
				getData(mWord, pageNo, pageSize, 1, 1);
				break;
			case 4:
				getData(mWord, pageNo, pageSize, 4, 4);
				break;

			case 6:
				getData(mWord, pageNo, pageSize, 6, 6);
				break;

			case 7:
				getData(mWord, pageNo, pageSize, 7, 7);
				break;
			default:
				break;
			}
			
					
		}
		
	}
	
	
	
	
	
	

	private void getData(final String _word, final int pageNo,
			final int pageSize, final int type, final int arg1) {
		mRunnable = new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				Map<String, String> params = new HashMap<String, String>();
				params.put("word", _word);
				params.put("type", String.valueOf(type));
				params.put("pageNo", String.valueOf(pageNo));
				params.put("pageSize", String.valueOf(pageSize));
				msg.arg1 = arg1;
				msg.obj = new ApiHttpUtil().postMethod(
						Config.getProperty("SEARCH", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
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

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		
		if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
				&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {


			if (count < mList.size() - 1 && count >= 0) {
				mPullToRefreshView.stopCanFlash();

				listFsize=0;
				pageNo = 1;
				diseases.clear();
				hospitals.clear();
				assays.clear();
				bodyDiseases.clear();
				if (diseaseAdp != null) {
					diseaseAdp.notifyDataSetChanged();
				}
				if (hospitalAdp != null) {
					hospitalAdp.notifyDataSetChanged();
				}
				if (assayAda != null) {
					assayAda.notifyDataSetChanged();
				}
				if (bodyDiseaseAda != null) {
					bodyDiseaseAda.notifyDataSetChanged();
				}
				count++;
				changeAnim(count);
				initProgressDialog();
				getData(mWord, pageNo, pageSize,
						Integer.valueOf(mList.get(count)),
						Integer.valueOf(mList.get(count)));

			}
		}
		else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
				&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

			if (count < mList.size() && count > 0) {
				mPullToRefreshView.stopCanFlash();
				listFsize=0;
				pageNo = 1;
				diseases.clear();
				hospitals.clear();
				assays.clear();
				bodyDiseases.clear();
				if (diseaseAdp != null) {
					diseaseAdp.notifyDataSetChanged();
				}
				if (hospitalAdp != null) {
					hospitalAdp.notifyDataSetChanged();
				}
				if (assayAda != null) {
					assayAda.notifyDataSetChanged();
				}
				if (bodyDiseaseAda != null) {
					bodyDiseaseAda.notifyDataSetChanged();
				}
				count--;
				changeAnim(count);
				initProgressDialog();
				getData(mWord, pageNo, pageSize,
						Integer.valueOf(mList.get(count)),
						Integer.valueOf(mList.get(count)));
			}
		}
		return false;
	}

	private void changeAnim(int arg0)
	{

		Animation animation = null;
//		if(tvs.size()==1)
//		{
//			mTab1=tvs.get(0);
////			animation = new TranslateAnimation(one, 0, 0, 0);
//			mTab1.setTextColor(getResources().getColor(color.common_green));
//			
//		}
//		else 
		if(tvs.size()==2)
		{
			mTab1=tvs.get(0);
			mTab2=tvs.get(1);
			switch (arg0) {
			case 0:
				mTab1.setTextColor(getResources().getColor(color.common_green));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setTextColor(getResources().getColor(
							color.common_grey));
				} 
				break;
			case 1:
				mTab2.setTextColor(getResources().getColor(color.common_green));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setTextColor(getResources().getColor(
							color.common_grey));
				} 
				break;

			}
		}
		
		else if(tvs.size()==3)
		{
			mTab1=tvs.get(0);
			mTab2=tvs.get(1);
			mTab3=tvs.get(2);
			switch (arg0) {
			case 0:
				mTab1.setTextColor(getResources().getColor(color.common_green));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setTextColor(getResources().getColor(
							color.common_grey));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setTextColor(getResources().getColor(
							color.common_grey));
				} 
				break;
			case 1:
				mTab2.setTextColor(getResources().getColor(color.common_green));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setTextColor(getResources().getColor(
							color.common_grey));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setTextColor(getResources().getColor(
							color.common_grey));
				} 
				break;
			case 2:
				mTab3.setTextColor(getResources().getColor(color.common_green));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setTextColor(getResources().getColor(
							color.common_grey));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setTextColor(getResources().getColor(
							color.common_grey));
				} 
				break;
				
				
			}
		}
		
		else if(tvs.size()==4)
		{
			mTab1=tvs.get(0);
			mTab2=tvs.get(1);
			mTab3=tvs.get(2);
			mTab4=tvs.get(3);
			switch (arg0) {
			case 0:
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
			case 3:
				mTab4.setTextColor(getResources().getColor(color.common_green));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setTextColor(getResources().getColor(
							color.common_grey));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setTextColor(getResources().getColor(
							color.common_grey));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setTextColor(getResources().getColor(
							color.common_grey));
				}
				break;
			}
			
		}
		if(animation!=null)
		{
		currIndex = arg0;
		count=arg0;
		animation.setFillAfter(true);
		animation.setDuration(150);
		mTabImg.startAnimation(animation);
		}
	}
	
	
	
	class TextViewClick implements View.OnClickListener
	{
		private int tag;
		private int _count;
		public TextViewClick(int tag,int count)
		{
			this.tag=tag;
			this._count=count;
		}

		@Override
		public void onClick(View v) {
			
			if(tvs.size()>1)
			{
				initProgressDialog();
				pageNo = 1;
				diseases.clear();
				hospitals.clear();
				assays.clear();
				bodyDiseases.clear();
				if (diseaseAdp != null) {
					diseaseAdp.notifyDataSetChanged();
				}
				if (hospitalAdp != null) {
					hospitalAdp.notifyDataSetChanged();
				}
				if (assayAda != null) {
					assayAda.notifyDataSetChanged();
				}
				if (bodyDiseaseAda != null) {
					bodyDiseaseAda.notifyDataSetChanged();
				}
			changeAnim(_count);
			getData(mWord, pageNo, pageSize, tag,tag);
			}
			
			
		}
		
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
