package com.guangyi.finddoctor.selfService;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.activity.R.color;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.Disease;
import com.guangyi.finddoctor.model.GuaHao;
import com.guangyi.finddoctor.onlineAsk.OnLineAddConsultInfoForChart;
import com.guangyi.finddoctor.secondActivity.ShospitalRegister;
import com.guangyi.finddoctor.utils.Config;
/**
* <p>Title: 网络医院运营支撑平台-APP个人版</p>
* <p>Description:疾病详情 </p>
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company:中国移动有限公司东莞分公司 </p>
* @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
* @version：1.0
* @since：2013-9-23
*/
public class SelfServiceLibraryDieseaseHome extends BasicActivity {
	private ViewPager mTabPager;
	private ImageView mTabImg;// 动画图片
	private TextView mTab1, mTab2;
	private int zero = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int one;// 单个水平动画位移
	private Button mBack;
	private View view1, view2;
	private Disease disease;
	private Drawable mDrawable1, mDrawable2;
	private TextView tv_disease_name, tv_ddisease_detail;
	private ToggleButton toggle_pathogen, toggle_symptom, toggle_recomm_depart,
			toggle_treatment, toggle_drug;
	private TextView tv_pathogen, tv_symptom, tv_treatment, tv_drug;
	private LinearLayout linear_recomm_depart;
	private Button inner_btn_hospital_register, inner_btn_online_ask;
	private TextView tv_around_hospital, tv_around_drugstore;
	private Runnable mRunnable;
	private Thread mThread;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_service_library_diesease_detail_home);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initTag();
		initDetailView(view1);
		initRequestView(view2);
		getView1();

	}
	
	private void commonThreadStart()
	{
		if(mRunnable!=null)
		{
		mThread=new Thread(mRunnable);
		mThread.start();
		}
	} 
	
	class MyHandler extends Handler
	{
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
						GuaHao guaHao;
						try {
							JSONObject jsonChildObject=jsonObject.getJSONObject("guanghao");
							guaHao=new GuaHao();
							guaHao.setGuahaoHostId(jsonChildObject.getString(GuaHao.GUAHAOHOSTID));
							guaHao.setHdeptId(jsonChildObject.getString(GuaHao.HDEPTID));
							guaHao.setHospCityName(jsonChildObject.getString(GuaHao.HOSPCITYNAME));
							guaHao.setHospProvinceName(jsonChildObject.getString(GuaHao.HOSPPROVINCENAME));
							guaHao.setHospName(jsonChildObject.getString(GuaHao.HOSPNAME));
							guaHao.setDepatName(jsonChildObject.getString(GuaHao.DEPATNAME));
							guaHao.setHospCity(jsonChildObject.getString(GuaHao.HOSPCITY));
							guaHao.setHospId(jsonChildObject.getInt(GuaHao.HOSPID));
							Intent intent=new Intent();
							intent.putExtra("close", 1);
							intent.putExtra("guahao", guaHao);
							intent.setClass(SelfServiceLibraryDieseaseHome.this, ShospitalRegister.class);
							startActivity(intent);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
	
	private void  getData()
	{
		
		initProgressDialog();
		mRunnable=new Runnable() {
			
			@Override
			public void run() {
				Message msg=new Message();
				String params=disease.getHdeptid();
				msg.arg1=1;
				msg.obj=new ApiHttpUtil().getMethod(Config.getProperty("GUAHAOBYDISEASEDEPTID",""),params);
				mHandler.sendMessage(msg);
				
			}
		};
		commonThreadStart();
		
	}

	protected void onStop() {
		mHandler.removeCallbacks(mRunnable);
		super.onStop();
	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	private void initParams() {
		mHandler=new MyHandler();
		disease = (Disease) getIntent().getSerializableExtra("disease");
		mDrawable1 = this.getResources().getDrawable(R.drawable.img1);
		mDrawable2 = this.getResources().getDrawable(R.drawable.img2);
	}

	private void initView() {
		mBack = (Button) findViewById(R.id.ib_back);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}
	
	

	private void initRequestView(View view) {
		tv_around_hospital = (TextView) view
				.findViewById(R.id.tv_around_hospital);
		tv_around_drugstore = (TextView) view
				.findViewById(R.id.tv_around_drugstore);
		tv_around_hospital.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SelfServiceLibraryDieseaseHome.this,
						SelfServiceMapHospitalActivity.class);
				startActivity(intent);

			}
		});
		
		tv_around_drugstore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SelfServiceLibraryDieseaseHome.this,
						SelfServiceMapDrugstoreActivity.class);
				startActivity(intent);
				
			}
		});

	}

	private void initDetailView(View view) {

		tv_disease_name = (TextView) view.findViewById(R.id.tv_disease_name);
		tv_ddisease_detail = (TextView) view
				.findViewById(R.id.tv_ddisease_detail);

		tv_pathogen = (TextView) view.findViewById(R.id.tv_pathogen);
		tv_symptom = (TextView) view.findViewById(R.id.tv_symptom);
		tv_treatment = (TextView) view.findViewById(R.id.tv_treatment);
		tv_drug = (TextView) view.findViewById(R.id.tv_drug);

		linear_recomm_depart = (LinearLayout) view
				.findViewById(R.id.linear_recomm_depart);
		inner_btn_hospital_register = (Button) view
				.findViewById(R.id.inner_btn_hospital_register);
		inner_btn_online_ask = (Button) view
				.findViewById(R.id.inner_btn_online_ask);

		toggle_pathogen = (ToggleButton) view
				.findViewById(R.id.toggle_pathogen);
		toggle_symptom = (ToggleButton) view.findViewById(R.id.toggle_symptom);
		toggle_recomm_depart = (ToggleButton) view
				.findViewById(R.id.toggle_recomm_depart);
		toggle_treatment = (ToggleButton) view
				.findViewById(R.id.toggle_treatment);
		toggle_drug = (ToggleButton) view.findViewById(R.id.toggle_drug);

		toggle_pathogen
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							toggle_pathogen.setBackgroundDrawable(mDrawable1);
							tv_pathogen.setVisibility(View.GONE);
						} else {
							toggle_pathogen.setBackgroundDrawable(mDrawable2);
							tv_pathogen.setVisibility(View.VISIBLE);
						}

					}
				});
		toggle_symptom
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							toggle_symptom.setBackgroundDrawable(mDrawable1);
							tv_symptom.setVisibility(View.GONE);
						} else {
							toggle_symptom.setBackgroundDrawable(mDrawable2);
							tv_symptom.setVisibility(View.VISIBLE);
						}

					}
				});
		toggle_recomm_depart
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							toggle_recomm_depart
									.setBackgroundDrawable(mDrawable1);
							linear_recomm_depart.setVisibility(View.GONE);
						} else {
							toggle_recomm_depart
									.setBackgroundDrawable(mDrawable2);
							linear_recomm_depart.setVisibility(View.VISIBLE);
						}

					}
				});
		toggle_treatment
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							toggle_treatment.setBackgroundDrawable(mDrawable1);
							tv_treatment.setVisibility(View.GONE);
						} else {
							toggle_treatment.setBackgroundDrawable(mDrawable2);
							tv_treatment.setVisibility(View.VISIBLE);
						}

					}
				});
		toggle_drug.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					toggle_drug.setBackgroundDrawable(mDrawable1);
					tv_drug.setVisibility(View.GONE);
				} else {
					toggle_drug.setBackgroundDrawable(mDrawable2);
					tv_drug.setVisibility(View.VISIBLE);
				}

			}
		});

		inner_btn_hospital_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent=new Intent();
