package com.guangyi.finddoctor.hospitalRegister;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.activity.R.color;
import com.guangyi.finddoctor.adapter.HospitalAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.City;
import com.guangyi.finddoctor.model.Hospital;
import com.guangyi.finddoctor.utils.Config;

/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:选择医院
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
public class ChooseHospitalActivity extends BasicActivity {
	private ViewPager mTabPager;
	private ImageView mTabImg;// 动画图片
	private TextView mTab1, mTab2, mTab3, mTab4;
	private int zero = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int one;// 单个水平动画位移
	private int two;
	private int three;
	private Button mBack;
	private View view1, view2, view3, view4;
	private List<Hospital> mList;
	private Runnable mRunnable;
	private Thread mThread;
	private String department;
		
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

	private Handler mHandler;

	private ApiHttpUtil mApiHttpUtil;

	private String mCityId;

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
						try {
							JSONArray jsonArray = jsonObject.getJSONArray("Hospital");
							Hospital hospital;
							for (int i = 0; i < jsonArray.length(); i++) {
								hospital = new Hospital();
								System.out.println(jsonArray.getJSONObject(i)
										.getString(Hospital.HOSPNAME));
								hospital.setHospName(jsonArray.getJSONObject(i)
										.getString(Hospital.HOSPNAME));
								hospital.setHospClass(jsonArray.getJSONObject(i)
										.getInt(Hospital.HOSPCLASS));
								System.out.println(jsonArray.getJSONObject(i).getInt(
										Hospital.HOSPCLASS));
								hospital.setHospId(jsonArray.getJSONObject(i).getInt(
										Hospital.HOSPID));
								hospital.setGuahaoHostId(jsonArray.getJSONObject(i)
										.getString(Hospital.GUAHAOHOSPID));
								System.out.println(jsonArray.getJSONObject(i).getInt(
										Hospital.HOSPID));
								mList.add(hospital);
							}
//							if (mList.size() > 0) {
								
//							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						
						finally
						{
							getView1(view1);
							getView2(view2);
							getView3(view3);
							getView(view4);
						}

					}
					
