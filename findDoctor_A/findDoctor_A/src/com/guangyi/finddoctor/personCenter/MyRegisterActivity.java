package com.guangyi.finddoctor.personCenter;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.alipay.android.msp.demo.Keys;
import com.alipay.android.msp.demo.Result;
import com.alipay.android.msp.demo.Rsa;
import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.activity.R.color;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.hospitalRegister.DoctorHomeActivity;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.imageManager.ImageManager2;
import com.guangyi.finddoctor.model.MyRegisterModel;
import com.guangyi.finddoctor.secondActivity.TabHomeActivity;
import com.guangyi.finddoctor.utils.Config;
/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:我的预约
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
public class MyRegisterActivity extends BasicActivity {
	public static final Handler MyHandler = null;
	private ViewPager cTabPager = null;
	private ImageView cTabImg;// 动画图片
	private TextView tv_myregister, tv_myconsult;
	private int zero = 0;// 动画图片偏移量
	private int currIdex = 0;// 当前页卡编号
	private int one;
	private View view1, view2;
	private Handler mHandler;
	private ApiHttpUtil mApiHttpUtil;
	private int userId = -1;
	private int flagtype = 0;
	private SharedPreferences mSharedPreferences;
	private List<MyRegisterModel> listregister, listconsult;
	private ListView lv_register, lv_consult;
	private MyConsultAdapter myConsultAdapter;
	private MyAdapter myAdapter;
	private Runnable mRunnable;
	private Thread mThread;
	private RadioGroup mRadioGroup;

	public static final String TAG = "alipay-sdk";

	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;

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
	//按下事件的监听事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			openActivity(TabHomeActivity.class, 3);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_register);
		FindDoctorApplication closeApplication = (FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initTag();

		if (flagtype == 1) {
//			 if(listconsult.size() > 0){
//			 listconsult.clear();
//			 }

			getData(2);

		} else {
			// if(listregister.size() > 0){
			// listregister.clear();
			// }
			getData(1);
		}

	}

	private void getType() {
		if (getIntent().getExtras() != null) {
			flagtype = getIntent().getExtras().getInt("flagtype");
		}

	}

	@Override
	protected void onStart() {

		// BadgeView badgeView=new BadgeView(this,mRadioGroup);
		// FindDoctorApplication application=(FindDoctorApplication)
		// getApplication();
		// badgeView.setText(application.getMsgCount()+"");
		// badgeView.show();
		super.onStart();
	}

	// @Override
	// protected void onStop() {
	//
	// unregisterReceiver(myBroadCastReceiver);
	// super.onDestroy();
	// }

	private void initParams() {
		getType();
		geSharedPreference();
		mHandler = new MyHandler();
		mApiHttpUtil = new ApiHttpUtil();
		listregister = new ArrayList<MyRegisterModel>();
		listconsult = new ArrayList<MyRegisterModel>();
	}

