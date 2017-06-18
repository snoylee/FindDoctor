package com.guangyi.finddoctor.activity;

import java.net.SocketException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.push.example.Utils;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.checkvision.UpdateManager;
import com.guangyi.finddoctor.custview.BadgeView;
import com.guangyi.finddoctor.custview.FocusGallery;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.Banner;
import com.guangyi.finddoctor.secondActivity.SselfService;
import com.guangyi.finddoctor.secondActivity.TabHomeActivity;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.StateInfos;
/**
 * <p>
 * Title: 网络医院运营支撑平台-APP个人版
 * </p>
 * <p>
 * Description:主页面
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:中国移动有限公司东莞分公司
 * </p>
 * @author：<a href=”mailto:jomin5120@sina.com”>jomin5120@sina.com</a>
 * @version：1.0
 * @since：2013-9-23
 */
public class MainActivity extends BasicActivity implements AMapLocationListener {
	private FocusGallery mGallery;
	private TextView mTitleText;
	private com.guangyi.finddoctor.adapter.FocusAdapter mFocusAdapter;
	private ImageView mFocusPointImage;
	private EditText editText1;
	private ImageView iv_search_btn;
	private long exitTime = 0;
	private Button iv_hospital_register_picgrid_item, iv_online_ask_pic,
			iv_phone_ask, iv_person_center_pic;
	private Handler handler;
	private SharedPreferences mSharedPreferences;
	private LocationManagerProxy mAMapLocManager = null;
	private boolean isLocated = false;
	private int gpsState;
	private BadgeView badgeView;
	private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initMySharedPreference();
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initListener();
		mHandler=new MyHandler();
		handler = new Handler() {
			boolean right = true;
			public void handleMessage(Message msg) {
				// mGallery.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,
				// R.anim.push_left_in));
				// mGallery.setSelection((msg.arg1+1)%5);

				if (msg.arg1 == 1) {
					right = false;
				}
				if (msg.arg1 == 0) {
					right = true;
				}
				if (right) {
					mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
				} else {
					mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
				}

				handler.sendMessageDelayed(new Message(), 4000);
				// FrameLayout
			};
		};

