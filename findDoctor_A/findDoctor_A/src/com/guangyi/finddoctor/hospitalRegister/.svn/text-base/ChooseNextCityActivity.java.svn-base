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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.adapter.ProvinceAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.model.City;
import com.guangyi.finddoctor.utils.Config;

public class ChooseNextCityActivity extends BasicActivity{
	
	private ListView mListView;
	private Button mBack;
	private Handler mHandler;
	private ApiHttpUtil mApiHttpUtil;
	private List<City> mListItem;
	private Runnable mRunnable;
	private Thread mThread;
	private String department;
	private String cityId;
	private TextView tv_topbar_center_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_city);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initListener();
		getData();

	}

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

	private void initView() {
		mListView = (ListView) findViewById(R.id.lv_chose);
		mBack = (Button) findViewById(R.id.ib_back);
		tv_topbar_center_text=(TextView) findViewById(R.id.tv_topbar_center_text);
		tv_topbar_center_text.setText("选择城市");

	}

	private void initParams() {
		
		mHandler = new MyHandler();
			department=getIntent().getExtras().getString("department");
			cityId=getIntent().getExtras().getString(City.ID);
		mApiHttpUtil = new ApiHttpUtil();
		mListItem = new ArrayList<City>();

	}

	private void bindData(List<City> list) {
			ProvinceAdapter adapter = new ProvinceAdapter(this, list);
			View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
			emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
			emptyView.setVisibility(View.GONE);   
			((ViewGroup)mListView.getParent()).addView(emptyView);   
			mListView.setEmptyView(emptyView);
			mListView.setAdapter(adapter);

	}

	// 获取省份信息
	private void getData() {
		initProgressDialog();
		mRunnable = new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				List<String> list = new ArrayList<String>();
				list.add(cityId+"");
				msg.arg1 = 1;
				if(department.length()>0)
				{
					list.add(department);
				}
					msg.obj = mApiHttpUtil.getMethod(
							Config.getProperty("REGION_STRING", ""), list);
				mHandler.sendMessage(msg);

			}
		};
		commonThreadStart();

	}

	private void initListener() {

		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					Intent it = new Intent();
					it.putExtra(City.ID, mListItem.get(position).getId());
					it.putExtra(City.REGIONNAME, mListItem.get(position)
							.getRegionName());
					setResult(ResultCodeCategory.RESULT_CODE_CHOSE_CITY, it);
					finish();


			}
		});

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
						JSONArray jsonArray;
						try {
							jsonArray = jsonObject.getJSONArray("Region");
							City city;
							for (int i = 0; i < jsonArray.length(); i++) {
								city = new City();
								Log.i("ChooseCityActivity",
										jsonArray.getJSONObject(i).getString(
												"regionName"));
								city.setId(jsonArray.getJSONObject(i).getString(
										"regionId"));
								city.setParentId(jsonArray.getJSONObject(i)
										.getInt("parentId"));
//								city.setPkId(jsonArray.getJSONObject(i).getInt(
//										"pkId"));
								city.setRegionName(jsonArray.getJSONObject(i)
										.getString("regionName"));
								city.setRegionType(jsonArray.getJSONObject(i)
										.getInt("regionType"));
								mListItem.add(city);
							}
							bindData(mListItem);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}


				} else {
					showToast(reason);
				}

			}

			super.handleMessage(msg);

		}
	}




}