	private void geSharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		userId = mSharedPreferences.getInt("userId", -1);

	}

	private void initView() {
		Button mBack = (Button) findViewById(R.id.ib_back);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openActivity(TabHomeActivity.class, 3);
				finish();
			}
		});

		RadioButton mRadioButtonHome, mRadioButtonMessage, mRadioButtonUser, mRadioButtonSearch;
		mRadioGroup = (RadioGroup) findViewById(R.id.radio_group_main);
		mRadioButtonHome = (RadioButton) findViewById(R.id.radio_home);
		mRadioButtonMessage = (RadioButton) findViewById(R.id.radio_message);
		mRadioButtonUser = (RadioButton) findViewById(R.id.radio_user);
		mRadioButtonSearch = (RadioButton) findViewById(R.id.radio_search);
		mRadioButtonSearch.setChecked(true);

		mRadioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.radio_home) {
							openActivity(TabHomeActivity.class, 0);
						} else if (checkedId == R.id.radio_message) {
							openActivity(TabHomeActivity.class, 1);
						} else if (checkedId == R.id.radio_user) {
							openActivity(TabHomeActivity.class, 2);
						} else if (checkedId == R.id.radio_search) {
							openActivity(TabHomeActivity.class, 3);
						} else {
						}
					}
				});

	}
	private void delDialog(final int id, final int type, final int position) {
		new AlertDialog.Builder(MyRegisterActivity.this).setTitle("删除预约")
				.setMessage("是否删除？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						delMyRegister(id, type, position);

					}
				}).setNegativeButton("取消", null).show();
	}
	private void delMyRegister(final int id, final int type, final int position) {
		initProgressDialog();
		mRunnable = new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				if (type == 1) {
					msg.arg1 = 3;
				} else if (type == 2) {
					msg.arg1 = 4;
				}
				msg.arg2 = position;
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(id));
				list.add(String.valueOf(2));

				msg.obj = mApiHttpUtil.getMethod(
						Config.getProperty("Cancel_MY_REGISTER", ""), list);
				mHandler.sendMessage(msg);
			}
		};
		commonThreadStart();
	}

	private void initTag() {
		cTabPager = (ViewPager) findViewById(R.id.ctabpager);
		cTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
		tv_myregister = (TextView) findViewById(R.id.tv_myregister);
		tv_myconsult = (TextView) findViewById(R.id.tv_myconsult);

		// cTabImg = (ImageView) findViewById(R.id.c_img_tab);

		tv_myregister.setOnClickListener(new MyOnClickListener(0));
		tv_myconsult.setOnClickListener(new MyOnClickListener(1));
		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		int displayHeight = currDisplay.getHeight();

		one = displayWidth / 2; // 设置水平动画平移大小
		// 滑动条自动匹配屏幕宽度
		cTabImg = new ImageView(this);
		cTabImg.setImageResource(R.drawable.navi_top_line);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				one, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		RelativeLayout main_bottom = (RelativeLayout) findViewById(R.id.main_bottom);
		main_bottom.addView(cTabImg, params);

		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.mycollect_list1, null);
		view2 = mLi.inflate(R.layout.mycollect_list1, null);
		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
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
		cTabPager.setCurrentItem(flagtype);

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
				tv_myregister.setTextColor(getResources().getColor(
						color.common_green));

				// if (flagtype == 1) {
				if (listregister.size() <= 0) {
					getData(1);
				}
				// }

				if (currIdex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					tv_myconsult.setTextColor(getResources().getColor(
							color.common_grey));
				}
				break;
			case 1:

				tv_myconsult.setTextColor(getResources().getColor(
						color.common_green));
				// if (flagtype == 0) {
				if (listconsult.size() <= 0) {
					getData(2);

				}
				// }

				if (currIdex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					tv_myregister.setTextColor(getResources().getColor(
							color.common_grey));
				}
				break;
			}

			currIdex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
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

	// 获取预约挂号或电话咨询信息
	private void getData(final int type) {
		initProgressDialog();
		mRunnable = new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();

				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(userId));
				list.add(String.valueOf(type));
				msg.arg1 = type;
				msg.obj = ApiHttpUtil.getMethod(
						Config.getProperty("GET_MY_REGISTER", ""), list);

				mHandler.sendMessage(msg);

			}
		};
		commonThreadStart();
	}

	public class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			canclePregressDialog();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			Log.i("myregister", jsonString);
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
						listregister.clear();
						try {
							JSONArray jsonArr = jsonObject
									.getJSONArray("listAppointment");
							MyRegisterModel model;
							for (int i = 0; i < jsonArr.length(); i++) {
								model = new MyRegisterModel();
								model.setDoctId(jsonArr.getJSONObject(i)
										.getInt("appoDoctor"));
								model.setAppoId(jsonArr.getJSONObject(i)
										.getInt("appoId"));
								model.setAppoState(jsonArr.getJSONObject(i)
										.getInt("appoState"));
								model.setAppoType(jsonArr.getJSONObject(i)
										.getInt("appoType"));
								model.setAppoTime(jsonArr.getJSONObject(i)
										.getString("appoTime"));
								model.setAppoMan(jsonArr.getJSONObject(i)
										.getInt("appoMan"));
								model.setDoctorName(jsonArr.getJSONObject(i)
										.getString("doctorName"));
								model.setDoctorPosi(jsonArr.getJSONObject(i)
										.getString("doctorPosi"));
								model.setDoctorHospName(jsonArr
										.getJSONObject(i).getString(
												"doctorHospName"));
								model.setDoctorDepName(jsonArr.getJSONObject(i)
										.getString("doctorDepName"));
								model.setAppConsId(jsonArr.getJSONObject(i)
										.getString("appConsId"));
								model.setAppoDoctor(jsonArr.getJSONObject(i)
										.getInt("appoDoctor"));
								model.setTimeSpace(jsonArr.getJSONObject(i)
										.getString("timeSpace"));
								model.setAppoConfigTime(jsonArr
										.getJSONObject(i).getString(
												"appoConfigTime"));
								model.setEvaStatus(Integer.valueOf(jsonArr
										.getJSONObject(i)
										.getString("evaStatus")));
								model.setSpace(jsonArr.getJSONObject(i)
										.getString(MyRegisterModel.SPACE));

								model.setAttachFileByte(Config.getProperty(
										"FILEURL", "")
										+ jsonArr.getJSONObject(i).getString(
												"attachFileUrl"));
								listregister.add(model);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							getRegisterView(view1, listregister);
						}
					}

					else if (msg.arg1 == 2) {
						try {
							listconsult.clear();
							canclePregressDialog();
							JSONArray jsonArr = jsonObject
									.getJSONArray("listAppointment");
							MyRegisterModel model;

							for (int i = 0; i < jsonArr.length(); i++) {
								model = new MyRegisterModel();
								model.setDoctId(jsonArr.getJSONObject(i)
										.getInt("appoDoctor"));
								model.setAppoId(jsonArr.getJSONObject(i)
										.getInt("appoId"));
								model.setAppoState(jsonArr.getJSONObject(i)
										.getInt("appoState"));
								model.setAppoType(jsonArr.getJSONObject(i)
										.getInt("appoType"));
								model.setAppoTime(jsonArr.getJSONObject(i)
										.getString("appoTime"));
								model.setAppoMan(jsonArr.getJSONObject(i)
										.getInt("appoMan"));
								model.setDoctorName(jsonArr.getJSONObject(i)
										.getString("doctorName"));
								model.setDoctorPosi(jsonArr.getJSONObject(i)
										.getString("doctorPosi"));
								model.setDoctorHospName(jsonArr
										.getJSONObject(i).getString(
												"doctorHospName"));
								model.setDoctorDepName(jsonArr.getJSONObject(i)
										.getString("doctorDepName"));
								model.setAppConsId(jsonArr.getJSONObject(i)
										.getString("appConsId"));
								model.setAppoDoctor(jsonArr.getJSONObject(i)
										.getInt("appoDoctor"));
								model.setTimeSpace(jsonArr.getJSONObject(i)
										.getString("timeSpace"));
								model.setAppoConfigTime(jsonArr
										.getJSONObject(i).getString(
												"appoConfigTime"));
								model.setEvaStatus(Integer.valueOf(jsonArr
										.getJSONObject(i)
										.getString("evaStatus")));
								model.setSpace(jsonArr.getJSONObject(i)
										.getString(MyRegisterModel.SPACE));

								model.setOrderId(jsonArr.getJSONObject(i)
										.getString("orderId"));

								model.setMoney(jsonArr.getJSONObject(i)
										.getString("money"));

								model.setAttachFileByte(Config.getProperty(
										"FILEURL", "")
										+ jsonArr.getJSONObject(i).getString(
												"attachFileUrl"));
								listconsult.add(model);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						finally {
							getConsultView(view2, listconsult);
						}
					}

					else if (msg.arg1 == 3) {
						showToast(reason);
						// listregister.clear();
						listregister.remove(msg.arg2);
						myAdapter.notifyDataSetChanged();
						// getData(1);
					}

					else if (msg.arg1 == 4) {

						showToast(reason);

						// listconsult.clear();
						listconsult.remove(msg.arg2);
						myConsultAdapter.notifyDataSetChanged();
						// getData(2);

					}

					else if (msg.arg1 == 5) {

						showToast(reason);
						listregister.clear();
						getData(1);

					}

					else if (msg.arg1 == 6) {
						showToast(reason);
						listconsult.clear();
						getData(2);
					}

				} else {
					showToast(reason);
				}

			}

			super.handleMessage(msg);

		}
	}

	private void getRegisterView(View view, List<MyRegisterModel> list) {

		// if(lv_register==null)
		// {
		lv_register = (ListView) view.findViewById(R.id.lv_cDoctor);
		View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view,
				null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_register.getParent()).addView(emptyView);
		lv_register.setEmptyView(emptyView);
		lv_register.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent(MyRegisterActivity.this,
						DoctorHomeActivity.class);
				it.putExtra("doctId", listregister.get(arg2).getDoctId());
				startActivity(it);
			}
		});// 进入医生主页
		lv_register.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				MyRegisterModel item = (MyRegisterModel) arg0
						.getItemAtPosition(arg2);
				int state = item.getAppoState();
				Float day = Float.valueOf(item.getTimeSpace().toString());
				final int id = item.getAppoId();
				if(state==0||state==2)
				{
					if (day > 0 && day < 1) {
						Toast.makeText(MyRegisterActivity.this, "预约时间1天之内不能删除!",
								Toast.LENGTH_SHORT).show();
					}
					else if(day>=1)
					{
						Toast.makeText(MyRegisterActivity.this, "预约成功不能删除，请先取消预约!",
								Toast.LENGTH_SHORT).show();
					}
					else
					{
						delDialog(id, 1, arg2);
					}
				}
				else 
				{
					delDialog(id, 1, arg2);
				}