		mAMapLocManager = LocationManagerProxy.getInstance(this);
		 mAMapLocManager.setGpsEnable(false);
		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
		 * API定位采用GPS和网络混合定位方式
		 * ，第一个参数是定位provider，第二个参数时间最短是5000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		 */
		mAMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);

	}
	private void checkVersion()
	{
		FindDoctorApplication application=(FindDoctorApplication) getApplication();
		if(application.getIsCheckUpdate()==0)
		{
			SharedPreferences mSharedPreferences = getSharedPreferences("personCenter",
					Context.MODE_PRIVATE); 
		UpdateManager manager = new UpdateManager(this);
//		// 检查软件更新
		HashMap<String, String> hashMap=new HashMap<String, String>();
		hashMap.put("versionCode", mSharedPreferences.getInt("versionCode", 0)+"");
		hashMap.put("versionName", mSharedPreferences.getString("versionName", ""));
		hashMap.put("versionUrl", mSharedPreferences.getString("versionUrl", ""));
		hashMap.put("updateLog", mSharedPreferences.getString("updateLog", ""));
		manager.checkUpdate(hashMap);
		}
	}

	private void initGpsState() {
		// if (mSharedPreferences.getBoolean("isFirstStart", false)) {
		initMySharedPreference();
		final Editor editor = mSharedPreferences.edit();

		LocationManager mLocationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// 判断GPS模块是否开启，如果没有则开启
		if (!mLocationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			// 转到手机设置界面，用户设置GPS
			// Intent intent=new Intent(Settings.ACTION_SECURITY_SETTINGS);
			// startActivityForResult(intent,0); //设置完成后返回到原来的界面
			new AlertDialog.Builder(this)
					.setMessage("如需获取更精确的位置服务，请您打开GPS设置")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									editor.putInt("gspState", 0);
									editor.commit();
									// 0 关闭 1打开
								}
							})
					.setPositiveButton("设置",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									editor.putInt("gspSetting", 0);
									// 0开始设置 1设置成功 2设置失败
									editor.commit();
									Intent intent = new Intent();
									intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									startActivity(intent);
								}
							}).show();
		}
		if (mSharedPreferences.getInt("gspSetting", -1) == 0
				&& mLocationManager
						.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			editor.putInt("gspSetting", 1);
			editor.putInt("gspState", 1);
			editor.commit();
		}

		if (mSharedPreferences.getInt("gspSetting", -1) == 0
				&& !mLocationManager
						.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			editor.putInt("gspSetting", 2);
			editor.commit();
		}
	}
	@Override
	protected void onStart() {
		//推送从这里开始的
		 PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_API_KEY, 
					Utils.getMetaValue(MainActivity.this, "api_key"));
		try {
			new StateInfos(MainActivity.this,"首页",1).postStateInfo();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initMySharedPreference();
		Log.i("gpsState", gpsState+"gpstate");
		if (gpsState ==-1) {
			initGpsState();
		}
		int count = mSharedPreferences.getInt("msgCount", -1);
		
		badgeView.setText(count + "");
		if (count > 0) {
			badgeView.show();
		} else {
			badgeView.hide();
		}
		int userId=getSharedPreferences("personCenter",Context.MODE_PRIVATE).getInt("userId", -1);
		if(userId>-1)
		{
			getData(userId);
		}
		
//		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
//		closeApplication.closeOtherActivity(this);
		
		checkVersion();
		initBanner();
		Message msg = new Message();
		msg.what = 0;
		msg.arg1 = 0;
		handler.sendMessageDelayed(msg, 4000);
		try {
			new StateInfos(MainActivity.this, "", 0).getPicture();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		
//		if(checkBrowser("com.guangyi.finddoctor.activity"))
//		{
//			
//			 dialog = new AlertDialog.Builder(this)
//	           .setTitle("提示")
//	           .setMessage("发现找医生存在旧版本，请卸载旧版本")
//	          .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//	           public void onClick(DialogInterface dialog,int whichButton){
//	        	   dialog.cancel();
//	        	   uninstallAPK("com.guangyi.finddoctor.activity");
//	                   
//	            }
//	          }).create();
//	          dialog.show();
//			
//		}
		super.onStart();

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
		}
	}

	@Override
	protected void onStop() {
		handler.removeMessages(0);
//		if(dialog!=null)
//		{
//			dialog.cancel();
//		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
			mAMapLocManager.destory();
		}
		mAMapLocManager = null;
		super.onDestroy();
	}

	// 初始化view
	private void initView() {
		editText1 = (EditText) findViewById(R.id.editText1);
		iv_search_btn = (ImageView) findViewById(R.id.iv_search_btn);
		iv_hospital_register_picgrid_item = (Button) findViewById(R.id.iv_hospital_register_picgrid_item);
		iv_online_ask_pic = (Button) findViewById(R.id.iv_online_ask_pic);
		iv_phone_ask = (Button) findViewById(R.id.iv_phone_ask);
		iv_person_center_pic = (Button) findViewById(R.id.iv_person_center_pic);
		badgeView = new BadgeView(this, iv_person_center_pic);
	}

	private void initListener() {
		iv_search_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
				//得到InputMethodManager的实例 
				if (imm.isActive()) { 
				//如果开启 
				imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS); 
				//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的 
				} 
				
				if (editText1.getText().toString().trim().length() > 0) {
					if(isNetworkConnected())
					{
					Intent it = new Intent();
					it.putExtra("word", editText1.getText().toString().trim());
					it.setClass(MainActivity.this, MainSearchActivity.class);
					startActivity(it);
					}
				} else {
					showToast("搜索内容不能为空!");
				}
			}
		});

		iv_hospital_register_picgrid_item
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// openActivity(ShospitalRegister.class);
						// finish();
						openActivity(TabHomeActivity.class, 0);
						// ActivityController.startView(new Intent(
						// ActivityController.hospitalRegister));

					}
				});

		iv_online_ask_pic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// openActivity(SonlineAsk.class);
				// finish();
				openActivity(TabHomeActivity.class, 1);
				// ActivityController.startView(new Intent(
				// ActivityController.onlineAsk));

			}
		});

		iv_phone_ask.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// openActivity(SselfService.class);
				// finish();
				// ActivityController.startView(new Intent(
				// ActivityController.selfService));
				openActivity(TabHomeActivity.class, 2);

			}
		});

		iv_person_center_pic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// openActivity(SpersonCenter.class);
				// finish();
				// ActivityController.startView(new Intent(
				// ActivityController.personCenter));
				openActivity(TabHomeActivity.class, 3);
			}
		});
	}
	private void initMySharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		gpsState = mSharedPreferences.getInt("gspState", -1);
	}
	private void changeBannerBg(int position) {
		mTitleText.setText(mFocusAdapter.getItem(position).getTitle());
		// mContentText.setText(map.get("content").toString());
		switch (position) {
		case 0:
			mFocusPointImage.setBackgroundResource(R.drawable.focus_point_1);
			break;
		case 1:
			mFocusPointImage.setBackgroundResource(R.drawable.focus_point_2);
			break;
//		case 2:
//			mFocusPointImage.setBackgroundResource(R.drawable.focus_point_3);
//			break;
//		case 3:
//			mFocusPointImage.setBackgroundResource(R.drawable.focus_point_4);
//			break;
		// case 4:
		// mFocusPointImage.setBackgroundResource(R.drawable.focus_point_5);
		// break;
		}
	}

	private void initBanner() {
		mGallery = (FocusGallery) findViewById(R.id.focusGallery);
		mTitleText = (TextView) findViewById(R.id.titleText);
		mFocusPointImage = (ImageView) findViewById(R.id.focusPointImage);
		final List<Banner> list = getObjectInfo();
		mFocusAdapter = new com.guangyi.finddoctor.adapter.FocusAdapter(this,
				list, mGallery);
		mGallery.setAdapter(mFocusAdapter);
		mGallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String id = list.get(arg2).getId();
				Log.i("id", id + "id test");
				if (id != "-1") {
					Intent it = new Intent();
					it.putExtra("id", id);
					it.setClass(MainActivity.this, BannerDetail.class);
					startActivity(it);
				}
			}
		});
		 mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
		 @Override
		 public void onItemSelected(AdapterView<?> arg0, View arg1,
		 int position, long arg3) {
		 // i=position;
		 Log.i("onItemSelected", position + "");
		 changeBannerBg(position);
		 handler.removeMessages(0);
		 Message msg = new Message();
		 msg.arg1 = position;
		 handler.sendMessageDelayed(msg, 4000);
		
		 }
		
		 @Override
		 public void onNothingSelected(AdapterView<?> arg0) {
		
		 }
		 });
		 
		 if(list.size()>0)
		 {
		 mTitleText.setText(mFocusAdapter.getItem(0).getTitle());
		 }
			// mContentText
			// .setText(mFocusAdapter.getItem(0).get("content").toString());
		 
		 
		// mGallery.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_DOWN) {
		// handler.removeMessages(0);
		// } else if (event.getAction() == MotionEvent.ACTION_UP) {
		// Message msg = new Message();
		// msg.arg1 = mGallery.getSelectedItemPosition();
		// handler.sendMessageDelayed(msg, 4000);
		//
		//
		// String id=list.get(mGallery.getSelectedItemPosition()).getId();
		// Log.i("id", id+"id test");
		// if(id!="-1")
		// {
		// Intent it=new Intent();
		// it.putExtra("id", id);
		// it.setClass(MainActivity.this, BannerDetail.class);
		// startActivity(it);
		// }
		// }
		// return false;
		// }
		// });

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				showToast("再按一次退出程序！");
				exitTime = System.currentTimeMillis();
			} else {
				cancleToast();
				FindDoctorApplication application=(FindDoctorApplication)this.getApplication();
				application.setIsCheckUpdate(0);
				
				Intent i = new Intent(Intent.ACTION_MAIN);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.addCategory(Intent.CATEGORY_HOME);
				 startActivity(i);
//				FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
//				closeApplication.exit();
				ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);  
		        am.killBackgroundProcesses("com.guangyi.finddoctor.activity");
		        editText1.setText("");
