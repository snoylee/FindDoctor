package com.guangyi.finddoctor.secondActivity;

import java.net.SocketException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.MainActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.hospitalRegister.ChooseCityActivity;
import com.guangyi.finddoctor.hospitalRegister.ChooseDepartmentActivity;
import com.guangyi.finddoctor.hospitalRegister.ChooseHospitalActivity;
import com.guangyi.finddoctor.hospitalRegister.DoctorListActivity;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.City;
import com.guangyi.finddoctor.model.Department;
import com.guangyi.finddoctor.model.GuaHao;
import com.guangyi.finddoctor.model.Hospital;
import com.guangyi.finddoctor.utils.Config;
import com.guangyi.finddoctor.utils.StateInfos;
public class ShospitalRegister extends BasicActivity implements
		AMapLocationListener {
	private TextView mChooseCity, mChooseHospital, mChooseDepartment,
			tv_appomentCount;
	private int mChooseHospitalId = -1;;
	private String mChooseCityId;
	private String mChooseDepartmentId;
	private Button mGoRegister;
	private GuaHao guahao;
	private String departString4Pass;
	private int close;
	private SharedPreferences mSharedPreferences;
	private LocationManagerProxy mAMapLocManager = null;
	private boolean isLocated = false;
	private Handler mHandler;
	private Runnable mRunnable;
	private Thread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospital_register);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		close = getIntent().getIntExtra("close", 0);
		initMySharedPreference();
		initHospitalRegisterView();
		initHospitalRegisterListener();
		initParams();
		initView();
	
		
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

	private void initMySharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
	}

	private void initParams() {
		guahao = (GuaHao) getIntent().getSerializableExtra("guahao");
		if (guahao != null) {
			departString4Pass = guahao.getDepatName();
			departString4Pass = URLEncoder.encode(departString4Pass);
			mChooseCity.setText(guahao.getHospCityName());
			mChooseHospital.setText(guahao.getHospName());
			mChooseDepartment.setText(guahao.getDepatName());
			mChooseDepartmentId = guahao.getHdeptId();
			mChooseCityId = guahao.getHospCity();
			mChooseHospitalId = guahao.getHospId();
		}
		mRunnable = new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.arg1 = 1;
				
//				msg.obj = new ApiHttpUtil().getMethod(
//						Config.getProperty("GETAPPOMENTCOUNT", ""), "");
//				Bundle bd=new Bundle();
//				bd.putString("key", new ApiHttpUtil().getMethod(
//						Config.getProperty("GETAPPOMENTCOUNT", ""), ""));
//				msg.obj=bd;
				msg.obj = new ApiHttpUtil().getMethod(
						Config.getProperty("GETAPPOMENTCOUNT", ""), "");
				mHandler.sendMessage(msg);

			}
		};
	}

	private void initView() {
		Button mBack;
		mBack = (Button) findViewById(R.id.ib_back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (close == 1) {
					finish();
				} else {
					openActivity(MainActivity.class);
					finish();
				}

			}
		});

	}
	// 预约挂号初始化
	private void initHospitalRegisterView() {
		mChooseCity = (TextView) findViewById(R.id.tv_city);
		mChooseHospital = (TextView) findViewById(R.id.tv_hospital);
		mChooseDepartment = (TextView) findViewById(R.id.tv_department);
		mGoRegister = (Button) findViewById(R.id.ib_go_register);
		tv_appomentCount = (TextView) findViewById(R.id.tv_appomentCount);

		if (close == 0) {

			mChooseCity.setText(mSharedPreferences.getString("cityStr", ""));
			mChooseCityId = mSharedPreferences.getString("cityId", "");
		}
		String count = mSharedPreferences.getString("appomentCount", "0");
		
		Float fCount=Float.parseFloat(count);
		
		if(fCount>10000)
		{
			DecimalFormat fnum = new DecimalFormat("##0.000"); 
			count=fnum.format(fCount/10000)+"万";
			
		}
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
		SpannableStringBuilder builder = new SpannableStringBuilder("已经有"
				+ count + "人成功预约!");
		builder.setSpan(redSpan, 3, count.length() + 3,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(count.length()>0)
		{
			tv_appomentCount.setText(builder);
		}
		mHandler = new MyHandler();
	}

	private void initHospitalRegisterListener() {
		mChooseCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				it.setClass(ShospitalRegister.this, ChooseCityActivity.class);
				if (departString4Pass != null) {
					it.putExtra("department", departString4Pass);
				} else {
					it.putExtra("department", "-1");
				}
				startActivityForResult(it, 0);
				// openActivityforResult(ChooseCityActivity.class);
			}
		});
		mChooseHospital.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent itIntent = new Intent();
				itIntent.putExtra(City.ID, mChooseCityId);
				if (departString4Pass != null) {
					itIntent.putExtra("department", departString4Pass);
				} else {
					itIntent.putExtra("department", "-1");
				}
				itIntent.setClass(ShospitalRegister.this,
						ChooseHospitalActivity.class);
				startActivityForResult(itIntent, 0);
			}
		});
		mChooseDepartment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent itIntent = new Intent();
				itIntent.putExtra(Hospital.HOSPID, mChooseHospitalId);
				if (departString4Pass != null) {
					itIntent.putExtra("department", departString4Pass);
				} else {
					itIntent.putExtra("department", "-1");
				}
				itIntent.setClass(ShospitalRegister.this,
						ChooseDepartmentActivity.class);
				startActivityForResult(itIntent, 0);
			}
		});
