package com.guangyi.finddoctor.selfService;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.SelfServiceCircleHospitalListAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.Hospital;
import com.guangyi.finddoctor.utils.Config;

/**
 * AMapV2地图中简单介绍显示定位小蓝点
 */
@SuppressLint("ResourceAsColor")
public class SelfServiceMapHospitalActivity extends BasicActivity implements LocationSource,
		AMapLocationListener,OnMarkerClickListener,
		OnInfoWindowClickListener, OnMapLoadedListener, InfoWindowAdapter,OnMapClickListener{
	private int time = 1;
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private List<Hospital> listHospital;
	private ListView listView;
	private TextView tv_topbar_center_text;
	private View infoView;
	private Marker currentMarker;
	private TextView info_tv_hospital_name,info_tv_class,info_tv_state;
	private Runnable mRunnable;
	private Thread thread;
	private SharedPreferences mSharedPreferences;
		
	
	private void initMySharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
	}

		protected void onStop() {
			handler.removeCallbacks(mRunnable);
			time=1;
			super.onStop();
		}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			canclePregressDialog();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
//			Log.i("mapJson", "mapJson"+jsonString);
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
							Log.i("hospital", jsonString);
							JSONArray jsonArray = jsonObject
									.getJSONArray("MapHospList");
							Hospital hosptital;
							for (int i = 0; i < jsonArray.length(); i++) {
								hosptital = new Hospital();
								hosptital.setGuahaoHostId(jsonArray.getJSONObject(i)
										.getJSONObject("hospt")
										.getString("guahaoHostId"));
								hosptital.setHospAddr(jsonArray.getJSONObject(i)
										.getJSONObject("hospt")
										.getString("hospAddress"));
								hosptital
										.setHospClass(jsonArray.getJSONObject(i)
												.getJSONObject("hospt")
												.getInt("hospClass"));
								hosptital.setHospId(jsonArray.getJSONObject(i)
										.getJSONObject("hospt").getInt("hospId"));
								hosptital.setHospIntroduction(jsonArray
										.getJSONObject(i).getJSONObject("hospt")
										.getString("hospIntroduction"));
								hosptital.setHospName(jsonArray.getJSONObject(i)
										.getJSONObject("hospt")
										.getString("hospName"));
								hosptital.setHospTel(jsonArray.getJSONObject(i)
										.getJSONObject("hospt")
										.getString("hospTel"));
								hosptital.setMapType(jsonArray.getJSONObject(i)
										.getJSONObject("hospt")
										.getInt("mapType"));
								hosptital.setX(jsonArray.getJSONObject(i)
										.getJSONObject("gaodejson")
										.getString("x"));
								hosptital.setY(jsonArray.getJSONObject(i)
										.getJSONObject("gaodejson")
										.getString("y"));
								hosptital.setDistance(jsonArray.getJSONObject(i)
										.getJSONObject("hospt")
										.getString("distance"));
								listHospital.add(hosptital);
							}


						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						finally{
							if(listHospital.size()>0)
							{
							drawMarkers();
							BindData();
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
	};
	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationsource_activity);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initMySharedPreference();
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
		initParams();
		initView();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		// myLocationStyle.radiusFillColor(color)//设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(5);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		
		
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		aMap.setOnMapClickListener(this);
		

	}
	public void drawMarkers() {
			MarkerOptions  markerOption;
			for (int i = 0; i < listHospital.size(); i++) {
				markerOption=new MarkerOptions();
				markerOption.snippet(listHospital.get(i).getHospClass()+","+listHospital.get(i).getMapType());
//				markerOption.snippet(listHospital.get(i).getMapType()+"");
				markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_mark));
				markerOption.title(listHospital.get(i).getHospName());
				String y=listHospital.get(i).getY();
				String x=listHospital.get(i).getX();
				if(x.length()>0&&y.length()>0)
				{
					markerOption.position( new LatLng(Double.valueOf(y), Double.valueOf(x)));
				}
				aMap.addMarker(markerOption);

			}
			
		

	}
	
	@Override
	protected void onStart() {
		initGPS();
		super.onStart();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}


	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
//			Log.i("y",aLocation.getLatitude()+"");
//			Log.i("x",aLocation.getLongitude()+"");
			
			
				if (time <= 1) {
					time++;
					final HashMap<String, String> params = new HashMap<String, String>();
					params.put("searchName", URLEncoder.encode("医院"));
					params.put("cenX", String.valueOf(aLocation.getLongitude()));
					params.put("cenY", String.valueOf(aLocation.getLatitude()));
					params.put("batch", "1");
					params.put("number", "10");
					params.put("range", "3000");
					getData(params);
					
					Editor mEditor = mSharedPreferences.edit();
					mEditor.putString("geoLat", String.valueOf(aLocation.getLatitude()));
					mEditor.putString("geoLng", String.valueOf(aLocation.getLongitude()));
					mEditor.commit();
				}
			// 设置所有maker显示在View中
//			LatLngBounds bounds = new LatLngBounds.Builder()
////					.include(Constants.XIAN).include(Constants.CHENGDU)
//					.include(marker1).include(marker2).include(marker3)
//					.include(marker4).include(marker5).include(marker6)
//					.include(marker7).include(marker8).include(marker9)
//					.include(marker10).build();
//			aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true 
			 * Location API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是5000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 5000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		info_tv_hospital_name.setText(arg0.getTitle());