//				intent.putExtra("flag", 0);
//				intent.setClass(SelfServiceLibraryDieseaseHome.this, MainTabActivity.class);
//				startActivity(intent);
				getData();

			}
		});

		inner_btn_online_ask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openActivity(OnLineAddConsultInfoForChart.class);

			}
		});

	}

	private void getView1() {
		//
		// private TextView tv_disease_name,tv_ddiseas`e_detail;
		// private TextView tv_pathogen,tv_symptom,tv_treatment,tv_drug;
		tv_disease_name.setText(Html.fromHtml(disease.getName() + ""));
		tv_ddisease_detail.setText(Html.fromHtml(disease.getDescript() + ""));
		tv_pathogen.setText(Html.fromHtml(disease.getPathogen() + ""));
		tv_symptom.setText(Html.fromHtml(disease.getSymptoms() + ""));
		toggle_recomm_depart.setTextOn(Html.fromHtml("建议科室:" + disease.getHdeptName()));
		toggle_recomm_depart.setTextOff(Html.fromHtml("建议科室:" + disease.getHdeptName()));
		tv_treatment.setText(Html.fromHtml(disease.getRehabilitation() + ""));
		tv_drug.setText(Html.fromHtml(disease.getMedication() + ""));

	}

	// @SuppressLint("NewApi")
	// private void iniDetailListener()
	// {
	//
	// toggle_pathogen.setOnCheckedChangeListener(new OnCheckedChangeListener()
	// {
	// Message msg=new Message();
	//
	// @Override
	// public void onCheckedChanged(CompoundButton buttonView, boolean
	// isChecked) {
	// if(isChecked)
	// {
	// toggle_pathogen.setBackgroundDrawable(mDrawable2);
	// }
	// else
	// {
	// toggle_pathogen.setBackgroundDrawable(mDrawable1);
	// }
	//
	// }
	// });
	// }

	// class MyHandler extends Handler
	// {
	// @Override
	// public void handleMessage(Message msg) {
	// if(msg.arg1==0)
	// {
	//
	// }
	// if(msg.arg1==1)
	// {
	//
	// }
	// super.handleMessage(msg);
	// }
	// }
	private void initTag() {
		mTabPager = (ViewPager) findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

		mTab1 = (TextView) findViewById(R.id.tv_third);
		mTab2 = (TextView) findViewById(R.id.tv_second);

		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		int displayHeight = currDisplay.getHeight();
		one = displayWidth / 2; // 设置水平动画平移大小
		mTabImg = new ImageView(this);
		mTabImg.setImageResource(R.drawable.navi_top_line);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				one, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		RelativeLayout main_bottom = (RelativeLayout) findViewById(R.id.main_bottom);
		main_bottom.addView(mTabImg, params);
		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi
				.inflate(R.layout.self_service_library_diesease_detail, null);
		view2 = mLi.inflate(R.layout.disease_request, null);

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
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

}
