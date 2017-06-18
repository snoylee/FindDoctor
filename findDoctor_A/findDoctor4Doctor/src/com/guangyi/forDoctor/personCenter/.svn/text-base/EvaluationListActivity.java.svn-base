package com.guangyi.forDoctor.personCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
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

import com.guangyi.forDoctor.activity.BasicActivity;
import com.guangyi.forDoctor.activity.R;
import com.guangyi.forDoctor.activity.R.color;
import com.guangyi.forDoctor.activity.SysApplication;
import com.guangyi.forDoctor.adapter.EvaluateListAdapter;
import com.guangyi.forDoctor.config.Config;
import com.guangyi.forDoctor.http.ApiHttpUtil;
import com.guangyi.forDoctor.model.Doctor;
import com.guangyi.forDoctor.model.Evaluate;
import com.guangyi.forDoctor.onlineConsult.OnLineMainActivity;
import com.guangyi.forDoctor.reflashView.PullToRefreshView;
import com.guangyi.forDoctor.reflashView.PullToRefreshView.OnFooterRefreshListener;
import com.guangyi.forDoctor.reflashView.PullToRefreshView.OnHeaderRefreshListener;
import com.guangyi.forDoctor.utils.DateTools;
public class EvaluationListActivity extends BasicActivity {

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
	private ApiHttpUtil mApiHttpUtil;
	private Handler mHandler;
	private int PageNo1 = 1, PageNo2 = 1, PageNo3 = 1;
	private int pageSize=4;
	private List<Evaluate> list1, list2, list3;
	private EvaluateListAdapter adapter1,adapter2, adapter3;
	private ListView listView1, listView2, listView3;
	private PullToRefreshView mPullToRefreshView1,mPullToRefreshView2,mPullToRefreshView3;
	private String doctId;
//	private type  2预约挂号		4在线咨询  3电话咨询 
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
		setContentView(R.layout.evaluate_main);
		SysApplication.getInstance().addActivity(this);
		initParams();
		initView();
		iniListener();
		initTag();
		initView1(view1);
		initView2(view2);
		initView3(view3);
		