//可能需要更改地方  解决方案：弹出一个对话框
		mGoRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent itIntent = new Intent();
				itIntent.putExtra(Department.HDEPTID, mChooseDepartmentId);
				itIntent.setClass(ShospitalRegister.this,
						DoctorListActivity.class);
				if (mChooseCity.getText().toString().equals("")
						|| mChooseHospital.getText().toString().equals("")
						|| mChooseDepartment.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "请先输入地区、医院、科室",
							Toast.LENGTH_SHORT).show();
				} else {
					startActivityForResult(itIntent, 0);
				}
			}
		});

	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			canclePregressDialog();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			Log.i("挂号", jsonString + "test");
			if (jsonString.equals(ApiHttpUtil.SOCONNTIMEOUT)) {
				// showToast(getResources().getString(R.string.soconntimeout));
			} else if (jsonString.equals(ApiHttpUtil.CONNTIMEOUT)) {
				// showToast(getResources().getString(R.string.conntimeout));
			} else {

				int code = -1;
				// String
				// reason=getResources().getString(R.string.network_preoblem);
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					code = jsonObject.getInt("code");
					// reason=jsonObject.getString("reason");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (code == 0) {
					if (msg.arg1 == 1) {
						try {
							String appomentCount = jsonObject
									.getString("appomentCount");
							Editor mEditor = mSharedPreferences.edit();
							mEditor.putString("appomentCount", appomentCount);
							mEditor.commit();
							
							if(appomentCount.length()<=0)
							{
								appomentCount="0";
							}
							Float fCount=Float.parseFloat(appomentCount);
							
							if(fCount>10000)
							{
								DecimalFormat fnum = new DecimalFormat("##0.000"); 
								appomentCount=fnum.format(fCount/10000)+"万";
							}
							ForegroundColorSpan redSpan = new ForegroundColorSpan(
									Color.RED);
							SpannableStringBuilder builder = new SpannableStringBuilder(
									"已经有" + appomentCount + "人成功预约!");
							builder.setSpan(redSpan, 3,
									appomentCount.length() + 3,
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							tv_appomentCount.setText(builder);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
				// else
				// {

				// }

			}
			super.handleMessage(msg);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == ResultCodeCategory.RESULT_CODE_CHOSE_CITY) {

			mChooseCity.setText(data.getExtras().getString(City.REGIONNAME));
			mChooseCityId = data.getExtras().getString(City.ID);
			mChooseHospital.setText("");
			mChooseDepartment.setText("");
			mChooseDepartmentId = "";
			mChooseHospitalId = -1;

		} else if (resultCode == ResultCodeCategory.RESULT_CODE_CHOSE_HOSPITAL) {
			mChooseHospital.setText(data.getExtras().getString(
					Hospital.HOSPNAME));
			mChooseHospitalId = data.getExtras().getInt(Hospital.HOSPID);
			mChooseDepartment.setText("");
			mChooseDepartmentId = "";

		} else if (resultCode == ResultCodeCategory.RESULT_CODE_CHOSE_DEPARTMENT) {
			mChooseDepartment.setText(data.getExtras().getString(
					Department.DEPANAME));
			mChooseDepartmentId = data.getExtras()
					.getString(Department.HDEPTID);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (close == 1) {
				finish();
			} else {
				openActivity(MainActivity.class);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
//		mHandler.post(mRunnable);
		thread=new Thread(mRunnable);
		thread.start();
		
		try {
			new StateInfos(ShospitalRegister.this, "预约挂号", 1).postStateInfo();
		} catch (SocketException e) {
			e.printStackTrace();
		}
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
		mHandler.removeCallbacks(mRunnable);
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

	/**
	 * 混合定位回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			// Double geoLat = location.getLatitude();
			// Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			if (!isLocated && close == 0) {
				Editor mEditor = mSharedPreferences.edit();
				mEditor.putString("cityId", location.getCityCode());
				mEditor.putString("cityStr", location.getCity());
				mEditor.commit();
				mChooseCity.setText(location.getCity());
				mChooseCityId = location.getCityCode();

				// String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
				// + "\n精    度    :" + location.getAccuracy() + "米"
				// + "\n定位方式:" + location.getProvider() + "\n城市编码:"
				// + cityCode + "\n位置描述:" + desc + "\n省:"
				// + location.getProvince() + "\n市:" + location.getCity()
				// + "\n区(县):" + location.getDistrict() + "\n城市编码:"
				// + location.getCityCode() + "\n区域编码:" + location.getAdCode());
				 isLocated=true;
				// Log.i("str", str);
			}


		}
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

}