//				if (day > 0 && day < 1) {
//					Toast.makeText(MyRegisterActivity.this, "预约时间1天之内不能删除！",
//							Toast.LENGTH_SHORT).show();
//				} else if (day > 1 && state == 2) {
//					Toast.makeText(MyRegisterActivity.this, "预约成功不能删除，请先取消预约！",
//							Toast.LENGTH_SHORT).show();
//				} else {
//					delDialog(id, 1, arg2);
//				}

				return true;
			}
		});
		// }
		myAdapter = new MyAdapter(this, list);
		lv_register.setAdapter(myAdapter);

	}

	private void getConsultView(View view, List<MyRegisterModel> list) {

		// if(lv_consult==null)
		// {
		lv_consult = (ListView) view.findViewById(R.id.lv_cDoctor);

		View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view,
				null);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_consult.getParent()).addView(emptyView);
		lv_consult.setEmptyView(emptyView);

		lv_consult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent(MyRegisterActivity.this,
						DoctorHomeActivity.class);
				it.putExtra("doctId", listconsult.get(arg2).getDoctId());
				startActivity(it);

			}
		});
		lv_consult.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				MyRegisterModel item = (MyRegisterModel) arg0
						.getItemAtPosition(arg2);
				final int id = item.getAppoId();
				int state = item.getAppoState();
				Float day = Float.valueOf(item.getTimeSpace().toString());
				
				