		initProgressDialog();
		getData(1, PageNo1, pageSize, 2);

	}

	@Override
	protected void onStart() {
//			initProgressDialog();
//			PageNo1=1;
//			PageNo2=1;
//			PageNo3=1;
//			list1.clear();
//			list2.clear();
//			list3.clear();
//			adapter1.notifyDataSetChanged();
//			adapter2.notifyDataSetChanged();
//			adapter3.notifyDataSetChanged();
//			getData(1, PageNo1, pageSize, 2);
//			getData(2, PageNo2, pageSize, 4);
//			getData(3, PageNo3, pageSize, 3);
			super.onStart();

	}

	@SuppressLint("HandlerLeak")
	private void initParams() {
		doctId=getCustomSharedPreference().getString(Doctor.DOCTID, "");
		mApiHttpUtil = new ApiHttpUtil();
		list1 = new ArrayList<Evaluate>();
		list2 = new ArrayList<Evaluate>();
		list3 = new ArrayList<Evaluate>();
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
									.getJSONArray("evaluate");
							Evaluate evaluate;
							switch (msg.arg1) {

							case 1:
								PageNo1 += 1;
								for (int m = 0; m < jsonArray.length(); m++) {

									evaluate = new Evaluate();
									evaluate.setEfficiency(jsonArray
											.getJSONObject(m).getInt(

													Evaluate.EFFICIENCY));
									evaluate.setEvaluateContext(jsonArray
											.getJSONObject(m).getString(

													Evaluate.EVALUATECONTEXT));
									evaluate.setEvaluateId(jsonArray
											.getJSONObject(m).getString(

													Evaluate.EVALUATEID));
									evaluate.setEvaluateTime(jsonArray
											.getJSONObject(m).getString(
													Evaluate.EVALUATETIME));
									evaluate.setService(jsonArray
											.getJSONObject(m).getInt(
													Evaluate.SERVICE));
									evaluate.setSkill(jsonArray
											.getJSONObject(m).getInt(
													Evaluate.SKILL));
									evaluate.setConsProblem(jsonArray
											.getJSONObject(m).getString(
													Evaluate.CONSPROBLEM));

									list1.add(evaluate);

								}
								break;
							case 2:
								PageNo2 += 1;
								for (int m = 0; m < jsonArray.length(); m++) {
									evaluate = new Evaluate();
									evaluate.setEfficiency(jsonArray
											.getJSONObject(m).getInt(
													Evaluate.EFFICIENCY));
									evaluate.setEvaluateContext(jsonArray
											.getJSONObject(m).getString(
													Evaluate.EVALUATECONTEXT));
									evaluate.setEvaluateId(jsonArray
											.getJSONObject(m).getString(
													Evaluate.EVALUATEID));
									evaluate.setEvaluateTime(jsonArray
											.getJSONObject(m).getString(
													Evaluate.EVALUATETIME));
									evaluate.setService(jsonArray
											.getJSONObject(m).getInt(
													Evaluate.SERVICE));
									evaluate.setSkill(jsonArray
											.getJSONObject(m).getInt(
													Evaluate.SKILL));
									evaluate.setConsProblem(jsonArray
											.getJSONObject(m).getString(
													Evaluate.CONSPROBLEM));
									evaluate.setConsId(jsonArray
											.getJSONObject(m).getInt(
													Evaluate.CONSID));
									list2.add(evaluate);

								}
								break;

							case 3:
								PageNo3 += 1;
								for (int m = 0; m < jsonArray.length(); m++) {

									evaluate = new Evaluate();
									evaluate.setEfficiency(jsonArray
											.getJSONObject(m).getInt(

													Evaluate.EFFICIENCY));
									evaluate.setEvaluateContext(jsonArray
											.getJSONObject(m).getString(

													Evaluate.EVALUATECONTEXT));
									evaluate.setEvaluateId(jsonArray
											.getJSONObject(m).getString(

													Evaluate.EVALUATEID));
									evaluate.setEvaluateTime(jsonArray
											.getJSONObject(m).getString(
													Evaluate.EVALUATETIME));
									evaluate.setService(jsonArray
											.getJSONObject(m).getInt(
													Evaluate.SERVICE));
									evaluate.setSkill(jsonArray
											.getJSONObject(m).getInt(
													Evaluate.SKILL));
									evaluate.setConsProblem(jsonArray
											.getJSONObject(m).getString(
													Evaluate.CONSPROBLEM));

									list3.add(evaluate);

								}
								break;
							}
						} else {
							showToast(jsonObject.getString("reason"));

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					finally
					{
						switch (msg.arg1) {
						case 1:
							if(list1.size()>0)
							{
								mPullToRefreshView1.openFootView();
							}
							else
							{
								mPullToRefreshView1.closeFootView();
							}
							
							View emptyView1 = LayoutInflater.from(EvaluationListActivity.this).inflate(R.layout.empty_view, null);
							emptyView1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
							emptyView1.setVisibility(View.GONE);   
							((ViewGroup)listView1.getParent()).addView(emptyView1);   
							listView1.setEmptyView(emptyView1);
							adapter1.notifyDataSetChanged();
							break;
						case 2:
							if(list2.size()>0)
							{
								mPullToRefreshView2.openFootView();
							}
							else
							{
								mPullToRefreshView2.closeFootView();
							}
							
							View emptyView2 = LayoutInflater.from(EvaluationListActivity.this).inflate(R.layout.empty_view, null);
							emptyView2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
							emptyView2.setVisibility(View.GONE);   
							((ViewGroup)listView2.getParent()).addView(emptyView2);   
							listView2.setEmptyView(emptyView2);
							adapter2.notifyDataSetChanged();
							break;

						case 3:
							if(list3.size()>0)
							{
								mPullToRefreshView3.openFootView();
							}
							else
							{
								mPullToRefreshView3.closeFootView();
							}
							
							View emptyView3 = LayoutInflater.from(EvaluationListActivity.this).inflate(R.layout.empty_view, null);
							emptyView3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
							emptyView3.setVisibility(View.GONE);   
							((ViewGroup)listView3.getParent()).addView(emptyView3);   
							listView3.setEmptyView(emptyView3);
							 adapter3.notifyDataSetChanged();
							break;
						}
					}
				}

				super.handleMessage(msg);
			}
		};
	}

	private void initView() {
		mBack = (Button) findViewById(R.id.ib_back);
	}

	private void iniListener() {
	mBack = (Button) findViewById(R.id.ib_back);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}
	
	
	


	
	
	private void initView1(View view) {
		mPullToRefreshView1 = (PullToRefreshView)view.findViewById(R.id.main_pull_refresh_view);
      mPullToRefreshView1.setOnHeaderRefreshListener(new OnHeaderRefresh1());
      mPullToRefreshView1.setOnFooterRefreshListener(new OnFooterRefresh1());
		listView1= (ListView) view.findViewById(R.id.mlist);
		adapter1 = new EvaluateListAdapter(this, list1);
		listView1.setAdapter(adapter1);
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent();
				it.putExtra("evaluate", list1.get(arg2));
				it.setClass(EvaluationListActivity.this, EvaluationDetail.class);
				startActivity(it);

			}
		});
		
	}

	private void initView2(View view) {
		mPullToRefreshView2 = (PullToRefreshView)view.findViewById(R.id.main_pull_refresh_view);
mPullToRefreshView2.setOnHeaderRefreshListener(new OnHeaderRefresh2());
mPullToRefreshView2.setOnFooterRefreshListener(new OnFooterRefresh2());
		listView2= (ListView) view.findViewById(R.id.mlist);
		adapter2 = new EvaluateListAdapter(this, list2);
		listView2.setAdapter(adapter2);
		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent();
				it.putExtra("evaluate", list2.get(arg2));
				it.setClass(EvaluationListActivity.this, EvaluationDetail.class);
				startActivity(it);

			}
		});
		
		
		
		
		
	}

	private void initView3(View view) {
		mPullToRefreshView3 = (PullToRefreshView)view.findViewById(R.id.main_pull_refresh_view);
mPullToRefreshView3.setOnHeaderRefreshListener(new OnHeaderRefresh3());
mPullToRefreshView3.setOnFooterRefreshListener(new OnFooterRefresh3());
		listView3= (ListView) view.findViewById(R.id.mlist);
		adapter3 = new EvaluateListAdapter(this, list3);
		listView3.setAdapter(adapter3);
		listView3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent();
		it.putExtra("evaluate", list3.get(arg2));
				it.setClass(EvaluationListActivity.this, EvaluationDetail.class);
				startActivity(it);

			}
		});
		
		
		


	}

	