//		if (arg0.getSnippet().toString().split(",")[0].equals("11") ) {
//			info_tv_class.setText("一级丙等");
//		}
//		else
//		if (arg0.getSnippet().toString().split(",")[0].equals("12") ) {
//			info_tv_class.setText("一级乙等");
//		}
//		else
//		if (arg0.getSnippet().toString().split(",")[0].equals("13") ) {
//			info_tv_class.setText("一级甲等");
//		}
//		
//		else
//		if (arg0.getSnippet().toString().split(",")[0].equals("21") ) {
//			info_tv_class.setText("二级丙等");
//		}
//		else
//		if (arg0.getSnippet().toString().split(",")[0].equals("22") ) {
//			info_tv_class.setText("二级乙等");
//		}
//		else
//		if (arg0.getSnippet().toString().split(",")[0].equals("23") ) {
//			info_tv_class.setText("二级甲等");
//		}
//		else
//		if (arg0.getSnippet().toString().split(",")[0].equals("31") ) {
//			info_tv_class.setText("三级丙等");
//		}
//		else
//		if (arg0.getSnippet().toString().split(",")[0].equals("32") ) {
//			info_tv_class.setText("三级乙等");
//		}
//		else
//		if (arg0.getSnippet().toString().split(",")[0].equals("33") ) {
//			info_tv_class.setText("三级甲等");
//		}
//		else
//		{
//			info_tv_class.setText("");
//		}
		if(arg0.getSnippet().toString().split(",")[1].equals("1"))
		{
		info_tv_state.setTextColor(getResources().getColor(R.color.white));
		info_tv_state.setText("可预约");
		info_tv_state.setBackgroundColor(getResources().getColor(R.color.common_green));
		}
		
		else 
		{
		info_tv_state.setTextColor(getResources().getColor(R.color.black));
		info_tv_state.setText("不可预约");
		info_tv_state.setBackgroundColor(getResources().getColor(R.color.common_grey));
		}
		currentMarker=arg0;
		return infoView;
	}

	@Override
	public void onMapLoaded() {
		
		Double geoLat= Double.valueOf(mSharedPreferences.getString("geoLat", "0"));
		Double geoLng= Double.valueOf(mSharedPreferences.getString("geoLng", "0"));

		if(geoLat>0&&geoLng>0)
		{
			LatLng mLatLng=new LatLng(geoLat, geoLng);
			aMap.moveCamera(CameraUpdateFactory.changeLatLng(mLatLng));
			
		}

	}

	/**
	 * 监听点击infowindow窗口事件回调
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		if(marker.getSnippet().split(",")[1].equals("1"))
		{
//		showToast("你点击了infoWindow窗口" + marker.getTitle());
		for (int i = 0; i < listHospital.size(); i++) {
			if(listHospital.get(i).getHospName().equals(marker.getTitle()))
			{
				Intent it = new Intent();
				it.setClass(getApplicationContext(),
						SelfServiceHospitalHome.class);
				it.putExtra("hospital", listHospital.get(i));
				startActivity(it);
			}
			
		}
		
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	private void initView() {
		tv_topbar_center_text = (TextView) findViewById(R.id.tv_topbar_center_text);
		tv_topbar_center_text.setText("地图模式");
		listView=(ListView) findViewById(R.id.list_circle_city);
		Button Back = (Button) findViewById(R.id.ib_back);

		Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		infoView=getLayoutInflater().inflate(R.layout.info_windows, null);
		info_tv_hospital_name=(TextView)infoView. findViewById(R.id.tv_hospital_name);
		info_tv_class=(TextView) infoView. findViewById(R.id.tv_class);
		info_tv_state=(TextView) infoView. findViewById(R.id.tv_state);

		final ImageButton ib_post = (ImageButton) findViewById(R.id.ib_post);
		ib_post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(tv_topbar_center_text.getText().toString().equals("地图模式"))
				{
				
					mapView.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
					tv_topbar_center_text.setText("文字模式") ;
					ib_post.setBackgroundResource(R.drawable.gps_pic);
					
				}
				else if(tv_topbar_center_text.getText().toString().equals("文字模式"))
				{
				
					mapView.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
					tv_topbar_center_text.setText("地图模式") ;
					ib_post.setBackgroundResource(R.drawable.gps_pic2);
					
				}
			}
		});

		
		
	}

	private void initParams() {
		listHospital = new ArrayList<Hospital>();
	}
	
	private void BindData() {
		SelfServiceCircleHospitalListAdapter adapter = new SelfServiceCircleHospitalListAdapter(
				this, listHospital);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
                if(listHospital.get(arg2).getMapType()==1)
                {
				Intent it = new Intent();
				it.setClass(getApplicationContext(),
						SelfServiceHospitalHome.class);
				it.putExtra("hospital", listHospital.get(arg2));
				startActivity(it);
                }

			}
		});
	}
	
	private void getData(final HashMap<String, String> params) {

		mRunnable=new Runnable() {
			@Override
			public void run() {
				String str = new ApiHttpUtil().postMethod(
						Config.getProperty("SELF_SERVICE_CIRCLE", ""), params);
				Message msg = new Message();
				msg.arg1=1;
				msg.obj = str;
				handler.sendMessage(msg);
			}
		};
		
//		handler.post(mRunnable);
		thread=new Thread(mRunnable);
//		new Thread(mRunnable).start();
		thread.start();

	}

	
	//方法已经废弃
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
	public void onMapClick(LatLng arg0) {
		if(currentMarker!=null)
		{
			currentMarker.hideInfoWindow();
		}
		
	}
}