					else if(msg.arg1==2)
					{
						
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
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
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
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_hospital);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		iniListener();
		initTag();

	}

	@Override
	protected void onStart() {
		getData();
		super.onStart();
	}

	// 获取医院信息
	private void getData() {
		initProgressDialog();
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(mCityId));
				list.add(department);
				msg.arg1 = 1;
				msg.obj = mApiHttpUtil.getMethod(
						Config.getProperty("HOSPITAL_STRING", ""), list);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
	}

	private void getView(View view) {
		ListView listView1 = (ListView) view.findViewById(R.id.lv_chose);
		HospitalAdapter adapter = new HospitalAdapter(this, mList);
		
		View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
		emptyView.setVisibility(View.GONE);   
		((ViewGroup)listView1.getParent()).addView(emptyView);   
		listView1.setEmptyView(emptyView);
		
		listView1.setAdapter(adapter);
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent();
				it.putExtra(Hospital.HOSPID, mList.get(position).getHospId());
				it.putExtra(Hospital.HOSPNAME, mList.get(position)
						.getHospName());
				setResult(ResultCodeCategory.RESULT_CODE_CHOSE_HOSPITAL, it);
				finish();

			}

		});

	}

	private void getView1(View view) {
		ListView listView1 = (ListView) view.findViewById(R.id.lv_chose);
		final List<Hospital> _List = new ArrayList<Hospital>();

		for (int i = 0; i < mList.size(); i++) {
			if (String.valueOf(mList.get(i).getHospClass()).substring(0, 1)
					.equals("1")) {
				_List.add(mList.get(i));
			}
		}

		HospitalAdapter adapter = new HospitalAdapter(this, _List);
		
		View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
		emptyView.setVisibility(View.GONE);   
		((ViewGroup)listView1.getParent()).addView(emptyView);   
		listView1.setEmptyView(emptyView);
		
		listView1.setAdapter(adapter);
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent();
				it.putExtra(Hospital.HOSPID, _List.get(position).getHospId());
				it.putExtra(Hospital.HOSPNAME, _List.get(position)
						.getHospName());
				setResult(ResultCodeCategory.RESULT_CODE_CHOSE_HOSPITAL, it);
				finish();

			}

		});

	}

	private void getView2(View view) {
		ListView listView1 = (ListView) view.findViewById(R.id.lv_chose);
		final List<Hospital> _List = new ArrayList<Hospital>();

		for (int i = 0; i < mList.size(); i++) {
			if (String.valueOf(mList.get(i).getHospClass()).substring(0, 1)
					.equals("2")) {
				_List.add(mList.get(i));
			}
		}

		HospitalAdapter adapter = new HospitalAdapter(this, _List);
		
		View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
		emptyView.setVisibility(View.GONE);   
		((ViewGroup)listView1.getParent()).addView(emptyView);   
		listView1.setEmptyView(emptyView);
		
		listView1.setAdapter(adapter);
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent();
				it.putExtra(Hospital.HOSPID, _List.get(position).getHospId());
				it.putExtra(Hospital.HOSPNAME, _List.get(position)
						.getHospName());
				setResult(ResultCodeCategory.RESULT_CODE_CHOSE_HOSPITAL, it);
				finish();

			}

		});

	};

	private void getView3(View view) {
		ListView listView1 = (ListView) view.findViewById(R.id.lv_chose);
		final List<Hospital> _List = new ArrayList<Hospital>();

		for (int i = 0; i < mList.size(); i++) {
			if (String.valueOf(mList.get(i).getHospClass()).substring(0, 1)
					.equals("3")) {
				_List.add(mList.get(i));
			}
		}

		HospitalAdapter adapter = new HospitalAdapter(this, _List);
		
		View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
		emptyView.setVisibility(View.GONE);   
		((ViewGroup)listView1.getParent()).addView(emptyView);   
		listView1.setEmptyView(emptyView);
		
		listView1.setAdapter(adapter);
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent();
				it.putExtra(Hospital.HOSPID, _List.get(position).getHospId());
				it.putExtra(Hospital.HOSPNAME, _List.get(position)
						.getHospName());
				setResult(ResultCodeCategory.RESULT_CODE_CHOSE_HOSPITAL, it);
				finish();

			}

		});

	}

	private void iniListener() {
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	private void initParams() {
		mCityId = getIntent().getExtras().getString(City.ID);
		department=getIntent().getExtras().getString("department");
		mList = new ArrayList<Hospital>();
		mHandler = new MyHandler();
	}

	private void initTag() {
		mTabPager = (ViewPager) findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		mTab1 = (TextView) findViewById(R.id.tv_other);
		mTab2 = (TextView) findViewById(R.id.tv_first);
		mTab3 = (TextView) findViewById(R.id.tv_second);
		mTab4 = (TextView) findViewById(R.id.tv_third);
		// mTabImg = (ImageView) findViewById(R.id.img_tab_now);
		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		mTab3.setOnClickListener(new MyOnClickListener(2));
		mTab4.setOnClickListener(new MyOnClickListener(3));
		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		int displayHeight = currDisplay.getHeight();
		one = displayWidth / 4; // 设置水平动画平移大小
		two = one * 2;
		three = one * 3;
		// mTabImg.setLayoutParams(new
		// RelativeLayout.LayoutParams(one,RelativeLayout.LayoutParams.WRAP_CONTENT)
		// );

		// 将要分页显示的View装入数组中
		mTabImg = new ImageView(this);
		mTabImg.setImageResource(R.drawable.navi_top_line);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				one, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		RelativeLayout main_bottom = (RelativeLayout) findViewById(R.id.main_bottom);
		main_bottom.addView(mTabImg, params);
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.choose_hospital_item, null);
		view2 =mLi.inflate(R.layout.choose_hospital_item, null);
		view3 = mLi.inflate(R.layout.choose_hospital_item, null);
		view4 = mLi.inflate(R.layout.choose_hospital_item, null);

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view4);
		views.add(view3);
		views.add(view2);
		views.add(view1);
		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}

			// @Override
			// public CharSequence getPageTitle(int position) {
			// return titles.get(position);
			// }

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
		};

		mTabPager.setAdapter(mPagerAdapter);
		mTabPager.setCurrentItem(currIndex);

	}

	private void initView() {
		mBack = (Button) findViewById(R.id.ib_back);
	}

}