//		        finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			if (!isLocated) {
				Editor mEditor = mSharedPreferences.edit();
				mEditor.putString("cityId", location.getCityCode());
				mEditor.putString("cityStr", location.getCity());
				mEditor.putString("geoLat", String.valueOf(geoLat));
				mEditor.putString("geoLng", String.valueOf(geoLng));
				mEditor.commit();
				isLocated = false;
			}
		}
	}
	
	public void uninstallAPK(String packageName){  
        Uri uri=Uri.parse("package:"+packageName);  
        Intent intent=new Intent(Intent.ACTION_DELETE,uri);  
        this.startActivity(intent);  
    }  
	
    public boolean checkBrowser(String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(
                    packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
    
    
	private void getData(final int userId) {
		Runnable mRunnable=new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				String params=userId+"";
				msg.arg1=1;
				msg.obj = new ApiHttpUtil().getMethod(
						Config.getProperty("CANREADCOUNT", ""), params);
				mHandler.sendMessage(msg);
			}
		};
		new Thread(mRunnable).start();

	}
	
	
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			canclePregressDialog();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			Log.i("canreadcount", jsonString);
			if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
//				showToast(getResources().getString(R.string.soconntimeout));
			} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
//				showToast(getResources().getString(R.string.conntimeout));
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

						try {
							SharedPreferences sharedPreferences =
									 getSharedPreferences("personCenter",
									Context.MODE_PRIVATE);
									sharedPreferences.edit().putInt("msgCount", jsonObject.getInt("canReadCount")).commit();
							if(jsonObject.getInt("canReadCount")>0)
							{
							badgeView.setText(jsonObject.getInt("canReadCount")+"");
							badgeView.show();
							}
							else
							{
								badgeView.hide();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				} 
//					else {
//					showToast(reason);
//				}

			}
			super.handleMessage(msg);
		}

}

}
