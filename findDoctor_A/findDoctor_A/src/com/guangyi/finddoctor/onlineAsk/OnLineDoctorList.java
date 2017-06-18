package com.guangyi.finddoctor.onlineAsk;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.guangyi.finddoctor.activity.BasicActivity;
import com.guangyi.finddoctor.adapter.OnLineDoctorListAdapter;
import com.guangyi.finddoctor.application.FindDoctorApplication;
import com.guangyi.finddoctor.config.ResultCodeCategory;
import com.guangyi.finddoctor.hospitalRegister.DoctorHomeActivity;
import com.guangyi.finddoctor.http.ApiHttpUtil;
import com.guangyi.finddoctor.activity.R;
import com.guangyi.finddoctor.model.Doctor;
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
 * Description:电话咨询医生列表
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
public class OnLineDoctorList extends BasicActivity implements OnScrollListener{
	private Button mBack;
	private Handler mHandler;
	private String mDepartmentID;
	private List<Doctor> mListItem;
	private SharedPreferences mSharedPreferences;
	private boolean isLogin;
	private Editor mEditor;
	private Runnable mRunnable;
	private Thread mThread;
	private int pageNo = 1, pageSize = 5;
	private OnLineDoctorListAdapter adapter;
	private ListView listView;
	private PullToRefreshView mPullToRefreshView;
	private int listFsize=0;

private int mvisibleItemCount=0;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_list);
		FindDoctorApplication closeApplication=(FindDoctorApplication) getApplication();
		closeApplication.addActivity(this);
		initParams();
		initView();
		initListView();
		initListener();
		getData(1);

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void initView() {
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		listView = (ListView) findViewById(R.id.mlist);
		listView.setOnScrollListener(this);
		mBack = (Button) findViewById(R.id.ib_back);
	}

	private void initListView() {
		mPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefresh());
		mPullToRefreshView.setOnFooterRefreshListener(new OnFooterRefresh());
		adapter = new OnLineDoctorListAdapter(this, mListItem, isLogin);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent();
				it.setClass(OnLineDoctorList.this, DoctorHomeActivity.class);
				it.putExtra("doctId", (Serializable) mListItem.get(arg2)
						.getDoctId());
				startActivity(it);

			}
		});

	}

	private void onLoaded() {
		mPullToRefreshView.onHeaderRefreshComplete(getResources().getString(
				R.string.pull_to_refresh_pull_last_time)
				+ DateTools.getCurrentDateString("MM-dd HH:mm:ss"));
		mPullToRefreshView.onFooterRefreshComplete();
	}

	class OnHeaderRefresh implements OnHeaderRefreshListener {

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			getData(0);
		}

	}

	class OnFooterRefresh implements OnFooterRefreshListener {

		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			getData(0);
		}

	}

	private void initParams() {
		mHandler = new MyHandler();
		new ApiHttpUtil();
		mDepartmentID = getIntent().getStringExtra("depId");
		mListItem = new ArrayList<Doctor>();
		getSharedPreference();
		// System.out.println(mDepartmentID);

	}

	private void getSharedPreference() {
		mSharedPreferences = getSharedPreferences("personCenter",
				Context.MODE_PRIVATE);
		isLogin = mSharedPreferences.getBoolean("isLogin", false);
	}

	// private void bindData(List<Doctor> list) {
	// OnLineDoctorListAdapter adapter = new OnLineDoctorListAdapter(this,
	// list, isLogin);
	//
	// mListView.setAdapter(adapter);
	// mListView.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// Doctor item = (Doctor) arg0.getItemAtPosition(arg2);
	// Intent intent = new Intent(OnLineDoctorList.this,
	// DoctorHomeActivity.class);
	// intent.putExtra("doctId", item.getDoctId());
	// startActivity(intent);
	// }
	// });
	// }

	private void getData(int from) {
		if (from == 1) {
			initProgressDialog();
		}
		mRunnable = new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				List<String> list = new ArrayList<String>();
				list.add(URLEncoder.encode(mDepartmentID));
				list.add(pageNo + "");
				list.add(pageSize + "");
				msg.arg1 = 1;
				msg.obj = new ApiHttpUtil().getMethod(
						Config.getProperty("ON_LINE_GET_DOCList_BY_DEPID", ""),
						list);
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
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			onLoaded();
			canclePregressDialog();
			JSONObject jsonObject = null;
			String jsonString = msg.obj.toString();
			Log.w("tttt", msg.obj.toString());
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
						try {
							JSONArray jsonArray = jsonObject
									.getJSONArray("DoctorList");
							listFsize=mListItem.size();
							Doctor doctor;
							for (int i = 0; i < jsonArray.length(); i++) {
								doctor = new Doctor();
								doctor.setDoctId(jsonArray.getJSONObject(i)
										.getInt(Doctor.DOCTID));
								doctor.setExpertId(jsonArray.getJSONObject(i)
										.getString(Doctor.EXPERTID));
								doctor.setDoctName(jsonArray.getJSONObject(i)
										.getString(Doctor.DOCTNAME));
								doctor.setDoctPosi(jsonArray.getJSONObject(i)
										.getString(Doctor.DOCTPOSI));
								doctor.setDoctorPosition(jsonArray
										.getJSONObject(i).getString(
												Doctor.DOCTORPOSITION));
								doctor.setDocIntroduction(jsonArray
										.getJSONObject(i).getString(
												Doctor.DOCINTRODUCTION));
								doctor.setDoctDepaid(jsonArray.getJSONObject(i)
										.getString(Doctor.DOCTDEPAID));
								doctor.setDoctHospid(jsonArray.getJSONObject(i)
										.getString(Doctor.DOCTHOSPID));
								doctor.setHospName(jsonArray.getJSONObject(i)
										.getString(Doctor.HOSPNAME));
								doctor.setDepaName(jsonArray.getJSONObject(i)
										.getString(Doctor.DEPANAME));
								doctor.setScore(jsonArray.getJSONObject(i)
										.getInt(Doctor.SCORE));
								doctor.setSumScore(jsonArray.getJSONObject(i)
										.getInt(Doctor.SCORENUM));
								doctor.setRegfee(jsonArray.getJSONObject(i)
										.getInt(Doctor.REGFEE));
								doctor.setMoney(jsonArray.getJSONObject(i)
										.getString(Doctor.MONEY));
								doctor.setAttachFileByte(Config.getProperty("FILEURL", "")+jsonArray.getJSONObject(i).getString(
										"attachFileUrl"));
								mListItem.add(doctor);

							}
						} catch (JSONException e) {

							e.printStackTrace();
						}
						finally
						{
							if (mListItem.size() > 0) {
								// bindData(mListItem);
								mPullToRefreshView.openFootView();
								
							}
							else
							{
								mPullToRefreshView.closeFootView();
							}
							
							View emptyView = LayoutInflater.from(OnLineDoctorList.this).inflate(R.layout.empty_view, null);
							emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
							emptyView.setVisibility(View.GONE);   
							((ViewGroup)listView.getParent()).addView(emptyView);   
							listView.setEmptyView(emptyView);
							
							adapter.notifyDataSetChanged();
							
							
							
							if(pageNo==1)
							{
							
								if (mListItem.size() > listFsize) {
									pageNo++;
									
								}
							}
							
							else
							{ 
								if (mListItem.size() > listFsize) {
									pageNo++;
									listView.setSelection(listFsize-mvisibleItemCount+1);
								}
								
								else
								{
									mPullToRefreshView.stopCanFlash();
								}
								
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
			getSharedPreference();
			// bindData(mListItem);
			if (adapter != null && listView != null) {

				adapter = new OnLineDoctorListAdapter(this, mListItem, isLogin);
				listView.setAdapter(adapter);
			}

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