//				if(state==2)
//				{
//					Toast.makeText(MyRegisterActivity.this, "电话预约成功不能删除!",
////							 Toast.LENGTH_LONG).show();
//				}
//				else 
//				{
					delDialog(id, 2, arg2);
//				}
				// if (day >= 0 && day <= 1) {
				// Toast.makeText(MyRegisterActivity.this, "预约时间1天之内不能删除",
				// Toast.LENGTH_LONG).show();
				// } else if (day > 1 && state == 2) {
				// Toast.makeText(MyRegisterActivity.this, "预约成功不能删除，请先取消预约！",
				// Toast.LENGTH_LONG).show();
				// } else {
				// delDialog(id, 2,arg2);
				// }

//				if (day > 0 && state == 2) {
//					Toast.makeText(MyRegisterActivity.this, "预约成功不能删除",
//							Toast.LENGTH_SHORT).show();
//				} else {
//					delDialog(id, 2, arg2);
//				}
				return true;
			}
		});

		myConsultAdapter = new MyConsultAdapter(this, list);
		lv_consult.setAdapter(myConsultAdapter);
		// }

	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// Intent intent = new Intent(MyRegisterActivity.this,
	// MainTabActivity.class);
	// intent.putExtra("flag", 3);
	// startActivity(intent);
	// return true;
	// }
	//
	// return super.onKeyDown(keyCode, event);
	// }

	/***
	 * 取消预约
	 * 
	 * @param appoId
	 * @param type
	 */
	private void cancelRegister(final int appoId, final int type) {
		initProgressDialog();
		mRunnable = new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				ApiHttpUtil mApiHttpUtil = new ApiHttpUtil();
				List<String> list = new ArrayList<String>();
				list.add(String.valueOf(appoId));
				list.add(String.valueOf(1));
				if (type == 1) {
					msg.arg1 = 5;
				} else if (type == 2) {
					msg.arg1 = 6;
				}

				msg.obj = mApiHttpUtil.getMethod(
						Config.getProperty("Cancel_MY_REGISTER", ""), list);
				mHandler.sendMessage(msg);

			}
		};
		commonThreadStart();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	};

	private class MyAdapter extends BaseAdapter {
		private Context mContext;
		private List<MyRegisterModel> mList;

		// private Handler myHandler = new MyRegisterActivity().MyHandler;

		public MyAdapter(Context context, List<MyRegisterModel> list) {
			this.mContext = context;
			this.mList = list;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.register_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_doctor_name = (TextView) convertView
						.findViewById(R.id.tv_doctor_name);
				viewHolder.tv_doctor_position = (TextView) convertView
						.findViewById(R.id.tv_doctor_position);
				viewHolder.tv_hospital_name = (TextView) convertView
						.findViewById(R.id.tv_hospital_name);
				viewHolder.tv_depater_name = (TextView) convertView
						.findViewById(R.id.tv_depater_name);
				viewHolder.tv_near_day = (TextView) convertView
						.findViewById(R.id.tv_near_day);
				viewHolder.tv_regiter_state = (TextView) convertView
						.findViewById(R.id.tv_register_state);
				viewHolder.tv_visit_time = (TextView) convertView
						.findViewById(R.id.tv_visit_time);
				viewHolder.btn_assess = (Button) convertView
						.findViewById(R.id.btn_assess);
				viewHolder.tv_near_text = (TextView) convertView
						.findViewById(R.id.tv_near_text);
				viewHolder.tv_time_text = (TextView) convertView
						.findViewById(R.id.tv_time_text);
				viewHolder.iv_doctor_pic = (ImageView) convertView
						.findViewById(R.id.iv_doctor_pic);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			ImageManager2.from(mContext).displayImage(viewHolder.iv_doctor_pic,
					mList.get(position).getAttachFileByte(),
					R.drawable.touxiang);
			int evaStatus = mList.get(position).getEvaStatus();
			final int appoId = mList.get(position).getAppoId();
			Float day = Float.valueOf(mList.get(position).getTimeSpace()
					.toString());
			int state = mList.get(position).getAppoState();
			String space = mList.get(position).getSpace();

			final int type = mList.get(position).getAppoType();
//			if (type == 2) {
//				viewHolder.tv_time_text.setText("通话时间：");
//			}
			
			
			
			
//			viewHolder.tv_time_text.setText("：");

			String registerData = mList.get(position).getAppoTime()
					.substring(0, 10)
					+ " " + mList.get(position).getAppoConfigTime();
			
			viewHolder.tv_near_day.setText(space);
			viewHolder.tv_doctor_name.setText(mList.get(position)
					.getDoctorName());
			viewHolder.tv_doctor_position.setText(mList.get(position)
					.getDoctorPosi());
			viewHolder.tv_hospital_name.setText(mList.get(position)
					.getDoctorHospName());
			viewHolder.tv_depater_name.setText(mList.get(position)
					.getDoctorDepName());
			viewHolder.tv_visit_time.setText(registerData
					.replaceAll(", ", "\n"));

			viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
			viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
			viewHolder.btn_assess.setVisibility(View.INVISIBLE);

			switch (state) {
			
			// 未付款
			case 0:
				if (day <= 0) {
					viewHolder.tv_regiter_state.setText("已诊断");
					viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
					viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
					viewHolder.btn_assess.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setText("评价");
					viewHolder.btn_assess
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(mContext,
											AssessActivity.class);
									intent.putExtra("flagType", 0);
									intent.putExtra("assessType", 2);
									intent.putExtra("appoId",
											mList.get(position).getAppoId());
									intent.putExtra("docId", mList
											.get(position).getAppoDoctor());

									mContext.startActivity(intent);

								}
							});
				} else if (0 < day && day < 1) {
					viewHolder.tv_regiter_state.setText("预约成功");
					viewHolder.tv_near_text.setVisibility(View.VISIBLE);
					viewHolder.tv_near_day.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setVisibility(View.INVISIBLE);
				} else {
					viewHolder.tv_regiter_state.setText("预约成功");
					viewHolder.tv_near_text.setVisibility(View.VISIBLE);
					viewHolder.tv_near_day.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setText("取消预约");
					viewHolder.btn_assess
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									new AlertDialog.Builder(mContext)
											.setMessage("是否要取消预约")
											.setPositiveButton(
													"确定",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															cancelRegister(
																	appoId,
																	type);
														}
													})
											.setNegativeButton("取消", null)
											.create().show();

								}
							});
				}
				break;

			// 开始预约，App上不会出现这种情况
			case 1:
				viewHolder.tv_regiter_state.setText("开始预约");
				viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
				viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
				viewHolder.btn_assess.setVisibility(View.INVISIBLE);
				break;

			// 预约成功
			case 2:
				if (day <= 0) {
					viewHolder.tv_regiter_state.setText("已诊断");
					viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
					viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
					viewHolder.btn_assess.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setText("评价");
					viewHolder.btn_assess
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(mContext,
											AssessActivity.class);
									intent.putExtra("flagType", 0);
									intent.putExtra("assessType", 2);
									intent.putExtra("appoId",
											mList.get(position).getAppoId());
									intent.putExtra("docId", mList
											.get(position).getAppoDoctor());

									mContext.startActivity(intent);

								}
							});
				} else if (0 < day && day < 1) {
					viewHolder.tv_regiter_state.setText("预约成功");
					viewHolder.tv_near_text.setVisibility(View.VISIBLE);
					viewHolder.tv_near_day.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setVisibility(View.INVISIBLE);
				} else {
					viewHolder.tv_regiter_state.setText("预约成功");
					viewHolder.tv_near_text.setVisibility(View.VISIBLE);
					viewHolder.tv_near_day.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setText("取消预约");
					viewHolder.btn_assess
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									new AlertDialog.Builder(mContext)
											.setMessage("是否要取消预约")
											.setPositiveButton(
													"确定",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															cancelRegister(
																	appoId,
																	type);
														}
													})
											.setNegativeButton("取消", null)
											.create().show();

								}
							});
				}
				break;

			// 取消预约
			case 3:
				viewHolder.tv_regiter_state.setText("取消预约");
				viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
				viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
				viewHolder.btn_assess.setVisibility(View.INVISIBLE);
				break;

			case 4:
//				viewHolder.tv_regiter_state.setText("已诊断");
//				viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
//				viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
//				viewHolder.btn_assess.setVisibility(View.VISIBLE);
//				if (evaStatus != 0) {
					viewHolder.tv_regiter_state.setText("已评价");
					viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
					viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
					viewHolder.btn_assess.setVisibility(View.INVISIBLE);
//				}
				break;
			default:
				break;
			}

			return convertView;
		}

		class ViewHolder {
			TextView tv_doctor_name, tv_doctor_position, tv_hospital_name,
					tv_depater_name, tv_near_day, tv_regiter_state,
					tv_visit_time, tv_near_text, tv_time_text;
			Button btn_assess;
			ImageView iv_doctor_pic;
		}

	}

	private class MyConsultAdapter extends BaseAdapter {
		private Context mContext;
		private List<MyRegisterModel> mList;

		// private Handler myHandler = new MyRegisterActivity().MyHandler;
		public MyConsultAdapter(Context context, List<MyRegisterModel> list) {
			this.mContext = context;
			this.mList = list;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.register_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_doctor_name = (TextView) convertView
						.findViewById(R.id.tv_doctor_name);
				viewHolder.tv_doctor_position = (TextView) convertView
						.findViewById(R.id.tv_doctor_position);
				viewHolder.tv_hospital_name = (TextView) convertView
						.findViewById(R.id.tv_hospital_name);
				viewHolder.tv_depater_name = (TextView) convertView
						.findViewById(R.id.tv_depater_name);
				viewHolder.tv_near_day = (TextView) convertView
						.findViewById(R.id.tv_near_day);
				viewHolder.tv_regiter_state = (TextView) convertView
						.findViewById(R.id.tv_register_state);
				viewHolder.tv_visit_time = (TextView) convertView
						.findViewById(R.id.tv_visit_time);
				viewHolder.btn_assess = (Button) convertView
						.findViewById(R.id.btn_assess);
				viewHolder.tv_near_text = (TextView) convertView
						.findViewById(R.id.tv_near_text);
				// viewHolder.tv_day_text = (TextView) convertView
				// .findViewById(R.id.tv_day_text);
				viewHolder.tv_time_text = (TextView) convertView
						.findViewById(R.id.tv_time_text);
				viewHolder.iv_doctor_pic = (ImageView) convertView
						.findViewById(R.id.iv_doctor_pic);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			ImageManager2.from(mContext).displayImage(viewHolder.iv_doctor_pic,
					mList.get(position).getAttachFileByte(),
					R.drawable.touxiang);
			int evaStatus = mList.get(position).getEvaStatus();
			final int appoId = mList.get(position).getAppoId();
			Float day = Float.valueOf(mList.get(position).getTimeSpace()
					.toString());
			String space = mList.get(position).getSpace();

			final int type = mList.get(position).getAppoType();

			int state = mList.get(position).getAppoState();
			String registerData = mList.get(position).getAppoTime()
					.substring(0, 10)
					+ " " + mList.get(position).getAppoConfigTime();

			viewHolder.tv_near_day.setText(space);
			viewHolder.tv_time_text.setText("通话时间：");
			viewHolder.tv_doctor_name.setText(mList.get(position)
					.getDoctorName());
			viewHolder.tv_doctor_position.setText(mList.get(position)
					.getDoctorPosi());
			viewHolder.tv_hospital_name.setText(mList.get(position)
					.getDoctorHospName());
			viewHolder.tv_depater_name.setText(mList.get(position)
					.getDoctorDepName());
			viewHolder.tv_visit_time.setText(registerData
					.replaceAll(", ", "\n"));
			viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
			viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
			viewHolder.btn_assess.setVisibility(View.INVISIBLE);

			switch (state) {
			// 未付款
			case 0:
				if (day <= 0) {
					viewHolder.tv_regiter_state.setText("已过期");
					viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
					viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
					viewHolder.btn_assess.setVisibility(View.INVISIBLE);
				} else {
					viewHolder.tv_regiter_state.setText("未付款");
					viewHolder.tv_near_text.setVisibility(View.VISIBLE);
					viewHolder.tv_near_day.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setText("去付款");
					viewHolder.btn_assess
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									toPay(mList.get(position).getOrderId(),
											mList.get(position).getMoney());
								}
							});
				}
				break;

			// 开始预约，App上不会出现这种情况
			case 1:
				viewHolder.tv_regiter_state.setText("开始预约");
				viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
				viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
				viewHolder.btn_assess.setVisibility(View.INVISIBLE);
				break;

			// 预约成功
			case 2:
				if (day <= 0) {
					viewHolder.tv_regiter_state.setText("已完成");
					viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
					viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
					viewHolder.btn_assess.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setText("评价");
					viewHolder.btn_assess
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(mContext,
											AssessActivity.class);
									intent.putExtra("flagType", 1);
									intent.putExtra("assessType", 3);
									intent.putExtra("appoId",
											mList.get(position).getAppoId());
									intent.putExtra("docId", mList
											.get(position).getAppoDoctor());

									mContext.startActivity(intent);

								}
							});

				} else if (0 < day && day < 1) {
					viewHolder.tv_regiter_state.setText("预约成功");
					viewHolder.tv_near_text.setVisibility(View.VISIBLE);
					viewHolder.tv_near_day.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setVisibility(View.INVISIBLE);
				} else {
					viewHolder.tv_regiter_state.setText("预约成功");
					viewHolder.tv_near_text.setVisibility(View.VISIBLE);
					viewHolder.tv_near_day.setVisibility(View.VISIBLE);
					viewHolder.btn_assess.setVisibility(View.INVISIBLE);
					// viewHolder.btn_assess.setText("取消预约");
				}
				break;

			// 取消预约
			case 3:
				viewHolder.tv_regiter_state.setText("取消预约");
				viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
				viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
				viewHolder.btn_assess.setVisibility(View.INVISIBLE);
				break;

			case 4:
//				viewHolder.tv_regiter_state.setText("已完成");
//				viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
//				viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
//				viewHolder.btn_assess.setVisibility(View.INVISIBLE);
//				if (evaStatus != 0) {
					viewHolder.tv_regiter_state.setText("已评价");
					viewHolder.tv_near_text.setVisibility(View.INVISIBLE);
					viewHolder.tv_near_day.setVisibility(View.INVISIBLE);
					viewHolder.btn_assess.setVisibility(View.INVISIBLE);
//				}
				break;
			default:
				break;
			}

			return convertView;
		}

		class ViewHolder {
			TextView tv_doctor_name, tv_doctor_position, tv_hospital_name,
					tv_depater_name, tv_near_day, tv_regiter_state,
					tv_visit_time, tv_near_text, tv_time_text;
			Button btn_assess;
			ImageView iv_doctor_pic;
		}

	}
	// 支付宝操作
	private void toPay(String id, String money) {
		try {
			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo(id, money);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i(TAG, "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(MyRegisterActivity.this,
							payHanlder);
					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);
					String result = alipay.pay(orderInfo);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					payHanlder.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(MyRegisterActivity.this,
					R.string.remote_call_failed, Toast.LENGTH_SHORT).show();
		}
	}

	private String getNewOrderInfo(String id, String money) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(id);
		sb.append("\"&subject=\"");
		sb.append("电话咨询");
		sb.append("\"&body=\"");
		sb.append("电话咨询");
		sb.append("\"&total_fee=\"");
		sb.append(money);
		sb.append("\"&notify_url=\"");
		sb.append(URLEncoder.encode(Config.getProperty("GETALIRESULT", "")));

		// 网址需要做URL编码
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	// private String getOutTradeNo() {
	// SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
	// Date date = new Date();
	// String key = format.format(date);
	//
	// java.util.Random r = new java.util.Random();
	// key += r.nextInt();
	// key = key.substring(0, 15);
	// Log.d(TAG, "outTradeNo: " + key);
	// return key;
	// }

	private String getSignType() {
		return "sign_type=\"RSA\"";

	}

	Handler payHanlder = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.obj != null && msg.obj.toString().length() > 0) {
				Result result = new Result((String) msg.obj);
				switch (msg.what) {
				case RQF_PAY:
					Log.w("pay resul", result.getResult().trim() + "test");
					if (result.getResult().trim().length() == 0) {

						if (listconsult.size() > 0) {
							listconsult.clear();
							getData(2);
						}
					}
					else if (result.getResult().trim().length() > 0) {
						Toast.makeText(MyRegisterActivity.this,
								result.getResult(), Toast.LENGTH_SHORT).show();
					}
				case RQF_LOGIN: {
					if (result.getResult().trim().length() != 0) {
						Toast.makeText(MyRegisterActivity.this,
								result.getResult(), Toast.LENGTH_SHORT).show();
					}
				}
					break;
				default:
					break;
				}
			}
		};
	};

}