private void onLoaded() {
		mPullToRefreshView1.onHeaderRefreshComplete(getResources().getString(R.string.pull_to_refresh_pull_last_time)+DateTools.getCurrentDateString("MM-dd HH:mm:ss"));
		mPullToRefreshView1.onFooterRefreshComplete();
		mPullToRefreshView2.onHeaderRefreshComplete(getResources().getString(R.string.pull_to_refresh_pull_last_time)+DateTools.getCurrentDateString("MM-dd HH:mm:ss"));
		mPullToRefreshView2.onFooterRefreshComplete();
		mPullToRefreshView3.onHeaderRefreshComplete(getResources().getString(R.string.pull_to_refresh_pull_last_time)+DateTools.getCurrentDateString("MM-dd HH:mm:ss"));
		mPullToRefreshView3.onFooterRefreshComplete();
	}


	class OnHeaderRefresh1 implements OnHeaderRefreshListener
	{
	

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			getData(1, PageNo1, pageSize, 2);
			
		}
		
	}
	
	class OnFooterRefresh1 implements OnFooterRefreshListener
	{

		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			getData(1, PageNo1, pageSize, 2);
			
		}
		
	}
	
	
	class OnHeaderRefresh2 implements OnHeaderRefreshListener
	{
	

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			getData(2, PageNo2, pageSize, 4);
			
		}
		
	}
	
	class OnFooterRefresh2 implements OnFooterRefreshListener
	{

		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			getData(2, PageNo2, pageSize, 4);
		    
			
		}
		
	}
	
	
	
	class OnHeaderRefresh3 implements OnHeaderRefreshListener
	{
	

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			getData(3, PageNo3, pageSize, 3);
		}
		
	}
	
	class OnFooterRefresh3 implements OnFooterRefreshListener
	{

		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			getData(3, PageNo3, pageSize, 3);
			
		}
		
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
		view1 = mLi.inflate(R.layout.online_main_list, null);
		view2 = mLi.inflate(R.layout.online_main_list, null);
		view3 = mLi.inflate(R.layout.online_main_list, null);

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

			// @Override
			// public CharSequence getPageTitle(int position) {
			// return titles.get(position);
			// }

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
					initProgressDialog();
					getData(1, PageNo1, pageSize, 2);
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
					initProgressDialog();
					getData(2, PageNo2, pageSize, 4);
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
					initProgressDialog();
					getData(3, PageNo3, pageSize, 3);
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

	private void getData(final int arg1, final int pageNo, final int pageSize,
			final int type) {
		mRunnable=new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("doctId", doctId);
				params.put("pageSize", String.valueOf(pageSize));
				params.put("pageNo", String.valueOf(pageNo));
				params.put("type", String.valueOf(type));
				msg.arg1 = arg1;
				msg.obj = mApiHttpUtil.postMethod(
						Config.getProperty("DOCTOREVALUATE", ""),
						params);
				mHandler.sendMessage(msg);

			}
		};
		commonThreadStart();
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	    	finish();
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
